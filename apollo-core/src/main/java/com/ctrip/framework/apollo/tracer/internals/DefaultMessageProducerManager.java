package com.ctrip.framework.apollo.tracer.internals;

import com.ctrip.framework.apollo.core.utils.ClassLoaderUtil;
import com.ctrip.framework.apollo.tracer.internals.cat.CatMessageProducer;
import com.ctrip.framework.apollo.tracer.internals.cat.CatNames;
import com.ctrip.framework.apollo.tracer.spi.MessageProducer;
import com.ctrip.framework.apollo.tracer.spi.MessageProducerManager;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public class DefaultMessageProducerManager implements MessageProducerManager {
  private static MessageProducer producer;

  public DefaultMessageProducerManager() {
    if (ClassLoaderUtil.isClassPresent(CatNames.CAT_CLASS)) {
      producer = new CatMessageProducer();
    } else {
      producer = new NullMessageProducerManager().getProducer();
    }
  }

  @Override
  public MessageProducer getProducer() {
    return producer;
  }
}
