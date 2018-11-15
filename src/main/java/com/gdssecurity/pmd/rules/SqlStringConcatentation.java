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

package com.gdssecurity.pmd.rules;


import java.text.MessageFormat;
import java.util.List;
import java.util.regex.Pattern;

import com.gdssecurity.pmd.Utils;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.java.ast.ASTAdditiveExpression;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.rule.regex.RegexHelper;
import net.sourceforge.pmd.lang.java.symboltable.JavaNameOccurrence;
import net.sourceforge.pmd.lang.symboltable.NameOccurrence;
import net.sourceforge.pmd.properties.PropertyDescriptor;
import net.sourceforge.pmd.properties.StringProperty;


public class SqlStringConcatentation extends BaseSecurityRule {

    private static final PropertyDescriptor<String> standardSqlRegexDescriptor = new StringProperty(
            "standardsqlregex",
            "regular expression for detecting standard SQL statements",
            "undefined", 1.0F);
    private static final PropertyDescriptor<String> customSqlRegexDescriptor = new StringProperty(
            "customsqlregex",
            "regular expression for detecting custom SQL, such as stored procedures and functions",
            "undefined", 1.0F);


    
    private static Pattern standardSqlRegex = null;
    private static Pattern customSqlRegex = null;

    
    public SqlStringConcatentation() {
    	super();
    	definePropertyDescriptor(standardSqlRegexDescriptor);
    	definePropertyDescriptor(customSqlRegexDescriptor);

    }
    
    @Override
	protected void init() {
    	super.init();
        if (standardSqlRegex == null) {
            standardSqlRegex = Pattern.compile(
            		getProperty(standardSqlRegexDescriptor), Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        }
        
        if (customSqlRegex == null) {
            customSqlRegex = Pattern.compile(
            		getProperty(customSqlRegexDescriptor), Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        }
        

    }



    @Override
	public Object visit(ASTAdditiveExpression astAdditiveExpression, Object obj) {
        RuleContext rc = (RuleContext) obj;
        int beginLine = astAdditiveExpression.getBeginLine();
        int endLine = astAdditiveExpression.getEndLine();
        String codeSnippet = Utils.getCodeSnippet(rc.getSourceCodeFilename(), beginLine, endLine);
        boolean match = false;
        
        if (standardSqlRegex != null && RegexHelper.isMatch(standardSqlRegex, codeSnippet)) {
            match = true;
        } else if (customSqlRegex != null && RegexHelper.isMatch(customSqlRegex, codeSnippet)) {
            match = true;
		}

		if (match) {
			List<ASTName> concatenatedVars = astAdditiveExpression.findDescendantsOfType(ASTName.class);

			for (ASTName astName : concatenatedVars) {
				String varName = astName.getImage();
				String varType = Utils.getType(astName, rc, varName);

				if (varType.contains("java.lang.String")) {
					NameOccurrence n = new JavaNameOccurrence(astName, astName.getImage());

					if (astAdditiveExpression.getScope().contains(n)) {
						addSecurityViolation(
								this,
								rc,
								astAdditiveExpression,
								MessageFormat.format(getMessage(), new Object[] { varName, varType,
										varName + " appears to be a method argument" }), "");
					} else {
						addSecurityViolation(
								this,
								rc,
								astAdditiveExpression,
								MessageFormat.format(getMessage(), new Object[] { varName, varType,
										"Check whether " + varName + " contains tainted data" }), "");
					}
				} else if (isUnsafeType(varType)) {
					addSecurityViolation(
							this,
							rc,
							astAdditiveExpression,
							MessageFormat.format(getMessage(), new Object[] { varName, varType,
									varType + " is  tainted data" }), "");
				} 
				else if (!isSafeType(varType)){
					addSecurityViolation(
							this,
							rc,
							astAdditiveExpression,
							MessageFormat.format(getMessage(), new Object[] { varName, varType,
									"Check whether " + varType + " contains tainted data" }), "");
				}
			}

		}

        return super.visit(astAdditiveExpression, obj);
    }

}
