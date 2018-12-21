/*
 * Copyright 2014 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */

package io.vertx.ext.web.handler.impl;

import io.vertx.core.VertxException;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.shareddata.impl.ClusterSerializable;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.impl.Utils;

import java.nio.charset.StandardCharsets;

/**
 * Helper class for getting the User object internally in Vert.x-Web
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class UserHolder implements ClusterSerializable {

  public RoutingContext context;
  public User user;

  public UserHolder() {
  }

  public UserHolder(RoutingContext context) {
    this.context = context;
  }

  @Override
  public void writeToBuffer(Buffer buffer) {
    // try to get the user from the context otherwise fall back to any cached version
    User user = context != null ? context.user() : this.user;
    if (user instanceof ClusterSerializable) {
      buffer.appendByte((byte)1);
      String className = user.getClass().getName();
      if (className == null) {
        throw new IllegalStateException("Cannot serialize " + user.getClass().getName());
      }
      byte[] bytes = className.getBytes(StandardCharsets.UTF_8);
      buffer.appendInt(bytes.length);
      buffer.appendBytes(bytes);
      ClusterSerializable cs = (ClusterSerializable)user;
      cs.writeToBuffer(buffer);
    } else {
      buffer.appendByte((byte)0);
    }
  }

  @Override
  public int readFromBuffer(int pos, Buffer buffer) {
    byte b = buffer.getByte(pos++);
    if (b == (byte)1) {
      int len = buffer.getInt(pos);
      pos += 4;
      byte[] bytes = buffer.getBytes(pos, pos + len);
      pos += len;
      String className = new String(bytes, StandardCharsets.UTF_8);
      try {
        Class<?> clazz = Utils.getClassLoader().loadClass(className);
        if (!ClusterSerializable.class.isAssignableFrom(clazz)) {
          throw new ClassCastException(className + " is not ClusterSerializable");
        }
        ClusterSerializable obj = (ClusterSerializable) clazz.getDeclaredConstructor().newInstance();
        pos = obj.readFromBuffer(pos, buffer);
        user = (User) obj;
      } catch (Exception e) {
        throw new VertxException(e);
      }
    } else {
      user = null;
    }
    return pos;
  }
}
