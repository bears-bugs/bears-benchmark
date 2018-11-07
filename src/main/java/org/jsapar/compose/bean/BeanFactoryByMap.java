package org.jsapar.compose.bean;

import org.jsapar.model.Cell;
import org.jsapar.model.Line;
import org.jsapar.parse.bean.Bean2Cell;
import org.jsapar.parse.bean.BeanMap;
import org.jsapar.parse.bean.BeanPropertyMap;

import java.lang.reflect.InvocationTargetException;

/**
 * Class that creates beans based on a {@link BeanMap}. The {@link BeanMap} have the information about how the beans
 * should be created and how properties can be assigned.
 * @param <T> common base class of all the expected beans. Use Object as base class if there is no common base class for all beans.
 */
public class BeanFactoryByMap<T> implements BeanFactory<T>{
    private BeanMap beanMap;

    public BeanFactoryByMap(BeanMap beanMap) {
        this.beanMap = beanMap;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T createBean(Line line) throws InstantiationException, IllegalAccessException, ClassCastException {
        BeanPropertyMap optionalBeanPropertyMap = beanMap.getBeanPropertyMap(line.getLineType());
        if(optionalBeanPropertyMap != null){
            return (T) optionalBeanPropertyMap.createBean();
        }
        return null;
    }

    @Override
    public void assignCellToBean(String lineType, T bean, Cell cell) throws InvocationTargetException, InstantiationException, IllegalAccessException, BeanComposeException {
        BeanPropertyMap beanPropertyMap = beanMap.getBeanPropertyMap(lineType);
        Bean2Cell bean2Cell = beanPropertyMap.getBean2CellByName(cell.getName());
        if(bean2Cell != null)
            bean2Cell.assign(bean, cell);
    }

}
