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

package com.gdssecurity.pmd.smap;


import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class SmapResolver {
    
    private static final String SMAP_HEADER = "SMAP"; 
    
    private static final String DEFAULT_STRATUM = "JSP"; 
    
    private static final String STRATUM_SECTION = "*S JSP"; 
    
    private static final String LINE_SECTION = "*L"; 
    
    private static final String FILE_SECTION = "*F"; 
        
    private static final String END_SECTION = "*E"; 
    
    private static final String FID_DELIM = "#"; 

    private SmapReader reader = null;
    
    private boolean resolved = false;
    
    private String defaultStratum = null;
    
    private String outputFileName = null;
    
    private Map<String, String> fsection = new Hashtable<String, String>(3);
    
    private boolean fsection_sourceNameSourcePath = false;
    
    private Map<String, String> jsp2java = new TreeMap<String, String>();
    
    private Map<String, String> java2jsp = new TreeMap<String, String>();

    public SmapResolver(SmapReader reader) {
        this.resolved = resolve(reader.readSmap());
        this.reader = reader;
    }

    @Override
	public String toString() {
        return this.reader.toString();
    }
        
    private boolean resolve(List<String> smap) {
        
        boolean fileSection = false;        
        boolean lineSection = false;        
        boolean jspStratumSection = false;  


        
        
        int counter = 1;
        int sectionCounter = 0; 
        
        String fileIndex = null;
        
        
        for (String token: smap) {
            
            if (counter == 1) {         
                if (!SMAP_HEADER.equals(token)) {
                    return false;
                }
            } else if (counter == 2) {  
                this.outputFileName = token;  
            } else if (counter == 3) {  
                this.defaultStratum = token;
            } else if (STRATUM_SECTION.equals(token)) {
                jspStratumSection = true;
            } else if (FILE_SECTION.equals(token) && jspStratumSection) {
                fileSection = true;
                sectionCounter = 0;
            } else if (LINE_SECTION.equals(token) && jspStratumSection) {
                fileSection = false;
                lineSection = true;
                sectionCounter = 0;
                fileIndex = "0";
            } else if (END_SECTION.equals(token) && jspStratumSection) {
                lineSection = false;
                fileSection = false;
                sectionCounter = 0;
            }

            if (fileSection) {
            	
                if (sectionCounter != 0) { 
                    storeFile(token);
                }
                sectionCounter++;
            }
            
            if (lineSection) { 
                if (sectionCounter != 0) {  
                    int hashPresent = token.indexOf(FID_DELIM);

                    if (hashPresent > -1) { 
                        fileIndex = token.substring(hashPresent + 1,
                                token.indexOf(':'));
                        if (fileIndex.indexOf(",") != -1) {
                            fileIndex = fileIndex.substring(0,
                                    fileIndex.indexOf(","));
                        }
                    }
                    
                    storeLine(token, fileIndex);
                }
                sectionCounter++;
            }
            counter++;
        }
        
        this.resolved = sanityCheck();
        return this.resolved;
    }
    
    private void storeFile(String token) {
    	
        String sourceName = "";
        String sourcePath = "";
        String id = "";
        boolean sourceNameSourcePath = false;
    	
        if (token.indexOf("+") != -1) {
            int firstSpaceIndex = token.indexOf(" ");
            int secondSpaceIndex = token.lastIndexOf(" ");

            id = token.substring(firstSpaceIndex + 1, secondSpaceIndex);
            sourceName = token.substring(secondSpaceIndex + 1);
            sourceNameSourcePath = true;
        } else if (this.fsection_sourceNameSourcePath) {
            sourcePath = token;
            if (token.lastIndexOf("/") != -1) {
                sourceName = sourcePath.substring(token.lastIndexOf("/") + 1,
                        sourcePath.length());
                id = getIndexByFileName(sourceName);
            } else {
                sourceName = sourcePath;
                id = getIndexByFileName(sourceName);
            }
    		
        }
    	
        this.fsection.put(id,this.fsection_sourceNameSourcePath ? sourcePath : sourceName);
        
        this.fsection_sourceNameSourcePath = sourceNameSourcePath;
    }

    private void storeLine(String token, String fileIndex) {
        int delimIndex = token.indexOf(":");
        
        String jspLine = token.substring(0, delimIndex);
        String javaLine = token.substring(delimIndex + 1);
        
        int hashPresent = jspLine.indexOf(FID_DELIM);
        int commaPresent = jspLine.indexOf(',');

        int jspIndex = 0;    
        int repeatCount = 0;

        if (commaPresent != -1) {
            repeatCount = Integer.parseInt(jspLine.substring(commaPresent + 1));
            if (hashPresent == -1) {
                jspIndex = Integer.parseInt(jspLine.substring(0, commaPresent));
            } else {
                jspIndex = Integer.parseInt(jspLine.substring(0, hashPresent));
            }
        } else {
            if (hashPresent == -1) {
                jspIndex = Integer.parseInt(jspLine);
            } else {
                jspIndex = Integer.parseInt(jspLine.substring(0, hashPresent));
            }
            repeatCount = 1;
        }
        
        commaPresent = javaLine.indexOf(',');
        
        int outputIncrement;
        int javaIndex;

        if (commaPresent != -1) {
            outputIncrement = Integer.parseInt(javaLine.substring(commaPresent + 1));
            javaIndex = Integer.parseInt(javaLine.substring(0, commaPresent));
        } else {
            outputIncrement = 1;
            javaIndex = Integer.parseInt(javaLine);
        }
        
        for (int i = 0; i < repeatCount; i++) {
            int jspL = jspIndex + i;
            int javaL = javaIndex + (i * outputIncrement);
            
            jspLine = Integer.toString(jspL).concat(FID_DELIM).concat(fileIndex);
            javaLine = Integer.toString(javaL);
            if (!this.jsp2java.containsKey(jspLine)) { 
                this.jsp2java.put(jspLine, javaLine);
            }
            
            jspLine = Integer.toString(jspL).concat("#").concat(fileIndex);
            
            javaLine = Integer.toString(javaL);
            
            if (!this.java2jsp.containsKey(javaLine)) { 
                this.java2jsp.put(javaLine, jspLine);
            }
        }
    }
    
    private boolean sanityCheck() {   
        if (!DEFAULT_STRATUM.equals(this.defaultStratum)) {
            return false;
        }
        if (!(this.outputFileName.endsWith(".java"))) {
            return false;
        }
        if (this.fsection.isEmpty()) {
            return false;
        }
        if (this.jsp2java.isEmpty()) {
            return false;
        }   
        if (this.java2jsp.isEmpty()) {
            return false;
        }   
        return true;
    }
    
    private String getFileNameByIndex(String index) {
        return this.fsection.get(index);
    }
    
    private String getIndexByFileName(String fname) {    	
    	for (Map.Entry<String, String> mentry: this.fsection.entrySet()){
    		String value =  mentry.getValue();            
            if (value.equalsIgnoreCase(fname)) {
                return mentry.getKey().toString();
            }
    	}
        return null;
    }
    
    public String getSourcePath(String fname) {
    	for (Map.Entry<String, String> mentry: this.fsection.entrySet()){
    		 String value =  mentry.getValue();
             int delim = value.lastIndexOf(":");
             String sourceName = value.substring(0, delim);
             String sourcePath = value.substring(delim + 1);
             
             if (sourceName.equalsIgnoreCase(fname)) {
                 return sourcePath;
             }
    	}
        return null;
    }

    public boolean isResolved() {
        return this.resolved;
    }
    
    public Map<Integer, String> getFileNames() {
        Map<Integer, String> h = new Hashtable<Integer,String>(this.fsection.size());
        int counter = 0;
        for (String fileName: this.fsection.values()){
        	h.put(counter++, fileName);
        }
        return h;
    }
    
    public String getPrimaryJspFileName() {
        TreeMap<String, String> tm = new TreeMap<String, String>(this.fsection);
        String o = tm.firstKey();
        String s = this.fsection.get(o);
        
        return s;
    }

    public boolean hasIncludedFiles() {
        return this.fsection.size() > 1;
    }
    
    public String getJavaLineType(int line, int col) {
        
        return null;
    }
    
    public boolean isEmpty() {
        return this.jsp2java.isEmpty();  
    }

    public String getJspFileName(int line, int col) {
        String key = Integer.toString(line);
        String value = this.java2jsp.get(key);
        
        if (value == null) {
            return null;
        }
        String index = value.substring(value.indexOf(FID_DELIM) + 1);
        
        return getFileNameByIndex(index);
    }
    
    public int mangle(String jspFileName, int line, int col) {
        String fileIndex = getIndexByFileName(jspFileName);

        if (fileIndex == null) {
            return -1;
        }
        String key = "".concat(Integer.toString(line)).concat("#").concat(
                fileIndex);
        String value = this.jsp2java.get(key);

        if (value == null) {
            return -1;
        }
        return Integer.parseInt(value);
    }

    public int unmangle(int line, int col) {
        String key = Integer.toString(line);
        String value = this.java2jsp.get(key);

        if (value == null) {
            return -1;
        }
        int jspline = Integer.parseInt(value.substring(0, value.indexOf("#")));

        return jspline;
    }
    
}
