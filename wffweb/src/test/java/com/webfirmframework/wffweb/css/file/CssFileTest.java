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
package com.webfirmframework.wffweb.css.file;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

import com.webfirmframework.wffweb.css.FontWeight;

public class CssFileTest {

    @Test
    public void testToCssString() {
        final SampleCssFile sampleCssFile = new SampleCssFile(FontWeight.NINE_HUNDRED);
        String cssString = sampleCssFile.toCssString();
        assertEquals("test-class1{font-style:normal;background-color:green;}test-class2{font-style:oblique;}test-class3{font-style:oblique;}", cssString);
        sampleCssFile.getFontWeights().remove(FontWeight.NINE_HUNDRED);
        cssString = sampleCssFile.toCssString();
        assertEquals("test-class1{font-style:normal;background-color:green;}test-class2{font-style:oblique;}test-class3{font-style:oblique;}", sampleCssFile.toCssString());
    }
    
    @Test
    public void testToCssStringBooleanTrue() {
        final SampleCssFile sampleCssFile = new SampleCssFile(FontWeight.NINE_HUNDRED);
        String cssString = sampleCssFile.toCssString();
        cssString = sampleCssFile.toCssString();
        assertEquals("test-class1{font-style:normal;background-color:green;}test-class2{font-style:oblique;}test-class3{font-style:oblique;}", cssString);
        sampleCssFile.getFontWeights().remove(FontWeight.NINE_HUNDRED);
        cssString = sampleCssFile.toCssString(true);
        cssString = sampleCssFile.toCssString(true);
        assertEquals("test-class1{font-style:normal;background-color:green;}test-class2{font-style:oblique;}", cssString);
    }
    
    @Test
    public void testToCssStringBooleanTrue2() {
        final SampleCssFile sampleCssFile = new SampleCssFile(FontWeight.BOLDER);
        String cssString = sampleCssFile.toCssString();
        cssString = sampleCssFile.toCssString();
        assertEquals("test-class1{font-style:normal;background-color:green;}test-class2{font-style:oblique;}", cssString);
    }
    
    @Test
    public void testToOutputStream() throws IOException {
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        final SampleCssFile sampleCssFile = new SampleCssFile(FontWeight.NINE_HUNDRED);
        sampleCssFile.toOutputStream(baos);
        String cssString = new String(baos.toByteArray());
        assertEquals("test-class1{font-style:normal;background-color:green;}test-class2{font-style:oblique;}test-class3{font-style:oblique;}", cssString);
        sampleCssFile.getFontWeights().remove(FontWeight.NINE_HUNDRED);
        
        baos = new ByteArrayOutputStream();
        sampleCssFile.toOutputStream(baos);
        cssString = sampleCssFile.toCssString();
//        assertEquals("test-class1{font-style:normal;background-color:green;}test-class2{font-style:oblique;}", cssString);
        assertEquals("test-class1{font-style:normal;background-color:green;}test-class2{font-style:oblique;}test-class3{font-style:oblique;}", sampleCssFile.toCssString());
//        fail("Not yet implemented");
    }
    
    @Test
    public void testToOutputStreamBooleanTrue() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        final SampleCssFile sampleCssFile = new SampleCssFile(FontWeight.NINE_HUNDRED);
        sampleCssFile.toOutputStream(baos);
        String cssString = new String(baos.toByteArray());
        assertEquals("test-class1{font-style:normal;background-color:green;}test-class2{font-style:oblique;}test-class3{font-style:oblique;}", cssString);
        sampleCssFile.getFontWeights().remove(FontWeight.NINE_HUNDRED);
        baos = new ByteArrayOutputStream();
        sampleCssFile.toOutputStream(baos, true);
        cssString = new String(baos.toByteArray());
        
        baos = new ByteArrayOutputStream();
        sampleCssFile.toOutputStream(baos, true);
        cssString = new String(baos.toByteArray());

        assertEquals("test-class1{font-style:normal;background-color:green;}test-class2{font-style:oblique;}", cssString);
    }
    
    @Test
    public void testToOutputStreamBooleanTrue2() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        final SampleCssFile sampleCssFile = new SampleCssFile(FontWeight.BOLDER);
        sampleCssFile.toOutputStream(baos);
        String cssString = new String(baos.toByteArray());
        
        baos = new ByteArrayOutputStream();
        sampleCssFile.toOutputStream(baos);
        cssString = new String(baos.toByteArray());
        assertEquals("test-class1{font-style:normal;background-color:green;}test-class2{font-style:oblique;}", cssString);
    }
    
    @Test
    public void testToOutputStreamBooleanTrue2WithPrependCharset() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        final SampleCssFile sampleCssFile = new SampleCssFile(FontWeight.BOLDER);
        sampleCssFile.setPrependCharset(true);
        sampleCssFile.toOutputStream(baos);
        String cssString = new String(baos.toByteArray());
        
        baos = new ByteArrayOutputStream();
        sampleCssFile.toOutputStream(baos);
        cssString = new String(baos.toByteArray());
        assertEquals("@CHARSET \"UTF-8\";\ntest-class1{font-style:normal;background-color:green;}test-class2{font-style:oblique;}", cssString);
    }

}
