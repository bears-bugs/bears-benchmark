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

package io.cassandrareaper.jmx;

import io.cassandrareaper.ReaperException;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class JmxProxyTest {

  @Test
  public void testVersionCompare() throws ReaperException {
    assertEquals(Integer.valueOf(0), JmxProxyImpl.versionCompare("1.0", "1.0"));
    assertEquals(Integer.valueOf(0), JmxProxyImpl.versionCompare("1000.999", "1000.999"));
    assertEquals(Integer.valueOf(-1), JmxProxyImpl.versionCompare("1.0", "1.1"));
    assertEquals(Integer.valueOf(-1), JmxProxyImpl.versionCompare("1.2", "2.1"));
    assertEquals(Integer.valueOf(1), JmxProxyImpl.versionCompare("10.0.0", "1.0"));
    assertEquals(Integer.valueOf(1), JmxProxyImpl.versionCompare("99.0.0", "9.0"));
    assertEquals(Integer.valueOf(1), JmxProxyImpl.versionCompare("99.0.10", "99.0.1"));
    assertEquals(Integer.valueOf(-1), JmxProxyImpl.versionCompare("99.0.10~1", "99.0.10~2"));
    assertEquals(Integer.valueOf(-1), JmxProxyImpl.versionCompare("2.0.17", "2.1.1"));
  }

}
