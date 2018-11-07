package org.jsapar.parse.bean;

import org.jsapar.TstPerson;
import org.jsapar.compose.bean.BeanComposeException;
import org.jsapar.model.BooleanCell;
import org.jsapar.model.Cell;
import org.jsapar.model.StringCell;
import org.junit.Test;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class Bean2CellTest {


     @Test
    public void getPropertyDescriptor_getCellName() throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(TstPerson.class);
        PropertyDescriptor propertyDescriptor = Arrays.stream(beanInfo.getPropertyDescriptors())
                .filter(it -> it.getName().equals("adult")).findFirst().orElseThrow(AssertionError::new);
        Bean2Cell bean2Cell = Bean2Cell.ofCellName("adult", propertyDescriptor);
        assertSame(propertyDescriptor, bean2Cell.getPropertyDescriptor());
        assertEquals("adult", bean2Cell.getCellName());
    }

    @Test
    public void makeCell() throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        Bean2Cell bean2Cell = makeBean2CellOfPropertyName(TstPerson.class, "adult");
        TstPerson tstPerson = new TstPerson();
        tstPerson.setAdult(true);
        Cell cell = bean2Cell.makeCell(tstPerson);
        assertEquals("adult", cell.getName());
        assertEquals(BooleanCell.class, cell.getClass());
        BooleanCell booleanCell = (BooleanCell) cell;
        assertEquals(Boolean.TRUE, booleanCell.getValue());
    }

    @Test
    public void assign_Boolean()
            throws IntrospectionException, InvocationTargetException, InstantiationException, IllegalAccessException,
            BeanComposeException {
        Bean2Cell bean2Cell = makeBean2CellOfPropertyName(TstPerson.class, "adult");
        TstPerson tstPerson = new TstPerson();
        assertFalse(tstPerson.isAdult());
        bean2Cell.assign(tstPerson, new BooleanCell("adult", true));
        assertTrue(tstPerson.isAdult());
        bean2Cell.assign(tstPerson, new BooleanCell("adult", false));
        assertFalse(tstPerson.isAdult());
    }

    @Test
    public void assign_String()
            throws IntrospectionException, InvocationTargetException, InstantiationException, IllegalAccessException,
            BeanComposeException {
        Bean2Cell bean2Cell = makeBean2CellOfPropertyName(TstPerson.class, "lastName");
        TstPerson tstPerson = new TstPerson();
        assertEquals("Nobody", tstPerson.getLastName());
        bean2Cell.assign(tstPerson, new StringCell("lastName", "Somebody"));
        assertEquals("Somebody", tstPerson.getLastName());
    }

    @SuppressWarnings("SameParameterValue")
    private Bean2Cell makeBean2CellOfPropertyName(Class<?> beanClass, String propertyName) throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
        PropertyDescriptor propertyDescriptor = Arrays.stream(beanInfo.getPropertyDescriptors())
                .filter(it -> it.getName().equals(propertyName)).findFirst().orElseThrow(AssertionError::new);
        return Bean2Cell.ofCellName(propertyName, propertyDescriptor);
    }

    @Test
    public void assign_empty()
            throws IntrospectionException, InvocationTargetException, InstantiationException, IllegalAccessException,
            BeanComposeException {
        Bean2Cell bean2Cell = makeBean2CellOfPropertyName(TstPerson.class, "lastName");
        TstPerson tstPerson = new TstPerson();
        assertEquals("Nobody", tstPerson.getLastName());
        bean2Cell.assign(tstPerson, StringCell.emptyOf("lastName"));
        assertEquals("Nobody", tstPerson.getLastName());
    }

}