/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.pdfbox.tools;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.fdf.FDFDocument;

/**
 * This example will take a PDF document and fill the fields with data from the
 * FDF fields.
 *
 * @author Ben Litchfield
 */
public class ExportXFDF
{
    /**
     * Creates a new instance of ImportFDF.
     */
    public ExportXFDF()
    {
    }

    /**
     * This will import an fdf document and write out another pdf.
     * <br>
     * see usage() for commandline
     *
     * @param args command line arguments
     * @throws IOException in case the file can not be read or the data can not be exported.
     *
     * @throws IOException If there is an error importing the FDF document.
     */
    public static void main(String[] args) throws IOException
    {
        // suppress the Dock icon on OS X
        System.setProperty("apple.awt.UIElement", "true");

        ExportXFDF exporter = new ExportXFDF();
        exporter.exportXFDF( args );
    }

    private void exportXFDF( String[] args ) throws IOException
    {
        if( args.length != 1 && args.length != 2 )
        {
            usage();
        }
        else
        {
            try (PDDocument pdf = PDDocument.load( new File(args[0]) ))
            {
                PDAcroForm form = pdf.getDocumentCatalog().getAcroForm();
                if( form == null )
                {
                    System.err.println( "Error: This PDF does not contain a form." );
                }
                else
                {
                    String fdfName = null;
                    if( args.length == 2 )
                    {
                        fdfName = args[1];
                    }
                    else
                    {
                        if( args[0].length() > 4 )
                        {
                            fdfName = args[0].substring( 0, args[0].length() -4 ) + ".xfdf";
                        }
                    }
                    
                    try (FDFDocument fdf = form.exportFDF())
                    {
                        fdf.saveXFDF( fdfName );
                    }
                }
            }
        }
    }

    /**
     * This will print out a message telling how to use this example.
     */
    private static void usage()
    {
        String message = "Usage: org.apache.pdfbox.ExportXFDF <inputfile> [output-xfdf-file]\n"
                + "\nOptions:\n"
                + "  [output-xfdf-file] : Default is pdf name, test.pdf->test.xfdf";
        
        System.err.println(message);
        System.exit(1);
    }
}
