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

import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import com.amazonaws.encryptionsdk.CryptoAlgorithm;
import com.amazonaws.encryptionsdk.exception.AwsCryptoException;
import com.amazonaws.encryptionsdk.exception.BadCiphertextException;
import com.amazonaws.encryptionsdk.model.CipherFrameHeaders;

/**
 * The frame decryption handler is a subclass of the decryption handler and
 * thereby provides an implementation of the Cryptography handler.
 * 
 * <p>
 * It implements methods for decrypting content that was encrypted and stored in
 * frames.
 */
class FrameDecryptionHandler implements CryptoHandler {
    private final SecretKey decryptionKey_;
    private final CryptoAlgorithm cryptoAlgo_;
    private final CipherHandler cipherHandler_;
    private final byte[] messageId_;

    private final short nonceLen_;

    private CipherFrameHeaders currentFrameHeaders_;
    private final int frameSize_;
    private long frameNumber_ = 1;

    boolean complete_ = false;
    private byte[] unparsedBytes_ = new byte[0];

    /**
     * Construct a decryption handler for decrypting bytes stored in frames.
     * 
     * @param customerMasterKey
     *            the master key to use when unwrapping the data key encoded in
     *            the ciphertext.
     */
    public FrameDecryptionHandler(final SecretKey decryptionKey, final short nonceLen,
            final CryptoAlgorithm cryptoAlgo, final byte[] messageId, final int frameLen) {
        decryptionKey_ = decryptionKey;
        nonceLen_ = nonceLen;
        cryptoAlgo_ = cryptoAlgo;
        messageId_ = messageId;
        frameSize_ = frameLen;
        cipherHandler_ = new CipherHandler(decryptionKey_, Cipher.DECRYPT_MODE, cryptoAlgo_);
    }

    /**
     * Decrypt the ciphertext bytes containing content encrypted using frames and put the plaintext
     * bytes into out.
     * 
     * <p>
     * It decrypts by performing the following operations:
     * <ol>
     * <li>parse the ciphertext headers</li>
     * <li>parse the ciphertext until encrypted content in a frame is available</li>
     * <li>decrypt the encrypted content</li>
     * <li>return decrypted bytes as output</li>
     * </ol>
     * 
     * @param in
     *            the input byte array.
     * @param inOff
     *            the offset into the in array where the data to be decrypted starts.
     * @param inLen
     *            the number of bytes to be decrypted.
     * @param out
     *            the output buffer the decrypted plaintext bytes go into.
     * @param outOff
     *            the offset into the output byte array the decrypted data starts at.
     * @return the number of bytes written to out and processed
     * @throws InvalidCiphertextException
     *             if frame number is invalid/out-of-order or if the bytes do not decrypt correctly.
     * @throws AwsCryptoException
     *             if the content type found in the headers is not of frame type.
     */
    @Override
    public ProcessingSummary processBytes(final byte[] in, final int off, final int len, final byte[] out,
            final int outOff)
            throws BadCiphertextException, AwsCryptoException {
        final long totalBytesToParse = unparsedBytes_.length + (long) len;
        if (totalBytesToParse > Integer.MAX_VALUE) {
            throw new AwsCryptoException(
                    "Integer overflow of the total bytes to parse and decrypt occured.");
        }

        final byte[] bytesToParse = new byte[(int) totalBytesToParse];
        // If there were previously unparsed bytes, add them as the first
        // set of bytes to be parsed in this call.
        System.arraycopy(unparsedBytes_, 0, bytesToParse, 0, unparsedBytes_.length);
        System.arraycopy(in, off, bytesToParse, unparsedBytes_.length, len);

        int actualOutLen = 0;
        int totalParsedBytes = 0;

        // Parse available bytes. Stop parsing when there aren't enough
        // bytes to complete parsing:
        // - the ciphertext headers
        // - the cipher frame
        while (!complete_ && totalParsedBytes < bytesToParse.length) {
            if (currentFrameHeaders_ == null) {
                currentFrameHeaders_ = new CipherFrameHeaders();
                currentFrameHeaders_.setNonceLength(nonceLen_);
                if (frameSize_ == 0) {
                    // if frame size in ciphertext headers is 0, the frame size
                    // will need to be parsed in individual frame headers.
                    currentFrameHeaders_.includeFrameSize(true);
                }
            }

            totalParsedBytes += currentFrameHeaders_.deserialize(bytesToParse, totalParsedBytes);

            // if we have all frame fields, process the encrypted content.
            if (currentFrameHeaders_.isComplete() == true) {
                int protectedContentLen = -1;
                if (currentFrameHeaders_.isFinalFrame()) {
                    protectedContentLen = currentFrameHeaders_.getFrameContentLength();
                } else {
                    protectedContentLen = frameSize_;
                }

                // include the tag which is added by the underlying cipher.
                protectedContentLen += cryptoAlgo_.getTagLen();

                if ((bytesToParse.length - totalParsedBytes) < protectedContentLen) {
                    // if we don't have all of the encrypted bytes, break
                    // until they become available.
                    break;
                }

                final byte[] bytesToDecrypt_ = Arrays.copyOfRange(bytesToParse, totalParsedBytes, totalParsedBytes
                        + protectedContentLen);
                totalParsedBytes += protectedContentLen;

                if (frameNumber_ == Constants.MAX_FRAME_NUMBER) {
                    throw new BadCiphertextException("Frame number exceeds the maximum allowed value.");
                }

                final byte[] decryptedBytes = decryptContent(bytesToDecrypt_, 0, bytesToDecrypt_.length);

                System.arraycopy(decryptedBytes, 0, out, (outOff + actualOutLen), decryptedBytes.length);
                actualOutLen += decryptedBytes.length;
                frameNumber_++;

                complete_ = currentFrameHeaders_.isFinalFrame();
                // reset frame headers as we are done processing current frame.
                currentFrameHeaders_ = null;
            } else {
                // if there aren't enough bytes to parse cipher frame,
                // we can't continue parsing.
                break;
            }
        }

        if (!complete_) {
            // buffer remaining bytes for parsing in the next round.
            unparsedBytes_ = Arrays.copyOfRange(bytesToParse, totalParsedBytes, bytesToParse.length);
            return new ProcessingSummary(actualOutLen, len);
        } else {
            final ProcessingSummary result = new ProcessingSummary(actualOutLen, totalParsedBytes
                    - unparsedBytes_.length);
            unparsedBytes_ = new byte[0];
            return result;
        }
    }

