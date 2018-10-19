/*****************************************************************************
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 ****************************************************************************/

package org.apache.xmpbox.type;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestResourceEventType extends AbstractStructuredTypeTester
{

    @Before
    public void before() throws Exception
    {
        super.before();
    }

    public TestResourceEventType(Class<? extends AbstractStructuredType> clz, String field, Types type)
    {
        super(clz, field, type);
    }

    @Override
    protected AbstractStructuredType getStructured()
    {
        return new ResourceEventType(xmp);
    }

    @Parameters
    public static Collection<Object[]> initializeParameters() throws Exception
    {
        Collection<Object[]> result = new ArrayList<>();

        result.add(new Object[] { ResourceEventType.class, "action", Types.Choice });
        result.add(new Object[] { ResourceEventType.class, "changed", Types.Text });
        result.add(new Object[] { ResourceEventType.class, "instanceID", Types.GUID });
        result.add(new Object[] { ResourceEventType.class, "parameters", Types.Text });
        result.add(new Object[] { ResourceEventType.class, "softwareAgent", Types.AgentName });
        result.add(new Object[] { ResourceEventType.class, "when", Types.Date });

        return result;

    }

}
