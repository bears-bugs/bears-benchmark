package org.jsapar.parse.bean;

import org.jsapar.error.BeanException;
import org.jsapar.schema.SchemaCell;
import org.jsapar.schema.SchemaLine;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class BeanPropertyMap {
    private String lineType;

    private Map<String, Bean2Cell> bean2CellByProperty = new HashMap<>();
    private Map<String, Bean2Cell> bean2CellByCellName = new HashMap<>();
    private BeanInfo beanInfo;
    private Class lineClass;

    private BeanPropertyMap(String lineType) {
        this.lineType = lineType;
    }

    private BeanPropertyMap(String lineType, Class lineClass) throws IntrospectionException {
        this.lineType = lineType;
        this.beanInfo = Introspector.getBeanInfo(lineClass);
        this.lineClass = lineClass;
    }

    boolean ignoreLine(){
        return lineClass == null;
    }

    public String getLineType() {
        return lineType;
    }

    Collection<Bean2Cell> getBean2Cells() {
        return bean2CellByProperty.values();
    }

    private Bean2Cell getBean2CellByProperty(String propertyName){
        return bean2CellByProperty.get(propertyName);
    }

    public Bean2Cell getBean2CellByName(String cellName){
        return bean2CellByCellName.get(cellName);
    }


    public Object createBean() throws IllegalAccessException, InstantiationException {
        return lineClass.newInstance();
    }

    static BeanPropertyMap ofSchemaLine(SchemaLine schemaLine, BeanPropertyMap overrideValues) {
        try {
            if(overrideValues.ignoreLine())
                return overrideValues;

            Map<String, String> cellNamesOfProperty = new HashMap<>(
                    schemaLine.stream().collect(Collectors.toMap(SchemaCell::getName, SchemaCell::getName)));
            overrideValues.bean2CellByCellName
                    .forEach((key, value) -> cellNamesOfProperty.put(value.getPropertyDescriptor().getName(), key));

            return ofPropertyNames(overrideValues.getLineClass().getName(), schemaLine.getLineType(), cellNamesOfProperty);
        } catch (ClassNotFoundException |IntrospectionException e) {
            throw new BeanException("Failed to create bean mapping based on schema", e);
        }
    }

    static BeanPropertyMap ofSchemaLine(SchemaLine schemaLine) throws BeanException {
        try {
            return ofPropertyNames(schemaLine.getLineType(), schemaLine.getLineType(), schemaLine.stream().collect(Collectors.toMap(SchemaCell::getName, SchemaCell::getName)));
        } catch (ClassNotFoundException |IntrospectionException e) {
            throw new BeanException("Failed to create bean mapping based on schema", e);
        }
    }

    @SuppressWarnings("WeakerAccess")
    public static BeanPropertyMap ofPropertyNames(String className, String lineType, Map<String, String> cellNamesOfProperty) throws ClassNotFoundException, IntrospectionException{
        if(className==null || className.isEmpty()){
            return new BeanPropertyMap(lineType);
        }
        Class lineClass = Class.forName(className);
        BeanPropertyMap beanPropertyMap = new BeanPropertyMap(lineType, lineClass);

        Map<String, PropertyDescriptor> descriptors = Arrays.stream(beanPropertyMap.beanInfo.getPropertyDescriptors())
                .collect(Collectors.toMap(PropertyDescriptor::getName, pd->pd));

        for(Map.Entry<String, String> propertyEntry : cellNamesOfProperty.entrySet()) {
            String propertyName = propertyEntry.getKey();
            String cellName = propertyEntry.getValue();
            if(cellName==null)
                cellName=propertyName;

            PropertyDescriptor propertyDescriptor = descriptors.get(propertyName);
            if(propertyDescriptor != null){
                beanPropertyMap.putBean2Cell(propertyName, Bean2Cell.ofCellName(cellName, propertyDescriptor));
            }
            else
                cellOfChildObject(cellName, propertyName, beanPropertyMap, descriptors);
        }
        return beanPropertyMap;
    }

    private static void cellOfChildObject(String cellName, String propertyName, BeanPropertyMap beanPropertyMap, Map<String, PropertyDescriptor> descriptors) throws IntrospectionException {
        if(propertyName.contains(".")){
            String[] propertyNames = propertyName.split("\\.", 2);
            PropertyDescriptor propertyDescriptor = descriptors.get(propertyNames[0]);
            if(propertyDescriptor != null) {
                beanPropertyMap.putBean2Cell(propertyNames[0], cellName,
                        cellOfChildObject(cellName,
                                propertyNames[1],
                                propertyDescriptor,
                                beanPropertyMap.getBean2CellByProperty(propertyNames[0])));
            }

        }
    }

    private static Bean2Cell cellOfChildObject(String cellName, String propertyName, PropertyDescriptor basePropertyDescriptor, Bean2Cell baseBean2Cell) throws IntrospectionException {
        if(baseBean2Cell == null) {
            Class childClass = basePropertyDescriptor.getReadMethod().getReturnType();
            baseBean2Cell = Bean2Cell.ofBaseProperty(basePropertyDescriptor, new BeanPropertyMap(basePropertyDescriptor.getName(), childClass));
        }
        BeanPropertyMap beanPropertyMap = baseBean2Cell.getChildren();
        Map<String, PropertyDescriptor> descriptors = Arrays.stream(beanPropertyMap.beanInfo.getPropertyDescriptors())
                .collect(Collectors.toMap(PropertyDescriptor::getName, pd->pd));

        PropertyDescriptor propertyDescriptor = descriptors.get(propertyName);
        if(propertyDescriptor != null){
            beanPropertyMap.putBean2Cell(propertyName, cellName, Bean2Cell.ofCellName(cellName, propertyDescriptor));
            return baseBean2Cell;
        }
        else
            cellOfChildObject(cellName, propertyName, beanPropertyMap, descriptors);
        return baseBean2Cell;
    }

    private void putBean2Cell(String propertyName, String cellName, Bean2Cell bean2Cell) {
        this.bean2CellByProperty.put(propertyName, bean2Cell);
        this.bean2CellByCellName.put(cellName, bean2Cell);
    }

    private void putBean2Cell(String propertyName, Bean2Cell bean2Cell) {
        putBean2Cell(propertyName, bean2Cell.getCellName(), bean2Cell);
    }

    Class getLineClass() {
        return lineClass;
    }

}
