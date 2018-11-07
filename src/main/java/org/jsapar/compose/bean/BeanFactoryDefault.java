package org.jsapar.compose.bean;

import org.jsapar.model.Cell;
import org.jsapar.model.Line;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanFactoryDefault<T> implements BeanFactory<T> {
    private static final String GET_PREFIX = "get";
    private static final String SET_PREFIX = "set";

    @SuppressWarnings("WeakerAccess")
    public BeanFactoryDefault() {
    }

    /**
     * This implementation creates the bean by using Class.forName method on the line type.
     * @see BeanFactory#createBean(Line)
     */
    @SuppressWarnings("unchecked")
    @Override
    public T createBean(Line line) throws ClassNotFoundException, InstantiationException, IllegalAccessException, ClassCastException, NoSuchMethodException, InvocationTargetException {
        Class<?> c = Class.forName(line.getLineType());
        return (T) c.getConstructor().newInstance();
    }

    @Override
    public void assignCellToBean(String lineType, T bean, Cell cell) throws BeanComposeException {
        String sName = cell.getName();
        try {
            String[] nameLevels = sName.split("\\.");
            Object currentObject = bean;
            for (int i = 0; i + 1 < nameLevels.length; i++) {
                try {
                    // Continue looping to next object.
                    currentObject = findOrCreateChildBean(currentObject, nameLevels[i]);
                } catch (InstantiationException e) {
                    throw new BeanComposeException("Skipped assigning cell - Failed to execute default constructor for class accessed by "
                                    + nameLevels[i], e);
                }
            }
            sName = nameLevels[nameLevels.length - 1];
            assignAttribute(cell, sName, currentObject);
        } catch (InvocationTargetException | IllegalArgumentException e) {
            throw new BeanComposeException("Skipped assigning cell - Failed to execute getter or setter method in class " + bean
                            .getClass().getName(), e);
        } catch (IllegalAccessException e) {
            throw new BeanComposeException("Skipped assigning cell - Failed to access getter or setter method in class " + bean
                            .getClass().getName(), e);
        } catch (NoSuchMethodException e) {
            throw new BeanComposeException(
                    "Skipped assigning cell - Missing getter or setter method in class " + bean.getClass()
                            .getName() + " or a sub class", e);
        }

    }

    /**
     * This implementation uses reflection methods to assign correct object.
     * @param parentBean The parent to create child of
     * @param childBeanName The name of the child property.
     * @return A newly created instance of the child bean
     */
    private Object findOrCreateChildBean(Object parentBean, String childBeanName) throws InstantiationException,
            IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException,
            InvocationTargetException {
        String getterMethodName = createGetMethodName(childBeanName);
        Method getterMethod = parentBean.getClass().getMethod(getterMethodName);
        Object childBean = getterMethod.invoke(parentBean);
        if (childBean == null) {
            // If there was no object we have to create it..
            Class<?> nextClass = getterMethod.getReturnType();
            childBean = nextClass.getConstructor().newInstance();
            // And assign it by using the setter.
            String setterMethodName = createSetMethodName(childBeanName);
            parentBean.getClass().getMethod(setterMethodName, nextClass).invoke(parentBean, childBean);
        }
        return childBean;
    }

    /**
     * Creates a set method name based on attribute name.
     * @param sAttributeName The attribute name
     * @return The set method that corresponds to this attribute.
     */
    private String createSetMethodName(String sAttributeName) {
        return createBeanMethodName(SET_PREFIX, sAttributeName);
    }

    /**
     * Creates a get method based on attribute name.
     * @param sAttributeName The attribute name
     * @return The get method that corresponds to this attribute.
     */
    private String createGetMethodName(String sAttributeName) {
        return createBeanMethodName(GET_PREFIX, sAttributeName);
    }

    /**
     * Creates an access method based on attribute
     * @param prefix The access method prefix.
     * @param sAttributeName The attribute name.
     * @return The setter or setter method that corresponds to this attribute.
     */
    private String createBeanMethodName(String prefix, String sAttributeName) {
        return prefix + sAttributeName.substring(0, 1).toUpperCase() + sAttributeName.substring(1);
    }

    /**
     * Assigns an attribute value to supplied object.
     *  @param cell           The cell to get the value from
     * @param sName          The name of the field
     * @param objectToAssign The object to assign to
     */
    private void assignAttribute(Cell cell, String sName, Object objectToAssign) throws BeanComposeException, InvocationTargetException, IllegalAccessException {
        if (cell.isEmpty())
            return;

        String sSetMethodName = createSetMethodName(sName);
        boolean success = assignParameterBySignature(objectToAssign, sSetMethodName, cell);
        if (!success) // Try again but use the name and try to cast.
            assignParameterByName(objectToAssign, sSetMethodName, cell);
    }

    /**
     * Assigns the cells of a line as attributes to an object.
     *
     * @param <B>            The type of the object to assign
     * @param cell           The cell to get the parameter from.
     * @param objectToAssign The object to assign cell attributes to. The object will be modified.
     * @return True if the parameter was assigned to the object, false otherwise.
     * @throws InvocationTargetException if the underlying method throws an exception.
     * @throws IllegalAccessException if this Method object is enforcing Java language access control and the underlying method is inaccessible.
     * @throws IllegalArgumentException if the method is an instance method and the specified object argument is not an instance of the class or interface declaring the underlying method (or of a subclass or implementor thereof); if the number of actual and formal parameters differ; if an unwrapping conversion for primitive arguments fails; or if, after possible unwrapping, a parameter value cannot be converted to the corresponding formal parameter type by a method invocation conversion.
     */
    private <B> boolean assignParameterBySignature(B objectToAssign, String sSetMethodName, Cell cell)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {

        if (cell.getValue() == null)
            return false;
        try {
            Class<?> type = cell.getValue().getClass();
            Method f = objectToAssign.getClass().getMethod(sSetMethodName, type);
            f.invoke(objectToAssign, cell.getValue());
            return true;
        } catch (NoSuchMethodException e) {
            // We don't care here since we will try again if this method fails.
        }
        return false;
    }

    /**
     * Assigns the cells of a line as attributes to an object.
     *
     * @param objectToAssign The object to assign cell attributes to. The object will be modified.
     * @param sSetMethodName The name of the setter
     * @param cell           The cell to get the parameter from.
     * @param <B>            The type of the object to assign
     * @throws InvocationTargetException if the underlying method throws an exception.
     * @throws IllegalAccessException if this Method object is enforcing Java language access control and the underlying method is inaccessible.
     * @throws IllegalArgumentException if the method is an instance method and the specified object argument is not an instance of the class or interface declaring the underlying method (or of a subclass or implementor thereof); if the number of actual and formal parameters differ; if an unwrapping conversion for primitive arguments fails; or if, after possible unwrapping, a parameter value cannot be converted to the corresponding formal parameter type by a method invocation conversion.
     * @throws BeanComposeException In case of unable to create or assign bean.
     */
    @SuppressWarnings("unchecked")
    private <B> void assignParameterByName(B objectToAssign, String sSetMethodName, Cell cell)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, BeanComposeException {

        try {
            Method[] methods = objectToAssign.getClass().getMethods();
            for (Method f : methods) {
                Class<?>[] paramTypes = f.getParameterTypes();
                if (paramTypes.length != 1 || !f.getName().equals(sSetMethodName))
                    continue;

                Object value = cell.getValue();
                // Casts between simple types does not work automatically
                Class<?> paramType = paramTypes[0];
                if (paramType == Integer.TYPE && value instanceof Number)
                    f.invoke(objectToAssign, ((Number) value).intValue());
                else if (paramType == Short.TYPE && value instanceof Number)
                    f.invoke(objectToAssign, ((Number) value).shortValue());
                else if (paramType == Byte.TYPE && value instanceof Number)
                    f.invoke(objectToAssign, ((Number) value).byteValue());
                else if (paramType == Float.TYPE && value instanceof Number)
                    f.invoke(objectToAssign, ((Number) value).floatValue());
                    // Will squeeze in first character of any datatype's string representation.
                else if (paramType == Character.TYPE) {
                    if (value instanceof Character) {
                        f.invoke(objectToAssign, (Character) value);
                    } else {
                        String sValue = value.toString();
                        if (!sValue.isEmpty())
                            f.invoke(objectToAssign, sValue.charAt(0));
                    }
                } else if (Enum.class.isAssignableFrom(paramType) && value instanceof String) {
                    f.invoke(objectToAssign, Enum.valueOf((Class<Enum>) paramType, String.valueOf(value)));
                } else {
                    try {
                        f.invoke(objectToAssign, value);
                    } catch (IllegalArgumentException e) {
                        // There may be more methods that fits the name.
                        continue;
                    }
                }
                return;
            }
            throw new BeanComposeException(
                    "Skipped assigning cell - No method called " + sSetMethodName + "() found in class "
                            + objectToAssign.getClass().getName() + " that fits the cell ");
        } catch (SecurityException e) {
            throw new BeanComposeException(
                    "Skipped assigning cell - The method " + sSetMethodName + "() in class " + objectToAssign.getClass()
                            .getName() + " does not have public access", e);
        }
    }


}
