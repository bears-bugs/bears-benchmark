/**
 *
 */
package org.jsapar.convert;

import org.jsapar.BeanCollection2TextConverter;
import org.jsapar.model.Line;

/**
 * You can register a class that implements the LineManipulator interface to the {@link ConvertTask} or any of the converter classes. Then, every time there is a
 * line event from the parser, the method manipulate gets called with a Line object. You may now modify the Line object
 * within the method and the modified values are then the ones that are fed to the output schema. You can register more than
 * one LineManipulator to the same converter and they will get called in the same order as they were registered.
 *
 * @see org.jsapar.Text2TextConverter
 * @see org.jsapar.Text2BeanConverter
 * @see org.jsapar.Text2StringConverter
 * @see org.jsapar.Text2XmlConverter
 * @see org.jsapar.Xml2TextConverter
 * @see BeanCollection2TextConverter
 * @see ConvertTask
 */
public interface LineManipulator {

    /**
     * Gets called every time that a line parsing event is fired within a converter. Changes of the line instance made
     * by an implementation of this method will
     * be reflected in the output. Cells can be added, altered or removed by a manipulator.
     *
     * @param line The line to manipulate
     * @return If this method returns false, the line will be omitted from the output.
     */
    boolean manipulate(Line line);

}
