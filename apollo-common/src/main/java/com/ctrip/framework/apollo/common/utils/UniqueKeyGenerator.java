package com.ctrip.framework.apollo.common.utils;

import com.google.common.base.Joiner;

import com.ctrip.framework.apollo.core.utils.ByteUtil;
import com.ctrip.framework.apollo.core.utils.MachineUtil;

import org.apache.commons.lang.time.FastDateFormat;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class UniqueKeyGenerator {

  private static final FastDateFormat TIMESTAMP_FORMAT = FastDateFormat.getInstance("yyyyMMddHHmmss");
  private static final AtomicInteger counter = new AtomicInteger(new SecureRandom().nextInt());
  private static final Joiner KEY_JOINER = Joiner.on("-");



  public static String generate(Object... args){
    String hexIdString =
        ByteUtil.toHexString(toByteArray(Objects.hash(args), MachineUtil.getMachineIdentifier(),
                                         counter.incrementAndGet()));

    return KEY_JOINER.join(TIMESTAMP_FORMAT.format(new Date()), hexIdString);

  }

  /**
   * Concat machine id, counter and key to byte array
   * Only retrieve lower 3 bytes of the id and counter and 2 bytes of the keyHashCode
   */
  protected static byte[] toByteArray(int keyHashCode, int machineIdentifier, int counter) {
    byte[] bytes = new byte[8];
    bytes[0] = ByteUtil.int1(keyHashCode);
    bytes[1] = ByteUtil.int0(keyHashCode);
    bytes[2] = ByteUtil.int2(machineIdentifier);
    bytes[3] = ByteUtil.int1(machineIdentifier);
    bytes[4] = ByteUtil.int0(machineIdentifier);
    bytes[5] = ByteUtil.int2(counter);
    bytes[6] = ByteUtil.int1(counter);
    bytes[7] = ByteUtil.int0(counter);
    return bytes;
  }


}
