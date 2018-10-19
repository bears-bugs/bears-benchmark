// $Id: MemberFilePersister.java,v 1.5 2005-07-05 19:38:30 bobtarling Exp $
// Copyright (c) 1996-2005 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectMember;

/**
 * A base class file persister for project members.
 * @author Bob Tarling
 */
public abstract class MemberFilePersister {
    /**
     * Load the member based on instance variables
     * which should have been setup in the constructor.
     *
     * @param project the project to persist
     * @param inputStream the inputStream to parse to load the member.
     * @throws OpenException on any parsing errors.
     */
    public abstract void load(Project project, InputStream inputStream)
        throws OpenException;

    /**
     * Gets the tag name which is the root tag for this member.
     * @return tag name.
     */
    public abstract String getMainTag();

    /**
     * Save the projectmember as XML to the given writer.
     *
     * @param member The project member to save.
     * @param writer The Writer to which appen the save.
     * @param indent The offset to which to indent the XML
     * @throws SaveException if the save fails
     */
    public abstract void save(
            ProjectMember member,
            Writer writer,
            Integer indent) throws SaveException;

    /**
     * Send an existing file of XML to the PrintWriter.
     * @param writer the PrintWriter.
     * @param file the File
     * @param indent How far to indent in the writer.
     * @throws SaveException on any errors.
     */
    protected void addXmlFileToWriter(PrintWriter writer, File file, int indent)
        throws SaveException {
        try {
            String padding =
                "                                          "
                	.substring(0, indent);
            BufferedReader reader =
                new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

            // Skip the <?xml... first line
            String line = reader.readLine();
            while (line != null && (line.startsWith("<?xml ")
                    || line.startsWith("<!DOCTYPE "))) {
                line = reader.readLine();
            }

            while (line != null) {
                (writer).print(padding);
                (writer).println(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException e) {
            throw new SaveException(e);
        } catch (IOException e) {
            throw new SaveException(e);
        }
    }

}
