/*
 * Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except
 * in compliance with the License. A copy of the License is located at
 * 
 * http://aws.amazon.com/apache2.0
 * 
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.amazonaws.encryptionsdk.internal;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.ECPrivateKey;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;

import com.amazonaws.encryptionsdk.CryptoAlgorithm;
import com.amazonaws.encryptionsdk.MasterKey;
import com.amazonaws.encryptionsdk.exception.AwsCryptoException;
import com.amazonaws.encryptionsdk.exception.BadCiphertextException;
import com.amazonaws.encryptionsdk.model.CiphertextFooters;
import com.amazonaws.encryptionsdk.model.CiphertextHeaders;
import com.amazonaws.encryptionsdk.model.CiphertextType;
import com.amazonaws.encryptionsdk.model.ContentType;
import com.amazonaws.encryptionsdk.model.EncryptionMaterials;
import com.amazonaws.encryptionsdk.model.KeyBlob;

/**
 * This class implements the CryptoHandler interface by providing methods for the encryption of
 * plaintext data.
 * 
 * <p>
 * This class creates the ciphertext headers and delegates the encryption of the plaintext to the
 * {@link BlockEncryptionHandler} or {@link FrameEncryptionHandler} based on the content type.
 */
public class EncryptionHandler implements MessageCryptoHandler {
    private static final CiphertextType CIPHERTEXT_TYPE = CiphertextType.CUSTOMER_AUTHENTICATED_ENCRYPTED_DATA;

    private final EncryptionMaterials encryptionMaterials_;
    private final Map<String, String> encryptionContext_;
    private final CryptoAlgorithm cryptoAlgo_;
    private final List<MasterKey> masterKeys_;
    private final List<KeyBlob> keyBlobs_;
    private final SecretKey encryptionKey_;
    private final byte version_;
    private final CiphertextType type_;
    private final byte nonceLen_;
    private final PrivateKey trailingSignaturePrivateKey_;
    private final MessageDigest trailingDigest_;
    private final Signature trailingSig_;

    private final CiphertextHeaders ciphertextHeaders_;
    private final byte[] ciphertextHeaderBytes_;
    private final CryptoHandler contentCryptoHandler_;

    private boolean firstOperation_ = true;
    private boolean complete_ = false;

    private long plaintextBytes_ = 0;
    private long plaintextByteLimit_ = -1;

    /**
     * Create an encryption handler using the provided master key and encryption context.
     *
     * @param frameSize The encryption frame size, or zero for a one-shot encryption task
     * @param result The EncryptionMaterials with the crypto materials for this encryption job
     * @throws AwsCryptoException
     *             if the encryption context or master key is null.
     */
    public EncryptionHandler(int frameSize, EncryptionMaterials result) throws AwsCryptoException {
        this.encryptionMaterials_ = result;
        this.encryptionContext_ = result.getEncryptionContext();
        this.cryptoAlgo_ = result.getAlgorithm();
        this.masterKeys_ = result.getMasterKeys();
        this.keyBlobs_ = result.getEncryptedDataKeys();
        this.trailingSignaturePrivateKey_ = result.getTrailingSignatureKey();

        if (keyBlobs_.isEmpty()) {
            throw new IllegalArgumentException("No encrypted data keys in materials result");
        }
        
        if (trailingSignaturePrivateKey_ != null) {
            try {
                TrailingSignatureAlgorithm algorithm = TrailingSignatureAlgorithm.forCryptoAlgorithm(cryptoAlgo_);
                trailingDigest_ = MessageDigest.getInstance(algorithm.getMessageDigestAlgorithm());
                trailingSig_ = Signature.getInstance(algorithm.getRawSignatureAlgorithm());

                trailingSig_.initSign(trailingSignaturePrivateKey_, Utils.getSecureRandom());
            } catch (final GeneralSecurityException ex) {
                throw new AwsCryptoException(ex);
            }
        } else {
            trailingDigest_ = null;
            trailingSig_ = null;
        }

        // set default values
        version_ = VersionInfo.CURRENT_CIPHERTEXT_VERSION;
        type_ = CIPHERTEXT_TYPE;
        nonceLen_ = cryptoAlgo_.getNonceLen();

        ContentType contentType;
        if (frameSize > 0) {
            contentType = ContentType.FRAME;
        } else if (frameSize == 0) {
            contentType = ContentType.SINGLEBLOCK;
        } else {
            throw Utils.cannotBeNegative("Frame size");
        }

        final CiphertextHeaders unsignedHeaders = createCiphertextHeaders(contentType, frameSize);
        try {
            encryptionKey_ = cryptoAlgo_.getEncryptionKeyFromDataKey(result.getCleartextDataKey(), unsignedHeaders);
        } catch (final InvalidKeyException ex) {
            throw new AwsCryptoException(ex);
        }
        ciphertextHeaders_ = signCiphertextHeaders(unsignedHeaders);
        ciphertextHeaderBytes_ = ciphertextHeaders_.toByteArray();
        byte[] messageId_ = ciphertextHeaders_.getMessageId();

        switch (contentType) {
            case FRAME:
                contentCryptoHandler_ = new FrameEncryptionHandler(encryptionKey_, nonceLen_, cryptoAlgo_, messageId_,
                                                                   frameSize);
                break;
            case SINGLEBLOCK:
                contentCryptoHandler_ = new BlockEncryptionHandler(encryptionKey_, nonceLen_, cryptoAlgo_, messageId_);
                break;
            default:
                // should never get here because a valid content type is always
                // set above based on the frame size.
                throw new AwsCryptoException("Unknown content type.");
        }
    }

