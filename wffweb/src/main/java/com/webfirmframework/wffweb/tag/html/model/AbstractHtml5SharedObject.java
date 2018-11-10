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
package com.webfirmframework.wffweb.tag.html.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.webfirmframework.wffweb.DataWffIdOutOfRangeError;
import com.webfirmframework.wffweb.WffSecurityException;
import com.webfirmframework.wffweb.security.object.SecurityClassConstants;
import com.webfirmframework.wffweb.tag.core.AbstractTagBase;
import com.webfirmframework.wffweb.tag.html.AbstractHtml;
import com.webfirmframework.wffweb.tag.html.attribute.listener.AttributeValueChangeListener;
import com.webfirmframework.wffweb.tag.html.html5.attribute.global.DataWffId;
import com.webfirmframework.wffweb.tag.html.listener.AttributeAddListener;
import com.webfirmframework.wffweb.tag.html.listener.AttributeRemoveListener;
import com.webfirmframework.wffweb.tag.html.listener.ChildTagAppendListener;
import com.webfirmframework.wffweb.tag.html.listener.ChildTagRemoveListener;
import com.webfirmframework.wffweb.tag.html.listener.InnerHtmlAddListener;
import com.webfirmframework.wffweb.tag.html.listener.InsertBeforeListener;
import com.webfirmframework.wffweb.tag.html.listener.PushQueue;
import com.webfirmframework.wffweb.tag.html.listener.WffBMDataDeleteListener;
import com.webfirmframework.wffweb.tag.html.listener.WffBMDataUpdateListener;

/**
 *
 * @author WFF
 * @since 1.0.0
 */
public class AbstractHtml5SharedObject implements Serializable {

    private static final long serialVersionUID = 1_0_1L;

    private boolean childModified;

    private transient volatile Set<AbstractTagBase> rebuiltTags;

    private ChildTagAppendListener childTagAppendListener;

    private ChildTagRemoveListener childTagRemoveListener;

    private AttributeAddListener attributeAddListener;

    private AttributeRemoveListener attributeRemoveListener;

    private InnerHtmlAddListener innerHtmlAddListener;

    /**
     * key : "S" + dataWffId and value : abstractHtml tag
     */
    private final Map<String, AbstractHtml> tagByWffId = new ConcurrentHashMap<>();

    private AttributeValueChangeListener valueChangeListener;

    private InsertBeforeListener insertBeforeListener;

    private WffBMDataDeleteListener wffBMDataDeleteListener;

    private WffBMDataUpdateListener wffBMDataUpdateListener;

    private PushQueue pushQueue;

    /**
     * no need to make it volatile
     */
    private final AtomicInteger dataWffId = new AtomicInteger(-1);

    private final AtomicBoolean dataWffIdSecondCycle = new AtomicBoolean(false);

    private final AbstractHtml rootTag;

    private final AtomicReference<Object> sharedData = new AtomicReference<>();

    /**
     * Java 8's {@code StampedLock} is not suitable here because it seems to be
     * thread independent. Eg:-
     *
     * <pre>
     * ReadWriteLock lock = new StampedLock().asReadWriteLock();
     * lock.writeLock().lock()&#59;
     * System.out.println("after writeLock")&#59;
     * lock.readLock().lock()&#59;
     * System.out.println("after readLock")&#59;
     * </pre>
     *
     * If the lock is a {@code StampedLock} then the code will block at
     * <code>lock.readLock().lock()&#59;</code> even in the same thread. But
     * {@code ReentrantReadWriteLock} will not block in the same thread, it will
     * print both system print.
     *
     * @since 3.0.1
     */
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(
            true);

    public AbstractHtml5SharedObject(final AbstractHtml rootTag) {
        this.rootTag = rootTag;
    }

