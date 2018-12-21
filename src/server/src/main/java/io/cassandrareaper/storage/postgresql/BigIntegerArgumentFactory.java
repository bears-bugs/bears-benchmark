/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.cassandrareaper.storage.postgresql;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.Argument;
import org.skife.jdbi.v2.tweak.ArgumentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides JDBI a method to map BigInteger value to a BIGINT value in database.
 */
public final class BigIntegerArgumentFactory implements ArgumentFactory<BigInteger> {

  private static final Logger LOG = LoggerFactory.getLogger(BigIntegerArgumentFactory.class);

  @Override
  public boolean accepts(Class<?> expectedType, Object value, StatementContext ctx) {
    return value instanceof BigInteger;
  }

  @Override
  public Argument build(Class<?> expectedType, final BigInteger value, StatementContext ctx) {
    return (int position, PreparedStatement statement, StatementContext ctx1) -> {
      statement.setBigDecimal(position, new BigDecimal(value));
    };
  }
}
