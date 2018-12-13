package com.ctrip.framework.apollo.tracer.spi;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public interface Transaction {
  String SUCCESS = "0";

  /**
   * Set the message status.
   *
   * @param status message status. "0" means success, otherwise error code.
   */
  public void setStatus(String status);

  /**
   * Set the message status with exception class name.
   *
   * @param e exception.
   */
  public void setStatus(Throwable e);

  /**
   * add one key-value pair to the message.
   */
  public void addData(String key, Object value);

  /**
   * Complete the message construction.
   */
  public void complete();
}