    /**
     * @return unique data-wff-id attribute
     * @since 2.0.0
     * @author WFF
     */
    public DataWffId getNewDataWffId(final Object accessObject) {

        if (accessObject == null || !((SecurityClassConstants.ABSTRACT_HTML
                .equals(accessObject.getClass().getName()))
                || (SecurityClassConstants.BROWSER_PAGE
                        .equals(accessObject.getClass().getName())))) {
            throw new WffSecurityException(
                    "Not allowed to consume this method. This method is for internal use.");
        }

        // needs further improvement for atomic operation

        while (tagByWffId.size() < Integer.MAX_VALUE) {

            final int incrementedDataWffId = dataWffId.incrementAndGet();

            if (incrementedDataWffId < 0 || dataWffIdSecondCycle.get()) {

                dataWffIdSecondCycle.compareAndSet(false, true);

                int newDataWffId = incrementedDataWffId < 0
                        ? ((incrementedDataWffId - Integer.MAX_VALUE) - 1)
                        : incrementedDataWffId;

                String id = "S" + (newDataWffId);

                while (tagByWffId.containsKey(id)) {
                    newDataWffId++;
                    if (newDataWffId < 0) {
                        newDataWffId = (newDataWffId - Integer.MAX_VALUE) - 1;
                    }
                    id = "S" + newDataWffId;
                }

                if (dataWffId.compareAndSet(incrementedDataWffId,
                        newDataWffId)) {
                    return new DataWffId("S" + newDataWffId);
                }
                continue;
            }

            return new DataWffId("S" + incrementedDataWffId);
        }
        throw new DataWffIdOutOfRangeError(
                "BrowserPage object has reached an impossible worst case! No enough DataWffId available to assign to a new tag.");

    }

    /**
     * @param accessObject
     * @return the ReadWriteLock lock
     * @since 3.0.1
     */
    public ReentrantReadWriteLock getLock(final Object accessObject) {
        if (accessObject == null || !(SecurityClassConstants.ABSTRACT_HTML
                .equals(accessObject.getClass().getName())
                || SecurityClassConstants.ABSTRACT_ATTRIBUTE
                        .equals(accessObject.getClass().getName()))) {
            throw new WffSecurityException(
                    "Not allowed to consume this method. This method is for internal use.");
        }
        return lock;
    }

    public int getLastDataWffId(final Object accessObject) {

        if (accessObject == null || !((SecurityClassConstants.ABSTRACT_HTML
                .equals(accessObject.getClass().getName())))) {
            throw new WffSecurityException(
                    "Not allowed to consume this method. This method is for internal use.");
        }
        return dataWffId.get();
    }

    /**
     * @return the childModified true if any of the children has been modified
     * @since 1.0.0
     * @author WFF
     */
    public boolean isChildModified() {
        return childModified;
    }

    /**
     * set true if any of the children has been modified.<br>
     * NB:- it's for internal use
     *
     * @param childModified
     *            the childModified to set
     * @since 1.0.0
     * @author WFF
     */
    public void setChildModified(final boolean childModified) {
        this.childModified = childModified;
    }

    /**
     * gets the set containing the objects which are rebuilt after modified by
     * its {@code AbstractTagBase} method. NB:- only for internal use. currently
     * it's not used anywhere
     *
     * @return the rebuiltTags
     * @since 1.0.0
     * @author WFF
     * @deprecated only for internal use currently it's not used anywhere. Needs
     *             to remove this method later.
     */
    @Deprecated
    public Set<AbstractTagBase> getRebuiltTags(final Object accessObject) {

        // TODO remove this method later
        if (accessObject == null || !(SecurityClassConstants.ABSTRACT_HTML
                .equals(accessObject.getClass().getName()))) {
            throw new WffSecurityException(
                    "Not allowed to consume this method. This method is for internal use.");
        }

        if (rebuiltTags == null) {
            synchronized (this) {
                if (rebuiltTags == null) {
                    rebuiltTags = Collections.newSetFromMap(
                            new WeakHashMap<AbstractTagBase, Boolean>());
                }
            }
        }
        return rebuiltTags;
    }

    /**
     * NB:- This method is for only for internal use
     *
     * @return the childTagAppendListener
     */
    public ChildTagAppendListener getChildTagAppendListener(
            final Object accessObject) {
        if (accessObject == null || !(SecurityClassConstants.ABSTRACT_HTML
                .equals(accessObject.getClass().getName()))) {
            throw new WffSecurityException(
                    "Not allowed to consume this method. This method is for internal use.");
        }
        return childTagAppendListener;
    }

