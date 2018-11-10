/*
 * Copyright 2014-2018 Web Firm Framework
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
package com.webfirmframework.wffweb.tag.html.attribute.global;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.webfirmframework.wffweb.tag.html.html5.attribute.AutoComplete;

public class ClassAttributeTest {

    @Test
    public void testClassAttribute() {
        // fail("Not yet implemented");
    }

    @Test
    public void testClassAttributeString() {
        {
            ClassAttribute classAttribute = new ClassAttribute("class1 class2");
            assertEquals("class=\"class1 class2\"",
                    classAttribute.toHtmlString());
        }
        {
            ClassAttribute classAttribute = new ClassAttribute("class1");
            assertEquals("class=\"class1\"", classAttribute.toHtmlString());
        }

        {
            ClassAttribute classAttribute = new ClassAttribute("class1",
                    "class2");
            assertEquals("class=\"class1 class2\"",
                    classAttribute.toHtmlString());
        }
    }

    @Test
    public void testAddClassNames() {
        // fail("Not yet implemented");
    }

    @Test
    public void testAddNewClassNames() {
        // fail("Not yet implemented");
    }

    @Test
    public void testAddAllClassNames() {
        // fail("Not yet implemented");
    }

    @Test
    public void testRemoveAllClassNames() {
        // fail("Not yet implemented");
    }

    @Test
    public void testRemoveClassName() {
        // fail("Not yet implemented");
    }
    
    @Test
    public void testGetAttributeValue() {
        Assert.assertNotNull(new ClassAttribute("one two").getAttributeValue());
        Assert.assertEquals("one two three four five six", new ClassAttribute("one two three four five six").getAttributeValue());
        
    }
    
    @Test
    public void testContains() {
        {
            ClassAttribute classAttribute = new ClassAttribute(AutoComplete.NAME);
            classAttribute.setValue(false, AutoComplete.NAME + " "
                    + AutoComplete.EMAIL + " " + AutoComplete.USERNAME);
            Assert.assertTrue(classAttribute.contains(AutoComplete.NAME));
            Assert.assertTrue(classAttribute.contains(AutoComplete.USERNAME));
            Assert.assertFalse(classAttribute.contains(AutoComplete.ADDRESS_LINE2));
        }
        {
            ClassAttribute classAttribute = new ClassAttribute();
            Assert.assertFalse(classAttribute.contains(AutoComplete.ADDRESS_LINE2));
        }
    }
    
    @Test
    public void testContainsAll() {
        {
            ClassAttribute classAttribute = new ClassAttribute();
            Assert.assertFalse(classAttribute.containsAll(Arrays.asList(AutoComplete.ADDRESS_LINE2)));
        }
        ClassAttribute classAttribute = new ClassAttribute(AutoComplete.NAME);
        classAttribute.setValue(false, AutoComplete.NAME + " "
                + AutoComplete.EMAIL + " " + AutoComplete.USERNAME);
        Assert.assertTrue(classAttribute.containsAll(Arrays.asList(AutoComplete.NAME, AutoComplete.USERNAME, AutoComplete.EMAIL)));
        Assert.assertTrue(classAttribute.containsAll(Arrays.asList(AutoComplete.USERNAME)));
        Assert.assertFalse(classAttribute.containsAll(Arrays.asList(AutoComplete.ADDRESS_LINE2)));
        Assert.assertFalse(classAttribute.containsAll(Arrays.asList(AutoComplete.NAME, AutoComplete.ADDRESS_LINE2)));
        
    }

}
