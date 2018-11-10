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
package com.webfirmframework.wffweb.css;

import com.webfirmframework.wffweb.InvalidValueException;
import com.webfirmframework.wffweb.WffRuntimeException;
import com.webfirmframework.wffweb.data.AbstractBean;
import com.webfirmframework.wffweb.util.StringUtil;

/**
 *
 * @author WFF
 * @since 1.0.0
 */
public class UrlCss3Value extends AbstractBean<UrlCss3Value> {

    private static final long serialVersionUID = 1_0_0L;

    // NB : it should not override equals and hashcode methods as its objects
    // are equalized by Objects.equals(obj1, obj2) method in some places.

    private String url;
    private int x = -1;
    private int y = -1;

    @SuppressWarnings("unused")
    private UrlCss3Value() {
    }

    /**
     * @param urlCssValue
     *            eg:- <code> url(\"Test.png\")75 158 </code> or
     *            <code> url(\"Test.png\")</code>
     * @param urlCssValue
     */
    public UrlCss3Value(final String urlCssValue) {
        super();
        extractAndAssign(urlCssValue);
    }

    public void setUrlCssValue(final String urlCssValue) {
        extractAndAssign(urlCssValue);
    }

    // analyze the code for optimization.
    /**
     * @param urlString
     * @since 1.0.0
     * @author WFF
     */
    private void extractAndAssign(final String urlString) {

        final String urlStringTrimmed = StringUtil.strip(urlString);

        final int lastIndexOfClosingParentheses = urlStringTrimmed
                .lastIndexOf(')');

        if (urlStringTrimmed.startsWith("url(")
                && lastIndexOfClosingParentheses != -1) {

            final String urlPart = urlStringTrimmed.substring(0,
                    lastIndexOfClosingParentheses);

            final int indexOfOpeningParantheses = urlPart.indexOf('(');

            String extractedUrl = "";

            if (indexOfOpeningParantheses + 1 < urlPart.length()) {
                extractedUrl = StringUtil.strip(
                        urlPart.substring(indexOfOpeningParantheses + 1));
            }

            extractedUrl = extractedUrl.length() > 0
                    && extractedUrl.charAt(0) == '"' ? extractedUrl.substring(1)
                            : extractedUrl;
            extractedUrl = extractedUrl.endsWith("\"")
                    ? extractedUrl.substring(0, extractedUrl.length() - 1)
                    : extractedUrl;

            url = extractedUrl;

            final int beginIndexCoordinatesPart = lastIndexOfClosingParentheses
                    + 1;

            if (beginIndexCoordinatesPart < urlStringTrimmed.length()) {

                final String coordinatesPart = StringUtil
                        .strip(urlStringTrimmed
                                .substring(beginIndexCoordinatesPart))
                        .replaceAll("\\s+", " ");

                final String[] coordinatesStringParts = StringUtil
                        .splitBySpace(coordinatesPart);

                if (coordinatesStringParts.length == 2) {
                    x = Integer.parseInt(coordinatesStringParts[0]);
                    y = Integer.parseInt(coordinatesStringParts[1]);
                }
            }
        } else {
            throw new InvalidValueException(urlStringTrimmed
                    + " is not a valid url string. It should be in the format of url(\"Test.png\")75 158  or url(\"Testing.png\")");
        }
    }

    /**
     * @param url
     *            eg:- Test.png
     * @param x
     *            x coordinate value. give -1 if no value needs to be defined.
     * @param y
     *            y coordinate value. give -1 if no value needs to be defined.
     */
    public UrlCss3Value(final String url, final int x, final int y) {
        super();
        this.url = url;
        this.x = x;
        this.y = y;
    }

    /**
     * @return the value of url(). i.e. it could be http: or data: string
     * @author WFF
     */
    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
    }

    public int getX() {
        return x;
    }

    public void setX(final int x) {
        final int previousX = this.x;
        try {
            this.x = x;
            if (getStateChangeInformer() != null) {
                getStateChangeInformer().stateChanged(this);
            }
        } catch (final WffRuntimeException e) {
            this.x = previousX;
            throw e;
        }
    }

    public int getY() {
        return y;
    }

    public void setY(final int y) {
        final int previousY = x;
        try {
            this.y = y;
            if (getStateChangeInformer() != null) {
                getStateChangeInformer().stateChanged(this);
            }
        } catch (final WffRuntimeException e) {
            this.y = previousY;
            throw e;
        }
    }

    @Override
    public String toString() {
        String urlString = "url(\"" + url + "\")";
        if (x > -1 && y > -1) {
            urlString = "url(\"" + url + "\") " + x + " " + y;
        }
        return urlString;
    }

    /**
     * @return the print format of these valus as a css value.
     * @since 1.0.0
     * @author WFF
     */
    @Override
    public String getValue() {
        String urlString = "url(\"" + url + "\")";
        if (x > -1 && y > -1) {
            urlString = "url(\"" + url + "\") " + x + " " + y;
        }
        return urlString;
    }
}
