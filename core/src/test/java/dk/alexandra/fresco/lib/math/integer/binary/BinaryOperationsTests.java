package dk.alexandra.fresco.lib.math.integer.binary;

import dk.alexandra.fresco.framework.Application;
import dk.alexandra.fresco.framework.DRes;
import dk.alexandra.fresco.framework.TestThreadRunner.TestThread;
import dk.alexandra.fresco.framework.TestThreadRunner.TestThreadFactory;
import dk.alexandra.fresco.framework.builder.numeric.AdvancedNumeric;
import dk.alexandra.fresco.framework.builder.numeric.AdvancedNumeric.RightShiftResult;
import dk.alexandra.fresco.framework.builder.numeric.ProtocolBuilderNumeric;
import dk.alexandra.fresco.framework.sce.resources.ResourcePool;
import dk.alexandra.fresco.framework.value.SInt;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Assert;


/**
 * Generic test cases for basic finite field operations.
 * <p>
 * Can be reused by a test case for any protocol suite that implements the basic field protocol
 * factory.
 * </p>
 */
public class BinaryOperationsTests {

  /**
   * Test binary right shift of a shared secret.
   */
  public static class TestRightShift<ResourcePoolT extends ResourcePool>
      extends TestThreadFactory<ResourcePoolT, ProtocolBuilderNumeric> {

    @Override
    public TestThread<ResourcePoolT, ProtocolBuilderNumeric> next() {

      return new TestThread<ResourcePoolT, ProtocolBuilderNumeric>() {
        private final BigInteger input = BigInteger.valueOf(12332157);
        private final int shifts = 3;

        @Override
        public void test() throws Exception {
          Application<List<BigInteger>, ProtocolBuilderNumeric> app =
              (ProtocolBuilderNumeric builder) -> {
                AdvancedNumeric rightShift = builder.advancedNumeric();
                DRes<SInt> encryptedInput = builder.numeric().known(input);
                DRes<RightShiftResult> shiftedRight =
                    rightShift.rightShiftWithRemainder(encryptedInput, shifts);
                DRes<BigInteger> openResult =
                    builder.numeric().open(() -> shiftedRight.out().getResult());
                DRes<BigInteger> openRemainder =
                    builder.numeric().open(() -> shiftedRight.out().getRemainder());
                return () -> Arrays.asList(openResult.out(), openRemainder.out());
              };
              List<BigInteger> output = runApplication(app);

          Assert.assertEquals(input.shiftRight(shifts), output.get(0));
          Assert.assertEquals(input.mod(BigInteger.ONE.shiftLeft(shifts)), output.get(1));
        }
      };
    }
  }


  /**
   * Test binary right shift of a shared secret.
   */
  public static class TestBitLength<ResourcePoolT extends ResourcePool>
      extends TestThreadFactory<ResourcePoolT, ProtocolBuilderNumeric> {

    @Override
    public TestThread<ResourcePoolT, ProtocolBuilderNumeric> next() {

      return new TestThread<ResourcePoolT, ProtocolBuilderNumeric>() {
        private final BigInteger input = BigInteger.valueOf(5);

        @Override
        public void test() throws Exception {
          Application<BigInteger, ProtocolBuilderNumeric> app = builder -> {
            DRes<SInt> sharedInput = builder.numeric().known(input);
            AdvancedNumeric bitLengthBuilder = builder.advancedNumeric();
            DRes<SInt> bitLength = bitLengthBuilder.bitLength(sharedInput, input.bitLength() * 2);
            return builder.numeric().open(bitLength);
          };
          BigInteger result = runApplication(app);

          Assert.assertEquals(BigInteger.valueOf(input.bitLength()), result);
        }
      };
    }
  }

  /**
   * Test binary right shift of a shared secret.
   */
  public static class TestBits<ResourcePoolT extends ResourcePool>
      extends TestThreadFactory<ResourcePoolT, ProtocolBuilderNumeric> {

    @Override
    public TestThread<ResourcePoolT, ProtocolBuilderNumeric> next() {

      return new TestThread<ResourcePoolT, ProtocolBuilderNumeric>() {
        private final BigInteger input = BigInteger.valueOf(12345);
        private final int max = 16;

        @Override
        public void test() throws Exception {
          Application<List<BigInteger>, ProtocolBuilderNumeric> app =
              producer -> producer.seq(builder -> {
                DRes<SInt> sharedInput = builder.numeric().known(input);
                return builder.advancedNumeric().toBits(sharedInput, max);
              }).seq((seq, result) -> {
                List<DRes<BigInteger>> outs =
                    result.stream().map(seq.numeric()::open).collect(Collectors.toList());
                return () -> outs.stream().map(DRes::out).collect(Collectors.toList());
              });

          List<BigInteger> result = runApplication(app);
          for (int i = 0; i < max; i++) {
            Assert.assertEquals(result.get(i).testBit(0), input.testBit(i));
          }
        }
      };
    }
  }
}
