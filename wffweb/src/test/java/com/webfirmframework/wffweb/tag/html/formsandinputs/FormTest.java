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
package com.webfirmframework.wffweb.tag.html.formsandinputs;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Assert;
import org.junit.Test;

import com.webfirmframework.wffweb.tag.html.TagNameConstants;
import com.webfirmframework.wffweb.tag.html.attribute.Name;
import com.webfirmframework.wffweb.tag.html.attribute.Type;
import com.webfirmframework.wffweb.tag.html.attribute.global.Id;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Div;

@SuppressWarnings("serial")
public class FormTest {

    @Test
    public void testGetNameBasedJsObjectSetOfString() {
        
        final Form form = new Form(null) {
            {
                new Input(this, new Name("name1"));
                new Div(this){{new Input(this, new Name("name2"));}};
                new Input(this, new Name("name3"));
                new Div(this){{new Input(this, new Name("name4"));}};
            }
        };
        
        final String jsObjectForNames = form.getNameBasedJsObject(
                new HashSet<String>(Arrays.asList("input")));
        
        Assert.assertEquals("{name1:name1.value,name3:name3.value,name4:name4.value,name2:name2.value}", jsObjectForNames);

    }
    
    @Test
    public void testGetNameBasedJsObjectSetOfString2() {
        
        final Form form = new Form(null) {
            {
                new Input(this, new Name("name1"));
                new Div(this){{new Input(this, new Name("name2"));}};
                new Input(this, new Name("name3"));
                new Div(this){{new Input(this, new Name("name4"));}};
                new Div(this){{new Input(this, new Name("name4"));}};
                new Div(this){{new Input(this, new Name("name4"));}};
            }
        };
        
        final String jsObjectForNames = form.getNameBasedJsObject(
                new HashSet<String>(Arrays.asList("input")));
        
        Assert.assertEquals("{name1:name1.value,name3:name3.value,name4:name4.value,name2:name2.value}", jsObjectForNames);

    }
    @Test
    public void testGetNameBasedJsObjectSetOfString3() {
        
        final Form form = new Form(null) {
            {
                new Input(this, new Name("name1"));
                new Div(this, new Name("excluded")){{new Input(this, new Name("name2"));}};
                new Input(this, new Name("name3"));
                new Div(this){{new Input(this, new Name("name4"));}};
                new Div(this){{new Input(this, new Name("name4"));}};
                new Div(this){{new Input(this, new Name("name4"));}};
                new TextArea(this, new Name("excluded"));
            }
        };
        
        final String jsObjectForNames = form.getNameBasedJsObject(
                new HashSet<String>(Arrays.asList("input")));
        
        Assert.assertEquals("{name1:name1.value,name3:name3.value,name4:name4.value,name2:name2.value}", jsObjectForNames);
        
    }

    @Test
    public void testGetNameBasedJsObject() {
        
        final Form form = new Form(null) {
            {
                new Input(this, new Name("name1"));
                new Div(this, new Name("excluded")){{new Input(this, new Name("name2"));}};
                new Input(this, new Name("name3"));
                new Div(this){{new Input(this, new Name("name4"));}};
                new Div(this){{new Input(this, new Name("name4"));}};
                new Div(this){{new Input(this, new Name("name4"));}};
                new Div(this){{new Input(this, new Name("name5"), new Type(Type.TEXT));}};
                new Div(this){{new Input(this, new Name("name6"), new Type(Type.CHECKBOX));}};
                new Div(this){{new Input(this, new Name("name7"), new Type(Type.RADIO));}};
                new TextArea(this, new Name("included"));
            }
        };
        
        final String jsObjectForNames = form.getNameBasedJsObject();
        
        Assert.assertEquals("{name1:name1.value,name3:name3.value,included:included.value,name7:name7.checked,name6:name6.checked,name5:name5.value,name4:name4.value,name2:name2.value}", jsObjectForNames);
        
    }
    
    @Test
    public void testGetIdBasedJsObject() {
        final Form form = new Form(null) {
            {
                new Input(this, new Id("name1"));
                new Div(this, new Id("excluded")){{new Input(this, new Id("name2"));}};
                new Input(this, new Id("name3"));
                new Div(this){{new Input(this, new Id("name4"));}};
                new Div(this){{new Input(this, new Id("name4"));}};
                new Div(this){{new Input(this, new Id("name4"));}};
                new Div(this){{new Input(this, new Id("name5"), new Type(Type.TEXT));}};
                new Div(this){{new Input(this, new Id("name6"), new Type(Type.CHECKBOX));}};
                new Div(this){{new Input(this, new Id("name7"), new Type(Type.RADIO));}};
                new TextArea(this, new Id("included"));
            }
        };
        
        final String jsObjectForNames = form.getIdBasedJsObject();
        Assert.assertEquals("{name1:document.getElementById('name1').value,name3:document.getElementById('name3').value,included:document.getElementById('included').value,name7:document.getElementById('name7').checked,name6:document.getElementById('name6').checked,name5:document.getElementById('name5').value,name4:document.getElementById('name4').value,name2:document.getElementById('name2').value}", jsObjectForNames);
    }
    
    
    
    @Test
    public void testGetIdBasedJsObjectPlus() {
        final Form form = new Form(null) {
            {
                new Button(this, new Id("name1"));
                new Div(this, new Id("excluded")){{new Input(this, new Id("name2"));}};
                new Input(this, new Id("name3"));
                new Div(this){{new Input(this, new Id("name4"));}};
                new Div(this){{new Input(this, new Id("name4"));}};
                new Div(this){{new Input(this, new Id("name4"));}};
                new Div(this){{new Input(this, new Id("name5"), new Type(Type.TEXT));}};
                new Div(this){{new Input(this, new Id("name6"), new Type(Type.CHECKBOX));}};
                new Div(this){{new Input(this, new Id("name7"), new Type(Type.RADIO));}};
                new TextArea(this, new Id("included"));
            }
        };
        
        final String jsObjectForNames = form.getIdBasedJsObjectPlus(Arrays.asList(TagNameConstants.BUTTON));
        Assert.assertEquals("{name1:document.getElementById('name1').value,name3:document.getElementById('name3').value,included:document.getElementById('included').value,name7:document.getElementById('name7').checked,name6:document.getElementById('name6').checked,name5:document.getElementById('name5').value,name4:document.getElementById('name4').value,name2:document.getElementById('name2').value}", jsObjectForNames);
    }

}
