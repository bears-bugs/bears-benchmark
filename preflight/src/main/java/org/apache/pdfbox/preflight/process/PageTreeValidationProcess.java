/*****************************************************************************
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 ****************************************************************************/

package org.apache.pdfbox.preflight.process;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import static org.apache.pdfbox.preflight.PreflightConfiguration.PAGE_PROCESS;
import static org.apache.pdfbox.preflight.PreflightConstants.ERROR_SYNTAX_NOCATALOG;

import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import static org.apache.pdfbox.preflight.PreflightConstants.ERROR_PDF_PROCESSING_MISSING;
import org.apache.pdfbox.preflight.PreflightContext;
import org.apache.pdfbox.preflight.ValidationResult.ValidationError;
import org.apache.pdfbox.preflight.exception.ValidationException;
import org.apache.pdfbox.preflight.utils.ContextHelper;

public class PageTreeValidationProcess extends AbstractProcess
{

    @Override
    public void validate(PreflightContext context) throws ValidationException
    {
        PDDocumentCatalog catalog = context.getDocument().getDocumentCatalog();
        if (catalog != null)
        {
            COSDictionary catalogDict = catalog.getCOSObject();
            if (!(catalogDict.getDictionaryObject(COSName.PAGES) instanceof COSDictionary))
            {
                addValidationError(context, new ValidationError(ERROR_PDF_PROCESSING_MISSING, 
                        "/Pages dictionary entry is missing in document catalog"));
                return;
            }
            int numPages = context.getDocument().getNumberOfPages();
            for (int i = 0; i < numPages; i++)
            {
                context.setCurrentPageNumber(i);
                validatePage(context, context.getDocument().getPage(i));
                context.setCurrentPageNumber(null);
            }
        }
        else
        {
            context.addValidationError(new ValidationError(ERROR_SYNTAX_NOCATALOG, "There are no Catalog entry in the Document"));
        }
    }

    protected void validatePage(PreflightContext context, PDPage page) throws ValidationException
    {
        ContextHelper.validateElement(context, page, PAGE_PROCESS);
    }
}