    /**
     * Encrypt a block of bytes from {@code in} putting the plaintext result into {@code out}.
     * 
     * <p>
     * It encrypts by performing the following operations:
     * <ol>
     * <li>if this is the first call to encrypt, write the ciphertext headers to the output being
     * returned.</li>
     * <li>else, pass off the input data to underlying content cryptohandler.</li>
     * </ol>
     * 
     * @param in
     *            the input byte array.
     * @param off
     *            the offset into the in array where the data to be encrypted starts.
     * @param len
     *            the number of bytes to be encrypted.
     * @param out
     *            the output buffer the encrypted bytes go into.
     * @param outOff
     *            the offset into the output byte array the encrypted data starts at.
     * @return the number of bytes written to out and processed
     * @throws AwsCryptoException
     *             if len or offset values are negative.
     * @throws BadCiphertextException
     *             thrown by the underlying cipher handler.
     */
    @Override
    public ProcessingSummary processBytes(final byte[] in, final int off, final int len, final byte[] out,
            final int outOff)
            throws AwsCryptoException, BadCiphertextException {
        if (len < 0 || off < 0) {
            throw new AwsCryptoException(String.format(
                    "Invalid values for input offset: %d and length: %d", off, len));
        }

        checkPlaintextSizeLimit(len);

        int actualOutLen = 0;

        if (firstOperation_ == true) {
            System.arraycopy(ciphertextHeaderBytes_, 0, out, outOff, ciphertextHeaderBytes_.length);
            actualOutLen += ciphertextHeaderBytes_.length;

            firstOperation_ = false;
        }

        ProcessingSummary contentOut =
                contentCryptoHandler_.processBytes(in, off, len, out, outOff + actualOutLen);
        actualOutLen += contentOut.getBytesWritten();
        updateTrailingSignature(out, outOff, actualOutLen);
        plaintextBytes_ += contentOut.getBytesProcessed();
        return new ProcessingSummary(actualOutLen, contentOut.getBytesProcessed());
    }

    /**
     * Finish encryption of the plaintext bytes.
     * 
     * @param out
     *            space for any resulting output data.
     * @param outOff
     *            offset into out to start copying the data at.
     * @return number of bytes written into out.
     * @throws BadCiphertextException
     *             thrown by the underlying cipher handler.
     */
    @Override
    public int doFinal(final byte[] out, final int outOff) throws BadCiphertextException {
        if (complete_) {
            throw new IllegalStateException("Attempted to call doFinal twice");
        }

        complete_ = true;

        checkPlaintextSizeLimit(0);

        int written = contentCryptoHandler_.doFinal(out, outOff);
        updateTrailingSignature(out, outOff, written);
        if (cryptoAlgo_.getTrailingSignatureLength() > 0) {
            try {
                CiphertextFooters footer = new CiphertextFooters(signContent());
                byte[] fBytes = footer.toByteArray();
                System.arraycopy(fBytes, 0, out, outOff + written, fBytes.length);
                return written + fBytes.length;
            } catch (final SignatureException ex) {
                throw new AwsCryptoException(ex);
            }
        } else {
            return written;
        }
    }

    private byte[] signContent() throws SignatureException {
        if (trailingDigest_ != null) {
            if (!trailingSig_.getAlgorithm().contains("ECDSA")) {
                throw new UnsupportedOperationException("Signatures calculated in pieces is only supported for ECDSA.");
            }
            final byte[] digest = trailingDigest_.digest();
            return generateEcdsaFixedLengthSignature(digest);
        }
        return trailingSig_.sign();
    }

    private byte[] generateEcdsaFixedLengthSignature(final byte[] digest) throws SignatureException {
        byte[] signature;
        // Unfortunately, we need deterministic lengths some signatures are non-deterministic in length.
        // So, retry until we get the right length :-(
        do {
            trailingSig_.update(digest);
            signature = trailingSig_.sign();
            if (signature.length != cryptoAlgo_.getTrailingSignatureLength()) {
                // Most of the time, a signature of the wrong length can be fixed
                // be negating s in the signature relative to the group order.
                ASN1Sequence seq = ASN1Sequence.getInstance(signature);
                ASN1Integer r = (ASN1Integer) seq.getObjectAt(0);
                ASN1Integer s = (ASN1Integer) seq.getObjectAt(1);
                ECPrivateKey ecKey = (ECPrivateKey) trailingSignaturePrivateKey_;
                s = new ASN1Integer(ecKey.getParams().getOrder().subtract(s.getPositiveValue()));
                seq = new DERSequence(new ASN1Encodable[]{r, s});
                try {
                    signature = seq.getEncoded();
                } catch (IOException ex) {
                    throw new SignatureException(ex);
                }
            }
        } while (signature.length != cryptoAlgo_.getTrailingSignatureLength());
        return signature;
    }