    /**
     * NB:- This method is for only for internal use
     *
     * @param childTagAppendListener
     *            the childTagAppendListener to set
     */
    public void setChildTagAppendListener(
            final ChildTagAppendListener childTagAppendListener,
            final Object accessObject) {
        if (accessObject == null || !(SecurityClassConstants.BROWSER_PAGE
                .equals(accessObject.getClass().getName()))) {
            throw new WffSecurityException(
                    "Not allowed to consume this method. This method is for internal use.");
        }
        this.childTagAppendListener = childTagAppendListener;
    }

    /**
     * NB:- This method is for only for internal use
     *
     * @return the childTagRemoveListener
     */
    public ChildTagRemoveListener getChildTagRemoveListener(
            final Object accessObject) {
        if (accessObject == null || !(SecurityClassConstants.ABSTRACT_HTML
                .equals(accessObject.getClass().getName()))) {
            throw new WffSecurityException(
                    "Not allowed to consume this method. This method is for internal use.");
        }
        return childTagRemoveListener;
    }

    /**
     * NB:- This method is for only for internal use
     *
     * @param childTagRemoveListener
     *            the childTagRemoveListener to set
     */
    public void setChildTagRemoveListener(
            final ChildTagRemoveListener childTagRemoveListener,
            final Object accessObject) {
        if (accessObject == null || !(SecurityClassConstants.BROWSER_PAGE
                .equals(accessObject.getClass().getName()))) {
            throw new WffSecurityException(
                    "Not allowed to consume this method. This method is for internal use.");
        }
        this.childTagRemoveListener = childTagRemoveListener;
    }

    /**
     * @return the attributeAddListener
     */
    public AttributeAddListener getAttributeAddListener(
            final Object accessObject) {
        if (accessObject == null || !(SecurityClassConstants.ABSTRACT_HTML
                .equals(accessObject.getClass().getName()))) {
            throw new WffSecurityException(
                    "Not allowed to consume this method. This method is for internal use.");
        }
        return attributeAddListener;
    }

    /**
     * @param attributeAddListener
     *            the attributeAddListener to set
     */
    public void setAttributeAddListener(
            final AttributeAddListener attributeAddListener,
            final Object accessObject) {
        if (accessObject == null || !(SecurityClassConstants.BROWSER_PAGE
                .equals(accessObject.getClass().getName()))) {
            throw new WffSecurityException(
                    "Not allowed to consume this method. This method is for internal use.");
        }
        this.attributeAddListener = attributeAddListener;
    }

    /**
     * @return the attributeRemoveListener
     */
    public AttributeRemoveListener getAttributeRemoveListener(
            final Object accessObject) {
        if (accessObject == null || !(SecurityClassConstants.ABSTRACT_HTML
                .equals(accessObject.getClass().getName()))) {
            throw new WffSecurityException(
                    "Not allowed to consume this method. This method is for internal use.");
        }
        return attributeRemoveListener;
    }

    /**
     * @param attributeRemoveListener
     *            the attributeRemoveListener to set
     */
    public void setAttributeRemoveListener(
            final AttributeRemoveListener attributeRemoveListener,
            final Object accessObject) {
        if (accessObject == null || !(SecurityClassConstants.BROWSER_PAGE
                .equals(accessObject.getClass().getName()))) {
            throw new WffSecurityException(
                    "Not allowed to consume this method. This method is for internal use.");
        }
        this.attributeRemoveListener = attributeRemoveListener;
    }

    /**
     * @return the innerHtmlAddListener
     */
    public InnerHtmlAddListener getInnerHtmlAddListener(
            final Object accessObject) {
        if (accessObject == null || !(SecurityClassConstants.ABSTRACT_HTML
                .equals(accessObject.getClass().getName()))) {
            throw new WffSecurityException(
                    "Not allowed to consume this method. This method is for internal use.");
        }
        return innerHtmlAddListener;
    }

    /**
     * @param innerHtmlAddListener
     *            the innerHtmlAddListener to set
     */
    public void setInnerHtmlAddListener(
            final InnerHtmlAddListener innerHtmlAddListener,
            final Object accessObject) {

        if (accessObject == null || !(SecurityClassConstants.BROWSER_PAGE
                .equals(accessObject.getClass().getName()))) {
            throw new WffSecurityException(
                    "Not allowed to consume this method. This method is for internal use.");
        }
        this.innerHtmlAddListener = innerHtmlAddListener;
    }

