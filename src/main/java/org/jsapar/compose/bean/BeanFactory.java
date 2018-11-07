package org.jsapar.compose.bean;

import org.jsapar.error.ValidationAction;
import org.jsapar.model.Cell;
import org.jsapar.model.Line;

import java.lang.reflect.InvocationTargetException;

/**
 * Interface for a bean factory that creates bean instances based on line content. Create your own implementation of this
 * interface if you need to be able to control how beans are created in more details.
 * @param <T> common base class of all the expected beans. Use Object as base class if there is no common base class for all beans.
 * @see BeanFactoryDefault
 * @see BeanFactoryByMap
 */
public interface BeanFactory<T> {

    /**
     * @param line The line to create a bean for.
     * @return A new instance of a java bean created for the supplied line. If this method returns null, the behavior of
     * the {@link BeanComposer} is denoted by the config parameter {@link BeanComposeConfig#setOnUndefinedLineType(ValidationAction)}
     * @throws ClassNotFoundException In case any of the classes described in the schema does not exist in the classpath.
     * @throws IllegalAccessException In case a setter method is not publicly accessible.
     * @throws InstantiationException If it was not possible to instantiate a bean.
     * @throws ClassCastException     In case an illegal cast is done while trying to use a setter.
     * @throws NoSuchMethodException  In case there is no public default constructor
     * @throws InvocationTargetException In case there was an exception while calling the default constructor.
     */
    T createBean(Line line) throws ClassNotFoundException, InstantiationException, IllegalAccessException, ClassCastException, NoSuchMethodException, InvocationTargetException;


    /** Should assign the value of the specified cell to the proper bean property.
     *
     * @param lineType The name of the line type to use.
     * @param bean The bean to assign to.
     * @param cell The cell to assign.
     * @throws BeanComposeException In case there is an error assigning the cell. This exception will be caught by the
     * calling {@link BeanComposer} and converted into an {@link org.jsapar.error.ErrorEvent}.
     * @throws InvocationTargetException If not possible to invoke method.
     * @throws IllegalAccessException In case a setter method is not publicly accessible.
     * @throws InstantiationException If it was not possible to instantiate a bean.
     */
    void assignCellToBean(String lineType, T bean, Cell cell) throws BeanComposeException, InvocationTargetException, InstantiationException, IllegalAccessException;

}