    /**
     * Return the size of the output buffer required for a {@code processBytes} plus a
     * {@code doFinal} with an input of inLen bytes.
     * 
     * @param inLen
     *            the length of the input.
     * @return the space required to accommodate a call to processBytes and doFinal with len bytes
     *         of input.
     */
    @Override
    public int estimateOutputSize(final int inLen) {
        int outSize = 0;
        if (firstOperation_ == true) {
            outSize += ciphertextHeaderBytes_.length;
        }
        outSize += contentCryptoHandler_.estimateOutputSize(inLen);

        if (cryptoAlgo_.getTrailingSignatureLength() > 0) {
            outSize += 2; // Length field in footer
            outSize += cryptoAlgo_.getTrailingSignatureLength();
        }
        return outSize;
    }

    @Override
    public int estimatePartialOutputSize(int inLen) {
        int outSize = 0;
        if (firstOperation_ == true) {
            outSize += ciphertextHeaderBytes_.length;
        }
        outSize += contentCryptoHandler_.estimatePartialOutputSize(inLen);

        return outSize;
    }

    @Override
    public int estimateFinalOutputSize() {
        return estimateOutputSize(0);
    }

    /**
     * Return the encryption context.
     * 
     * @return the key-value map containing encryption context.
     */
    @Override
    public Map<String, String> getEncryptionContext() {
        return encryptionContext_;
    }

    @Override
    public CiphertextHeaders getHeaders() {
        return ciphertextHeaders_;
    }

    @Override
    public void setMaxInputLength(long size) {
        if (size < 0) {
            throw Utils.cannotBeNegative("Max input length");
        }

        if (plaintextByteLimit_ == -1 || plaintextByteLimit_ > size) {
            plaintextByteLimit_ = size;
        }

        // check that we haven't already exceeded the limit
        checkPlaintextSizeLimit(0);
    }

    private void checkPlaintextSizeLimit(long additionalBytes) {
        if (plaintextByteLimit_ != -1 && plaintextBytes_ + additionalBytes > plaintextByteLimit_) {
            throw new IllegalStateException("Plaintext size exceeds max input size limit");
        }
    }

    /**
     * Compute the MAC tag of the header bytes using the provided key, nonce, AAD, and crypto
     * algorithm identifier.
     *
     * @param nonce
     *            the nonce to use in computing the MAC tag.
     * @param aad
     *            the AAD to use in computing the MAC tag.
     * @return the bytes containing the computed MAC tag.
     */
    private byte[] computeHeaderTag(final byte[] nonce, final byte[] aad) {
        final CipherHandler cipherHandler = new CipherHandler(encryptionKey_,
                Cipher.ENCRYPT_MODE,
                cryptoAlgo_);

        return cipherHandler.cipherData(nonce, aad, new byte[0], 0, 0);
    }

    /**
     * Create ciphertext headers using the instance variables, and the provided content type and
     * frame size.
     * 
     * @param contentType
     *            the content type to set in the ciphertext headers.
     * @param frameSize
     *            the frame size to set in the ciphertext headers.
     * @return the bytes containing the ciphertext headers.
     */
    private CiphertextHeaders createCiphertextHeaders(final ContentType contentType, final int frameSize) {
        // create the ciphertext headers
        final byte[] headerNonce = new byte[nonceLen_];
        // We use a deterministic IV of zero for the header authentication.

        final byte[] encryptionContextBytes = EncryptionContextSerializer.serialize(encryptionContext_);
        final CiphertextHeaders ciphertextHeaders = new CiphertextHeaders(version_, type_, cryptoAlgo_,
                encryptionContextBytes, keyBlobs_, contentType, frameSize);
        ciphertextHeaders.setHeaderNonce(headerNonce);

        return ciphertextHeaders;
    }

    private CiphertextHeaders signCiphertextHeaders(final CiphertextHeaders unsignedHeaders) {
        final byte[] headerFields = unsignedHeaders.serializeAuthenticatedFields();
        final byte[] headerTag = computeHeaderTag(unsignedHeaders.getHeaderNonce(), headerFields);

        unsignedHeaders.setHeaderTag(headerTag);

        return unsignedHeaders;
    }

    @Override
    public List<? extends MasterKey<?>> getMasterKeys() {
        //noinspection unchecked
        return (List)masterKeys_; // This is unmodifiable
    }

    private void updateTrailingSignature(byte[] input, int offset, int len) {
        if (trailingDigest_ != null) {
            trailingDigest_.update(input, offset, len);
        } else if (trailingSig_ != null) {
            try {
                trailingSig_.update(input, offset, len);
            } catch (final SignatureException ex) {
                throw new AwsCryptoException(ex);
            }
        }
    }

    @Override
    public boolean isComplete() {
        return complete_;
    }
}