    /**
     * @param accessObject
     * @return the map containing wffid and tag
     * @since 2.0.0
     * @author WFF
     */
    public Map<String, AbstractHtml> getTagByWffId(final Object accessObject) {
        if (accessObject == null || !((SecurityClassConstants.ABSTRACT_HTML
                .equals(accessObject.getClass().getName()))
                || (SecurityClassConstants.BROWSER_PAGE
                        .equals(accessObject.getClass().getName())))) {
            throw new WffSecurityException(
                    "Not allowed to consume this method. This method is for internal use.");
        }
        return tagByWffId;
    }

    /**
     * @param accessObject
     * @since 2.0.0
     * @author WFF
     * @return the map containing wffid and tag
     */
    public Map<String, AbstractHtml> initTagByWffId(final Object accessObject) {
        if (accessObject == null || !(SecurityClassConstants.BROWSER_PAGE
                .equals(accessObject.getClass().getName()))) {
            throw new WffSecurityException(
                    "Not allowed to consume this method. This method is for internal use.");
        }
        return tagByWffId;
    }

    /**
     * NB:- This listener is used for internal purpose and should not be
     * consumed. Instead, use addValueChangeListener and getValueChangeListeners
     * methods.
     *
     * @param accessObject
     *            access object of this method
     * @return the valueChangeListener
     */
    public AttributeValueChangeListener getValueChangeListener(
            final Object accessObject) {
        if (accessObject == null || !(SecurityClassConstants.ABSTRACT_ATTRIBUTE
                .equals(accessObject.getClass().getName()))) {
            throw new WffSecurityException(
                    "Not allowed to consume this method. Instead, use addValueChangeListener and getValueChangeListeners methods.");
        }
        return valueChangeListener;
    }

    /**
     * NB:- This listener is used for internal purpose and should not be
     * consumed. Instead, use addValueChangeListener and getValueChangeListeners
     * methods.
     *
     * @param valueChangeListener
     *            the valueChangeListener to set
     * @param accessObject
     *            access object of this method
     */
    public void setValueChangeListener(
            final AttributeValueChangeListener valueChangeListener,
            final Object accessObject) {

        if (accessObject == null || !(SecurityClassConstants.BROWSER_PAGE
                .equals(accessObject.getClass().getName()))) {
            throw new WffSecurityException(
                    "Not allowed to consume this method. Instead, use addValueChangeListener and getValueChangeListeners methods.");
        }
        this.valueChangeListener = valueChangeListener;
    }

    /**
     * NB:- This method is for only for internal use
     *
     * @return the insertBeforeListener
     *
     * @since 2.1.1
     */
    public InsertBeforeListener getInsertBeforeListener(
            final Object accessObject) {

        if (accessObject == null || !(SecurityClassConstants.ABSTRACT_HTML
                .equals(accessObject.getClass().getName()))) {
            throw new WffSecurityException(
                    "Not allowed to consume this method. This method is for internal use.");
        }

        return insertBeforeListener;
    }

    /**
     * NB:- This method is for only for internal use
     *
     * @param insertBeforeListener
     *            the insertBeforeListener to set
     * @since 2.1.1
     */
    public void setInsertBeforeListener(
            final InsertBeforeListener insertBeforeListener,
            final Object accessObject) {

        if (accessObject == null || !(SecurityClassConstants.BROWSER_PAGE
                .equals(accessObject.getClass().getName()))) {
            throw new WffSecurityException(
                    "Not allowed to consume this method. This method is for internal use.");
        }

        this.insertBeforeListener = insertBeforeListener;
    }

    /**
     * NB:- This method is for only for internal use
     *
     * @param wffBMDataDeleteListener
     *            the wffDataDeleteListener to set
     * @since 2.1.8
     */
    public void setWffBMDataDeleteListener(
            final WffBMDataDeleteListener wffBMDataDeleteListener,
            final Object accessObject) {
        if (accessObject == null || !(SecurityClassConstants.BROWSER_PAGE
                .equals(accessObject.getClass().getName()))) {
            throw new WffSecurityException(
                    "Not allowed to consume this method. This method is for internal use.");
        }
        this.wffBMDataDeleteListener = wffBMDataDeleteListener;
    }

