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

package com.amazonaws.encryptionsdk;

import static com.amazonaws.encryptionsdk.AwsCrypto.getDefaultFrameSize;
import static com.amazonaws.encryptionsdk.FastTestsOnlySuite.isFastTestSuiteActive;
import static com.amazonaws.encryptionsdk.TestUtils.assertThrows;
import static com.amazonaws.encryptionsdk.TestUtils.insecureRandomBytes;
import static com.amazonaws.encryptionsdk.TestUtils.toByteArray;
import static com.amazonaws.encryptionsdk.internal.TestIOUtils.getSha256Hash;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.ArgumentCaptor;

import com.amazonaws.encryptionsdk.exception.BadCiphertextException;
import com.amazonaws.encryptionsdk.internal.TestIOUtils;
import com.amazonaws.encryptionsdk.jce.JceMasterKey;
import com.amazonaws.encryptionsdk.model.EncryptionMaterialsRequest;

@RunWith(Enclosed.class)
public class CryptoOutputStreamTest {
    private static final SecureRandom RND = new SecureRandom();
    private static final MasterKey<JceMasterKey> customerMasterKey;
    private static final AtomicReference<byte[]> RANDOM_BUFFER = new AtomicReference<>(new byte[0]);

    static {
        byte[] rawKey = new byte[16];
        RND.nextBytes(rawKey);
        customerMasterKey = JceMasterKey.getInstance(new SecretKeySpec(rawKey, "AES"), "mockProvider", "mockKey",
                                                     "AES/GCM/NoPadding");
    }

    private static void testRoundTrip(
            int dataSize,
            Consumer<AwsCrypto> customizer,
            Callback onEncrypt,
            Callback onDecrypt
    ) throws Exception {
        AwsCrypto awsCrypto = new AwsCrypto();
        customizer.accept(awsCrypto);

        byte[] plaintext = insecureRandomBytes(dataSize);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(plaintext);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        onEncrypt.process(awsCrypto, inputStream, outputStream);

        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        outputStream = new ByteArrayOutputStream();

        onDecrypt.process(awsCrypto, inputStream, outputStream);

        assertArrayEquals(getSha256Hash(plaintext), getSha256Hash(outputStream.toByteArray()));
    }

    private interface Callback {
        void process(AwsCrypto crypto, InputStream inStream, OutputStream outStream) throws Exception;
    }

    private static Callback encryptWithContext(Map<String, String> encryptionContext) {
        return (awsCrypto, inStream, outStream) -> {
            final OutputStream encryptionOutStream = awsCrypto.createEncryptingStream(
                    customerMasterKey,
                    outStream,
                    encryptionContext);

            TestIOUtils.copyInStreamToOutStream(inStream, encryptionOutStream);
        };
    }

    private static Callback encryptWithoutContext() {
        return (awsCrypto, inStream, outStream) -> {
            final OutputStream encryptionOutStream = awsCrypto.createEncryptingStream(
                    customerMasterKey,
                    outStream);

            TestIOUtils.copyInStreamToOutStream(inStream, encryptionOutStream);
        };
    }

    private static Callback basicDecrypt(int readLen) {
        return (awsCrypto, inStream, outStream) -> {
            final OutputStream decryptionOutStream = awsCrypto.createDecryptingStream(
                    customerMasterKey,
                    outStream);

            TestIOUtils.copyInStreamToOutStream(inStream, decryptionOutStream, readLen);
        };
    }

    private static Callback basicDecrypt() {
        return (awsCrypto, inStream, outStream) -> {
            final OutputStream decryptionOutStream = awsCrypto.createDecryptingStream(
                    customerMasterKey,
                    outStream);

            TestIOUtils.copyInStreamToOutStream(inStream, decryptionOutStream);
        };
    }

    @RunWith(Parameterized.class)
    public static class ParameterizedEncryptDecryptTest {
        private final CryptoAlgorithm cryptoAlg;
        private final int byteSize, frameSize, readLen;

        public ParameterizedEncryptDecryptTest(
                CryptoAlgorithm cryptoAlg, int byteSize, int frameSize, int readLen
        ) {
            this.cryptoAlg = cryptoAlg;
            this.byteSize = byteSize;
            this.frameSize = frameSize;
            this.readLen = readLen;
        }

