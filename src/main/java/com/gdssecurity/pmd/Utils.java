/*
(C) Copyright  2014-2015 Alberto Fernández <infjaf@gmail.com>
(C) Copyright  2012      Gotham Digital Science, LLC -- All Rights Reserved
 
Unless explicitly acquired and licensed from Licensor under another
license, the contents of this file are subject to the Reciprocal Public
License ("RPL") Version 1.5, or subsequent versions as allowed by the RPL,
and You may not copy or use this file in either source code or executable
form, except in compliance with the terms and conditions of the RPL.

All software distributed under the RPL is provided strictly on an "AS
IS" basis, WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND
LICENSOR HEREBY DISCLAIMS ALL SUCH WARRANTIES, INCLUDING WITHOUT
LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific
language governing rights and limitations under the RPL. 

This code is licensed under the Reciprocal Public License 1.5 (RPL1.5)
http://www.opensource.org/licenses/rpl1.5

*/


package com.gdssecurity.pmd;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTExpression;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryPrefix;



public final class Utils {

    private static final Logger LOG = Logger.getLogger("com.gdssecurity.pmd.rules");
	
    private Utils () {
    	throw new AssertionError("No instances allowed");
    }
    
	public static String getCodeSnippet(String fileName, int start, int end) {
        StringBuilder sb = new StringBuilder();
        try {
        	File file = new File(fileName);
        	List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        	for (int i = 0; i < lines.size(); i++) {
        		int lineNumber = i+1;
        		if (lineNumber >= start && lineNumber <= end) {
        			sb.append(lines.get(i)).append("\n");
        		}
        	}

        }  catch (IOException ioe) {
        	LOG.log(Level.WARNING, "Unexpected error while retrieving code snippet from " + fileName, ioe);
        } 
        return sb.toString();
    }
	
    public static String getType(Node node, RuleContext rc, String method) {
        String methodMsg = "Utils::getType - {0}";
		
        String cannonicalName = "";
        Class<? extends Object> type = null;
		if (node == null) {
			return "UNKNOWN_TYPE";
		}
        try {
        	Class<?> nodeClass = node.getClass();
            if (nodeClass == ASTExpression.class) {				
                type = node.getFirstChildOfType(ASTPrimaryExpression.class).getFirstChildOfType(ASTName.class).getType();
            } else if (nodeClass == ASTPrimaryExpression.class) {
                if (node.hasDescendantOfType(ASTClassOrInterfaceType.class)) {					
                    type = node.getFirstDescendantOfType(ASTClassOrInterfaceType.class).getType();
                } else {	
                	ASTPrimaryPrefix prefix = node.getFirstChildOfType(ASTPrimaryPrefix.class);
                	ASTName astName = prefix.getFirstChildOfType(ASTName.class);        	
                	if (astName != null) {
                		type = node.getFirstDescendantOfType(ASTName.class).getType();
                	}                    
                }
            } else if (nodeClass == ASTName.class) {
                type = ((ASTName) node).getType();
            }            
			if (type != null) {
				cannonicalName = type.getCanonicalName();
			}
			else {
				cannonicalName = "UNKNOWN_TYPE";
			}
        } catch (Exception ex1) {
    		
            LOG.log(Level.INFO, methodMsg,
                    "Unable to get type for " + method + " at "
                    + rc.getSourceCodeFilename() + " (" + node.getBeginLine()
                    + ")");
            cannonicalName = "UNKNOWN_TYPE";
        }
		
        return cannonicalName;
    }
	
  

	public static Set<String> arrayAsSet(String[] array) {
		Set<String> hashSet = new HashSet<String>((int) Math.ceil(array.length / 0.75));

		for (String element : array) {
			element = element.trim();
			if (!StringUtils.isBlank(element)) {
				hashSet.add(element);
			}
		}
		return hashSet;
	}
	
	public static Method[] getMethods (Class<?> clazz) {
		Set<Method> set = new HashSet<Method>();
		try {
			if (clazz != null) {
				set.addAll(Arrays.asList(clazz.getDeclaredMethods()));
				set.addAll(Arrays.asList(clazz.getMethods()));
			}
			return set.toArray(new Method[set.size()]);
		}
		catch (NoClassDefFoundError | ExceptionInInitializerError  err) { 
			return new Method[0];
		}
	}
	
	public static Constructor<?>[] getConstructors(Class<?> clazz) {
		return clazz.getDeclaredConstructors();
	}

}