    /**
     * NB:- This method is for only for internal use
     *
     * @param wffBMDataUpdateListener
     *            the wffDataUpdateListener to set
     * @since 2.1.8
     */
    public void setWffBMDataUpdateListener(
            final WffBMDataUpdateListener wffBMDataUpdateListener,
            final Object accessObject) {
        if (accessObject == null || !(SecurityClassConstants.BROWSER_PAGE
                .equals(accessObject.getClass().getName()))) {
            throw new WffSecurityException(
                    "Not allowed to consume this method. This method is for internal use.");
        }
        this.wffBMDataUpdateListener = wffBMDataUpdateListener;
    }

    /**
     * NB:- This method is for only for internal use
     *
     * @return the insertBeforeListener
     *
     * @since 2.1.8
     */
    public WffBMDataDeleteListener getWffBMDataDeleteListener(
            final Object accessObject) {

        if (accessObject == null || !(SecurityClassConstants.ABSTRACT_JS_OBJECT
                .equals(accessObject.getClass().getName()))) {
            throw new WffSecurityException(
                    "Not allowed to consume this method. This method is for internal use.");
        }

        return wffBMDataDeleteListener;
    }

    /**
     * NB:- This method is for only for internal use
     *
     * @return the insertBeforeListener
     *
     * @since 2.1.8
     */
    public WffBMDataUpdateListener getWffBMDataUpdateListener(
            final Object accessObject) {

        if (accessObject == null || !(SecurityClassConstants.ABSTRACT_JS_OBJECT
                .equals(accessObject.getClass().getName()))) {
            throw new WffSecurityException(
                    "Not allowed to consume this method. This method is for internal use.");
        }

        return wffBMDataUpdateListener;
    }

    /**
     * @return the rootTag set through the constructor.
     * @since 2.1.11
     * @author WFF
     */
    public AbstractHtml getRootTag() {
        return rootTag;
    }

    /**
     * Gets the object which is accessible in all of this tag hierarchy.
     *
     * @return the sharedData object set by setSharedData method. This object is
     *         same across all of this tag hierarchy.
     * @since 2.1.11
     * @author WFF
     */
    public Object getSharedData() {
        return sharedData.get();
    }

    /**
     * Sets the object which will be accessible by getSharedData method in all
     * of this tag hierarchy. {@code setData} sets an object for the specific
     * tag but {@code setSharedData} sets an object for all of the tag
     * hierarchy.
     *
     * @param sharedData
     *            the object to access through all of this tag hierarchy.
     * @since 2.1.11
     * @author WFF
     */
    public void setSharedData(final Object sharedData) {
        this.sharedData.set(sharedData);
    }

    /**
     * @param sharedData
     * @return true if set or false if it already had a value
     * @since 3.0.1
     */
    public boolean setSharedDataIfNull(final Object sharedData) {
        return this.sharedData.compareAndSet(null, sharedData);
    }

    /**
     * NB:- This method is for only for internal use
     *
     * @param pushQueue
     *            the BrowserPage push queue to set
     */
    public void setPushQueue(final PushQueue pushQueue,
            final Object accessObject) {
        if (accessObject == null || !(SecurityClassConstants.BROWSER_PAGE
                .equals(accessObject.getClass().getName()))) {
            throw new WffSecurityException(
                    "Not allowed to consume this method. This method is for internal use.");
        }
        this.pushQueue = pushQueue;
    }

    /**
     * NB:- This method is for only for internal use
     *
     * @return the the push queue of BrowserPage
     */
    public PushQueue getPushQueue(final Object accessObject) {
        if (accessObject == null || !(SecurityClassConstants.ABSTRACT_HTML
                .equals(accessObject.getClass().getName())
                || SecurityClassConstants.ABSTRACT_ATTRIBUTE
                        .equals(accessObject.getClass().getName())
                || SecurityClassConstants.ABSTRACT_JS_OBJECT
                        .equals(accessObject.getClass().getName()))) {
            throw new WffSecurityException(
                    "Not allowed to consume this method. This method is for internal use.");
        }
        return pushQueue;
    }

}
