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
 */
package com.webfirmframework.wffweb.tag.html;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import com.webfirmframework.wffweb.tag.html.attribute.core.AbstractAttribute;

/**
 * @author WFF
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public abstract class DocType extends AbstractHtml {

    private static final long serialVersionUID = 1_0_0L;

    private boolean prependDocType;

    private String docTypeTag = "<!DOCTYPE html>";

    /**
     * should be invoked to generate opening and closing tag
     *
     * @param tagName
     * @param attributes
     */
    public DocType(final String tagName, final AbstractHtml base,
            final AbstractAttribute[] attributes) {
        super(tagName, base, attributes);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.webfirmframework.wffweb.tag.html.AbstractHtml#toHtmlString()
     */
    @Override
    public String toHtmlString() {
        if (prependDocType) {
            return new String((docTypeTag + "\n" + super.toHtmlString())
                    .getBytes(getCharset()), getCharset());
        }
        return super.toHtmlString();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.webfirmframework.wffweb.tag.html.AbstractHtml#toBigHtmlString()
     */
    @Override
    public String toBigHtmlString() {
        if (prependDocType) {
            return new String((docTypeTag + "\n" + super.toBigHtmlString())
                    .getBytes(getCharset()), getCharset());
        }
        return super.toBigHtmlString();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.webfirmframework.wffweb.tag.html.AbstractHtml#toBigHtmlString(
     * boolean)
     */
    @Override
    public String toBigHtmlString(final boolean rebuild) {
        if (prependDocType) {
            return new String(
                    (docTypeTag + "\n" + super.toBigHtmlString(rebuild))
                            .getBytes(getCharset()),
                    getCharset());
        }
        return super.toBigHtmlString(rebuild);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.webfirmframework.wffweb.tag.html.AbstractHtml#toHtmlString(java.nio.
     * charset.Charset)
     */
    @Override
    public String toHtmlString(final Charset charset) {
        final Charset previousCharset = super.getCharset();
        try {
            super.setCharset(charset);
            if (prependDocType) {
                // assigning it to new variable is very important here as this
                // line of code should invoke before finally block
                final String htmlString = new String(
                        (super.toHtmlString(super.getCharset()))
                                .getBytes(super.getCharset()),
                        getCharset());
                return htmlString;
            }

            // assigning it to new variable is very important here as this line
            // of code should invoke before finally block
            final String htmlString = super.toHtmlString(super.getCharset());
            return htmlString;
        } finally {
            super.setCharset(previousCharset);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.webfirmframework.wffweb.tag.html.AbstractHtml#toHtmlString(java.lang.
     * String)
     */
    @Override
    public String toHtmlString(final String charset) {
        final Charset previousCharset = super.getCharset();
        try {
            super.setCharset(Charset.forName(charset));
            if (prependDocType) {
                // assigning it to new variable is very important here as this
                // line of code should invoke before finally block
                final String htmlString = new String(
                        (super.toHtmlString(super.getCharset()))
                                .getBytes(super.getCharset()),
                        getCharset());
                return htmlString;
            }
            // assigning it to new variable is very important here as this
            // line of code should invoke before finally block
            final String htmlString = super.toHtmlString(super.getCharset());
            return htmlString;
        } finally {
            super.setCharset(previousCharset);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.webfirmframework.wffweb.tag.html.AbstractHtml#toHtmlString(boolean)
     */
    @Override
    public String toHtmlString(final boolean rebuild) {
        if (prependDocType) {
            return new String((docTypeTag + "\n" + super.toHtmlString(rebuild))
                    .getBytes(getCharset()), getCharset());
        }
        return super.toHtmlString(rebuild);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.webfirmframework.wffweb.tag.html.AbstractHtml#toHtmlString(boolean,
     * java.nio.charset.Charset)
     */
    @Override
    public String toHtmlString(final boolean rebuild, final Charset charset) {
        final Charset previousCharset = super.getCharset();
        try {
            super.setCharset(charset);
            if (prependDocType) {
                // assigning it to new variable is very important here as this
                // line of code should invoke before finally block
                final String htmlString = new String(
                        (super.toHtmlString(rebuild, super.getCharset()))
                                .getBytes(getCharset()),
                        getCharset());
                return htmlString;
            }
            // assigning it to new variable is very important here as this
            // line of code should invoke before finally block
            final String htmlString = super.toHtmlString(rebuild,
                    super.getCharset());
            return htmlString;
        } finally {
            super.setCharset(previousCharset);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.webfirmframework.wffweb.tag.html.AbstractHtml#toHtmlString(boolean,
     * java.lang.String)
     */
    @Override
    public String toHtmlString(final boolean rebuild, final String charset) {
        final Charset previousCharset = super.getCharset();
        try {
            super.setCharset(previousCharset);
            if (prependDocType) {
                // assigning it to new variable is very important here as this
                // line of code should invoke before finally block
                final String htmlString = new String(
                        (super.toHtmlString(rebuild, super.getCharset()))
                                .getBytes(getCharset()),
                        getCharset());
                return htmlString;
            }
            // assigning it to new variable is very important here as this
            // line of code should invoke before finally block
            final String htmlString = super.toHtmlString(rebuild,
                    super.getCharset());
            return htmlString;
        } finally {
            super.setCharset(previousCharset);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.webfirmframework.wffweb.tag.html.AbstractHtml#toOutputStream(java.io.
     * OutputStream)
     */
    @Override
    public int toOutputStream(final OutputStream os) throws IOException {
        int docTypeTagLength = 0;
        if (prependDocType) {
            final byte[] docTypeTagBytes = (docTypeTag + "\n")
                    .getBytes(getCharset());
            os.write(docTypeTagBytes);
            docTypeTagLength = docTypeTagBytes.length;
        }
        return docTypeTagLength + super.toOutputStream(os);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.webfirmframework.wffweb.tag.html.AbstractHtml#toOutputStream(java.io.
     * OutputStream, boolean)
     */
    @Override
    public int toOutputStream(final OutputStream os, final boolean rebuild)
            throws IOException {
        int docTypeTagLength = 0;
        if (prependDocType) {
            final byte[] docTypeTagBytes = (docTypeTag + "\n")
                    .getBytes(getCharset());
            os.write(docTypeTagBytes);
            docTypeTagLength = docTypeTagBytes.length;
        }
        return docTypeTagLength + super.toOutputStream(os, rebuild);
    }

    /**
     * @param os
     *            object of OutputStream to which the bytes to be written
     * @param charset
     *            the charset to encode for the bytes
     *
     * @return the total number of bytes written
     * @throws IOException
     *
     * @since 1.0.0
     * @author WFF
     */
    @Override
    public int toOutputStream(final OutputStream os, final Charset charset)
            throws IOException {
        int docTypeTagLength = 0;
        if (prependDocType) {
            final byte[] docTypeTagBytes = (docTypeTag + "\n")
                    .getBytes(charset);
            os.write(docTypeTagBytes);
            docTypeTagLength = docTypeTagBytes.length;
        }
        return docTypeTagLength + super.toOutputStream(os, charset);
    }

    /**
     * @param os
     *            object of OutputStream to which the bytes to be written
     * @param charset
     *            the charset to encode for the bytes
     * @return the total number of bytes written
     * @throws IOException
     * @since 1.0.0
     * @author WFF
     */
    @Override
    public int toOutputStream(final OutputStream os, final String charset)
            throws IOException {
        int docTypeTagLength = 0;
        final Charset cs = Charset.forName(charset);
        if (prependDocType) {
            final byte[] docTypeTagBytes = (docTypeTag + "\n").getBytes(cs);
            os.write(docTypeTagBytes);
            docTypeTagLength = docTypeTagBytes.length;
        }
        return docTypeTagLength + super.toOutputStream(os, cs);
    }

    /**
     * @param os
     *            object of OutputStream to which the bytes to be written
     * @param rebuild
     *            true to rebuild the tags
     * @param charset
     *            the charset to encode for the bytes
     * @return the total number of bytes written
     * @throws IOException
     * @since 1.0.0
     * @author WFF
     * @return the total number of bytes written
     */
    @Override
    public int toOutputStream(final OutputStream os, final boolean rebuild,
            final Charset charset) throws IOException {
        int docTypeTagLength = 0;
        if (prependDocType) {
            final byte[] docTypeTagBytes = (docTypeTag + "\n")
                    .getBytes(charset);
            os.write(docTypeTagBytes);
            docTypeTagLength = docTypeTagBytes.length;
        }
        return docTypeTagLength + super.toOutputStream(os, rebuild, charset);
    }

    /**
     * @param os
     *            object of OutputStream to which the bytes to be written
     * @param rebuild
     *            true to rebuild the tags
     * @param charset
     *            the charset to encode for the bytes
     * @return the total number of bytes written
     * @throws IOException
     * @since 1.0.0
     * @author WFF
     * @return the total number of bytes written
     */
    @Override
    public int toOutputStream(final OutputStream os, final boolean rebuild,
            final String charset) throws IOException {
        int docTypeTagLength = 0;
        final Charset cs = Charset.forName(charset);
        if (prependDocType) {
            final byte[] docTypeTagBytes = (docTypeTag + "\n").getBytes(cs);
            os.write(docTypeTagBytes);
            docTypeTagLength = docTypeTagBytes.length;
        }
        return docTypeTagLength + super.toOutputStream(os, rebuild, cs);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.webfirmframework.wffweb.tag.html.AbstractHtml#toBigOutputStream(java.
     * io.OutputStream)
     */
    @Override
    public int toBigOutputStream(final OutputStream os) throws IOException {
        int docTypeTagLength = 0;
        if (prependDocType) {
            final byte[] docTypeTagBytes = (docTypeTag + "\n")
                    .getBytes(getCharset());
            os.write(docTypeTagBytes);
            docTypeTagLength = docTypeTagBytes.length;
        }
        return docTypeTagLength + super.toBigOutputStream(os);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.webfirmframework.wffweb.tag.html.AbstractHtml#toBigOutputStream(java.
     * io.OutputStream, boolean)
     */
    @Override
    public int toBigOutputStream(final OutputStream os, final boolean rebuild)
            throws IOException {
        int docTypeTagLength = 0;
        if (prependDocType) {
            final byte[] docTypeTagBytes = (docTypeTag + "\n")
                    .getBytes(getCharset());
            os.write(docTypeTagBytes);
            docTypeTagLength = docTypeTagBytes.length;
        }
        return docTypeTagLength + super.toBigOutputStream(os, rebuild);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.webfirmframework.wffweb.tag.html.AbstractHtml#toBigOutputStream(java.
     * io.OutputStream, java.nio.charset.Charset)
     */
    @Override
    public int toBigOutputStream(final OutputStream os, final Charset charset)
            throws IOException {
        int docTypeTagLength = 0;
        if (prependDocType) {
            final byte[] docTypeTagBytes = (docTypeTag + "\n")
                    .getBytes(charset);
            os.write(docTypeTagBytes);
            docTypeTagLength = docTypeTagBytes.length;
        }
        return docTypeTagLength + super.toBigOutputStream(os, charset);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.webfirmframework.wffweb.tag.html.AbstractHtml#toBigOutputStream(java.
     * io.OutputStream, java.lang.String)
     */
    @Override
    public int toBigOutputStream(final OutputStream os, final String charset)
            throws IOException {
        int docTypeTagLength = 0;
        final Charset cs = Charset.forName(charset);
        if (prependDocType) {
            final byte[] docTypeTagBytes = (docTypeTag + "\n").getBytes(cs);
            os.write(docTypeTagBytes);
            docTypeTagLength = docTypeTagBytes.length;
        }
        return docTypeTagLength + super.toBigOutputStream(os, cs);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.webfirmframework.wffweb.tag.html.AbstractHtml#toBigOutputStream(java.
     * io.OutputStream, boolean, java.nio.charset.Charset)
     */
    @Override
    public int toBigOutputStream(final OutputStream os, final boolean rebuild,
            final Charset charset) throws IOException {
        int docTypeTagLength = 0;
        if (prependDocType) {
            final byte[] docTypeTagBytes = (docTypeTag + "\n")
                    .getBytes(charset);
            os.write(docTypeTagBytes);
            docTypeTagLength = docTypeTagBytes.length;
        }
        return docTypeTagLength + super.toBigOutputStream(os, rebuild, charset);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.webfirmframework.wffweb.tag.html.AbstractHtml#toBigOutputStream(java.
     * io.OutputStream, boolean, java.lang.String)
     */
    @Override
    public int toBigOutputStream(final OutputStream os, final boolean rebuild,
            final String charset) throws IOException {
        int docTypeTagLength = 0;
        final Charset cs = Charset.forName(charset);
        if (prependDocType) {
            final byte[] docTypeTagBytes = (docTypeTag + "\n").getBytes(cs);
            os.write(docTypeTagBytes);
            docTypeTagLength = docTypeTagBytes.length;
        }
        return docTypeTagLength + super.toBigOutputStream(os, rebuild, cs);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.webfirmframework.wffweb.tag.html.AbstractHtml#toString()
     */
    // it is not a best practice to print html string by this method because if
    // it is used in ThreadLocal class it may cause memory leak.
    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * @return the prependDocType
     * @author WFF
     * @since 1.0.0
     */
    public boolean isPrependDocType() {
        return prependDocType;
    }

    /**
     * @param prependDocType
     *            the prependDocType to set
     * @author WFF
     * @since 1.0.0
     */
    public void setPrependDocType(final boolean prependDocType) {
        this.prependDocType = prependDocType;
    }

    /**
     * @return the docTypeTag
     * @author WFF
     * @since 1.0.0
     */
    public String getDocTypeTag() {
        return docTypeTag;
    }

    /**
     * the default doc type is <code>&lt;!DOCTYPE html&gt;</code>
     *
     * @param docTypeTag
     *            the docTypeTag to set
     * @author WFF
     * @since 1.0.0
     */
    public void setDocTypeTag(final String docTypeTag) {
        this.docTypeTag = docTypeTag;
    }

}
