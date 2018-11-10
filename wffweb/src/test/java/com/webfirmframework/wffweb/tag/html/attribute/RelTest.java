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
package com.webfirmframework.wffweb.tag.html.attribute;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.webfirmframework.wffweb.tag.html.html5.attribute.AutoComplete;

public class RelTest {

    @Test
    public void testRelString() {
        {
            Rel rel = new Rel(Rel.NOOPENER);
            assertEquals("rel=\"noopener\"", rel.toHtmlString());
            
        }
        {
            Rel rel = new Rel(Rel.NOOPENER.concat(" ").concat(Rel.NOREFERRER));
            assertEquals("rel=\"noopener noreferrer\"", rel.toHtmlString());
            
        }
        
    }

    @Test
    public void testRelStringArray() {
        {
            Rel rel = new Rel(Rel.NOOPENER);
            assertEquals("rel=\"noopener\"", rel.toHtmlString());
            
        }
        {
            Rel rel = new Rel(Rel.NOOPENER, Rel.NOREFERRER);
            assertEquals("rel=\"noopener noreferrer\"", rel.toHtmlString());
            
        }
        {
            Rel rel = new Rel(Rel.NOOPENER.concat(" ").concat(Rel.NOREFERRER), Rel.NOFOLLOW);
            assertEquals("rel=\"noopener noreferrer nofollow\"", rel.toHtmlString());
            
        }
    }
    
    @Test
    public void testContains() {
        {
            Rel rel = new Rel(AutoComplete.NAME);
            rel.setValue(false, AutoComplete.NAME + " "
                    + AutoComplete.EMAIL + " " + AutoComplete.USERNAME);
            Assert.assertTrue(rel.contains(AutoComplete.NAME));
            Assert.assertTrue(rel.contains(AutoComplete.USERNAME));
            Assert.assertFalse(rel.contains(AutoComplete.ADDRESS_LINE2));
        }
        {
            Rel rel = new Rel("");
            Assert.assertFalse(rel.contains(AutoComplete.ADDRESS_LINE2));
        }
    }
    
    @Test
    public void testContainsAll() {
        {
            Rel rel = new Rel("");
            Assert.assertFalse(rel.containsAll(Arrays.asList(AutoComplete.ADDRESS_LINE2)));
        }
        Rel rel = new Rel(AutoComplete.NAME);
        rel.setValue(false, AutoComplete.NAME + " "
                + AutoComplete.EMAIL + " " + AutoComplete.USERNAME);
        Assert.assertTrue(rel.containsAll(Arrays.asList(AutoComplete.NAME, AutoComplete.USERNAME, AutoComplete.EMAIL)));
        Assert.assertTrue(rel.containsAll(Arrays.asList(AutoComplete.USERNAME)));
        Assert.assertFalse(rel.containsAll(Arrays.asList(AutoComplete.ADDRESS_LINE2)));
        Assert.assertFalse(rel.containsAll(Arrays.asList(AutoComplete.NAME, AutoComplete.ADDRESS_LINE2)));
        
    }

}
