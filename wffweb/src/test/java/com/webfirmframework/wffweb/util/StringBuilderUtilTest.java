package com.webfirmframework.wffweb.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringBuilderUtilTest {

    @Test
    public void testGetTrimmedString1() {
        StringBuilder builder = new StringBuilder();
        builder.append("         ");
        builder.append(StringUtil.join(' ', "hi", "how", "are", "you"));
        builder.append("         ");        
        
        assertEquals("hi how are you", StringBuilderUtil.getTrimmedString(builder));
    }
    
    @Test
    public void testGetTrimmedString2() {
        StringBuilder builder = new StringBuilder();
        builder.append("         ");
        builder.append("  \t");
        builder.append(" \n");
        builder.append("         ");
        
        assertEquals("", StringBuilderUtil.getTrimmedString(builder));
    }


}
