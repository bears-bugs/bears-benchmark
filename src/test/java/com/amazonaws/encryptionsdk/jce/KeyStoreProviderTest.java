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

package com.amazonaws.encryptionsdk.jce;

import static com.amazonaws.encryptionsdk.internal.RandomBytesGenerator.generate;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStoreException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.junit.Before;
import org.junit.Test;

import com.amazonaws.encryptionsdk.AwsCrypto;
import com.amazonaws.encryptionsdk.CryptoResult;
import com.amazonaws.encryptionsdk.MasterKeyProvider;
import com.amazonaws.encryptionsdk.exception.CannotUnwrapDataKeyException;
import com.amazonaws.encryptionsdk.multi.MultipleProviderFactory;

@SuppressWarnings("deprecation")
public class KeyStoreProviderTest {
    private static final SecureRandom RND = new SecureRandom();
    private static final KeyPairGenerator KG;
    private static final byte[] PLAINTEXT = generate(1024);
    private static final char[] PASSWORD = "Password".toCharArray();
    private static final KeyStore.PasswordProtection PP = new PasswordProtection(PASSWORD);
    private KeyStore ks;

    static {
        try {
            Security.addProvider(new BouncyCastleProvider());
            KG = KeyPairGenerator.getInstance("RSA", "BC");
            KG.initialize(2048);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Before
    public void setup() throws Exception {
        ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null, PASSWORD);
    }

    @Test
    public void singleKeyPkcs1() throws GeneralSecurityException {
        addEntry("key1");
        final KeyStoreProvider mkp = new KeyStoreProvider(ks, PP, "KeyStore", "RSA/ECB/PKCS1Padding", "key1");
        final JceMasterKey mk1 = mkp.getMasterKey("key1");
        final AwsCrypto crypto = new AwsCrypto();
        final CryptoResult<byte[], JceMasterKey> ct = crypto.encryptData(mkp, PLAINTEXT);
        assertEquals(1, ct.getMasterKeyIds().size());
        final CryptoResult<byte[], JceMasterKey> result = crypto.decryptData(mkp, ct.getResult());
        assertArrayEquals(PLAINTEXT, result.getResult());
        // Only the first found key should be used
        assertEquals(1, result.getMasterKeys().size());
        assertEquals(mk1, result.getMasterKeys().get(0));
    }

    @Test
    public void singleKeyOaepSha1() throws GeneralSecurityException {
        addEntry("key1");
        final KeyStoreProvider mkp = new KeyStoreProvider(ks, PP, "KeyStore", "RSA/ECB/OAEPWithSHA-1AndMGF1Padding",
                "key1");
        final JceMasterKey mk1 = mkp.getMasterKey("key1");
        final AwsCrypto crypto = new AwsCrypto();
        final CryptoResult<byte[], JceMasterKey> ct = crypto.encryptData(mkp, PLAINTEXT);
        assertEquals(1, ct.getMasterKeyIds().size());
        final CryptoResult<byte[], JceMasterKey> result = crypto.decryptData(mkp, ct.getResult());
        assertArrayEquals(PLAINTEXT, result.getResult());
        // Only the first found key should be used
        assertEquals(1, result.getMasterKeys().size());
        assertEquals(mk1, result.getMasterKeys().get(0));
    }

    @Test
    public void singleKeyOaepSha256() throws GeneralSecurityException {
        addEntry("key1");
        final KeyStoreProvider mkp = new KeyStoreProvider(ks, PP, "KeyStore", "RSA/ECB/OAEPWithSHA-256AndMGF1Padding",
                "key1");
        final JceMasterKey mk1 = mkp.getMasterKey("key1");
        final AwsCrypto crypto = new AwsCrypto();
        final CryptoResult<byte[], JceMasterKey> ct = crypto.encryptData(mkp, PLAINTEXT);
        assertEquals(1, ct.getMasterKeyIds().size());
        final CryptoResult<byte[], JceMasterKey> result = crypto.decryptData(mkp, ct.getResult());
        assertArrayEquals(PLAINTEXT, result.getResult());
        // Only the first found key should be used
        assertEquals(1, result.getMasterKeys().size());
        assertEquals(mk1, result.getMasterKeys().get(0));
    }

    @Test
    public void multipleKeys() throws GeneralSecurityException {
        addEntry("key1");
        addEntry("key2");
        final KeyStoreProvider mkp = new KeyStoreProvider(ks, PP, "KeyStore", "RSA/ECB/OAEPWithSHA-256AndMGF1Padding",
                "key1",
                "key2");
        @SuppressWarnings("unused")
        final JceMasterKey mk1 = mkp.getMasterKey("key1");
        final JceMasterKey mk2 = mkp.getMasterKey("key2");
        final AwsCrypto crypto = new AwsCrypto();
        final CryptoResult<byte[], JceMasterKey> ct = crypto.encryptData(mkp, PLAINTEXT);
        assertEquals(2, ct.getMasterKeyIds().size());
        CryptoResult<byte[], JceMasterKey> result = crypto.decryptData(mkp, ct.getResult());
        assertArrayEquals(PLAINTEXT, result.getResult());
        // Order is non-deterministic
        assertEquals(1, result.getMasterKeys().size());

        // Delete the first key and see if it works
        ks.deleteEntry("key1");
        result = crypto.decryptData(mkp, ct.getResult());
        assertArrayEquals(PLAINTEXT, result.getResult());
        // Only the first found key should be used
        assertEquals(1, result.getMasterKeys().size());
        assertEquals(mk2, result.getMasterKeys().get(0));
    }

    @Test(expected = CannotUnwrapDataKeyException.class)
    public void encryptOnly() throws GeneralSecurityException {
        addPublicEntry("key1");
        final KeyStoreProvider mkp = new KeyStoreProvider(ks, PP, "KeyStore", "RSA/ECB/OAEPWithSHA-256AndMGF1Padding",
                "key1");
        final AwsCrypto crypto = new AwsCrypto();
        final CryptoResult<byte[], JceMasterKey> ct = crypto.encryptData(mkp, PLAINTEXT);
        assertEquals(1, ct.getMasterKeyIds().size());
        crypto.decryptData(mkp, ct.getResult());
    }

    @Test
    public void escrowAndSymmetric() throws GeneralSecurityException {
        addPublicEntry("key1");
        addEntry("key2");
        final KeyStoreProvider mkp = new KeyStoreProvider(ks, PP, "KeyStore", "RSA/ECB/OAEPWithSHA-256AndMGF1Padding",
                "key1",
                "key2");
        @SuppressWarnings("unused")
        final JceMasterKey mk1 = mkp.getMasterKey("key1");
        final JceMasterKey mk2 = mkp.getMasterKey("key2");
        final AwsCrypto crypto = new AwsCrypto();
        final CryptoResult<byte[], JceMasterKey> ct = crypto.encryptData(mkp, PLAINTEXT);
        assertEquals(2, ct.getMasterKeyIds().size());
        CryptoResult<byte[], JceMasterKey> result = crypto.decryptData(mkp, ct.getResult());
        assertArrayEquals(PLAINTEXT, result.getResult());
        // Only could have decrypted with the keypair
        assertEquals(1, result.getMasterKeys().size());
        assertEquals(mk2, result.getMasterKeys().get(0));

        // Delete the first key and see if it works
        ks.deleteEntry("key1");
        result = crypto.decryptData(mkp, ct.getResult());
        assertArrayEquals(PLAINTEXT, result.getResult());
        // Only the first found key should be used
        assertEquals(1, result.getMasterKeys().size());
        assertEquals(mk2, result.getMasterKeys().get(0));
    }

    @Test
    public void escrowAndSymmetricSecondProvider() throws GeneralSecurityException {
        addPublicEntry("key1");
        addEntry("key2");
        final KeyStoreProvider mkp = new KeyStoreProvider(ks, PP, "KeyStore", "RSA/ECB/OAEPWithSHA-256AndMGF1Padding",
                "key1",
                "key2");
        @SuppressWarnings("unused")
        final JceMasterKey mk1 = mkp.getMasterKey("key1");
        final JceMasterKey mk2 = mkp.getMasterKey("key2");
        final AwsCrypto crypto = new AwsCrypto();
        final CryptoResult<byte[], JceMasterKey> ct = crypto.encryptData(mkp, PLAINTEXT);
        assertEquals(2, ct.getMasterKeyIds().size());

        final KeyStoreProvider mkp2 = new KeyStoreProvider(ks, PP, "KeyStore", "RSA/ECB/OAEPWithSHA-256AndMGF1Padding",
                "key1");
        CryptoResult<byte[], JceMasterKey> result = crypto.decryptData(mkp2, ct.getResult());
        assertArrayEquals(PLAINTEXT, result.getResult());
        // Only could have decrypted with the keypair
        assertEquals(1, result.getMasterKeys().size());
        assertEquals(mk2, result.getMasterKeys().get(0));
    }

    @Test
    public void escrowCase() throws GeneralSecurityException, IOException {
        addEntry("escrowKey");
        KeyStore ks2 = KeyStore.getInstance(KeyStore.getDefaultType());
        ks2.load(null, PASSWORD);
        copyPublicPart(ks, ks2, "escrowKey");

        final KeyStoreProvider mkp = new KeyStoreProvider(ks, PP, "KeyStore", "RSA/ECB/OAEPWithSHA-256AndMGF1Padding",
                "escrowKey");
        final KeyStoreProvider escrowProvider = new KeyStoreProvider(ks2, PP, "KeyStore",
                "RSA/ECB/OAEPWithSHA-256AndMGF1Padding",
                "escrowKey");

        final JceMasterKey mk1 = escrowProvider.getMasterKey("escrowKey");
        final AwsCrypto crypto = new AwsCrypto();
        final CryptoResult<byte[], JceMasterKey> ct = crypto.encryptData(escrowProvider, PLAINTEXT);
        assertEquals(1, ct.getMasterKeyIds().size());

        try {
            crypto.decryptData(escrowProvider, ct.getResult());
            fail("Expected CannotUnwrapDataKeyException");
        } catch (final CannotUnwrapDataKeyException ex) {
            // expected
        }
        CryptoResult<byte[], JceMasterKey> result = crypto.decryptData(mkp, ct.getResult());
        assertArrayEquals(PLAINTEXT, result.getResult());
        // Only could have decrypted with the keypair
        assertEquals(1, result.getMasterKeys().size());
        assertEquals(mk1, result.getMasterKeys().get(0));
    }

    @Test
    public void keystoreAndRawProvider() throws GeneralSecurityException, IOException {
        addEntry("key1");
        final SecretKeySpec k1 = new SecretKeySpec(generate(32), "AES");
        final JceMasterKey jcep = JceMasterKey.getInstance(k1, "jce", "1", "AES/GCM/NoPadding");
        final KeyStoreProvider ksp = new KeyStoreProvider(ks, PP, "KeyStore", "RSA/ECB/OAEPWithSHA-256AndMGF1Padding",
                "key1");

        MasterKeyProvider<JceMasterKey> multiProvider = MultipleProviderFactory.buildMultiProvider(JceMasterKey.class,
                jcep, ksp);

        assertEquals(jcep, multiProvider.getMasterKey("jce", "1"));

        final AwsCrypto crypto = new AwsCrypto();
        final CryptoResult<byte[], JceMasterKey> ct = crypto.encryptData(multiProvider, PLAINTEXT);
        assertEquals(2, ct.getMasterKeyIds().size());
        CryptoResult<byte[], JceMasterKey> result = crypto.decryptData(multiProvider, ct.getResult());
        assertArrayEquals(PLAINTEXT, result.getResult());
        assertEquals(jcep, result.getMasterKeys().get(0));

        // Decrypt just using each individually
        assertArrayEquals(PLAINTEXT, crypto.decryptData(jcep, ct.getResult()).getResult());
        assertArrayEquals(PLAINTEXT, crypto.decryptData(ksp, ct.getResult()).getResult());
    }

    private void addEntry(final String alias) throws GeneralSecurityException {
        final KeyPair pair = KG.generateKeyPair();
        // build a certificate generator
        final X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
        final X500Principal dnName = new X500Principal("cn=" + alias);

        certGen.setSerialNumber(new BigInteger(256, RND));
        certGen.setSubjectDN(new X509Name("dc=" + alias));
        certGen.setIssuerDN(dnName); // use the same
        certGen.setNotBefore(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000));
        certGen.setNotAfter(new Date(System.currentTimeMillis() + 2 * 365 * 24 * 60 * 60 * 1000));
        certGen.setPublicKey(pair.getPublic());
        certGen.setSignatureAlgorithm("SHA256WithRSA");
        final X509Certificate cert = certGen.generate(pair.getPrivate(), "BC");

        ks.setEntry(alias, new KeyStore.PrivateKeyEntry(pair.getPrivate(), new X509Certificate[] { cert }), PP);
    }

    private void addPublicEntry(final String alias) throws GeneralSecurityException {
        final KeyPair pair = KG.generateKeyPair();
        // build a certificate generator
        final X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
        final X500Principal dnName = new X500Principal("cn=" + alias);

        certGen.setSerialNumber(new BigInteger(256, RND));
        certGen.setSubjectDN(new X509Name("dc=" + alias));
        certGen.setIssuerDN(dnName); // use the same
        certGen.setNotBefore(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000));
        certGen.setNotAfter(new Date(System.currentTimeMillis() + 2 * 365 * 24 * 60 * 60 * 1000));
        certGen.setPublicKey(pair.getPublic());
        certGen.setSignatureAlgorithm("SHA256WithRSA");
        final X509Certificate cert = certGen.generate(pair.getPrivate(), "BC");

        ks.setEntry(alias, new KeyStore.TrustedCertificateEntry(cert), null);
    }

    private void copyPublicPart(final KeyStore src, final KeyStore dst, final String alias) throws KeyStoreException {
        Certificate cert = src.getCertificate(alias);
        dst.setCertificateEntry(alias, cert);
    }
}