        @Parameterized.Parameters(name="{index}: encryptDecrypt(algorithm={0}, byteSize={1}, frameSize={2}, readLen={3})")
        public static Collection<Object[]> encryptDecryptParams() {
            ArrayList<Object[]> cases = new ArrayList<>();

            for (final CryptoAlgorithm cryptoAlg : EnumSet.allOf(CryptoAlgorithm.class)) {
              final int[] frameSizeToTest = TestUtils.getFrameSizesToTest(cryptoAlg);

                // iterate over frame size to test
                for (int i = 0; i < frameSizeToTest.length; i++) {
                    final int frameSize = frameSizeToTest[i];
                    int[] bytesToTest = { 0, 1, frameSize - 1, frameSize, frameSize + 1, (int) (frameSize * 1.5),
                                          frameSize * 2, 1000000 };

                    if (isFastTestSuiteActive()) {
                        // Exclude the last two sizes, as they're the slowest
                        bytesToTest = Arrays.copyOfRange(bytesToTest, 0, bytesToTest.length - 2);
                    }

                    // iterate over byte size to test
                    for (int j = 0; j < bytesToTest.length; j++) {
                        final int byteSize = bytesToTest[j];
                        int[] readLenVals = { byteSize - 1, byteSize, byteSize + 1, byteSize * 2, 1000000 };

                        if (isFastTestSuiteActive()) {
                            // Only test one read() call buffer length in the fast tests. This greatly cuts down on
                            // the combinatorial explosion of test cases here.
                            readLenVals = Arrays.copyOfRange(readLenVals, 0, 1);
                        }

                        // iterate over read lengths to test
                        for (int k = 0; k < readLenVals.length; k++) {
                            final int readLen = readLenVals[k];
                            if (byteSize >= 0 && readLen > 0) {
                                cases.add(new Object[] { cryptoAlg, byteSize, frameSize, readLen });
                            }
                        }
                    }
                }
            }

            return cases;
        }

        @Test
        public void encryptDecrypt() throws Exception {
            final Map<String, String> encryptionContext = new HashMap<String, String>(1);
            encryptionContext.put("ENC", "Streaming Test");

            testRoundTrip(byteSize,
                          awsCrypto -> {
                              awsCrypto.setEncryptionFrameSize(frameSize);
                              awsCrypto.setEncryptionAlgorithm(cryptoAlg);
                          },
                          encryptWithContext(encryptionContext),
                          basicDecrypt(readLen)
            );
        }
    }

    public static class NonParameterized {
        private AwsCrypto encryptionClient_;

        public NonParameterized() {}

        @Before
        public void setup() throws IOException {
            encryptionClient_ = new AwsCrypto();
        }

        @Test
        public void singleByteWrite() throws Exception {
            final Map<String, String> encryptionContext = new HashMap<String, String>(1);
            encryptionContext.put("ENC", "Streaming Test");

            testRoundTrip(10_000,
                          awsCrypto -> {
                          },
                          (awsCrypto, inStream, outStream) -> {
                              final OutputStream encryptionOutStream = awsCrypto.createEncryptingStream(
                                      customerMasterKey,
                                      outStream,
                                      encryptionContext);

                              // write a single plaintext byte at a time
                              final byte[] writeBytes = new byte[2048];
                              int read_len = 0;
                              while (read_len >= 0) {
                                  read_len = inStream.read(writeBytes);
                                  if (read_len > 0) {
                                      for (int i = 0; i < read_len; i++) {
                                          encryptionOutStream.write(writeBytes[i]);
                                      }
                                  }
                              }

                              encryptionOutStream.close();
                          },
                          (awsCrypto, inStream, outStream) -> {
                              final OutputStream decryptionOutStream = awsCrypto.createDecryptingStream(
                                      customerMasterKey,
                                      outStream);

                              // write a single decrypted byte at a time
                              final byte[] writeBytes = new byte[2048];
                              int read_len = 0;
                              while (read_len >= 0) {
                                  read_len = inStream.read(writeBytes);
                                  if (read_len > 0) {
                                      for (int i = 0; i < read_len; i++) {
                                          decryptionOutStream.write(writeBytes[i]);
                                      }
                                  }
                              }

                              decryptionOutStream.close();
                          }
            );
        }

        @Test
        public void doEncryptDecryptWithoutEncContext() throws Exception {
            testRoundTrip(1_000_000,
                          awsCrypto -> {
                          },
                          encryptWithoutContext(),
                          basicDecrypt()
            );
        }

        @Test
        public void doEncryptDecryptWithContext() throws Exception {
            Map<String, String> encryptionContext = new HashMap<String, String>(1);
            encryptionContext.put("ENC", "Streaming Test: inputStreamCompatiblilty");

            testRoundTrip(1_000_000,
                          awsCrypto -> awsCrypto.setEncryptionFrameSize(getDefaultFrameSize()),
                          encryptWithContext(encryptionContext),
                          basicDecrypt()
            );
        }

