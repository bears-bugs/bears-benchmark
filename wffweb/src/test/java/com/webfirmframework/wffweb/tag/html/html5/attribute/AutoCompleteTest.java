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
package com.webfirmframework.wffweb.tag.html.html5.attribute;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class AutoCompleteTest {

    @Test
    public void testGetAttributeValue() {
        AutoComplete autoComplete = new AutoComplete(AutoComplete.NAME);
        autoComplete.setValue(false, AutoComplete.NAME + " " + AutoComplete.EMAIL + " " + AutoComplete.USERNAME);
        assertEquals("name email username", autoComplete.getAttributeValue());
    }
    
    @Test
    public void testGetValueSet() {
        AutoComplete autoComplete = new AutoComplete(AutoComplete.NAME);
        autoComplete.setValue(false, AutoComplete.NAME + " " + AutoComplete.EMAIL + " " + AutoComplete.USERNAME);
        Set<String> values = new LinkedHashSet<String>();
        values.add(AutoComplete.NAME);
        values.add(AutoComplete.EMAIL);
        values.add(AutoComplete.USERNAME);
        assertEquals(values, autoComplete.getValueSet());
        assertEquals("[name, email, username]", autoComplete.getValueSet().toString());
    }

    @Test
    public void testAutoComplete() {
        AutoComplete autoComplete = new AutoComplete(AutoComplete.NAME + " " + AutoComplete.EMAIL + " " + AutoComplete.USERNAME);
        assertEquals("autocomplete=\"name email username\"", autoComplete.toHtmlString());
    }

    @Test
    public void testRemoveValue() {
        AutoComplete autoComplete = new AutoComplete(AutoComplete.NAME + " " + AutoComplete.EMAIL + " " + AutoComplete.USERNAME);
        assertEquals("autocomplete=\"name email username\"", autoComplete.toHtmlString());
        autoComplete.removeValue(AutoComplete.NAME);
        assertEquals("autocomplete=\"email username\"", autoComplete.toHtmlString());
    }

    @Test
    public void testRemoveValues() {
        AutoComplete autoComplete = new AutoComplete(AutoComplete.NAME + " " + AutoComplete.EMAIL + " " + AutoComplete.USERNAME);
        assertEquals("autocomplete=\"name email username\"", autoComplete.toHtmlString());
        autoComplete.removeValues(Arrays.asList(AutoComplete.EMAIL, AutoComplete.USERNAME));
        assertEquals("autocomplete=\"name\"", autoComplete.toHtmlString());
    }

    @Test
    public void testAddValues() {
        AutoComplete autoComplete = new AutoComplete(AutoComplete.NAME);
        assertEquals("autocomplete=\"name\"", autoComplete.toHtmlString());
        autoComplete.addValues(Arrays.asList(AutoComplete.EMAIL, AutoComplete.USERNAME));        
        assertEquals("autocomplete=\"name email username\"", autoComplete.toHtmlString());
    }

    @Test
    public void testAddValue() {
        AutoComplete autoComplete = new AutoComplete(AutoComplete.NAME);
        assertEquals("autocomplete=\"name\"", autoComplete.toHtmlString());
        autoComplete.addValue(AutoComplete.EMAIL);        
        autoComplete.addValue(AutoComplete.USERNAME);        
        assertEquals("autocomplete=\"name email username\"", autoComplete.toHtmlString());
    }

    @Test
    public void testSetValueString() {
        AutoComplete autoComplete = new AutoComplete(AutoComplete.NAME);
        autoComplete.setValue(AutoComplete.EMAIL + " " + AutoComplete.USERNAME);
        assertEquals("autocomplete=\"email username\"", autoComplete.toHtmlString());
    }

    @Test
    public void testSetValueBooleanString() {
        AutoComplete autoComplete = new AutoComplete(AutoComplete.NAME);
        autoComplete.setValue(false, AutoComplete.NAME + " " + AutoComplete.EMAIL + " " + AutoComplete.USERNAME);
        assertEquals("autocomplete=\"name email username\"", autoComplete.toHtmlString());
    }

    @Test
    public void testGetValue() {
        AutoComplete autoComplete = new AutoComplete(AutoComplete.NAME);
        autoComplete.setValue(false, AutoComplete.NAME + " " + AutoComplete.EMAIL + " " + AutoComplete.USERNAME);
        assertEquals("name email username", autoComplete.getValue());
    }
    
    @Test
    public void testContains() {
        {
            AutoComplete autoComplete = new AutoComplete(AutoComplete.NAME);
            autoComplete.setValue(false, AutoComplete.NAME + " "
                    + AutoComplete.EMAIL + " " + AutoComplete.USERNAME);
            Assert.assertTrue(autoComplete.contains(AutoComplete.NAME));
            Assert.assertTrue(autoComplete.contains(AutoComplete.USERNAME));
            Assert.assertFalse(autoComplete.contains(AutoComplete.ADDRESS_LINE2));
        }
        {
            AutoComplete autoComplete = new AutoComplete();
            
            Assert.assertFalse(autoComplete.contains(AutoComplete.ADDRESS_LINE2));
        }
    }
    
    @Test
    public void testContainsAll() {
        {
            AutoComplete autoComplete = new AutoComplete();
            Assert.assertFalse(autoComplete.containsAll(Arrays.asList(AutoComplete.ADDRESS_LINE2)));
        }
        AutoComplete autoComplete = new AutoComplete(AutoComplete.NAME);
        autoComplete.setValue(false, AutoComplete.NAME + " "
                + AutoComplete.EMAIL + " " + AutoComplete.USERNAME);
        Assert.assertTrue(autoComplete.containsAll(Arrays.asList(AutoComplete.NAME, AutoComplete.USERNAME, AutoComplete.EMAIL)));
        Assert.assertTrue(autoComplete.containsAll(Arrays.asList(AutoComplete.USERNAME)));
        Assert.assertFalse(autoComplete.containsAll(Arrays.asList(AutoComplete.ADDRESS_LINE2)));
        Assert.assertFalse(autoComplete.containsAll(Arrays.asList(AutoComplete.NAME, AutoComplete.ADDRESS_LINE2)));
        
    }

}
