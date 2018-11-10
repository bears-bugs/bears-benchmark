/*
 * Copyright 2014-2018 Web Firm Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * @author WFF
 */
package com.webfirmframework.wffweb.tag.html.attribute.event;

import java.util.logging.Logger;

import com.webfirmframework.wffweb.tag.html.attribute.core.AbstractAttribute;
import com.webfirmframework.wffweb.util.StringUtil;

/**
 *
 * All event attributes will be extend by this class. It contains some common
 * features which all event attribute classes want.
 *
 * @since 2.0.0
 * @author WFF
 *
 */
public abstract class AbstractEventAttribute extends AbstractAttribute
        implements EventAttribute {

    private static final long serialVersionUID = 1_0_0L;

    public static final Logger LOGGER = Logger
            .getLogger(AbstractEventAttribute.class.getName());

    private ServerAsyncMethod serverAsyncMethod;

    private String jsFilterFunctionBody;

    private String jsPreFunctionBody;

    private String jsPostFunctionBody;

    // shortname for wffServerMethods is wffSM

    // for better readability it's not capitalized
    // shortname for invokeAsyncWithPreFilterFun is iawpff
    private static final String invokeAsyncWithPreFilterFun = "wffSM.iawpff";

    // for better readability it's not capitalized
    // shortname for invokeAsyncWithPreFun is iawpf
    private static final String invokeAsyncWithPreFun = "wffSM.iawpf";

    // for better readability it's not capitalized
    // shortname for invokeAsyncWithFilterFun is iawff
    private static final String invokeAsyncWithFilterFun = "wffSM.iawff";

    // for better readability it's not capitalized
    // shortname for invokeAsync is ia
    private static final String invokeAsync = "wffSM.ia";

    {
        init();
    }

    protected AbstractEventAttribute() {
    }

    /**
     *
     * @param value
     *            the value for the attribute
     * @since 2.0.0
     * @author WFF
     */
    protected AbstractEventAttribute(final String attributeName,
            final String value) {
        super.setAttributeName(attributeName);
        super.setAttributeValue(value);
    }

    /**
     * @param attributeName
     * @param jsPreFunctionBody
     *            the body part javascript function (without function
     *            declaration). It must return true/false. This function will
     *            invoke at client side before {@code serverAsyncMethod}. If the
     *            jsPrefunction returns true then only {@code serverAsyncMethod}
     *            method will invoke (if it is implemented). It has implicit
     *            objects like {@code event} and {@code source} which gives the
     *            reference of the current tag. <br>
     *            Eg:-
     *
     *            <pre>
     *            if (source.type == 'button') {
     *               return true;
     *            }
     *            return false;
     *            </pre>
     *
     * @param serverAsyncMethod
     *            This method will invoke at server side with an argument
     *            {@code wffBMObject}. The {@code wffBMObject} is the
     *            representational javascript object returned by
     *            {@code jsFilterFunctionBody}.
     *
     * @param jsFilterFunctionBody
     *            The body part of a javascript function (without function
     *            declaration). It can return a javascript object so that it
     *            will be available at server side in {@code serverAsyncMethod}
     *            as {@code wffBMObject} parameter. There are implicit objects
     *            {@code event} and {@code source} in the scope.<br>
     *            Eg:-
     *
     *            <pre>
     *            var bName = source.name;
     *            return {buttonName: bName, author:'wff', dateOfYear: 2014};
     *            </pre>
     *
     * @param jsPostFunctionBody
     *            The body part of a javascript function (without function
     *            declaration). The {@code wffBMObject} returned by
     *            {@code serverAsyncMethod} will be available as an implicit
     *            object {@code jsObject} in the scope. There are common
     *            implicit objects {@code event} and {@code source} in the
     *            scope.
     * @since 2.0.0
     * @author WFF
     */
    protected AbstractEventAttribute(final String attributeName,
            final String jsPreFunctionBody,
            final ServerAsyncMethod serverAsyncMethod,
            final String jsFilterFunctionBody,
            final String jsPostFunctionBody) {

        setAttributeName(attributeName);
        setServerAsyncMethod(jsPreFunctionBody, serverAsyncMethod,
                jsFilterFunctionBody, jsPostFunctionBody);
    }

    /**
     * invokes only once per object
     *
     * @author WFF
     * @since 2.0.0
     */
    protected void init() {
        // to override and use this method
    }

    /**
     * sets the value for this attribute
     *
     * @param value
     *            the value for the attribute.
     * @since 2.0.0
     * @author WFF
     */
    public void setValue(final String value) {
        super.setAttributeValue(value);
    }

    /**
     * gets the value of this attribute
     *
     * @return the value of the attribute
     * @since 2.0.0
     * @author WFF
     */
    public String getValue() {
        return super.getAttributeValue();
    }

    /**
     * @param serverAsyncMethod
     *            the {@code ServerAsyncMethod} object to set.
     * @author WFF
     */
    public void setServerAsyncMethod(
            final ServerAsyncMethod serverAsyncMethod) {
        if (serverAsyncMethod != null) {

            setServerAsyncMethod(jsPreFunctionBody, serverAsyncMethod,
                    jsFilterFunctionBody, jsPostFunctionBody);
        }
    }

    private static String getPreparedJsFunctionBody(
            final String jsfunctionBody) {

        final String functionBody = StringUtil.strip(jsfunctionBody);
        final StringBuilder builder = new StringBuilder(26);

        builder.append("function(event, source){").append(functionBody);
        if (functionBody.charAt(functionBody.length() - 1) != ';') {
            builder.append(';');
        }

        return builder.append('}').toString();
    }

    /**
     * @param jsPreFunctionBody
     *            the body part javascript function (without function
     *            declaration). It must return true/false. This function will
     *            invoke at client side before {@code serverAsyncMethod}. If the
     *            jsPrefunction returns true then only {@code serverAsyncMethod}
     *            method will invoke (if it is implemented). It has implicit
     *            objects like {@code event} and {@code source} which gives the
     *            reference of the current tag. <br>
     *            Eg:-
     *
     *            <pre>
     *            if (source.type == 'button') {
     *               return true;
     *            }
     *            return false;
     *            </pre>
     *
     * @param serverAsyncMethod
     *            This method will invoke at server side with an argument
     *            {@code wffBMObject}. The {@code wffBMObject} is the
     *            representational javascript object returned by
     *            {@code jsFilterFunctionBody}.
     *
     * @param jsFilterFunctionBody
     *            The body part of a javascript function (without function
     *            declaration). It can return a javascript object so that it
     *            will be available at server side in {@code serverAsyncMethod}
     *            as {@code wffBMObject} parameter. There are implicit objects
     *            {@code event} and {@code source} in the scope.<br>
     *            Eg:-
     *
     *            <pre>
     *            var bName = source.name;
     *            return {buttonName: bName, author:'wff', dateOfYear: 2014};
     *            </pre>
     *
     * @param jsPostFunctionBody
     *            The body part of a javascript function (without function
     *            declaration). The {@code wffBMObject} returned by
     *            {@code serverAsyncMethod} will be available as an implicit
     *            object {@code jsObject} in the scope. There are common
     *            implicit objects {@code event} and {@code source} in the
     *            scope.
     * @since 2.0.0
     * @author WFF
     */
    public void setServerAsyncMethod(final String jsPreFunctionBody,
            final ServerAsyncMethod serverAsyncMethod,
            final String jsFilterFunctionBody,
            final String jsPostFunctionBody) {

        if (serverAsyncMethod != null) {

            if (jsPreFunctionBody != null && jsPostFunctionBody != null
                    && jsFilterFunctionBody != null) {

                super.setAttributeValue(
                        new StringBuilder(invokeAsyncWithPreFilterFun)
                                .append("(event, this,'")
                                .append(getAttributeName()).append("',")
                                .append(getPreparedJsFunctionBody(
                                        jsPreFunctionBody))
                                .append(',')
                                .append(getPreparedJsFunctionBody(
                                        jsFilterFunctionBody))
                                .append(')').toString());

            } else if (jsPreFunctionBody != null
                    && jsPostFunctionBody != null) {

                super.setAttributeValue(new StringBuilder(invokeAsyncWithPreFun)
                        .append("(event, this,'").append(getAttributeName())
                        .append("',")
                        .append(getPreparedJsFunctionBody(jsPreFunctionBody))
                        .append(')').toString());

            } else if (jsPreFunctionBody != null
                    && jsFilterFunctionBody != null) {

                super.setAttributeValue(
                        new StringBuilder(invokeAsyncWithPreFilterFun)
                                .append("(event, this,'")
                                .append(getAttributeName()).append("',")
                                .append(getPreparedJsFunctionBody(
                                        jsPreFunctionBody))
                                .append(',')
                                .append(getPreparedJsFunctionBody(
                                        jsFilterFunctionBody))
                                .append(')').toString());

            } else if (jsPostFunctionBody != null
                    && jsFilterFunctionBody != null) {

                super.setAttributeValue(
                        new StringBuilder(invokeAsyncWithFilterFun)
                                .append("(event, this,'")
                                .append(getAttributeName()).append("',")
                                .append(getPreparedJsFunctionBody(
                                        jsFilterFunctionBody))
                                .append(')').toString());

            } else if (jsPreFunctionBody != null) {

                super.setAttributeValue(new StringBuilder(invokeAsyncWithPreFun)
                        .append("(event, this,'").append(getAttributeName())
                        .append("',")
                        .append(getPreparedJsFunctionBody(jsPreFunctionBody))
                        .append(')').toString());

            } else if (jsFilterFunctionBody != null) {

                super.setAttributeValue(
                        new StringBuilder(invokeAsyncWithFilterFun)
                                .append("(event, this,'")
                                .append(getAttributeName()).append("',")
                                .append(getPreparedJsFunctionBody(
                                        jsFilterFunctionBody))
                                .append(')').toString());
            } else if (jsPostFunctionBody != null) {
                super.setAttributeValue(new StringBuilder(invokeAsync)
                        .append("(event, this,'").append(getAttributeName())
                        .append("')").toString());
            } else {

                super.setAttributeValue(new StringBuilder(invokeAsync)
                        .append("(event, this,'").append(getAttributeName())
                        .append("')").toString());
            }

            this.jsPreFunctionBody = jsPreFunctionBody;
            this.jsFilterFunctionBody = jsFilterFunctionBody;
            this.jsPostFunctionBody = jsPostFunctionBody;
            this.serverAsyncMethod = serverAsyncMethod;
        } else {
            LOGGER.warning(
                    "serverAsyncMethod is null so jsPreFunctionBody, jsFilterFunctionBody and jsPostFunctionBody are not also set.They are valid only if serverAsyncMethod is NOT null.");
        }
    }

    @Override
    public ServerAsyncMethod getServerAsyncMethod() {
        return serverAsyncMethod;
    }

    @Override
    public String getJsPostFunctionBody() {
        return jsPostFunctionBody;
    }

    /**
     * @return
     * @since 2.1.9
     * @author WFF
     */
    public String getJsFilterFunctionBody() {
        return jsFilterFunctionBody;
    }

    /**
     * @return
     * @since 2.1.9
     * @author WFF
     */
    public String getJsPreFunctionBody() {
        return jsPreFunctionBody;
    }

    /**
     * Sets the post function body JavaScript.
     *
     * @param jsPostFunctionBody
     *            the post function body JavaScript.
     * @author WFF
     */
    public void setJsPostFunctionBody(final String jsPostFunctionBody) {
        setServerAsyncMethod(jsPreFunctionBody, serverAsyncMethod,
                jsFilterFunctionBody, jsPostFunctionBody);
    }

    /**
     * Sets the pre function body JavaScript.
     *
     * @param jsPreFunctionBody
     * @since 2.1.9
     * @author WFF
     */
    public void setJsPreFunctionBody(final String jsPreFunctionBody) {
        setServerAsyncMethod(jsPreFunctionBody, serverAsyncMethod,
                jsFilterFunctionBody, jsPostFunctionBody);
    }

    /**
     * Sets the filter function body JavaScript.
     *
     * @param jsFilterFunctionBody
     * @since 2.1.9
     * @author WFF
     */
    public void setJsFilterFunctionBody(final String jsFilterFunctionBody) {
        setServerAsyncMethod(jsPreFunctionBody, serverAsyncMethod,
                jsFilterFunctionBody, jsPostFunctionBody);
    }
}
