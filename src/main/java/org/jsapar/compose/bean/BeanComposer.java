package org.jsapar.compose.bean;

import org.jsapar.compose.ComposeException;
import org.jsapar.compose.Composer;
import org.jsapar.compose.ValidationHandler;
import org.jsapar.error.ErrorEvent;
import org.jsapar.error.ErrorEventListener;
import org.jsapar.error.ExceptionErrorEventListener;
import org.jsapar.model.Cell;
import org.jsapar.model.Line;

import java.lang.reflect.InvocationTargetException;

/**
 * Composer class that composes java beans based on a document or by single lines. The result is that for each bean that
 * was successfully composed, a {@link BeanEvent} is generated to all registered {@link BeanEventListener}.
 * You can register a {@link BeanEventListener} by calling {@link #setComposedEventListener(BeanEventListener)}
 *
 * @param <T> common base class of all the expected beans. Use Object as base class if there is no common base class for all beans.
 */
@SuppressWarnings("UnusedReturnValue")
public class BeanComposer<T> implements Composer, BeanEventListener<T>, ErrorEventListener {
    private BeanEventListener<T> composedEventListener;
    private ErrorEventListener errorEventListener = new ExceptionErrorEventListener();
    private BeanFactory<T> beanFactory;
    private BeanComposeConfig config;
    private ValidationHandler validationHandler = new ValidationHandler();

    /**
     * Creates a bean composer with {@link BeanFactoryDefault} as {@link BeanFactory}
     */
    public BeanComposer() {
        this(new BeanComposeConfig(), new BeanFactoryDefault<>());
    }

    /**
     * @param config Configuration to use
     */
    public BeanComposer(BeanComposeConfig config) {
        this(config, new BeanFactoryDefault<>());
    }

    /**
     * Creates a bean composer with a customized {@link BeanFactory}. You can implement your own {@link BeanFactory} in
     * order to control which bean class should be created for each line that is composed.
     *
     * @param beanFactory An implementation of the {@link BeanFactory} interface.
     */
    public BeanComposer(BeanFactory<T> beanFactory) {
        this(new BeanComposeConfig(), beanFactory);
    }

    /**
     * Creates a bean composer with a customized {@link BeanFactory}. You can implement your own {@link BeanFactory} in
     * order to control which bean class should be created for each line that is composed.
     * @param config Configuration to use
     * @param beanFactory An implementation of the {@link BeanFactory} interface.
     */
    public BeanComposer(BeanComposeConfig config, BeanFactory<T> beanFactory) {
        this.config = config;
        this.beanFactory = beanFactory;
    }


    @Override
    public boolean composeLine(Line line) {
        T bean = null;
        try {
            bean = beanFactory.createBean(line);
            if (bean == null) {
                if (!validationHandler.lineValidationError(this, line,
                        "BeanFactory failed to instantiate object for this line because there was no associated class. You can errors like this by setting config.onUndefinedLineType=OMIT_LINE",
                        config.getOnUndefinedLineType(), this)) {
                    return false;
                }
            } else {
                assign(line, bean);
            }
        } catch (InstantiationException|NoSuchMethodException|InvocationTargetException e) {
            generateErrorEvent(line, "Failed to instantiate object. Skipped creating bean", e);
        } catch (IllegalAccessException e) {
            generateErrorEvent(line, "Failed to call set method. Skipped creating bean", e);
        } catch (ClassNotFoundException e) {
            generateErrorEvent(line, "Class not found. Skipped creating bean", e);
        } catch (ClassCastException e) {
            generateErrorEvent(line,
                    "Class of the created bean is not inherited from the generic type specified when creating the BeanComposer",
                    e);
        }
        beanComposedEvent(new BeanEvent<>(this, bean, line));
        return true;
    }

    private void generateErrorEvent(Line line, String message, Throwable t) {
        errorEvent(new ErrorEvent(this, new ComposeException(message, line, t)));
    }


    public void setComposedEventListener(BeanEventListener<T> eventListener) {
        this.composedEventListener = eventListener;
    }

    @Override
    public void setErrorEventListener(ErrorEventListener errorEventListener) {
        this.errorEventListener = errorEventListener;
    }

    public BeanFactory<T> getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(BeanFactory<T> beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void beanComposedEvent(BeanEvent<T> event) {
        if (composedEventListener != null) {
            composedEventListener.beanComposedEvent(event);
        }
    }

    @Override
    public void errorEvent(ErrorEvent event) {
        errorEventListener.errorEvent(event);
    }

    /**
     * Assigns the cells of a line as attributes to an object.
     *
     * @param line           The line to get parameters from.
     * @param objectToAssign The object to assign cell attributes to. The object will be modified.
     * @return The object that was assigned. The same object that was supplied as parameter.
     */
    private T assign(Line line, T objectToAssign) {

        for (Cell cell : line) {
            String sName = cell.getName();
            if (sName == null || sName.isEmpty() || cell.isEmpty())
                continue;

            try {
                beanFactory.assignCellToBean(line.getLineType(), objectToAssign, cell);
            } catch (BeanComposeException
                    | IllegalArgumentException
                    | IllegalAccessException
                    | InvocationTargetException
                    | InstantiationException e) {
                errorEventListener.errorEvent(new ErrorEvent(this, new ComposeException(e.getMessage() + " while handling cell " + cell, e)));
            }
        }
        return objectToAssign;
    }


    public BeanComposeConfig getConfig() {
        return config;
    }

    public void setConfig(BeanComposeConfig config) {
        this.config = config;
    }
}
