package com.webfirmframework.wffweb.css;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class CssColorNameTest {

    @Test
    public void testGetHex() {
       final String hex = CssColorName.ALICE_BLUE.getHex();
       
       assertNotNull(hex);
       
       assertEquals("#F0F8FF", hex);
       
       final int r = CssColorName.WHITE.getR();
       
       assertEquals(255, r);
       
       assertEquals(255, CssColorName.WHITE.getG());
       
       assertEquals(255, CssColorName.WHITE.getB());
    }
    
    @Test
    public void testHashcode() throws Exception {
        //"FB" and "Ea" have same hashcode so this test is relevant 
        final CssColorName[] values = CssColorName.values();
        Set<Integer> hashcodes = new HashSet<>();
        for (CssColorName cssColorName : values) {
            if (hashcodes.contains(cssColorName.getColorName().hashCode())) {
                fail("multiple css color names have same hashcode");
            }
            hashcodes.add(cssColorName.getColorName().hashCode());
        }
        
    }

}
