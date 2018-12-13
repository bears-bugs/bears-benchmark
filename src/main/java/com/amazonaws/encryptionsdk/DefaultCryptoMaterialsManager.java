package com.amazonaws.encryptionsdk;

import static com.amazonaws.encryptionsdk.internal.Utils.assertNonNull;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.encryptionsdk.exception.AwsCryptoException;
import com.amazonaws.encryptionsdk.exception.CannotUnwrapDataKeyException;
import com.amazonaws.encryptionsdk.internal.Constants;
import com.amazonaws.encryptionsdk.internal.Utils;
import com.amazonaws.encryptionsdk.internal.TrailingSignatureAlgorithm;
import com.amazonaws.encryptionsdk.model.DecryptionMaterialsRequest;
import com.amazonaws.encryptionsdk.model.DecryptionMaterials;
import com.amazonaws.encryptionsdk.model.EncryptionMaterialsRequest;
import com.amazonaws.encryptionsdk.model.KeyBlob;
import com.amazonaws.encryptionsdk.model.EncryptionMaterials;

/**
 * The default implementation of {@link CryptoMaterialsManager}, used implicitly when passing a
 * {@link MasterKeyProvider} to methods in {@link AwsCrypto}.
 *
 * This default implementation delegates to a specific {@link MasterKeyProvider} specified at construction time. It also
 * handles generating trailing signature keys when needed, placing them in the encryption context (and extracting them
 * at decrypt time).
 */
public class DefaultCryptoMaterialsManager implements CryptoMaterialsManager {
    private final MasterKeyProvider<?> mkp;

    /**
     * @param mkp The master key provider to delegate to
     */
    public DefaultCryptoMaterialsManager(MasterKeyProvider<?> mkp) {
        Utils.assertNonNull(mkp, "mkp");
        this.mkp = mkp;
    }

    @Override public EncryptionMaterials getMaterialsForEncrypt(EncryptionMaterialsRequest request) {
        Map<String, String> context = request.getContext();

        CryptoAlgorithm algo = request.getRequestedAlgorithm();
        if (algo == null) {
            algo = AwsCrypto.getDefaultCryptoAlgorithm();
        }

        KeyPair trailingKeys = null;
        if (algo.getTrailingSignatureLength() > 0) {
            try {
                trailingKeys = generateTrailingSigKeyPair(algo);
                if (context.containsKey(Constants.EC_PUBLIC_KEY_FIELD)) {
                    throw new IllegalArgumentException("EncryptionContext contains reserved field "
                                                               + Constants.EC_PUBLIC_KEY_FIELD);
                }
                // make mutable
                context = new HashMap<>(context);
                context.put(Constants.EC_PUBLIC_KEY_FIELD, serializeTrailingKeyForEc(algo, trailingKeys));
            } catch (final GeneralSecurityException ex) {
                throw new AwsCryptoException(ex);
            }
        }

        final MasterKeyRequest.Builder mkRequestBuilder = MasterKeyRequest.newBuilder();
        mkRequestBuilder.setEncryptionContext(context);

        mkRequestBuilder.setStreaming(request.getPlaintextSize() == -1);
        if (request.getPlaintext() != null) {
            mkRequestBuilder.setPlaintext(request.getPlaintext());
        } else {
            mkRequestBuilder.setSize(request.getPlaintextSize());
        }

        @SuppressWarnings("unchecked")
        final List<MasterKey> mks
                = (List<MasterKey>)assertNonNull(mkp, "provider")
                        .getMasterKeysForEncryption(mkRequestBuilder.build());

        if (mks.isEmpty()) {
            throw new IllegalArgumentException("No master keys provided");
        }

        DataKey<?> dataKey = mks.get(0).generateDataKey(algo, context);

        List<KeyBlob> keyBlobs = new ArrayList<>(mks.size());
        keyBlobs.add(new KeyBlob(dataKey));

        for (int i = 1; i < mks.size(); i++) {
            //noinspection unchecked
            keyBlobs.add(new KeyBlob(mks.get(i).encryptDataKey(algo, context, dataKey)));
        }

        //noinspection unchecked
        return EncryptionMaterials.newBuilder()
                                  .setAlgorithm(algo)
                                  .setCleartextDataKey(dataKey.getKey())
                                  .setEncryptedDataKeys(keyBlobs)
                                  .setEncryptionContext(context)
                                  .setTrailingSignatureKey(trailingKeys == null ? null : trailingKeys.getPrivate())
                                  .setMasterKeys(mks)
                                  .build();
    }

    @Override public DecryptionMaterials decryptMaterials(DecryptionMaterialsRequest request) {
        DataKey<?> dataKey = mkp.decryptDataKey(
                request.getAlgorithm(),
                request.getEncryptedDataKeys(),
                request.getEncryptionContext()
        );

        if (dataKey == null) {
            throw new CannotUnwrapDataKeyException("Could not decrypt any data keys");
        }

        PublicKey pubKey = null;
        if (request.getAlgorithm().getTrailingSignatureLength() > 0) {
            try {
                String serializedPubKey = request.getEncryptionContext().get(Constants.EC_PUBLIC_KEY_FIELD);

                if (serializedPubKey == null) {
                    throw new AwsCryptoException("Missing trailing signature public key");
                }

                pubKey = deserializeTrailingKeyFromEc(request.getAlgorithm(), serializedPubKey);
            } catch (final GeneralSecurityException ex) {
                throw new AwsCryptoException(ex);
            }
        }

        return DecryptionMaterials.newBuilder()
                                  .setDataKey(dataKey)
                                  .setTrailingSignatureKey(pubKey)
                                  .build();
    }

    private PublicKey deserializeTrailingKeyFromEc(
            CryptoAlgorithm algo,
            String pubKey
    ) throws GeneralSecurityException {
        return TrailingSignatureAlgorithm.forCryptoAlgorithm(algo).deserializePublicKey(pubKey);
    }

    private static String serializeTrailingKeyForEc(CryptoAlgorithm algo, KeyPair trailingKeys) {
        return TrailingSignatureAlgorithm.forCryptoAlgorithm(algo).serializePublicKey(trailingKeys.getPublic());
    }

    private static KeyPair generateTrailingSigKeyPair(CryptoAlgorithm algo) throws GeneralSecurityException {
        return TrailingSignatureAlgorithm.forCryptoAlgorithm(algo).generateKey();
    }
}
