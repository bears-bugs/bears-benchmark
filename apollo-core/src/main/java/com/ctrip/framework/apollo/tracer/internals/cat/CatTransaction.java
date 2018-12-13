package com.ctrip.framework.apollo.tracer.internals.cat;

import com.ctrip.framework.apollo.tracer.spi.Transaction;

import java.lang.reflect.Method;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public class CatTransaction implements Transaction {
  private static Class CAT_TRANSACTION_CLASS;
  private static Method SET_STATUS_WITH_STRING;
  private static Method SET_STATUS_WITH_THROWABLE;
  private static Method ADD_DATA_WITH_KEY_AND_VALUE;
  private static Method COMPLETE;

  private Object catTransaction;

  static {
    try {
      CAT_TRANSACTION_CLASS = Class.forName(CatNames.CAT_TRANSACTION_CLASS);
      SET_STATUS_WITH_STRING = CAT_TRANSACTION_CLASS.getMethod(CatNames.SET_STATUS_METHOD, String.class);
      SET_STATUS_WITH_THROWABLE = CAT_TRANSACTION_CLASS.getMethod(CatNames.SET_STATUS_METHOD,
          Throwable.class);
      ADD_DATA_WITH_KEY_AND_VALUE = CAT_TRANSACTION_CLASS.getMethod(CatNames.ADD_DATA_METHOD,
          String.class, Object.class);
      COMPLETE = CAT_TRANSACTION_CLASS.getMethod(CatNames.COMPLETE_METHOD);
    } catch (Throwable ex) {
      throw new IllegalStateException("Initialize Cat transaction failed", ex);
    }
  }

  static void init() {
    //do nothing, just to initialize the static variables
  }

  public CatTransaction(Object catTransaction) {
    this.catTransaction = catTransaction;
  }

  @Override
  public void setStatus(String status) {
    try {
      SET_STATUS_WITH_STRING.invoke(catTransaction, status);
    } catch (Throwable ex) {
      throw new IllegalStateException(ex);
    }
  }

  @Override
  public void setStatus(Throwable status) {
    try {
      SET_STATUS_WITH_THROWABLE.invoke(catTransaction, status);
    } catch (Throwable ex) {
      throw new IllegalStateException(ex);
    }
  }

  @Override
  public void addData(String key, Object value) {
    try {
      ADD_DATA_WITH_KEY_AND_VALUE.invoke(catTransaction, key, value);
    } catch (Throwable ex) {
      throw new IllegalStateException(ex);
    }
  }

  @Override
  public void complete() {
    try {
      COMPLETE.invoke(catTransaction);
    } catch (Throwable ex) {
      throw new IllegalStateException(ex);
    }
  }
}