    /**
     * Finish processing of the bytes. This function does nothing since the
     * final frame will be processed and decrypted in processBytes().
     * 
     * @param out
     *            space for any resulting output data.
     * @param outOff
     *            offset into out to start copying the data at.
     * @return
     *         0
     */
    @Override
    public int doFinal(final byte[] out, final int outOff) {
        return 0;
    }

    /**
     * Return the size of the output buffer required for a processBytes plus a
     * doFinal with an input of inLen bytes.
     * 
     * @param inLen
     *            the length of the input.
     * @return
     *         the space required to accommodate a call to processBytes and
     *         doFinal with len bytes of input.
     */
    @Override
    public int estimateOutputSize(final int inLen) {
        int outSize = 0;

        final int totalBytesToDecrypt = unparsedBytes_.length + inLen;
        if (totalBytesToDecrypt > 0) {
            int frames = totalBytesToDecrypt / frameSize_;
            frames += 1; // add one for final frame which might be < frame size.
            outSize += (frameSize_ * frames);
        }

        return outSize;
    }

    @Override
    public int estimatePartialOutputSize(int inLen) {
        return estimateOutputSize(inLen);
    }

    @Override
    public int estimateFinalOutputSize() {
        return 0;
    }

    /**
     * Returns the plaintext bytes of the encrypted content.
     * 
     * @param input
     *            the input bytes containing the content
     * @param off
     *            the offset into the input array where the data to be decrypted
     *            starts.
     * @param len
     *            the number of bytes to be decrypted.
     * @return
     *         the plaintext bytes of the encrypted content.
     * @throws BadCiphertextException
     *             if the bytes do not decrypt correctly.
     */
    private byte[] decryptContent(final byte[] input, final int off, final int len) throws BadCiphertextException {
        final byte[] nonce = currentFrameHeaders_.getNonce();

        byte[] contentAad = null;
        if (currentFrameHeaders_.isFinalFrame() == true) {
            contentAad = Utils.generateContentAad(
                    messageId_,
                    Constants.FINAL_FRAME_STRING_ID,
                    (int) frameNumber_,
                    currentFrameHeaders_.getFrameContentLength());
        } else {
            contentAad = Utils.generateContentAad(
                    messageId_,
                    Constants.FRAME_STRING_ID,
                    (int) frameNumber_,
                    frameSize_);
        }

        return cipherHandler_.cipherData(nonce, contentAad, input, off, len);
    }

    @Override
    public boolean isComplete() {
        return complete_;
    }
}