        @Test
        public void encryptOneShotDecryptStream() throws Exception {
            Map<String, String> encryptionContext = new HashMap<String, String>(1);
            encryptionContext.put("ENC", "encryptAPICompatibility");

            testRoundTrip(1_000_000,
                          awsCrypto -> {
                          },
                          (crypto, inStream, outStream) -> {
                              outStream.write(encryptionClient_.encryptData(
                                      customerMasterKey,
                                      toByteArray(inStream),
                                      encryptionContext).getResult());
                          },
                          (crypto, inStream, outStream) -> {
                              final OutputStream decryptionOutStream = encryptionClient_.createDecryptingStream(
                                      customerMasterKey,
                                      outStream);

                              decryptionOutStream.write(toByteArray(inStream));
                              decryptionOutStream.close();
                          }
            );
        }

        @Test
        public void encryptStreamDecryptOneShot() throws Exception {
            Map<String, String> encryptionContext = new HashMap<String, String>(1);
            encryptionContext.put("ENC", "decryptAPICompatibility");

            testRoundTrip(1_000_000,
                          awsCrypto -> {
                          },
                          (crypto, inStream, outStream) -> {
                              final OutputStream encryptionOutStream = encryptionClient_.createEncryptingStream(
                                      customerMasterKey,
                                      outStream,
                                      encryptionContext);

                              TestIOUtils.copyInStreamToOutStream(inStream, encryptionOutStream);
                          },
                          (crypto, inStream, outStream) -> {
                              outStream.write(
                                      encryptionClient_.decryptData(customerMasterKey,
                                                                    toByteArray(inStream)).getResult());
                          }
            );
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullWrite() throws IOException {
            final OutputStream outStream = new ByteArrayOutputStream();
            final OutputStream encryptionOutStream = encryptionClient_.createEncryptingStream(
                    customerMasterKey,
                    outStream);

            encryptionOutStream.write(null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullWrite2() throws BadCiphertextException, IOException {
            final OutputStream outStream = new ByteArrayOutputStream();
            final OutputStream encryptionOutStream = encryptionClient_.createEncryptingStream(
                    customerMasterKey,
                    outStream);

            encryptionOutStream.write(null, 0, 0);
        }

        @Test(expected = IllegalArgumentException.class)
        public void negativeWriteLen() throws BadCiphertextException, IOException {
            final OutputStream outStream = new ByteArrayOutputStream();
            final OutputStream encryptionOutStream = encryptionClient_.createEncryptingStream(
                    customerMasterKey,
                    outStream);

            final byte[] writeBytes = new byte[0];
            encryptionOutStream.write(writeBytes, 0, -1);
        }

        @Test(expected = IllegalArgumentException.class)
        public void negativeWriteOffset() throws BadCiphertextException, IOException {
            final OutputStream outStream = new ByteArrayOutputStream();
            final OutputStream encryptionOutStream = encryptionClient_.createEncryptingStream(
                    customerMasterKey,
                    outStream);

            final byte[] writeBytes = new byte[2048];
            encryptionOutStream.write(writeBytes, -1, writeBytes.length);
        }

        @Test
        public void checkInvalidValues() throws Exception {
            // test for the two formats - single-block and frame.
            final int[] frameSizeToTest = {0, getDefaultFrameSize()};

            // iterate over frame size to test
            for (int i = 0; i < frameSizeToTest.length; i++) {
                final int frameSize = frameSizeToTest[i];
                invalidWriteLen(frameSize);
                invalidWriteOffset(frameSize);
                noOpStream(frameSize);
            }
        }

        private void invalidWriteLen(final int frameSize) throws BadCiphertextException, IOException {
            AwsCrypto awsCrypto = new AwsCrypto();

            awsCrypto.setEncryptionFrameSize(frameSize);

            final OutputStream outStream = new ByteArrayOutputStream();
            final OutputStream encryptionOutStream = awsCrypto.createEncryptingStream(
                    customerMasterKey,
                    outStream);

            final byte[] writeBytes = new byte[2048];

            assertThrows(IndexOutOfBoundsException.class,
                         () -> encryptionOutStream.write(writeBytes, 0, 2 * writeBytes.length));

        }

        private void invalidWriteOffset(final int frameSize) throws BadCiphertextException, IOException {
            AwsCrypto awsCrypto = new AwsCrypto();

            awsCrypto.setEncryptionFrameSize(frameSize);

            final OutputStream outStream = new ByteArrayOutputStream();
            final OutputStream encryptionOutStream = awsCrypto.createEncryptingStream(
                    customerMasterKey,
                    outStream);

            final byte[] writeBytes = new byte[2048];

            assertThrows(IndexOutOfBoundsException.class,
                         () -> encryptionOutStream.write(writeBytes, writeBytes.length + 1, writeBytes.length));
        }

        private void noOpStream(final int frameSize) throws Exception {
            final Map<String, String> encryptionContext = new HashMap<String, String>(1);
            encryptionContext.put("ENC", "noOpStream size " + frameSize);

            final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            final OutputStream encryptionOutStream = encryptionClient_.createEncryptingStream(
                    customerMasterKey,
                    outStream,
                    encryptionContext);

            encryptionOutStream.close();

            testRoundTrip(0,
                          crypto -> crypto.setEncryptionFrameSize(frameSize),
                          encryptWithContext(encryptionContext),
                          basicDecrypt()
                          );
        }

        @Test
        public void decryptEmptyFile() throws IOException {
            final InputStream inStream = new ByteArrayInputStream(new byte[0]);
            final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            final OutputStream decryptionOutStream = encryptionClient_.createDecryptingStream(
                    customerMasterKey,
                    outStream);

            TestIOUtils.copyInStreamToOutStream(inStream, decryptionOutStream);
            inStream.close();
            decryptionOutStream.close();

            assertEquals(0, outStream.size());
        }

        @Test
        public void checkEncContext() throws Exception {
            Map<String, String> context = new HashMap<String, String>(1);
            context.put("ENC", "Streaming Test " + UUID.randomUUID());

            testRoundTrip(1,
                          awsCrypto -> {
                          },
                          encryptWithContext(context),
                          (crypto, inStream, outStream) -> {
                              final CryptoOutputStream<JceMasterKey> decryptionOutStream
                                      = encryptionClient_.createDecryptingStream(
                                      customerMasterKey,
                                      outStream);

                              TestIOUtils.copyInStreamToOutStream(inStream, decryptionOutStream);

                              Map<String, String> getEncryptionContext = decryptionOutStream.getCryptoResult()
                                                                                            .getEncryptionContext();

                              // Since more values may have been added, we need to check to ensure that all
                              // of setEncryptionContext is present, not that there is nothing else
                              for (final Map.Entry<String, String> e : context.entrySet()) {
                                  assertEquals(e.getValue(), getEncryptionContext.get(e.getKey()));
                              }
                          }
            );
        }

        @Test
        public void checkKeyId() throws Exception {
            Map<String, String> context = new HashMap<String, String>(1);
            context.put("ENC", "Streaming Test " + UUID.randomUUID());

            testRoundTrip(1,
                          awsCrypto -> {
                          },
                          encryptWithContext(context),
                          (crypto, inStream, outStream) -> {
                              final CryptoOutputStream<JceMasterKey> decryptionOutStream
                                      = encryptionClient_.createDecryptingStream(
                                      customerMasterKey,
                                      outStream);

                              TestIOUtils.copyInStreamToOutStream(inStream, decryptionOutStream);

                              final String returnedKeyId = decryptionOutStream.getCryptoResult()
                                                                              .getMasterKeys()
                                                                              .get(0)
                                                                              .getKeyId();

                              assertEquals("mockKey", returnedKeyId);
                          }
            );
        }

        @Test
        public void whenGetResultCalledTooEarly_exceptionThrown() throws Exception {
            testRoundTrip(
                    1024,
                    awsCrypto -> {},
                    (awsCrypto2, inStream, outStream) -> {
                        final CryptoOutputStream<?> encryptionOutStream = awsCrypto2.createEncryptingStream(
                                customerMasterKey, outStream
                        );

                        // we should be able to get the cryptoResult on the encrypt path immediately
                        encryptionOutStream.getCryptoResult();

                        TestIOUtils.copyInStreamToOutStream(inStream, encryptionOutStream);
                    },
                    (awsCrypto1, inStream, outStream) -> {
                        final CryptoOutputStream<?> decryptionOutStream = awsCrypto1.createDecryptingStream(
                                customerMasterKey, outStream
                        );

                        // Can't get headers until we write them to the outstream
                        assertThrows(IllegalStateException.class, decryptionOutStream::getCryptoResult);

                        TestIOUtils.copyInStreamToOutStream(inStream, decryptionOutStream);

                        // Now we can get headers
                        decryptionOutStream.getCryptoResult();
                    }
            );
        }

        @Test
        public void encryptUsingCryptoMaterialsManager() throws Exception {
            RecordingMaterialsManager cmm = new RecordingMaterialsManager(customerMasterKey);

            testRoundTrip(
                    1024,
                    awsCrypto -> {},
                    (crypto, inStream, outStream) -> {
                        final CryptoOutputStream<?> cryptoStream = crypto.createEncryptingStream(cmm, outStream);

                        TestIOUtils.copyInStreamToOutStream(inStream, cryptoStream);

                        assertEquals("bar", cryptoStream.getCryptoResult().getEncryptionContext().get("foo"));
                    },
                    basicDecrypt()
            );
        }

        @Test
        public void decryptUsingCryptoMaterialsManager() throws Exception {
            RecordingMaterialsManager cmm = new RecordingMaterialsManager(customerMasterKey);

            testRoundTrip(
                    1024,
                    awsCrypto -> {},
                    encryptWithoutContext(),
                    (crypto, inStream, outStream) -> {
                        final CryptoOutputStream<?> cryptoStream = crypto.createDecryptingStream(cmm, outStream);

                        assertFalse(cmm.didDecrypt);

                        TestIOUtils.copyInStreamToOutStream(inStream, cryptoStream);

                        assertTrue(cmm.didDecrypt);
                    }
            );
        }

        @Test
        public void whenStreamSizeSetEarly_streamSizePassedToCMM() throws Exception {
            CryptoMaterialsManager cmm = spy(new DefaultCryptoMaterialsManager(customerMasterKey));

            CryptoOutputStream<?> os = new AwsCrypto().createEncryptingStream(cmm, new ByteArrayOutputStream());

            os.setMaxInputLength(1);

            os.write(0);

            ArgumentCaptor<EncryptionMaterialsRequest> captor = ArgumentCaptor.forClass(EncryptionMaterialsRequest.class);
            verify(cmm).getMaterialsForEncrypt(captor.capture());

            assertEquals(1L, captor.getValue().getPlaintextSize());
        }

        @Test
        public void whenStreamSizeSetEarly_andExceeded_exceptionThrown() throws Exception {
            CryptoMaterialsManager cmm = spy(new DefaultCryptoMaterialsManager(customerMasterKey));

            CryptoOutputStream<?> os = new AwsCrypto().createEncryptingStream(cmm, new ByteArrayOutputStream());

            os.setMaxInputLength(1);
            os.write(0);

            assertThrows(()-> os.write(0));
        }
        @Test
        public void whenStreamSizeSetLate_andExceeded_exceptionThrown() throws Exception {
            CryptoMaterialsManager cmm = spy(new DefaultCryptoMaterialsManager(customerMasterKey));

            CryptoOutputStream<?> os = new AwsCrypto().createEncryptingStream(cmm, new ByteArrayOutputStream());

            os.write(0);
            os.setMaxInputLength(1);

            assertThrows(() -> os.write(0));
        }

        @Test
        public void whenStreamSizeSet_afterBeingExceeded_exceptionThrown() throws Exception {
            CryptoMaterialsManager cmm = spy(new DefaultCryptoMaterialsManager(customerMasterKey));

            CryptoOutputStream<?> os = new AwsCrypto().createEncryptingStream(cmm, new ByteArrayOutputStream());

            os.write(0);
            os.write(0);

            assertThrows(() -> os.setMaxInputLength(1));
        }

        @Test
        public void whenStreamSizeNegative_setSizeThrows() throws Exception {
            CryptoOutputStream<?> is
                    = new AwsCrypto().createEncryptingStream(customerMasterKey, new ByteArrayOutputStream());

            assertThrows(() -> is.setMaxInputLength(-1));
        }

        @Test
        public void whenStreamSizeSet_roundTripSucceeds() throws Exception {
            testRoundTrip(
                    1024,
                    ignored -> {},
                    (awsCrypto, inStream, outStream) -> {
                        final CryptoOutputStream<?> encryptionOutStream = awsCrypto.createEncryptingStream(
                                customerMasterKey,
                                outStream);

                        encryptionOutStream.setMaxInputLength(1024);

                        TestIOUtils.copyInStreamToOutStream(inStream, encryptionOutStream);
                    },
                    (awsCrypto, inStream, outStream) -> {
                        final CryptoOutputStream<?> decryptionOutStream = awsCrypto.createDecryptingStream(
                                customerMasterKey,
                                outStream);

                        // we happen to know inStream is a ByteArrayInputStream which will give an accurate number
                        // of bytes remaining on .available()
                        decryptionOutStream.setMaxInputLength(inStream.available());

                        TestIOUtils.copyInStreamToOutStream(inStream, decryptionOutStream);
                    }
            );
        }
    }
}
