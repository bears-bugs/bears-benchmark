package org.jsapar.compose.bean;

import org.jsapar.TstPerson;
import org.jsapar.model.IntegerCell;
import org.jsapar.model.Line;
import org.jsapar.model.StringCell;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

public class BeanFactoryDefaultTest {

    @Test
    public void createBean() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Line line = new Line("org.jsapar.TstPerson");
        BeanFactoryDefault<TstPerson> beanFactory = new BeanFactoryDefault<>();
        TstPerson person = beanFactory.createBean(line);
        assertNotNull(person);
    }

    public class NoDefault {
        public NoDefault(String someValue) {
        }
    }
    @Test(expected = NoSuchMethodException.class)
    public void createBean_noDefault() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Line line = new Line("org.jsapar.compose.bean.BeanFactoryDefaultTest$NoDefault");
        BeanFactoryDefault<NoDefault> beanFactory = new BeanFactoryDefault<>();
        beanFactory.createBean(line);
        fail("Should throw exception");
    }

    @Test
    public void assignCellToBean() throws BeanComposeException {
        String lineType = "org.jsapar.TstPerson";
        BeanFactoryDefault<TstPerson> beanFactory = new BeanFactoryDefault<>();
        TstPerson person = new TstPerson();

        beanFactory.assignCellToBean(lineType, person, new StringCell("firstName", "Nils"));
        assertEquals("Nils", person.getFirstName());
        beanFactory.assignCellToBean(lineType, person, new IntegerCell("luckyNumber", 4711));
        assertEquals(4711, person.getLuckyNumber());
    }

    @Test(expected = BeanComposeException.class)
    public void assignCellToBean_notProperty() throws BeanComposeException {
        String lineType = "org.jsapar.TstPerson";
        BeanFactoryDefault<TstPerson> beanFactory = new BeanFactoryDefault<>();
        TstPerson person = new TstPerson();

        beanFactory.assignCellToBean(lineType, person, new StringCell("doesNotExist", "the value"));
        fail("Should throw exception");
    }

    @Test
    public void assignCellToBean_nested() throws BeanComposeException {
        String lineType = "org.jsapar.TstPerson";
        BeanFactoryDefault<TstPerson> beanFactory = new BeanFactoryDefault<>();
        TstPerson person = new TstPerson();

        beanFactory.assignCellToBean(lineType, person, new StringCell("address.street", "Helsinki road"));
        assertEquals("Helsinki road", person.getAddress().getStreet());
    }

}