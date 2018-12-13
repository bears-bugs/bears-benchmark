package org.traccar.web;

import org.junit.Test;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class WebServerTest {

    @Test
    public void contextTest() throws NamingException {
        DataSource mockDataSource = (DataSource) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[] { DataSource.class }, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        return null;
                    }
                });

        Context context = new InitialContext();
        context.bind("java:/DefaultDS", mockDataSource);
    }

}
