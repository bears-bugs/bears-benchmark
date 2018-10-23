/*
 * Copyright 2015 MiLaboratory.com
 *
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
package com.milaboratory.util;

import org.junit.Assert;
import org.junit.Test;

public class VersionInfoTest {
    @Test
    public void test1() throws Exception {
        VersionInfo versionInfo = VersionInfo.getVersionInfo("/util/milib-build.properties");
        Assert.assertEquals("1.1.3-SNAPSHOT", versionInfo.version);
        Assert.assertEquals("6a235a64bf84b829f28b0ef3cfb03bd41d2e74eb", versionInfo.revision);
    }

    @Test
    public void test2() throws Exception {
        VersionInfo versionInfo = VersionInfo.getVersionInfo("/util/asdafas");
        Assert.assertNull(versionInfo);
    }

    @Test
    public void test3() throws Exception {
        VersionInfo versionInfo = VersionInfo.getVersionInfoForArtifact("milib");
        System.out.println(versionInfo);
    }
}