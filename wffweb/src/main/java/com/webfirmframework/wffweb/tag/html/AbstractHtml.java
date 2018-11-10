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
package com.webfirmframework.wffweb.tag.html;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.stream.Stream;

import com.webfirmframework.wffweb.InvalidTagException;
import com.webfirmframework.wffweb.MethodNotImplementedException;
import com.webfirmframework.wffweb.NoParentException;
import com.webfirmframework.wffweb.WffRuntimeException;
import com.webfirmframework.wffweb.WffSecurityException;
import com.webfirmframework.wffweb.clone.CloneUtil;
import com.webfirmframework.wffweb.security.object.SecurityClassConstants;
import com.webfirmframework.wffweb.streamer.WffBinaryMessageOutputStreamer;
import com.webfirmframework.wffweb.tag.core.AbstractJsObject;
import com.webfirmframework.wffweb.tag.html.attribute.core.AbstractAttribute;
import com.webfirmframework.wffweb.tag.html.attribute.core.AttributeUtil;
import com.webfirmframework.wffweb.tag.html.attributewff.CustomAttribute;
import com.webfirmframework.wffweb.tag.html.core.TagRegistry;
import com.webfirmframework.wffweb.tag.html.html5.attribute.global.DataWffId;
import com.webfirmframework.wffweb.tag.html.listener.AttributeAddListener;
import com.webfirmframework.wffweb.tag.html.listener.AttributeRemoveListener;
import com.webfirmframework.wffweb.tag.html.listener.ChildTagAppendListener;
import com.webfirmframework.wffweb.tag.html.listener.ChildTagAppendListener.ChildMovedEvent;
import com.webfirmframework.wffweb.tag.html.listener.ChildTagRemoveListener;
import com.webfirmframework.wffweb.tag.html.listener.InnerHtmlAddListener;
import com.webfirmframework.wffweb.tag.html.listener.InsertBeforeListener;
import com.webfirmframework.wffweb.tag.html.listener.PushQueue;
import com.webfirmframework.wffweb.tag.html.model.AbstractHtml5SharedObject;
import com.webfirmframework.wffweb.tag.htmlwff.CustomTag;
import com.webfirmframework.wffweb.tag.htmlwff.NoTag;
import com.webfirmframework.wffweb.tag.repository.TagRepository;
import com.webfirmframework.wffweb.util.WffBinaryMessageUtil;
import com.webfirmframework.wffweb.util.data.NameValue;
import com.webfirmframework.wffweb.wffbm.data.WffBMArray;
import com.webfirmframework.wffweb.wffbm.data.WffBMData;
import com.webfirmframework.wffweb.wffbm.data.WffBMObject;

/**
 * @author WFF
 * @since 1.0.0
 * @version 3.0.1
 *
 */
public abstract class AbstractHtml extends AbstractJsObject {

    // if this class' is refactored then SecurityClassConstants should be
    // updated.

    private static final long serialVersionUID = 3_0_1L;

    private static final Security ACCESS_OBJECT;

    protected static int tagNameIndex;

    private volatile AbstractHtml parent;

    /**
     * NB: iterator in this children is not synchronized so for-each loop may
     * make ConcurrentModificationException if the children object is not used
     * in synchronized block eg: synchronized (children) {} if possible replace
     * this with a new implementation of a concurrent linked hashset.
     * Unfortunately, jdk doesn't contain such class upto java 11
     */
    private final Set<AbstractHtml> children;

    private String openingTag;

    // should be initialized with empty string
    private String closingTag = "";

    private StringBuilder htmlStartSB;

    private volatile StringBuilder htmlMiddleSB;

    private StringBuilder htmlEndSB;

    private String tagName;

    private StringBuilder tagBuilder;

    private AbstractAttribute[] attributes;

    private volatile Map<String, AbstractAttribute> attributesMap;

    private AbstractHtml5SharedObject sharedObject;

    private boolean htmlStartSBAsFirst;

    // for future development
    private WffBinaryMessageOutputStreamer wffBinaryMessageOutputStreamer;

    // only for toWffBMBytes method
    private int wffSlotIndex = -1;

    private volatile DataWffId dataWffId;

    private transient Charset charset = Charset.defaultCharset();

    private TagType tagType = TagType.OPENING_CLOSING;

    public static enum TagType {
        OPENING_CLOSING, SELF_CLOSING, NON_CLOSING;

        private TagType() {
        }
    }

    // for security purpose, the class name should not be modified
    private static final class Security implements Serializable {

        private static final long serialVersionUID = 1L;

        private Security() {
        }
    }

    static {
        ACCESS_OBJECT = new Security();
    }

    {
        // NB: iterator in this children is not synchronized
        // so for-each loop may make ConcurrentModificationException
        // if the children object is not used in synchronized block
        // eg: synchronized (children) {}
        // if possible replace this with a new implementation of
        // a concurrent linked hashset. Unfortunately, jdk doesn't contain
        // such class upto java 10
        //
        children = new LinkedHashSet<AbstractHtml>() {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean remove(final Object child) {

                final boolean removed = super.remove(child);
                // this method is getting called when removeAll method
                // is called.
                //

                if (removed) {
                    sharedObject.setChildModified(removed);
                }

                return removed;
            }

            @Override
            public boolean add(final AbstractHtml e) {
                final boolean added = super.add(e);
                if (added) {
                    sharedObject.setChildModified(added);
                }
                return added;
            }

            @Override
            public boolean removeAll(final Collection<?> children) {

                final AbstractHtml[] removedAbstractHtmls = children
                        .toArray(new AbstractHtml[children.size()]);

                final boolean removedAll = super.removeAll(children);
                if (removedAll) {

                    initNewSharedObjectInAllNestedTagsAndSetSuperParentNull(
                            removedAbstractHtmls);

                    final ChildTagRemoveListener listener = sharedObject
                            .getChildTagRemoveListener(ACCESS_OBJECT);

                    if (listener != null) {
                        listener.childrenRemoved(
                                new ChildTagRemoveListener.Event(
                                        AbstractHtml.this,
                                        removedAbstractHtmls));
                    }

                }
                if (removedAll) {
                    sharedObject.setChildModified(removedAll);
                }
                return removedAll;
            }

            @Override
            public boolean retainAll(final Collection<?> c) {
                throw new MethodNotImplementedException(
                        "This method is not implemented yet, may be implemented in future");
            }

            @Override
            public void clear() {
                if (super.size() > 0) {
                    sharedObject.setChildModified(true);
                }
                super.clear();
            }

            // @Override
            // public boolean add(AbstractHtml child) {
            // boolean added = super.add(child);
            // if (added) {
            // if (child.parent != null) {
            //
            // final Stack<Set<AbstractHtml>> childrenStack = new
            // Stack<Set<AbstractHtml>>();
            // childrenStack.push(new HashSet<AbstractHtml>(
            // Arrays.asList(child)));
            //
            // while (childrenStack.size() > 0) {
            // use poll instead of pop, pop will throw exp
            // final Set<AbstractHtml> children = childrenStack
            // .pop();
            //
            // for (final AbstractHtml eachChild : children) {
            //
            // eachChild.sharedObject = AbstractHtml.this.sharedObject;
            //
            // final Set<AbstractHtml> subChildren = eachChild
            // .getChildren();
            //
            // if (subChildren != null
            // && subChildren.size() > 0) {
            // childrenStack.push(subChildren);
            // }
            //
            // }
            // }
            //
            // } else {
            // child.sharedObject = AbstractHtml.this.sharedObject;
            // }
            //
            // child.parent = AbstractHtml.this;
            // final ChildTagAppendListener listener =
            // child.sharedObject
            // .getChildTagAppendListener(ACCESS_OBJECT);
            // if (listener != null) {
            // final ChildTagAppendListener.Event event = new
            // ChildTagAppendListener.Event(
            // AbstractHtml.this, child);
            // listener.childAppended(event);
            // }
            //
            // }
            // return added;
            // }

            @Override
            public boolean addAll(
                    final Collection<? extends AbstractHtml> children) {
                throw new MethodNotImplementedException(
                        "This method is not implemented");
                // No need to implement as it will call add method
                // boolean addedAll = super.addAll(children);
                // if (addedAll) {
                //
                // for (AbstractHtml child : children) {
                // child.parent = AbstractHtml.this;
                // child.sharedObject = AbstractHtml.this.sharedObject;
                // final ChildTagAppendListener listener =
                // child.sharedObject
                // .getChildTagAppendListener(ACCESS_OBJECT);
                // if (listener != null) {
                // final ChildTagAppendListener.Event event = new
                // ChildTagAppendListener.Event(
                // AbstractHtml.this, children);
                // listener.childAppended(event);
                // }
                // }
                //
                //
                // }
                // return super.addAll(children);
            }

        };
        init();
    }

    @SuppressWarnings("unused")
    private AbstractHtml() {
        throw new AssertionError();
    }

    /**
     * @param base
     *            the parent tag of this object
     * @param children
     *            the tags which will be added as a children tag of this object.
     * @author WFF
     */
    public AbstractHtml(final AbstractHtml base,
            final Collection<? extends AbstractHtml> children) {
        this(base, children.toArray(new AbstractHtml[children.size()]));
    }

    /**
     * @param base
     *            the parent tag of this object
     * @param children
     *            the tags which will be added as a children tag of this object.
     * @author WFF
     * @since 3.0.1
     */
    public AbstractHtml(final AbstractHtml base,
            final AbstractHtml... children) {

        if (base == null) {
            sharedObject = new AbstractHtml5SharedObject(this);
        }

        initInConstructor();

        buildOpeningTag(false);
        buildClosingTag();
        if (base != null) {
            base.addChild(this);
            // base.children.add(this);
            // should not uncomment the below codes as it is handled in the
            // above add method
            // parent = base;
            // sharedObject = base.sharedObject;
        }
        // sharedObject initialization must come first
        // else {
        // sharedObject = new AbstractHtml5SharedObject(this);
        // }
        // this.children.addAll(children);
        this.appendChildren(children);
        // childAppended(parent, this);
    }

    /**
     * @param base
     * @param childContent
     *            any text, it can also be html text.
     */
    public AbstractHtml(final AbstractHtml base, final String childContent) {

        if (base == null) {
            sharedObject = new AbstractHtml5SharedObject(this);
        }

        initInConstructor();

        htmlStartSBAsFirst = true;
        getHtmlMiddleSB().append(childContent);
        buildOpeningTag(false);
        buildClosingTag();
        if (base != null) {
            base.addChild(this);
            // base.children.add(this);
            // should not uncomment the below codes as it is handled in the
            // above add method
            // parent = base;
            // sharedObject = base.sharedObject;
        }
        // sharedObject initialization must come first
        // else {
        // sharedObject = new AbstractHtml5SharedObject(this);
        // }
        setRebuild(true);

        // childAppended(parent, this);
    }

    /**
     * should be invoked to generate opening and closing tag base class
     * containing the functionalities to generate html string.
     *
     * @param tagName
     *            TODO
     * @param base
     *            TODO
     * @author WFF
     */
    public AbstractHtml(final String tagName, final AbstractHtml base,
            final AbstractAttribute[] attributes) {
        this.tagName = tagName;
        if (base == null) {
            sharedObject = new AbstractHtml5SharedObject(this);
        }

        initAttributes(attributes);

        initInConstructor();

        markOwnerTag(attributes);
        buildOpeningTag(false);
        buildClosingTag();
        if (base != null) {
            base.addChild(this);
            // base.children.add(this);
            // should not uncomment the below codes as it is handled in the
            // above add method
            // parent = base;
            // sharedObject = base.sharedObject;
        }

        // else {
        // sharedObject = new AbstractHtml5SharedObject(this);
        // }

        // childAppended(parent, this);
    }

    private void init() {
        tagBuilder = new StringBuilder();
        setRebuild(true);
    }

    /**
     * Appends the given child tag to its children.
     *
     * @param child
     *            the tag to append to its children.
     * @return true if the given child tag is appended as child of this tag.
     * @author WFF
     */
    public boolean appendChild(final AbstractHtml child) {
        // TODO fix bug in addChild(child);
        // directly calling addChild(child); here will not work
        // it will block thread in a large no of threads

        // this method works fine
        // lock is called inside appendChildren
        appendChildren(child);
        return true;
    }

    /**
     * Removes all children from this tag.
     *
     * @author WFF
     */
    public void removeAllChildren() {

        boolean listenerInvoked = false;
        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).writeLock();

        try {

            lock.lock();

            final AbstractHtml[] removedAbstractHtmls = children
                    .toArray(new AbstractHtml[children.size()]);
            children.clear();

            initNewSharedObjectInAllNestedTagsAndSetSuperParentNull(
                    removedAbstractHtmls);
            final ChildTagRemoveListener listener = sharedObject
                    .getChildTagRemoveListener(ACCESS_OBJECT);
            if (listener != null) {
                listener.allChildrenRemoved(new ChildTagRemoveListener.Event(
                        this, removedAbstractHtmls));
                listenerInvoked = true;
            }

        } finally {
            lock.unlock();
        }
        if (listenerInvoked) {
            final PushQueue pushQueue = sharedObject
                    .getPushQueue(ACCESS_OBJECT);
            if (pushQueue != null) {
                pushQueue.push();
            }
        }
    }

    /**
     * removes all children and adds the given tag
     *
     * @param innerHtml
     *            the inner html tag to add
     * @author WFF
     */
    public void addInnerHtml(final AbstractHtml innerHtml) {
        addInnerHtmls(innerHtml);
    }

    /**
     * Removes all children and adds the given tags as children.
     *
     * @param innerHtmls
     *            the inner html tags to add
     * @since 2.1.3
     * @author WFF
     */
    public void addInnerHtmls(final AbstractHtml... innerHtmls) {
        addInnerHtmls(true, innerHtmls);
    }

    /**
     * Removes all children and adds the given tags as children.
     *
     * @param updateClient
     *            true to update client browser page if it is available. The
     *            default value is true but it will be ignored if there is no
     *            client browser page.
     * @param innerHtmls
     *            the inner html tags to add
     *
     * @since 2.1.15
     * @author WFF
     */
    protected void addInnerHtmls(final boolean updateClient,
            final AbstractHtml... innerHtmls) {

        boolean listenerInvoked = false;
        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).writeLock();

        try {
            lock.lock();

            final AbstractHtml[] removedAbstractHtmls = children
                    .toArray(new AbstractHtml[children.size()]);
            children.clear();

            initNewSharedObjectInAllNestedTagsAndSetSuperParentNull(
                    removedAbstractHtmls);

            final InnerHtmlAddListener listener = sharedObject
                    .getInnerHtmlAddListener(ACCESS_OBJECT);

            if (listener != null && updateClient) {

                final InnerHtmlAddListener.Event[] events = new InnerHtmlAddListener.Event[innerHtmls.length];

                int index = 0;

                for (final AbstractHtml innerHtml : innerHtmls) {

                    AbstractHtml previousParentTag = null;

                    if (innerHtml.parent != null
                            && innerHtml.parent.sharedObject == sharedObject) {
                        previousParentTag = innerHtml.parent;
                    }

                    addChild(innerHtml, false);

                    events[index] = new InnerHtmlAddListener.Event(this,
                            innerHtml, previousParentTag);
                    index++;

                }

                listener.innerHtmlsAdded(this, events);
                listenerInvoked = true;
            } else {
                for (final AbstractHtml innerHtml : innerHtmls) {
                    addChild(innerHtml, false);
                }
            }

        } finally {
            lock.unlock();
        }

        if (listenerInvoked) {
            final PushQueue pushQueue = sharedObject
                    .getPushQueue(ACCESS_OBJECT);
            if (pushQueue != null) {
                pushQueue.push();
            }
        }

    }

    /**
     * Removes the given tags from its children tags.
     *
     * @param children
     *            the tags to remove from its children.
     * @return true given given children tags have been removed.
     * @author WFF
     */
    public boolean removeChildren(final Collection<AbstractHtml> children) {
        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).writeLock();
        boolean result = false;
        try {
            lock.lock();
            result = this.children.removeAll(children);
        } finally {
            lock.unlock();
        }
        final PushQueue pushQueue = sharedObject.getPushQueue(ACCESS_OBJECT);
        if (pushQueue != null) {
            pushQueue.push();
        }
        return result;
    }

    /**
     * Removes the given tags from its children tags.
     *
     * @param children
     *            the tags to remove from its children.
     * @return true given given children tags have been removed.
     * @author WFF
     * @since 3.0.1
     */
    public boolean removeChildren(final AbstractHtml... children) {
        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).writeLock();
        boolean result = false;
        try {
            lock.lock();

            // must be list as the order of children is important
            // java 8 stream is very slower, check CodePerformanceTest
            result = this.children.removeAll(Arrays.asList(children));
        } finally {
            lock.unlock();
        }
        final PushQueue pushQueue = sharedObject.getPushQueue(ACCESS_OBJECT);
        if (pushQueue != null) {
            pushQueue.push();
        }
        return result;
    }

    /**
     * Removes the given tag from its children only if the given tag is a child
     * of this tag.
     *
     * @param child
     *            the tag to remove from its children
     * @return true if removed
     * @author WFF
     */
    public boolean removeChild(final AbstractHtml child) {

        boolean listenerInvoked = false;
        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).writeLock();
        boolean removed = false;
        try {
            lock.lock();

            removed = children.remove(child);

            if (removed) {

                // making child.parent = null inside the below method.
                initNewSharedObjectInAllNestedTagsAndSetSuperParentNull(child);

                final ChildTagRemoveListener listener = sharedObject
                        .getChildTagRemoveListener(ACCESS_OBJECT);

                if (listener != null) {
                    listener.childRemoved(
                            new ChildTagRemoveListener.Event(this, child));
                    listenerInvoked = true;
                }

            }

        } finally {
            lock.unlock();
        }
        if (listenerInvoked) {
            final PushQueue pushQueue = sharedObject
                    .getPushQueue(ACCESS_OBJECT);
            if (pushQueue != null) {
                pushQueue.push();
            }
        }
        return removed;
    }

    private boolean addChild(final AbstractHtml child) {
        // this method should contain lock even if it is a private method
        // because this method is called in constructors.
        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).writeLock();
        boolean result = false;
        try {
            lock.lock();
            result = addChild(child, true);
        } finally {
            lock.unlock();
        }
        final PushQueue pushQueue = sharedObject.getPushQueue(ACCESS_OBJECT);
        if (pushQueue != null) {
            pushQueue.push();
        }
        return result;
    }

    /**
     * NB: This method is for internal use
     *
     * @param accessObject
     * @param child
     * @param invokeListener
     * @return
     * @since 2.0.0
     * @author WFF
     */
    public boolean addChild(final Object accessObject, final AbstractHtml child,
            final boolean invokeListener) {
        if (accessObject == null || !(SecurityClassConstants.BROWSER_PAGE
                .equals(accessObject.getClass().getName()))) {
            throw new WffSecurityException(
                    "Not allowed to consume this method. This method is for internal use.");
        }

        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).writeLock();
        boolean result = false;
        try {

            lock.lock();

            result = addChild(child, invokeListener);
        } finally {
            lock.unlock();
        }
        if (invokeListener) {
            final PushQueue pushQueue = sharedObject
                    .getPushQueue(ACCESS_OBJECT);
            if (pushQueue != null) {
                pushQueue.push();
            }
        }
        return result;
    }

    private boolean addChild(final AbstractHtml child,
            final boolean invokeListener) {

        final boolean added = children.add(child);
        if (added) {

            // if alreadyHasParent = true then it means the child is moving
            // from
            // one tag to another.
            final boolean alreadyHasParent = child.parent != null;
            final AbstractHtml previousParent = child.parent;

            if (alreadyHasParent) {
                child.parent.children.remove(child);
            }

            initParentAndSharedObject(child);

            if (invokeListener) {

                if (alreadyHasParent) {
                    final ChildTagAppendListener listener = sharedObject
                            .getChildTagAppendListener(ACCESS_OBJECT);

                    if (listener != null) {
                        listener.childMoved(
                                new ChildTagAppendListener.ChildMovedEvent(
                                        previousParent, this, child));
                    }

                } else {

                    final ChildTagAppendListener listener = sharedObject
                            .getChildTagAppendListener(ACCESS_OBJECT);
                    if (listener != null) {
                        final ChildTagAppendListener.Event event = new ChildTagAppendListener.Event(
                                this, child);
                        listener.childAppended(event);
                    }
                }
            }

        }
        return added;

    }

    private void initParentAndSharedObject(final AbstractHtml child) {

        initSharedObject(child);

        child.parent = this;
    }

    private void initSharedObject(final AbstractHtml child) {

        final Deque<Set<AbstractHtml>> childrenStack = new ArrayDeque<>();
        final Set<AbstractHtml> initialSet = new HashSet<>(1);
        initialSet.add(child);
        childrenStack.push(initialSet);

        Set<AbstractHtml> children;
        while ((children = childrenStack.poll()) != null) {

            for (final AbstractHtml eachChild : children) {

                eachChild.sharedObject = sharedObject;

                // no need to add data-wff-id if the tag is not rendered by
                // BrowserPage (if it is rended by BrowserPage then
                // getLastDataWffId will not be -1)
                if (sharedObject.getLastDataWffId(ACCESS_OBJECT) != -1
                        && eachChild.getDataWffId() == null
                        && eachChild.getTagName() != null
                        && !eachChild.getTagName().isEmpty()) {

                    eachChild.initDataWffId(sharedObject);

                }

                final Set<AbstractHtml> subChildren = eachChild.children;

                if (subChildren != null && subChildren.size() > 0) {
                    childrenStack.push(subChildren);
                }

            }
        }
    }

    /**
     * adds the given children to the last position of the current children of
     * this object.
     *
     * @param children
     *            children to append in this object's existing children.
     * @author WFF
     */
    public void appendChildren(final Collection<AbstractHtml> children) {

        boolean listenerInvoked = false;
        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).writeLock();
        try {
            lock.lock();

            final Collection<ChildMovedEvent> movedOrAppended = new ArrayDeque<>(
                    children.size());

            for (final AbstractHtml child : children) {
                final AbstractHtml previousParent = child.parent;

                addChild(child, false);

                final ChildMovedEvent event = new ChildMovedEvent(
                        previousParent, this, child);
                movedOrAppended.add(event);

            }

            final ChildTagAppendListener listener = sharedObject
                    .getChildTagAppendListener(ACCESS_OBJECT);
            if (listener != null) {
                listener.childrendAppendedOrMoved(movedOrAppended);
                listenerInvoked = true;
            }
        } finally {
            lock.unlock();
        }
        if (listenerInvoked) {
            final PushQueue pushQueue = sharedObject
                    .getPushQueue(ACCESS_OBJECT);
            if (pushQueue != null) {
                pushQueue.push();
            }
        }
    }

    /**
     * adds the given children to the last position of the current children of
     * this object.
     *
     * @param children
     *            children to append in this object's existing children.
     * @author WFF
     * @since 2.1.6
     */
    public void appendChildren(final AbstractHtml... children) {

        // NB: any changes to this method should also be applied in
        // appendChildrenLockless(AbstractHtml... children)
        // this method in consumed in constructor

        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).writeLock();

        boolean listenerInvoked = false;

        try {
            lock.lock();

            final Collection<ChildMovedEvent> movedOrAppended = new ArrayDeque<>(
                    children.length);

            for (final AbstractHtml child : children) {
                final AbstractHtml previousParent = child.parent;

                addChild(child, false);

                final ChildMovedEvent event = new ChildMovedEvent(
                        previousParent, this, child);
                movedOrAppended.add(event);

            }

            final ChildTagAppendListener listener = sharedObject
                    .getChildTagAppendListener(ACCESS_OBJECT);
            if (listener != null) {
                listener.childrendAppendedOrMoved(movedOrAppended);
                listenerInvoked = true;
            }
        } finally {
            lock.unlock();
        }
        if (listenerInvoked) {
            final PushQueue pushQueue = sharedObject
                    .getPushQueue(ACCESS_OBJECT);
            if (pushQueue != null) {
                pushQueue.push();
            }
        }
    }

    /**
     * prepends the given children to the first position of the current children
     * of this object. <br>
     * Eg:-
     *
     * <pre>
     * Div div = new Div(null, new Id("one")) {
     *     {
     *         new Div(this, new Id("child1"));
     *     }
     * };
     *
     * Span span = new Span(null);
     *
     * P p = new P(null);
     *
     * Br br = new Br(null);
     *
     * div.prependChildren(span, p, br);
     *
     * System.out.println(div.toHtmlString());
     *
     * </pre>
     *
     * This prints
     *
     * <pre>
     * &lt;div id=&quot;one&quot;&gt;
     *     &lt;span&gt;&lt;/span&gt;
     *     &lt;p&gt;&lt;/p&gt;
     *     &lt;br/&gt;
     *     &lt;div id=&quot;child1&quot;&gt;&lt;/div&gt;
     * &lt;/div&gt;
     * </pre>
     *
     * @param children
     *            children to prepend in this object's existing children.
     * @author WFF
     * @since 3.0.1
     */
    public void prependChildren(final AbstractHtml... children) {

        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).writeLock();

        boolean listenerInvoked = false;

        try {
            lock.lock();

            final Iterator<AbstractHtml> iterator = this.children.iterator();
            if (iterator.hasNext()) {
                final AbstractHtml firstChild = iterator.next();

                final AbstractHtml[] removedParentChildren = this.children
                        .toArray(new AbstractHtml[this.children.size()]);

                listenerInvoked = firstChild.insertBefore(removedParentChildren,
                        children);

                if (listenerInvoked) {
                    final InsertBeforeListener insertBeforeListener = sharedObject
                            .getInsertBeforeListener(ACCESS_OBJECT);
                    if (insertBeforeListener == null) {
                        listenerInvoked = false;
                    }
                }

            } else {

                // NB: similar impl is done in appendChildren(AbstractHtml...
                // children) so any improvements here will be applicable in
                // there also
                final Collection<ChildMovedEvent> movedOrAppended = new ArrayDeque<>(
                        children.length);

                for (final AbstractHtml child : children) {
                    final AbstractHtml previousParent = child.parent;

                    addChild(child, false);

                    final ChildMovedEvent event = new ChildMovedEvent(
                            previousParent, this, child);
                    movedOrAppended.add(event);

                }

                final ChildTagAppendListener listener = sharedObject
                        .getChildTagAppendListener(ACCESS_OBJECT);
                if (listener != null) {
                    listener.childrendAppendedOrMoved(movedOrAppended);
                    listenerInvoked = true;
                }

            }
        } finally {
            lock.unlock();
        }

        if (listenerInvoked) {
            final PushQueue pushQueue = sharedObject
                    .getPushQueue(ACCESS_OBJECT);
            if (pushQueue != null) {
                pushQueue.push();
            }
        }

    }

    /**
     * NB: lockless implementation of
     * {@code appendChildren(AbstractHtml... children)} <br>
     * adds the given children to the last position of the current children of
     * this object.
     *
     * @param children
     *            children to append in this object's existing children.
     * @author WFF
     * @since 3.0.1
     */
    private void appendChildrenLockless(final AbstractHtml... children) {

        // any changes to this method should also be applied in
        // appendChildren(AbstractHtml... children)

        final Collection<ChildMovedEvent> movedOrAppended = new ArrayDeque<>(
                children.length);

        for (final AbstractHtml child : children) {
            final AbstractHtml previousParent = child.parent;

            addChild(child, false);

            final ChildMovedEvent event = new ChildMovedEvent(previousParent,
                    this, child);
            movedOrAppended.add(event);

        }

        final ChildTagAppendListener listener = sharedObject
                .getChildTagAppendListener(ACCESS_OBJECT);
        if (listener != null) {
            listener.childrendAppendedOrMoved(movedOrAppended);
        }

    }

    /**
     * initializes attributes in this.attributes and also in attributesMap. this
     * should be called only once per object.
     *
     * @param attributes
     * @since 2.0.0
     * @author WFF
     */
    private void initAttributes(final AbstractAttribute... attributes) {

        if (attributes == null || attributes.length == 0) {
            return;
        }

        attributesMap = new ConcurrentHashMap<>();

        for (final AbstractAttribute attribute : attributes) {
            attributesMap.put(attribute.getAttributeName(), attribute);
        }

        this.attributes = new AbstractAttribute[attributesMap.size()];
        attributesMap.values().toArray(this.attributes);
    }

    /**
     * adds the given attributes to this tag.
     *
     * @param attributes
     *            attributes to add
     * @since 2.0.0
     * @author WFF
     */
    public void addAttributes(final AbstractAttribute... attributes) {
        addAttributes(true, attributes);
    }

    /**
     * adds the given attributes to this tag.
     *
     * @param updateClient
     *            true to update client browser page if it is available. The
     *            default value is true but it will be ignored if there is no
     *            client browser page.
     * @param attributes
     *            attributes to add
     * @since 2.0.0 initial implementation
     * @since 2.0.15 changed to public scope
     * @author WFF
     */
    public void addAttributes(final boolean updateClient,
            final AbstractAttribute... attributes) {

        boolean listenerInvoked = false;
        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).writeLock();
        try {
            lock.lock();
            listenerInvoked = addAttributesLockless(updateClient, attributes);
        } finally {
            lock.unlock();
        }

        if (listenerInvoked) {
            final PushQueue pushQueue = sharedObject
                    .getPushQueue(ACCESS_OBJECT);
            if (pushQueue != null) {
                pushQueue.push();
            }
        }

    }

    /**
     * adds the given attributes to this tag.
     *
     * @param updateClient
     *            true to update client browser page if it is available. The
     *            default value is true but it will be ignored if there is no
     *            client browser page.
     * @param attributes
     *            attributes to add
     * @since 3.0.1 initial implementation
     * @author WFF
     * @return true if the listener invoked else false. DO NOT confuse it with
     *         whether attributes are added.
     */
    private boolean addAttributesLockless(final boolean updateClient,
            final AbstractAttribute... attributes) {

        boolean listenerInvoked = false;

        if (attributesMap == null) {
            synchronized (this) {
                if (attributesMap == null) {
                    attributesMap = new ConcurrentHashMap<>(attributes.length);
                }
            }
        }

        if (this.attributes != null) {
            for (final AbstractAttribute attribute : this.attributes) {
                attributesMap.put(attribute.getAttributeName(), attribute);
            }
        }

        for (final AbstractAttribute attribute : attributes) {
            attribute.setOwnerTag(this);
            final AbstractAttribute previous = attributesMap
                    .put(attribute.getAttributeName(), attribute);
            if (previous != null && !attribute.equals(previous)) {
                previous.unsetOwnerTag(this);
            }
        }

        this.attributes = new AbstractAttribute[attributesMap.size()];
        attributesMap.values().toArray(this.attributes);
        setModified(true);
        sharedObject.setChildModified(true);

        // invokeListener
        if (updateClient) {
            final AttributeAddListener attributeAddListener = sharedObject
                    .getAttributeAddListener(ACCESS_OBJECT);
            if (attributeAddListener != null) {
                final AttributeAddListener.AddEvent event = new AttributeAddListener.AddEvent();
                event.setAddedToTag(this);
                event.setAddedAttributes(attributes);
                attributeAddListener.addedAttributes(event);
                listenerInvoked = true;
            }
        }

        return listenerInvoked;
    }

    /**
     * @return the collection of attributes
     * @since 2.0.0
     * @author WFF
     */
    public Collection<AbstractAttribute> getAttributes() {

        if (attributesMap == null) {
            return null;
        }

        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).readLock();
        try {
            lock.lock();
            final Collection<AbstractAttribute> result = Collections
                    .unmodifiableCollection(attributesMap.values());
            return result;
        } finally {
            lock.unlock();
        }
    }

    /**
     * gets the attribute by attribute name
     *
     * @return the attribute object for the given attribute name if exists
     *         otherwise returns null.
     * @since 2.0.0
     * @author WFF
     */
    public AbstractAttribute getAttributeByName(final String attributeName) {
        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).readLock();
        AbstractAttribute result = null;
        try {
            lock.lock();

            if (attributesMap != null) {
                result = attributesMap.get(attributeName);
            }
        } finally {
            lock.unlock();
        }
        return result;
    }

    /**
     * removes the given attributes from this tag.
     *
     * @param attributes
     *            attributes to remove
     * @return true if any of the attributes are removed.
     * @since 2.0.0
     * @author WFF
     */
    public boolean removeAttributes(final AbstractAttribute... attributes) {
        return removeAttributes(true, attributes);
    }

    /**
     * removes the given attributes from this tag.
     *
     * @param invokeListener
     *            true to invoke listener
     * @param attributes
     *            attributes to remove
     * @return true if any of the attributes are removed.
     * @since 2.0.0
     * @author WFF
     */
    public boolean removeAttributes(final Object accessObject,
            final boolean invokeListener,
            final AbstractAttribute... attributes) {

        if (accessObject == null || !(SecurityClassConstants.BROWSER_PAGE
                .equals(accessObject.getClass().getName()))) {
            throw new WffSecurityException(
                    "Not allowed to consume this method. This method is for internal use.");

        }
        // lock is not required here. it is used in
        // removeAttributes(invokeListener, attributes)
        return removeAttributes(invokeListener, attributes);
    }

    /**
     * removes the given attributes from this tag.
     *
     * @param updateClient
     *            true to update client browser page if it is available. The
     *            default value is true but it will be ignored if there is no
     *            client browser page.
     * @param attributes
     *            attributes to remove
     * @return true if any of the attributes are removed.
     * @since 2.0.0 initial implementation
     * @since 2.0.15 changed to public scope
     * @author WFF
     */
    public boolean removeAttributes(final boolean updateClient,
            final AbstractAttribute... attributes) {

        boolean listenerInvoked = false;
        boolean removed = false;
        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).writeLock();
        try {
            lock.lock();

            if (attributesMap == null) {
                return false;
            }

            final Deque<String> removedAttributeNames = new ArrayDeque<>(
                    attributes.length);

            for (final AbstractAttribute attribute : attributes) {

                if (attribute.unsetOwnerTag(this)) {
                    final String attributeName = attribute.getAttributeName();
                    attributesMap.remove(attributeName);
                    removed = true;
                    removedAttributeNames.add(attributeName);
                }

            }

            if (removed) {
                this.attributes = new AbstractAttribute[attributesMap.size()];
                attributesMap.values().toArray(this.attributes);
                setModified(true);
                sharedObject.setChildModified(true);

                // invokeListener
                if (updateClient) {
                    final AttributeRemoveListener listener = sharedObject
                            .getAttributeRemoveListener(ACCESS_OBJECT);
                    if (listener != null) {
                        final AttributeRemoveListener.RemovedEvent event = new AttributeRemoveListener.RemovedEvent(
                                this,
                                removedAttributeNames.toArray(
                                        new String[removedAttributeNames
                                                .size()]));

                        listener.removedAttributes(event);
                        listenerInvoked = true;

                    }
                }
            }
        } finally {
            lock.unlock();
        }

        if (listenerInvoked) {
            final PushQueue pushQueue = sharedObject
                    .getPushQueue(ACCESS_OBJECT);
            if (pushQueue != null) {
                pushQueue.push();
            }
        }

        return removed;
    }

    /**
     * removes the given attributes from this tag.
     *
     * @param attributeNames
     *            to remove the attributes having in the given names.
     * @return true if any of the attributes are removed.
     * @since 2.0.0
     * @author WFF
     */
    public boolean removeAttributes(final String... attributeNames) {
        return removeAttributes(true, attributeNames);
    }

    /**
     * removes the given attributes from this tag.
     *
     * @param invokeListener
     *            true to invoke listener
     * @param attributeNames
     *            to remove the attributes having in the given names.
     * @return true if any of the attributes are removed.
     * @since 2.0.0
     * @author WFF
     */
    public boolean removeAttributes(final Object accessObject,
            final boolean invokeListener, final String... attributeNames) {
        if (accessObject == null || !(SecurityClassConstants.BROWSER_PAGE
                .equals(accessObject.getClass().getName()))) {
            throw new WffSecurityException(
                    "Not allowed to consume this method. This method is for internal use.");

        }
        // lock is not required here. it is used in
        // removeAttributes(invokeListener, attributes)
        return removeAttributes(invokeListener, attributeNames);
    }

    /**
     * removes the given attributes from this tag.
     *
     * @param updateClient
     *            true to update client browser page if it is available. The
     *            default value is true but it will be ignored if there is no
     *            client browser page.
     * @param attributeNames
     *            to remove the attributes having in the given names.
     * @return true if any of the attributes are removed.
     * @since 2.0.0 initial implementation
     * @since 2.0.15 changed to public scope
     * @author WFF
     */
    public boolean removeAttributes(final boolean updateClient,
            final String... attributeNames) {

        boolean removed = false;
        boolean listenerInvoked = false;
        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).writeLock();
        try {
            lock.lock();

            if (attributesMap == null) {
                return false;
            }

            for (final String attributeName : attributeNames) {

                final AbstractAttribute attribute = attributesMap
                        .get(attributeName);

                if (attribute != null) {
                    attribute.unsetOwnerTag(this);
                    attributesMap.remove(attributeName);
                    removed = true;
                }

            }

            if (removed) {
                attributes = new AbstractAttribute[attributesMap.size()];
                attributesMap.values().toArray(attributes);
                setModified(true);
                sharedObject.setChildModified(true);

                // invokeListener
                if (updateClient) {
                    final AttributeRemoveListener listener = sharedObject
                            .getAttributeRemoveListener(ACCESS_OBJECT);
                    if (listener != null) {
                        final AttributeRemoveListener.RemovedEvent event = new AttributeRemoveListener.RemovedEvent(
                                this, attributeNames);

                        listener.removedAttributes(event);
                        listenerInvoked = true;
                    }
                }
            }
        } finally {
            lock.unlock();
        }

        if (listenerInvoked) {
            final PushQueue pushQueue = sharedObject
                    .getPushQueue(ACCESS_OBJECT);
            if (pushQueue != null) {
                pushQueue.push();
            }
        }

        return removed;
    }

    /**
     * should be invoked to generate opening and closing tag base class
     * containing the functionalities to generate html string.
     *
     * @param tagType
     *
     * @param tagName
     *            TODO
     * @param base
     *            TODO
     * @author WFF
     */
    protected AbstractHtml(final TagType tagType, final String tagName,
            final AbstractHtml base, final AbstractAttribute[] attributes) {
        this.tagType = tagType;
        this.tagName = tagName;

        if (base == null) {
            sharedObject = new AbstractHtml5SharedObject(this);
        }

        initAttributes(attributes);

        initInConstructor();

        markOwnerTag(attributes);

        buildOpeningTag(false);

        if (tagType == TagType.OPENING_CLOSING) {
            buildClosingTag();
        }

        if (base != null) {
            base.addChild(this);
            // base.children.add(this);
            // not required it is handled in the above add method
            // parent = base;
            // sharedObject = base.sharedObject;
        }
        //
        // else {
        // sharedObject = new AbstractHtml5SharedObject(this);
        // }
    }

    /**
     * marks the owner tag in the attributes
     *
     * @param attributes
     * @since 1.0.0
     * @author WFF
     */
    private void markOwnerTag(final AbstractAttribute[] attributes) {
        if (attributes == null) {
            return;
        }
        for (final AbstractAttribute abstractAttribute : attributes) {
            abstractAttribute.setOwnerTag(this);
        }
    }

    /**
     * to initialize objects in the constructor
     *
     * @since 1.0.0
     * @author WFF
     */
    private void initInConstructor() {
        htmlStartSB = new StringBuilder(tagName == null ? 0
                : tagName.length() + 2
                        + ((attributes == null ? 0 : attributes.length) * 16));

        htmlEndSB = new StringBuilder(
                tagName == null ? 16 : tagName.length() + 3);
    }

    public AbstractHtml getParent() {
        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).readLock();
        try {
            lock.lock();

            return parent;
        } finally {
            lock.unlock();
        }
    }

    /**
     * @param parent
     * @since 2.0.0
     * @author WFF
     * @deprecated This method is not allowed to use. It's not implemented.
     */
    @Deprecated
    public void setParent(final AbstractHtml parent) {
        throw new MethodNotImplementedException(
                "This method is not implemented");
        // this.parent = parent;
    }

    /**
     * @return the unmodifiable list of children
     * @since 2.0.0
     * @author WFF
     */
    public List<AbstractHtml> getChildren() {

        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).readLock();
        try {
            lock.lock();

            return Collections.unmodifiableList(new ArrayList<>(children));
        } finally {
            lock.unlock();
        }
    }

    /**
     * NB: this method is for internal use. The returned object should not be
     * modified.
     *
     * @return the internal children object.
     * @since 2.0.0
     * @author WFF
     */
    public Set<AbstractHtml> getChildren(final Object accessObject) {

        if (accessObject == null || !(SecurityClassConstants.BROWSER_PAGE
                .equals(accessObject.getClass().getName()))) {
            throw new WffSecurityException(
                    "Not allowed to consume this method. This method is for internal use.");
        }
        return children;
    }

    /**
     * Removes all current children and adds the given children under this tag.
     * Unlike setter methods, it will not reuse the given set object but it will
     * copy all children from the given set object. <br>
     *
     * @param children
     *            which will be set as the children tag after removing all
     *            current children. Empty set or null will remove all current
     *            children from this tag.
     * @since 2.1.12 proper implementation is available since 2.1.12
     * @author WFF
     */
    public void setChildren(final Set<AbstractHtml> children) {
        if (children == null || children.size() == 0) {
            removeAllChildren();
        } else {
            addInnerHtmls(children.toArray(new AbstractHtml[children.size()]));
        }
    }

    /**
     * Gets the children of this tag as an array. An efficient way to get the
     * children as an array.
     *
     * @return the array of children of this tag.
     * @since 3.0.1
     * @author WFF
     */
    public AbstractHtml[] getChildrenAsArray() {

        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).readLock();
        try {
            lock.lock();

            return children.toArray(new AbstractHtml[children.size()]);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Gets the first child of this tag. The efficient way to get the first
     * child.
     *
     * @return the first child of this tag or null if there is no child.
     *
     * @since 3.0.1
     * @author WFF
     */
    public AbstractHtml getFirstChild() {
        // this block must be synchronized otherwise may get null or
        // ConcurrentModificationException
        // the test cases are written to check its thread safety and can be
        // reproduce by uncommenting this synchronized block, checkout
        // AbstractHtmlTest class for it.
        // synchronized (children) {
        //
        // }
        // it's been replaced with locking

        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).readLock();
        try {
            lock.lock();

            // this must be most efficient because the javadoc of findFirst says
            // "This is a short-circuiting terminal operation."
            // return (T) children.stream().findFirst().orElse(null);

            // but as per CodePerformanceTest.testPerformanceOfFindFirst
            // the below is faster

            final Iterator<AbstractHtml> iterator = children.iterator();
            if (iterator.hasNext()) {
                return iterator.next();
            }
            return null;
        } finally {
            lock.unlock();
        }

    }

    /**
     * Gets the number of children in this tag. An efficient way to find the
     * size of children.
     *
     * @return the size of children.
     * @since 3.0.1
     * @author WFF
     */
    public int getChildrenSize() {
        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).readLock();
        try {
            lock.lock();

            return children.size();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Gets the child at the specified position. An efficient way to get the
     * child at particular position. If you want to get the child at 0th(zeroth)
     * index then use {@code AbstractHtml#getFirstChild()} method instead of
     * this method.
     *
     * @param index
     *            from this index the tag will be returned
     * @return the child at the specified index.
     * @since 3.0.1
     * @author WFF
     */
    public AbstractHtml getChildAt(final int index) {

        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).readLock();
        try {
            lock.lock();

            return children.toArray(new AbstractHtml[children.size()])[index];
        } finally {
            lock.unlock();
        }
    }

    /**
     * Checks whether a tag is contained in its direct children. An efficient
     * way to check if the given tag is a direct child of this tag.
     *
     * @param childTag
     * @return true if the given tag is a child of this tags.
     * @since 3.0.1
     * @author WFF
     */
    public boolean containsChild(final AbstractHtml childTag) {

        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).readLock();
        try {
            lock.lock();

            return children.contains(childTag);
        } finally {
            lock.unlock();
        }
    }

    /**
     * For internal purpose. Not recommended for external purpose.
     *
     * @return the opening tag of this object
     * @author WFF
     */
    public final String getOpeningTag() {
        if (isRebuild() || isModified()) {
            final Lock lock = sharedObject.getLock(ACCESS_OBJECT).writeLock();
            try {
                lock.lock();

                buildOpeningTag(true);
            } finally {
                lock.unlock();
            }

        }
        return openingTag;
    }

    /**
     * For internal purpose.
     *
     * @return the closing tag of this object
     * @author WFF
     */
    public String getClosingTag() {
        return closingTag;
    }

    /**
     * @return {@code String} equalent to the html string of the tag including
     *         the child tags.
     * @since 1.0.0
     * @author WFF
     */
    protected String getPrintStructure() {
        if (isRebuild() || isModified()) {
            final Lock lock = sharedObject.getLock(ACCESS_OBJECT).writeLock();
            try {
                lock.lock();

                final String printStructure = getPrintStructure(true);
                setRebuild(false);
                return printStructure;
            } finally {
                lock.unlock();
            }
        } else {
            final Lock lock = sharedObject.getLock(ACCESS_OBJECT).readLock();
            try {
                lock.lock();

                return tagBuilder.toString();
            } finally {
                lock.unlock();
            }

        }

    }

    /**
     *
     *
     * @param rebuild
     * @return
     * @since 1.0.0
     * @author WFF
     */
    protected String getPrintStructure(final boolean rebuild) {

        if (rebuild || isRebuild() || isModified()) {

            final Lock lock = sharedObject.getLock(ACCESS_OBJECT).writeLock();
            try {
                lock.lock();
                beforePrintStructure();
                if (tagBuilder.length() > 0) {
                    tagBuilder.delete(0, tagBuilder.length());
                }
                final Set<AbstractHtml> localChildren = new LinkedHashSet<>(1);
                localChildren.add(this);
                recurChildren(tagBuilder, localChildren, true);
                setRebuild(false);
                tagBuilder.trimToSize();
            } finally {
                lock.unlock();
            }
        }
        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).readLock();
        try {
            lock.lock();
            return tagBuilder.toString();
        } finally {
            lock.unlock();
        }
    }

    /**
     * @param rebuild
     * @since 1.0.0
     * @author WFF
     * @return the total number of bytes written
     * @throws IOException
     */
    protected int writePrintStructureToOutputStream(final Charset charset,
            final OutputStream os, final boolean rebuild) throws IOException {

        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).writeLock();
        try {
            lock.lock();

            beforeWritePrintStructureToOutputStream();
            final int[] totalWritten = { 0 };
            final Set<AbstractHtml> localChildren = new LinkedHashSet<>(1);
            localChildren.add(this);
            recurChildrenToOutputStream(totalWritten, charset, os,
                    localChildren, rebuild);
            return totalWritten[0];
        } finally {
            lock.unlock();
        }
    }

    // for future development
    /**
     * @param rebuild
     * @since 2.0.0
     * @author WFF
     * @throws IOException
     */
    protected void writePrintStructureToWffBinaryMessageOutputStream(
            final boolean rebuild) throws IOException {

        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).writeLock();
        try {
            lock.lock();

            beforeWritePrintStructureToWffBinaryMessageOutputStream();
            final Set<AbstractHtml> localChildren = new LinkedHashSet<>(1);
            localChildren.add(this);
            recurChildrenToWffBinaryMessageOutputStream(localChildren, true);
        } finally {
            lock.unlock();
        }
    }

    /**
     * to form html string from the children
     *
     * @param children
     * @param rebuild
     *            TODO
     * @since 1.0.0
     * @author WFF
     */
    private static void recurChildren(final StringBuilder tagBuilder,
            final Set<AbstractHtml> children, final boolean rebuild) {
        if (children != null && children.size() > 0) {
            for (final AbstractHtml child : children) {
                child.setRebuild(rebuild);
                tagBuilder.append(child.getOpeningTag());

                final Set<AbstractHtml> childrenOfChildren = child.children;

                recurChildren(tagBuilder, childrenOfChildren, rebuild);

                tagBuilder.append(child.closingTag);
            }
        }
    }

    /**
     * Use this method to produce HTML from very heavy and complicated tag
     * hierarchy. For normal and simple HTML hierarchy use {@code toHtmlString}
     * method which is faster than this method. The advantage of
     * {@code toBigHtmlString} over {@code toHtmlString} is it will never throw
     * {@code StackOverflowError}. <br>
     * NB:- this method has not been undergone all testing process.
     *
     * @return the HTML string similar to toHtmlString method.
     * @since 2.1.12
     * @author WFF
     */
    public String toBigHtmlString() {

        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).writeLock();
        try {
            lock.lock();

            final String printStructure = getPrintStructureWithoutRecursive(
                    getSharedObject().isChildModified());

            if (parent == null) {
                sharedObject.setChildModified(false);
            }

            return printStructure;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Use this method to produce HTML from very heavy and complicated tag
     * hierarchy. For normal and simple HTML hierarchy use {@code toHtmlString}
     * method which is faster than this method. The advantage of
     * {@code toBigHtmlString} over {@code toHtmlString} is it will never throw
     * {@code StackOverflowError}. <br>
     * NB:- this method has not been undergone all testing process.
     *
     * @param rebuild
     *            true to rebuild the tag hierarchy or false to return from
     *            cache if available.
     * @return the HTML string similar to toHtmlString method.
     * @since 2.1.12
     * @author WFF
     */
    public String toBigHtmlString(final boolean rebuild) {
        return getPrintStructureWithoutRecursive(rebuild);
    }

    private String getPrintStructureWithoutRecursive(final boolean rebuild) {

        if (rebuild || isRebuild() || isModified()) {
            final Lock lock = sharedObject.getLock(ACCESS_OBJECT).writeLock();
            try {
                lock.lock();

                beforePrintStructure();
                if (tagBuilder.length() > 0) {
                    tagBuilder.delete(0, tagBuilder.length());
                }

                appendPrintStructureWithoutRecursive(tagBuilder, this, true);
                setRebuild(false);
                tagBuilder.trimToSize();

            } finally {
                lock.unlock();
            }
        }

        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).readLock();
        try {
            lock.lock();

            return tagBuilder.toString();
        } finally {
            lock.unlock();
        }

    }

    /**
     * Use this method to produce HTML from very heavy and complicated tag
     * hierarchy. For normal and simple HTML hierarchy use
     * {@code toOutputStream} method which is faster than this method. The
     * advantage of {@code toBigOutputStream} over {@code toOutputStream} is it
     * will never throw {@code StackOverflowError} and the memory consumed at
     * the time of writing could be available for GC (depends on JVM GC rules).
     * <br>
     * NB:- this method has not been undergone all testing process.
     *
     * @param os
     *            the object of {@code OutputStream} to write to.
     * @return the total number of bytes written
     * @throws IOException
     * @since 2.1.12
     */
    public int toBigOutputStream(final OutputStream os) throws IOException {
        return writePrintStructureToOSWithoutRecursive(charset, os, true);
    }

    /**
     * Use this method to produce HTML from very heavy and complicated tag
     * hierarchy. For normal and simple HTML hierarchy use
     * {@code toOutputStream} method which is faster than this method. The
     * advantage of {@code toBigOutputStream} over {@code toOutputStream} is it
     * will never throw {@code StackOverflowError} and the memory consumed at
     * the time of writing could be available for GC (depends on JVM GC rules).
     * <br>
     * NB:- this method has not been undergone all testing process.
     *
     * @param os
     *            the object of {@code OutputStream} to write to.
     * @param charset
     *            the charset
     * @return
     * @throws IOException
     * @since 2.1.12
     */
    public int toBigOutputStream(final OutputStream os, final Charset charset)
            throws IOException {
        return writePrintStructureToOSWithoutRecursive(charset, os, true);
    }

    /**
     * Use this method to produce HTML from very heavy and complicated tag
     * hierarchy. For normal and simple HTML hierarchy use
     * {@code toOutputStream} method which is faster than this method. The
     * advantage of {@code toBigOutputStream} over {@code toOutputStream} is it
     * will never throw {@code StackOverflowError} and the memory consumed at
     * the time of writing could be available for GC (depends on JVM GC rules).
     * <br>
     * NB:- this method has not been undergone all testing process.
     *
     * @param os
     *            the object of {@code OutputStream} to write to.
     * @param charset
     *            the charset
     * @return the total number of bytes written
     * @throws IOException
     * @since 2.1.12
     */
    public int toBigOutputStream(final OutputStream os, final String charset)
            throws IOException {

        if (charset == null) {
            return writePrintStructureToOSWithoutRecursive(
                    Charset.forName(charset), os, true);
        }
        return writePrintStructureToOSWithoutRecursive(this.charset, os, true);
    }

    /**
     * Use this method to produce HTML from very heavy and complicated tag
     * hierarchy. For normal and simple HTML hierarchy use
     * {@code toOutputStream} method which is faster than this method. The
     * advantage of {@code toBigOutputStream} over {@code toOutputStream} is it
     * will never throw {@code StackOverflowError} and the memory consumed at
     * the time of writing could be available for GC (depends on JVM GC rules).
     * <br>
     * NB:- this method has not been undergone all testing process.
     *
     * @param os
     *            the object of {@code OutputStream} to write to.
     * @param rebuild
     *            true to rebuild &amp; false to write previously built bytes.
     * @return the total number of bytes written
     *
     * @throws IOException
     * @since 2.1.12
     */
    public int toBigOutputStream(final OutputStream os, final boolean rebuild)
            throws IOException {
        return writePrintStructureToOSWithoutRecursive(charset, os, rebuild);
    }

    /**
     * Use this method to produce HTML from very heavy and complicated tag
     * hierarchy. For normal and simple HTML hierarchy use
     * {@code toOutputStream} method which is faster than this method. The
     * advantage of {@code toBigOutputStream} over {@code toOutputStream} is it
     * will never throw {@code StackOverflowError} and the memory consumed at
     * the time of writing could be available for GC (depends on JVM GC rules).
     * <br>
     * NB:- this method has not been undergone all testing process.
     *
     * @param os
     *            the object of {@code OutputStream} to write to.
     * @param rebuild
     *            true to rebuild &amp; false to write previously built bytes.
     * @param charset
     *            the charset
     * @return the total number of bytes written
     * @throws IOException
     * @since 2.1.12
     */
    public int toBigOutputStream(final OutputStream os, final boolean rebuild,
            final Charset charset) throws IOException {
        if (charset == null) {
            return writePrintStructureToOSWithoutRecursive(this.charset, os,
                    rebuild);
        }
        return writePrintStructureToOSWithoutRecursive(charset, os, rebuild);
    }

    /**
     * Use this method to produce HTML from very heavy and complicated tag
     * hierarchy. For normal and simple HTML hierarchy use
     * {@code toOutputStream} method which is faster than this method. The
     * advantage of {@code toBigOutputStream} over {@code toOutputStream} is it
     * will never throw {@code StackOverflowError} and the memory consumed at
     * the time of writing could be available for GC (depends on JVM GC rules).
     * <br>
     * NB:- this method has not been undergone all testing process.
     *
     * @param os
     *            the object of {@code OutputStream} to write to.
     * @param rebuild
     *            true to rebuild &amp; false to write previously built bytes.
     * @param charset
     *            the charset
     * @return the total number of bytes written
     * @throws IOException
     * @since 2.1.12
     */
    public int toBigOutputStream(final OutputStream os, final boolean rebuild,
            final String charset) throws IOException {

        if (charset == null) {
            return writePrintStructureToOSWithoutRecursive(this.charset, os,
                    rebuild);
        }
        return writePrintStructureToOSWithoutRecursive(Charset.forName(charset),
                os, rebuild);
    }

    private int writePrintStructureToOSWithoutRecursive(final Charset charset,
            final OutputStream os, final boolean rebuild) throws IOException {

        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).writeLock();
        try {
            lock.lock();

            beforeWritePrintStructureToOutputStream();
            final int[] totalWritten = { 0 };
            writePrintStructureToOSWithoutRecursive(totalWritten, charset, os,
                    this, rebuild);
            return totalWritten[0];

        } finally {
            lock.unlock();
        }
    }

    private static void appendPrintStructureWithoutRecursive(
            final StringBuilder builder, final AbstractHtml topBase,
            final boolean rebuild) {

        AbstractHtml current = topBase;

        while (current != null) {

            final AbstractHtml child = current;
            current = null;

            final AbstractHtml bottomChild = appendOpeningTagAndReturnBottomTag(
                    builder, child, rebuild);

            builder.append(bottomChild.closingTag);

            if (topBase.equals(bottomChild)) {
                break;
            }

            final List<AbstractHtml> childrenHoldingBottomChild = new ArrayList<>(
                    bottomChild.parent.children);

            final int indexOfNextToBottomChild = childrenHoldingBottomChild
                    .indexOf(bottomChild) + 1;

            if (indexOfNextToBottomChild < childrenHoldingBottomChild.size()) {
                final AbstractHtml nextToBottomChild = childrenHoldingBottomChild
                        .get(indexOfNextToBottomChild);
                current = nextToBottomChild;
            } else {

                if (bottomChild.parent.parent == null) {
                    builder.append(bottomChild.parent.closingTag);
                    break;
                }

                final List<AbstractHtml> childrenHoldingParent = new ArrayList<>(
                        bottomChild.parent.parent.children);

                final int indexOfNextToBottomParent = childrenHoldingParent
                        .indexOf(bottomChild.parent) + 1;

                if (indexOfNextToBottomParent < childrenHoldingParent.size()) {
                    builder.append(bottomChild.parent.closingTag);

                    if (topBase.equals(bottomChild.parent)) {
                        break;
                    }

                    final AbstractHtml nextToParent = childrenHoldingParent
                            .get(indexOfNextToBottomParent);
                    current = nextToParent;
                } else {
                    current = appendClosingTagUptoRootReturnFirstMiddleChild(
                            builder, topBase, bottomChild);
                }

            }

        }

    }

    /**
     * @param totalWritten
     * @param charset
     * @param os
     * @param topBase
     * @param rebuild
     * @throws IOException
     * @since 2.1.12
     * @author WFF
     */
    private static void writePrintStructureToOSWithoutRecursive(
            final int[] totalWritten, final Charset charset,
            final OutputStream os, final AbstractHtml topBase,
            final boolean rebuild) throws IOException {

        AbstractHtml current = topBase;

        while (current != null) {

            final AbstractHtml child = current;
            current = null;

            final AbstractHtml bottomChild = writeOpeningTagAndReturnBottomTag(
                    totalWritten, charset, os, child, rebuild);

            {
                final byte[] closingTagBytes = bottomChild.closingTag
                        .getBytes(charset);
                os.write(closingTagBytes);

                totalWritten[0] += closingTagBytes.length;

                if (topBase.equals(bottomChild)) {
                    break;
                }
            }

            final List<AbstractHtml> childrenHoldingBottomChild = new ArrayList<>(
                    bottomChild.parent.children);

            final int indexOfNextToBottomChild = childrenHoldingBottomChild
                    .indexOf(bottomChild) + 1;

            if (indexOfNextToBottomChild < childrenHoldingBottomChild.size()) {
                final AbstractHtml nextToBottomChild = childrenHoldingBottomChild
                        .get(indexOfNextToBottomChild);
                current = nextToBottomChild;
            } else {

                {
                    if (bottomChild.parent.parent == null) {

                        final byte[] closingTagBytes = bottomChild.parent.closingTag
                                .getBytes(charset);
                        os.write(closingTagBytes);

                        totalWritten[0] += closingTagBytes.length;
                        break;
                    }
                }

                final List<AbstractHtml> childrenHoldingParent = new ArrayList<>(
                        bottomChild.parent.parent.children);

                final int indexOfNextToBottomParent = childrenHoldingParent
                        .indexOf(bottomChild.parent) + 1;

                if (indexOfNextToBottomParent < childrenHoldingParent.size()) {

                    final byte[] closingTagBytes = bottomChild.parent.closingTag
                            .getBytes(charset);
                    os.write(closingTagBytes);

                    totalWritten[0] += closingTagBytes.length;

                    if (topBase.equals(bottomChild.parent)) {
                        break;
                    }

                    final AbstractHtml nextToParent = childrenHoldingParent
                            .get(indexOfNextToBottomParent);
                    current = nextToParent;
                } else {
                    current = writeClosingTagUptoRootReturnFirstMiddleChild(
                            totalWritten, charset, os, topBase, bottomChild);
                }

            }

        }

    }

    private static AbstractHtml appendOpeningTagAndReturnBottomTag(
            final StringBuilder builder, final AbstractHtml base,
            final boolean rebuild) {

        AbstractHtml bottomChild = base;

        Set<AbstractHtml> current = new HashSet<>(1);
        current.add(base);

        while (current != null) {

            final Set<AbstractHtml> children = current;
            current = null;
            final Iterator<AbstractHtml> iterator = children.iterator();
            // only first child is required here
            if (iterator.hasNext()) {
                final AbstractHtml child = iterator.next();
                child.setRebuild(rebuild);
                builder.append(child.getOpeningTag());
                bottomChild = child;

                final Set<AbstractHtml> subChildren = child.children;
                if (subChildren != null && subChildren.size() > 0) {
                    current = subChildren;
                }

            }
        }

        return bottomChild;
    }

    private static AbstractHtml writeOpeningTagAndReturnBottomTag(
            final int[] totalWritten, final Charset charset,
            final OutputStream os, final AbstractHtml base,
            final boolean rebuild) throws IOException {

        AbstractHtml bottomChild = base;

        Set<AbstractHtml> current = new HashSet<>(1);
        current.add(base);

        while (current != null) {

            final Set<AbstractHtml> children = current;
            current = null;

            final Iterator<AbstractHtml> iterator = children.iterator();
            // only first child is required here
            if (iterator.hasNext()) {
                final AbstractHtml child = iterator.next();

                child.setRebuild(rebuild);

                final byte[] openingTagBytes = child.getOpeningTag()
                        .getBytes(charset);
                os.write(openingTagBytes);
                totalWritten[0] += openingTagBytes.length;

                bottomChild = child;

                final Set<AbstractHtml> subChildren = child.children;
                if (subChildren != null && subChildren.size() > 0) {
                    current = subChildren;
                }
            }

        }

        return bottomChild;
    }

    private static AbstractHtml appendClosingTagUptoRootReturnFirstMiddleChild(
            final StringBuilder builder, final AbstractHtml topBase,
            final AbstractHtml bottomChild) {

        AbstractHtml current = bottomChild;

        while (current != null) {
            final AbstractHtml child = current;

            current = null;

            if (child.parent != null) {

                final List<AbstractHtml> childrenHoldingChild = new ArrayList<>(
                        child.parent.children);
                final int nextIndexOfChild = childrenHoldingChild.indexOf(child)
                        + 1;
                if (nextIndexOfChild < childrenHoldingChild.size()) {
                    return childrenHoldingChild.get(nextIndexOfChild);
                } else {
                    builder.append(child.parent.closingTag);
                    if (topBase.equals(child.parent)) {
                        break;
                    }
                    current = child.parent;

                }
            }
        }
        return null;
    }

    private static AbstractHtml writeClosingTagUptoRootReturnFirstMiddleChild(
            final int[] totalWritten, final Charset charset,
            final OutputStream os, final AbstractHtml topBase,
            final AbstractHtml bottomChild) throws IOException {

        AbstractHtml current = bottomChild;

        while (current != null) {
            final AbstractHtml child = current;

            current = null;

            if (child.parent != null) {
                final List<AbstractHtml> childrenHoldingChild = new ArrayList<>(
                        child.parent.children);

                final int nextIndexOfChild = childrenHoldingChild.indexOf(child)
                        + 1;
                if (nextIndexOfChild < childrenHoldingChild.size()) {
                    return childrenHoldingChild.get(nextIndexOfChild);
                } else {
                    final byte[] closingTagBytes = child.parent.closingTag
                            .getBytes(charset);
                    os.write(closingTagBytes);

                    totalWritten[0] += closingTagBytes.length;

                    if (topBase.equals(child.parent)) {
                        break;
                    }
                    current = child.parent;

                }
            }
        }
        return null;
    }

    /**
     * to form html string from the children
     *
     * @param children
     * @param rebuild
     *            TODO
     * @since 1.0.0
     * @author WFF
     * @throws IOException
     */
    private static void recurChildrenToOutputStream(final int[] totalWritten,
            final Charset charset, final OutputStream os,
            final Set<AbstractHtml> children, final boolean rebuild)
            throws IOException {

        if (children != null && children.size() > 0) {
            for (final AbstractHtml child : children) {
                child.setRebuild(rebuild);
                final byte[] openingTagBytes = child.getOpeningTag()
                        .getBytes(charset);
                os.write(openingTagBytes);
                totalWritten[0] += openingTagBytes.length;

                final Set<AbstractHtml> childrenOfChildren = child.children;

                recurChildrenToOutputStream(totalWritten, charset, os,
                        childrenOfChildren, rebuild);
                final byte[] closingTagBytes = child.closingTag
                        .getBytes(charset);
                os.write(closingTagBytes);

                totalWritten[0] += closingTagBytes.length;
            }
        }
    }

    // for future development
    /**
     * to form html string from the children
     *
     * @param children
     * @param rebuild
     *            TODO
     * @since 2.0.0
     * @author WFF
     * @throws IOException
     */
    private void recurChildrenToWffBinaryMessageOutputStream(
            final Set<AbstractHtml> children, final boolean rebuild)
            throws IOException {
        if (children != null && children.size() > 0) {
            for (final AbstractHtml child : children) {
                child.setRebuild(rebuild);
                // wffBinaryMessageOutputStreamer

                // outputStream.write(child.getOpeningTag().getBytes(charset));

                final NameValue nameValue = new NameValue();

                final int tagNameIndex = TagRegistry.getTagNames()
                        .indexOf(child.getTagName());

                // if the tag index is -1 i.e. it's not indexed then the tag
                // name prepended with 0 value byte should be set.
                // If the first byte == 0 and length is greater than 1 then it's
                // a tag name, if the first byte is greater than 0 then it is
                // index bytes

                byte[] closingTagNameConvertedBytes = null;
                if (tagNameIndex == -1) {

                    final byte[] tagNameBytes = child.getTagName()
                            .getBytes(charset);

                    final byte[] nameBytes = new byte[tagNameBytes.length + 1];

                    nameBytes[0] = 0;

                    System.arraycopy(tagNameBytes, 0, nameBytes, 1,
                            tagNameBytes.length);
                    nameValue.setName(nameBytes);
                    closingTagNameConvertedBytes = nameBytes;
                } else {
                    final byte[] indexBytes = WffBinaryMessageUtil
                            .getOptimizedBytesFromInt(tagNameIndex);
                    nameValue.setName(indexBytes);
                    closingTagNameConvertedBytes = WffBinaryMessageUtil
                            .getOptimizedBytesFromInt((tagNameIndex * (-1)));
                }

                nameValue.setValues(
                        child.getAttributeHtmlBytesCompressedByIndex(rebuild,
                                charset));

                wffBinaryMessageOutputStreamer.write(nameValue);

                final Set<AbstractHtml> childrenOfChildren = child.children;

                recurChildrenToWffBinaryMessageOutputStream(childrenOfChildren,
                        rebuild);

                final NameValue closingTagNameValue = new NameValue();
                closingTagNameValue.setName(closingTagNameConvertedBytes);
                closingTagNameValue.setValues(new byte[0][0]);
                wffBinaryMessageOutputStreamer.write(closingTagNameValue);

                // outputStream.write(child.closingTag.getBytes(charset));
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.webfirmframework.wffweb.tag.TagBase#toHtmlString()
     *
     * @since 1.0.0
     *
     * @author WFF
     */
    @Override
    public String toHtmlString() {
        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).writeLock();
        try {
            lock.lock();

            final String printStructure = getPrintStructure(
                    getSharedObject().isChildModified());

            if (parent == null) {
                sharedObject.setChildModified(false);
            }

            return printStructure;
        } finally {
            lock.unlock();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.webfirmframework.wffweb.tag.core.TagBase#toHtmlString(java.nio.
     * charset.Charset)
     */
    @Override
    public String toHtmlString(final Charset charset) {
        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).writeLock();
        final Charset previousCharset = this.charset;
        try {
            lock.lock();
            this.charset = charset;
            // assigning it to new variable is very important here as this
            // line of code should invoke before finally block
            final String htmlString = toHtmlString();
            return htmlString;
        } finally {
            this.charset = previousCharset;
            lock.unlock();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.webfirmframework.wffweb.tag.core.TagBase#toHtmlString(java.lang.
     * String)
     */
    @Override
    public String toHtmlString(final String charset) {
        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).writeLock();
        final Charset previousCharset = this.charset;
        try {
            lock.lock();
            this.charset = Charset.forName(charset);
            // assigning it to new variable is very important here as this
            // line of code should invoke before finally block
            final String htmlString = toHtmlString();
            return htmlString;
        } finally {
            this.charset = previousCharset;
            lock.unlock();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.webfirmframework.wffweb.tag.TagBase#toHtmlString(boolean)
     *
     * @since 1.0.0
     *
     * @author WFF
     */
    @Override
    public String toHtmlString(final boolean rebuild) {
        return getPrintStructure(rebuild);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.webfirmframework.wffweb.tag.core.TagBase#toHtmlString(boolean,
     * java.nio.charset.Charset)
     */
    @Override
    public String toHtmlString(final boolean rebuild, final Charset charset) {
        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).writeLock();
        final Charset previousCharset = this.charset;
        try {
            lock.lock();
            this.charset = charset;
            // assigning it to new variable is very important here as this
            // line of code should invoke before finally block
            final String htmlString = toHtmlString(rebuild);
            return htmlString;
        } finally {
            this.charset = previousCharset;
            lock.unlock();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.webfirmframework.wffweb.tag.core.TagBase#toHtmlString(boolean,
     * java.lang.String)
     */
    @Override
    public String toHtmlString(final boolean rebuild, final String charset) {
        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).writeLock();
        final Charset previousCharset = this.charset;
        try {
            lock.lock();
            this.charset = Charset.forName(charset);
            // assigning it to new variable is very important here as this
            // line of code should invoke before finally block
            final String htmlString = toHtmlString(rebuild);
            return htmlString;
        } finally {
            this.charset = previousCharset;
            lock.unlock();
        }
    }

    // TODO for future implementation
    /**
     * @param os
     *            the object of {@code OutputStream} to write to.
     * @throws IOException
     * @deprecated this method is for future implementation so it should not be
     *             consumed
     */
    @Deprecated
    void toOutputStream(final boolean asWffBinaryMessage, final OutputStream os)
            throws IOException {

        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).writeLock();
        try {
            lock.lock();

            if (asWffBinaryMessage) {
                try {
                    wffBinaryMessageOutputStreamer = new WffBinaryMessageOutputStreamer(
                            os);
                    writePrintStructureToWffBinaryMessageOutputStream(true);
                } finally {
                    wffBinaryMessageOutputStreamer = null;
                }
            } else {
                toOutputStream(os);
            }
        } finally {
            lock.unlock();
        }

    }

    /**
     * @param os
     *            the object of {@code OutputStream} to write to.
     * @return the total number of bytes written
     * @throws IOException
     */
    public int toOutputStream(final OutputStream os) throws IOException {
        return writePrintStructureToOutputStream(charset, os, true);
    }

    /**
     * @param os
     *            the object of {@code OutputStream} to write to.
     * @param charset
     *            the charset
     * @return
     * @throws IOException
     */
    public int toOutputStream(final OutputStream os, final Charset charset)
            throws IOException {
        return writePrintStructureToOutputStream(charset, os, true);
    }

    /**
     * @param os
     *            the object of {@code OutputStream} to write to.
     * @param charset
     *            the charset
     * @return the total number of bytes written
     * @throws IOException
     */
    public int toOutputStream(final OutputStream os, final String charset)
            throws IOException {

        if (charset == null) {
            return writePrintStructureToOutputStream(Charset.forName(charset),
                    os, true);
        }
        return writePrintStructureToOutputStream(this.charset, os, true);
    }

    /**
     * @param os
     *            the object of {@code OutputStream} to write to.
     * @param rebuild
     *            true to rebuild &amp; false to write previously built bytes.
     * @return the total number of bytes written
     *
     * @throws IOException
     */
    public int toOutputStream(final OutputStream os, final boolean rebuild)
            throws IOException {
        return writePrintStructureToOutputStream(charset, os, rebuild);
    }

    /**
     * @param os
     *            the object of {@code OutputStream} to write to.
     * @param rebuild
     *            true to rebuild &amp; false to write previously built bytes.
     * @param charset
     *            the charset
     * @return the total number of bytes written
     * @throws IOException
     */
    public int toOutputStream(final OutputStream os, final boolean rebuild,
            final Charset charset) throws IOException {
        if (charset == null) {
            return writePrintStructureToOutputStream(this.charset, os, rebuild);
        }
        return writePrintStructureToOutputStream(charset, os, rebuild);
    }

    /**
     * @param os
     *            the object of {@code OutputStream} to write to.
     * @param rebuild
     *            true to rebuild &amp; false to write previously built bytes.
     * @param charset
     *            the charset
     * @return the total number of bytes written
     * @throws IOException
     */
    public int toOutputStream(final OutputStream os, final boolean rebuild,
            final String charset) throws IOException {

        if (charset == null) {
            return writePrintStructureToOutputStream(this.charset, os, rebuild);
        }
        return writePrintStructureToOutputStream(Charset.forName(charset), os,
                rebuild);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    // it is not a best practice to print html string by this method because if
    // it is used in ThreadLocal class it may cause memory leak.
    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * Eg tag names :- html, body, div table, input, button etc...
     *
     * @return the tagName set by {@code AbstractHtml5#setTagName(String)}
     *         method.
     * @since 1.0.0
     * @author WFF
     */
    public String getTagName() {
        return tagName;
    }

    public byte[][] getAttributeHtmlBytesCompressedByIndex(
            final boolean rebuild, final Charset charset) throws IOException {
        return AttributeUtil.getAttributeHtmlBytesCompressedByIndex(rebuild,
                charset, attributes);
    }

    /**
     *
     * @param rebuild
     *            TODO
     * @since 1.0.0
     * @author WFF
     */
    private void buildOpeningTag(final boolean rebuild) {

        // final String attributeHtmlString = AttributeUtil
        // .getAttributeHtmlString(rebuild, charset, attributes);

        if (htmlStartSB.length() > 0) {
            htmlStartSB.delete(0, htmlStartSB.length());
        }

        if (tagName != null) {
            // previously attributeHtmlString was used in append method
            // as argument.
            htmlStartSB.append('<').append(tagName).append(AttributeUtil
                    .getAttributeHtmlString(rebuild, charset, attributes));
            if (tagType == TagType.OPENING_CLOSING) {
                htmlStartSB.append('>');
            } else if (tagType == TagType.SELF_CLOSING) {
                htmlStartSB.append(new char[] { '/', '>' });
            } else {
                // here it will be tagType == TagType.NON_CLOSING as there are
                // three types in TagType class
                htmlStartSB.append('>');
            }
            htmlStartSB.trimToSize();
            openingTag = htmlStartSB.toString();
        } else {
            htmlStartSB.trimToSize();
            openingTag = "";
        }
    }

    /**
     *
     * @since 1.0.0
     * @author WFF
     */
    private void buildClosingTag() {
        if (htmlEndSB.length() > 0) {
            htmlEndSB.delete(0, htmlEndSB.length());
        }
        if (tagName != null) {
            htmlEndSB.append(new char[] { '<', '/' }).append(tagName)
                    .append('>');
        } else {
            if (htmlStartSB != null) {
                htmlEndSB.append(getHtmlMiddleSB());
            }
        }
        htmlEndSB.trimToSize();
        closingTag = htmlEndSB.toString();
    }

    /**
     * @return the sharedObject
     * @since 1.0.0
     * @author WFF
     */
    @Override
    public final AbstractHtml5SharedObject getSharedObject() {
        return sharedObject;
    }

    /**
     * @return the htmlMiddleSB
     * @since 1.0.0
     * @author WFF
     */
    protected StringBuilder getHtmlMiddleSB() {
        if (htmlMiddleSB == null) {
            synchronized (this) {
                if (htmlMiddleSB == null) {
                    htmlMiddleSB = new StringBuilder();
                }
            }
        }
        return htmlMiddleSB;
    }

    /**
     * @return the htmlStartSBAsFirst
     * @since 1.0.0
     * @author WFF
     */
    public boolean isHtmlStartSBAsFirst() {
        return htmlStartSBAsFirst;
    }

    protected AbstractHtml deepClone(final AbstractHtml objectToBeClonned)
            throws CloneNotSupportedException {
        return CloneUtil.<AbstractHtml> deepClone(objectToBeClonned);
    }

    /**
     * invokes just before {@code getPrintStructure(final boolean} method and
     * only if the getPrintStructure(final boolean} rebuilds the structure.
     *
     * @since 1.0.0
     * @author WFF
     */
    protected void beforePrintStructure() {
        // NOP override and use
    }

    /**
     * invokes just before
     * {@code writePrintStructureToOutputStream(final OutputStream} method.
     *
     * @since 1.0.0
     * @author WFF
     */
    protected void beforeWritePrintStructureToOutputStream() {
        // NOP override and use
    }

    /**
     * invokes just before
     * {@code writePrintStructureToWffBinaryMessageOutputStream(final OutputStream}
     * method.
     *
     * @since 2.0.0
     * @author WFF
     */
    protected void beforeWritePrintStructureToWffBinaryMessageOutputStream() {
        // NOP override and use
    }

    /**
     * Creates and returns a deeply cloned copy of this object. The precise
     * meaning of "copy" may depend on the class of the object. The general
     * intent is that, for any object {@code x}, the expression: <blockquote>
     *
     * <pre>
     * x.clone() != x
     * </pre>
     *
     * </blockquote> will be true, and that the expression: <blockquote>
     *
     * <pre>
     * x.clone().getClass() == x.getClass()
     * </pre>
     *
     * </blockquote> will be {@code true}, but these are not absolute
     * requirements. While it is typically the case that: <blockquote>
     *
     * <pre>
     * x.clone().equals(x)
     * </pre>
     *
     * </blockquote> will be {@code true}, this is not an absolute requirement.
     * <p>
     * By convention, the returned object should be obtained by calling
     * {@code super.clone}. If a class and all of its superclasses (except
     * {@code Object}) obey this convention, it will be the case that
     * {@code x.clone().getClass() == x.getClass()}.
     * <p>
     * By convention, the object returned by this method should be independent
     * of this object (which is being cloned). To achieve this independence, it
     * may be necessary to modify one or more fields of the object returned by
     * {@code super.clone} before returning it. Typically, this means copying
     * any mutable objects that comprise the internal "deep structure" of the
     * object being cloned and replacing the references to these objects with
     * references to the copies. If a class contains only primitive fields or
     * references to immutable objects, then it is usually the case that no
     * fields in the object returned by {@code super.clone} need to be modified.
     * <p>
     * The method {@code clone} for class {@code AbstractHtml} performs a
     * specific cloning operation. First, if the class of this object does not
     * implement the interfaces {@code Cloneable} and {@code Serializable}, then
     * a {@code CloneNotSupportedException} is thrown. Note that all arrays are
     * considered to implement the interface {@code Cloneable} and that the
     * return type of the {@code clone} method of an array type {@code T[]} is
     * {@code T[]} where T is any reference or primitive type. Otherwise, this
     * method creates a new instance of the class of this object and initializes
     * all its fields with exactly the contents of the corresponding fields of
     * this object, as if by assignment; the contents of the fields are not
     * themselves cloned. Thus, this method performs a "shallow copy" of this
     * object, not a "deep copy" operation.
     *
     * @return a deep clone of this instance.
     * @exception CloneNotSupportedException
     *                if the object's class does not support the
     *                {@code Cloneable} and {@code Serializable} interfaces.
     *                Subclasses that override the {@code clone} method can also
     *                throw this exception to indicate that an instance cannot
     *                be cloned.
     * @see java.lang.Cloneable
     * @see java.io.Serializable
     * @author WFF
     */
    @Override
    public AbstractHtml clone() throws CloneNotSupportedException {
        return deepClone(this);
    }

    /**
     * Creates and returns a deeply cloned copy of this object. The precise
     * meaning of "copy" may depend on the class of the object. The general
     * intent is that, for any object {@code x}, the expression: <blockquote>
     *
     * <pre>
     * x.clone() != x
     * </pre>
     *
     * </blockquote> will be true, and that the expression: <blockquote>
     *
     * <pre>
     * x.clone().getClass() == x.getClass()
     * </pre>
     *
     * </blockquote> will be {@code true}, but these are not absolute
     * requirements. While it is typically the case that: <blockquote>
     *
     * <pre>
     * x.clone().equals(x)
     * </pre>
     *
     * </blockquote> will be {@code true}, this is not an absolute requirement.
     * <p>
     * By convention, the returned object should be obtained by calling
     * {@code super.clone}. If a class and all of its superclasses (except
     * {@code Object}) obey this convention, it will be the case that
     * {@code x.clone().getClass() == x.getClass()}.
     * <p>
     * By convention, the object returned by this method should be independent
     * of this object (which is being cloned). To achieve this independence, it
     * may be necessary to modify one or more fields of the object returned by
     * {@code super.clone} before returning it. Typically, this means copying
     * any mutable objects that comprise the internal "deep structure" of the
     * object being cloned and replacing the references to these objects with
     * references to the copies. If a class contains only primitive fields or
     * references to immutable objects, then it is usually the case that no
     * fields in the object returned by {@code super.clone} need to be modified.
     * <p>
     * The method {@code clone} for class {@code AbstractHtml} performs a
     * specific cloning operation. First, if the class of this object does not
     * implement the interfaces {@code Cloneable} and {@code Serializable}, then
     * a {@code CloneNotSupportedException} is thrown. Note that all arrays are
     * considered to implement the interface {@code Cloneable} and that the
     * return type of the {@code clone} method of an array type {@code T[]} is
     * {@code T[]} where T is any reference or primitive type. Otherwise, this
     * method creates a new instance of the class of this object and initializes
     * all its fields with exactly the contents of the corresponding fields of
     * this object, as if by assignment; the contents of the fields are not
     * themselves cloned. Thus, this method performs a "shallow copy" of this
     * object, not a "deep copy" operation.
     *
     * @param excludeAttributes
     *            pass the attributes names which need to be excluded from all
     *            tags including their child tags.
     *
     * @return a deep clone of this instance without the given attributes.
     * @exception CloneNotSupportedException
     *                if the object's class does not support the
     *                {@code Cloneable} and {@code Serializable} interfaces.
     *                Subclasses that override the {@code clone} method can also
     *                throw this exception to indicate that an instance cannot
     *                be cloned.
     * @see java.lang.Cloneable
     * @see java.io.Serializable
     * @author WFF
     * @since 2.0.0
     */
    public AbstractHtml clone(final String... excludeAttributes)
            throws CloneNotSupportedException {

        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).readLock();
        try {
            lock.lock();

            final AbstractHtml clonedObject = deepClone(this);

            final Deque<Set<AbstractHtml>> childrenStack = new ArrayDeque<>();
            final Set<AbstractHtml> initialSet = new LinkedHashSet<>(1);
            initialSet.add(clonedObject);
            childrenStack.push(initialSet);

            Set<AbstractHtml> children;
            while ((children = childrenStack.poll()) != null) {

                for (final AbstractHtml child : children) {
                    child.removeAttributes(excludeAttributes);

                    final Set<AbstractHtml> subChildren = child.children;
                    if (subChildren != null && subChildren.size() > 0) {
                        childrenStack.push(subChildren);
                    }
                }
            }

            return clonedObject;

        } finally {
            lock.unlock();
        }
    }

    /**
     * @return the charset
     */
    public Charset getCharset() {
        return charset;
    }

    /**
     * @param charset
     *            the charset to set
     */
    public void setCharset(final Charset charset) {
        this.charset = charset;
    }

    private void initNewSharedObjectInAllNestedTagsAndSetSuperParentNull(
            final AbstractHtml[] removedAbstractHtmls) {
        for (final AbstractHtml abstractHtml : removedAbstractHtmls) {
            initNewSharedObjectInAllNestedTagsAndSetSuperParentNull(
                    abstractHtml);

        }
    }

    private void initNewSharedObjectInAllNestedTagsAndSetSuperParentNull(
            final AbstractHtml abstractHtml) {

        abstractHtml.parent = null;
        abstractHtml.sharedObject = new AbstractHtml5SharedObject(abstractHtml);

        final Deque<Set<AbstractHtml>> removedTagsStack = new ArrayDeque<>();
        final HashSet<AbstractHtml> initialSet = new HashSet<>(1);
        initialSet.add(abstractHtml);
        removedTagsStack.push(initialSet);

        Set<AbstractHtml> stackChildren;
        while ((stackChildren = removedTagsStack.poll()) != null) {

            for (final AbstractHtml stackChild : stackChildren) {

                final DataWffId dataWffId = stackChild.getDataWffId();
                if (dataWffId != null) {
                    sharedObject.getTagByWffId(ACCESS_OBJECT)
                            .remove(dataWffId.getValue());
                }

                stackChild.sharedObject = abstractHtml.sharedObject;

                final Set<AbstractHtml> subChildren = stackChild.children;

                if (subChildren != null && subChildren.size() > 0) {
                    removedTagsStack.push(subChildren);
                }
            }

        }
    }

    /**
     * @return the Wff Binary Message bytes of this tag. It uses default charset
     *         for encoding values.
     * @since 2.0.0
     * @author WFF
     */
    public byte[] toWffBMBytes() {
        return toWffBMBytes(charset.name());
    }

    /**
     * @param charset
     * @return the Wff Binary Message bytes of this tag
     * @since 2.0.0
     * @author WFF
     * @throws InvalidTagException
     */
    public byte[] toWffBMBytes(final String charset) {

        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).readLock();
        try {
            lock.lock();

            final Deque<NameValue> nameValues = new ArrayDeque<>();

            // ArrayDeque give better performance than Stack, LinkedList
            final Deque<Set<AbstractHtml>> childrenStack = new ArrayDeque<>();
            final HashSet<AbstractHtml> initialSet = new HashSet<>(1);
            initialSet.add(this);
            childrenStack.push(initialSet);

            Set<AbstractHtml> children;
            while ((children = childrenStack.poll()) != null) {

                for (final AbstractHtml tag : children) {

                    final String nodeName = tag.getTagName();

                    if (nodeName != null && !nodeName.isEmpty()) {

                        final NameValue nameValue = new NameValue();

                        final byte[] nodeNameBytes = nodeName.getBytes(charset);
                        final byte[][] wffAttributeBytes = AttributeUtil
                                .getWffAttributeBytes(charset, tag.attributes);

                        final int parentWffSlotIndex = tag.parent == null ? -1
                                : tag.parent.wffSlotIndex;
                        nameValue.setName(WffBinaryMessageUtil
                                .getBytesFromInt(parentWffSlotIndex));

                        final byte[][] values = new byte[wffAttributeBytes.length
                                + 1][0];

                        values[0] = nodeNameBytes;

                        System.arraycopy(wffAttributeBytes, 0, values, 1,
                                wffAttributeBytes.length);

                        nameValue.setValues(values);
                        tag.wffSlotIndex = nameValues.size();
                        nameValues.add(nameValue);

                    } else if (!tag.getClosingTag().isEmpty()) {

                        final int parentWffSlotIndex = tag.parent == null ? -1
                                : tag.parent.wffSlotIndex;

                        final NameValue nameValue = new NameValue();

                        // # short for #text
                        final byte[] nodeNameBytes = "#".getBytes(charset);

                        nameValue.setName(WffBinaryMessageUtil
                                .getBytesFromInt(parentWffSlotIndex));

                        final byte[][] values = new byte[2][0];

                        values[0] = nodeNameBytes;
                        values[1] = tag.getClosingTag().getBytes(charset);

                        nameValue.setValues(values);

                        tag.wffSlotIndex = nameValues.size();
                        nameValues.add(nameValue);
                    }

                    final Set<AbstractHtml> subChildren = tag.children;

                    if (subChildren != null && subChildren.size() > 0) {
                        childrenStack.push(subChildren);
                    }

                }

            }

            final NameValue nameValue = nameValues.getFirst();
            nameValue.setName(new byte[] {});

            return WffBinaryMessageUtil.VERSION_1
                    .getWffBinaryMessageBytes(nameValues);
        } catch (final NoSuchElementException e) {
            throw new InvalidTagException(
                    "Not possible to build wff bm bytes on this tag.\nDon't use an empty new NoTag(null, \"\") or new Blank(null, \"\")",
                    e);
        } catch (final UnsupportedEncodingException e) {
            throw new WffRuntimeException(e.getMessage(), e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * @param bmMessageBytes
     * @return the AbstractHtml instance from the given Wff BM bytes. It uses
     *         system default charset.
     * @since 2.0.0
     * @author WFF
     */
    public static AbstractHtml getTagFromWffBMBytes(
            final byte[] bmMessageBytes) {
        return getTagFromWffBMBytes(bmMessageBytes,
                Charset.defaultCharset().name());
    }

    /**
     * @param bmMessageBytes
     * @param charset
     *            of value bytes
     * @return the AbstractHtml instance from the given Wff BM bytes
     * @since 2.0.0
     * @author WFF
     */
    public static AbstractHtml getTagFromWffBMBytes(final byte[] bmMessageBytes,
            final String charset) {

        try {
            final List<NameValue> nameValuesAsList = WffBinaryMessageUtil.VERSION_1
                    .parse(bmMessageBytes);

            final NameValue[] nameValues = nameValuesAsList
                    .toArray(new NameValue[nameValuesAsList.size()]);

            final NameValue superParentNameValue = nameValues[0];
            final byte[][] superParentValues = superParentNameValue.getValues();

            final AbstractHtml[] allTags = new AbstractHtml[nameValues.length];

            AbstractHtml parent = null;

            if (superParentValues[0][0] == '#') {
                parent = new NoTag(null,
                        new String(superParentValues[1], charset));
            } else {
                final String tagName = new String(superParentValues[0],
                        charset);

                final AbstractAttribute[] attributes = new AbstractAttribute[superParentValues.length
                        - 1];

                for (int i = 1; i < superParentValues.length; i++) {
                    final String attrNameValue = new String(
                            superParentValues[i], charset);
                    final int indexOfHash = attrNameValue.indexOf('=');
                    final String attrName = attrNameValue.substring(0,
                            indexOfHash);
                    final String attrValue = attrNameValue
                            .substring(indexOfHash + 1, attrNameValue.length());
                    // CustomAttribute should be replaced with relevant class
                    // later
                    attributes[i - 1] = new CustomAttribute(attrName,
                            attrValue);
                }
                // CustomTag should be replaced with relevant class later
                parent = new CustomTag(tagName, null, attributes);
            }
            allTags[0] = parent;

            for (int i = 1; i < nameValues.length; i++) {

                final NameValue nameValue = nameValues[i];
                final int indexOfParent = WffBinaryMessageUtil
                        .getIntFromOptimizedBytes(nameValue.getName());

                final byte[][] values = nameValue.getValues();

                AbstractHtml child;
                if (values[0][0] == '#') {
                    child = new NoTag(allTags[indexOfParent],
                            new String(values[1], charset));
                } else {
                    final String tagName = new String(values[0], charset);

                    final AbstractAttribute[] attributes = new AbstractAttribute[values.length
                            - 1];

                    for (int j = 1; j < values.length; j++) {
                        final String attrNameValue = new String(values[j],
                                charset);
                        final int indexOfHash = attrNameValue.indexOf('=');
                        final String attrName = attrNameValue.substring(0,
                                indexOfHash);
                        final String attrValue = attrNameValue.substring(
                                indexOfHash + 1, attrNameValue.length());
                        // CustomAttribute should be replaced with relevant
                        // class later
                        attributes[j - 1] = new CustomAttribute(attrName,
                                attrValue);
                    }
                    // CustomTag should be replaced with relevant class later
                    child = new CustomTag(tagName, allTags[indexOfParent],
                            attributes);
                }
                allTags[i] = child;
            }

            return parent;
        } catch (final UnsupportedEncodingException e) {
            throw new WffRuntimeException(e.getMessage(), e);

        }
    }

    private void initDataWffId(final AbstractHtml5SharedObject sharedObject) {
        if (dataWffId == null) {
            synchronized (this) {
                if (dataWffId == null) {
                    final DataWffId newDataWffId = sharedObject
                            .getNewDataWffId(ACCESS_OBJECT);
                    addAttributesLockless(false, newDataWffId);
                    dataWffId = newDataWffId;
                }
            }
        } else {
            throw new WffRuntimeException("dataWffId already exists");
        }
    }

    /**
     * @return the dataWffId
     */
    public DataWffId getDataWffId() {
        return dataWffId;
    }

    /**
     *
     * adds data-wff-id for the tag if doesn't already exist. NB:- this method
     * is ony for internal use.
     *
     * @param dataWffId
     *            the dataWffId to set
     */
    public void setDataWffId(final DataWffId dataWffId) {
        if (this.dataWffId == null) {
            synchronized (this) {
                if (this.dataWffId == null) {
                    addAttributes(false, dataWffId);
                    this.dataWffId = dataWffId;
                }
            }
        } else {
            throw new WffRuntimeException("dataWffId already exists");
        }
    }

    /**
     * Inserts the given tags before this tag. There must be a parent for this
     * method tag.
     *
     * @param abstractHtmls
     *            to insert before this tag
     * @return true if inserted otherwise false.
     * @since 2.1.1
     * @author WFF
     */
    public boolean insertBefore(final AbstractHtml... abstractHtmls) {

        if (parent == null) {
            throw new NoParentException("There must be a parent for this tag.");
        }

        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).writeLock();
        boolean result = false;
        try {
            lock.lock();

            final AbstractHtml[] removedParentChildren = parent.children
                    .toArray(new AbstractHtml[parent.children.size()]);

            result = insertBefore(removedParentChildren, abstractHtmls);
        } finally {
            lock.unlock();
        }
        final PushQueue pushQueue = sharedObject.getPushQueue(ACCESS_OBJECT);
        if (pushQueue != null) {
            pushQueue.push();
        }
        return result;
    }

    /**
     * should be used inside a synchronized block. NB:- It's removing
     * removedParentChildren by parent.children.clear(); in this method.
     *
     * @param removedParentChildren
     *            just pass the parent children, no need to remove it from
     *            parent. It's removing by parent.children.clear();
     * @param abstractHtmls
     * @return true if inserted otherwise false.
     * @since 2.1.6
     * @author WFF
     */
    private boolean insertBefore(final AbstractHtml[] removedParentChildren,
            final AbstractHtml[] abstractHtmls) {

        final int parentChildrenSize = parent.children.size();
        if (parentChildrenSize > 0) {

            final InsertBeforeListener insertBeforeListener = sharedObject
                    .getInsertBeforeListener(ACCESS_OBJECT);

            parent.children.clear();

            final InsertBeforeListener.Event[] events = new InsertBeforeListener.Event[abstractHtmls.length];

            int count = 0;

            for (final AbstractHtml parentChild : removedParentChildren) {

                if (equals(parentChild)) {

                    for (final AbstractHtml abstractHtmlToInsert : abstractHtmls) {

                        final boolean alreadyHasParent = abstractHtmlToInsert.parent != null;

                        if (insertBeforeListener != null) {
                            AbstractHtml previousParent = null;
                            if (abstractHtmlToInsert.parent != null
                                    && abstractHtmlToInsert.parent.sharedObject == sharedObject) {
                                previousParent = abstractHtmlToInsert.parent;
                            }
                            final InsertBeforeListener.Event event = new InsertBeforeListener.Event(
                                    parent, abstractHtmlToInsert, this,
                                    previousParent);
                            events[count] = event;
                            count++;
                        }

                        // if alreadyHasParent = true then it means the
                        // child is
                        // moving from one tag to another.

                        if (alreadyHasParent) {
                            abstractHtmlToInsert.parent.children
                                    .remove(abstractHtmlToInsert);
                        }

                        initSharedObject(abstractHtmlToInsert);

                        abstractHtmlToInsert.parent = parent;

                        parent.children.add(abstractHtmlToInsert);
                    }

                }

                parent.children.add(parentChild);
            }

            if (insertBeforeListener != null) {
                insertBeforeListener.insertedBefore(events);
            }

            return true;

        }
        return false;
    }

    /**
     * Inserts the given tags after this tag. There must be a parent for this
     * method tag. <br>
     * Note : This {@code insertAfter} method might be bit slower (in terms of
     * nano optimization) than {@code insertBefore} method as it internally uses
     * {@code insertBefore} method. This will be improved in the future version.
     *
     * @param abstractHtmls
     *            to insert after this tag
     * @return true if inserted otherwise false.
     * @since 2.1.6
     * @author WFF
     */
    public boolean insertAfter(final AbstractHtml... abstractHtmls) {

        if (parent == null) {
            throw new NoParentException("There must be a parent for this tag.");
        }

        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).writeLock();
        boolean result = false;
        try {
            lock.lock();

            final AbstractHtml[] childrenOfParent = parent.children
                    .toArray(new AbstractHtml[parent.children.size()]);

            for (int i = 0; i < childrenOfParent.length; i++) {

                if (equals(childrenOfParent[i])) {

                    if (i < (childrenOfParent.length - 1)) {

                        return childrenOfParent[i + 1]
                                .insertBefore(childrenOfParent, abstractHtmls);

                    } else {
                        parent.appendChildrenLockless(abstractHtmls);
                    }

                    result = true;
                }

            }

        } finally {
            lock.unlock();
        }
        final PushQueue pushQueue = sharedObject.getPushQueue(ACCESS_OBJECT);
        if (pushQueue != null) {
            pushQueue.push();
        }
        return result;
    }

    /**
     * Loops through all nested children tags (excluding the given tag) of the
     * given tag. The looping is in a random order to gain maximum performance
     * and minimum memory footprint.
     *
     * @param nestedChild
     *            the object of NestedChild from which the
     *            eachChild(AbstractHtml) to be invoked.
     * @param includeParents
     *            true to include the given parent tags in the loop
     * @param parents
     *            the tags from which to loop through.
     *
     * @since 2.1.8
     * @author WFF
     */
    protected static void loopThroughAllNestedChildren(
            final NestedChild nestedChild, final boolean includeParents,
            final AbstractHtml... parents) {

        final Deque<Set<AbstractHtml>> childrenStack = new ArrayDeque<>();

        if (includeParents) {
            final Set<AbstractHtml> parentsSet = new HashSet<>(parents.length);
            Collections.addAll(parentsSet, parents);
            childrenStack.push(parentsSet);
        } else {
            for (final AbstractHtml parent : parents) {
                childrenStack.push(parent.children);
            }
        }

        Set<AbstractHtml> children;

        exit: while ((children = childrenStack.poll()) != null) {

            for (final AbstractHtml eachChild : children) {

                if (!nestedChild.eachChild(eachChild)) {
                    break exit;
                }

                final Set<AbstractHtml> subChildren = eachChild.children;

                if (subChildren != null && subChildren.size() > 0) {
                    childrenStack.push(subChildren);
                }
            }
        }
    }

    /**
     * @param key
     * @param wffBMData
     * @return
     * @since 2.1.8
     * @author WFF
     */
    protected WffBMData addWffData(final String key,
            final WffBMData wffBMData) {
        return AbstractJsObject.addWffData(this, key, wffBMData);
    }

    /**
     * @param key
     * @return
     * @since 2.1.8
     * @author WFF
     */
    protected WffBMData removeWffData(final String key) {
        return AbstractJsObject.removeWffData(this, key);
    }

    /**
     * @param key
     * @return
     * @since 3.0.1
     * @author WFF
     */
    protected WffBMData getWffData(final String key) {
        return AbstractJsObject.getWffData(this, key);
    }

    /**
     * Gets the unmodifiable map of wffObjects which are upserted by
     * {@link TagRepository#upsert(AbstractHtml, String, WffBMObject)} or
     * {@link TagRepository#upsert(AbstractHtml, String, WffBMArray)}.
     * {@code null} checking is required while consuming this map.
     *
     * @return the unmodifiable map of wffObjects. The value may either be an
     *         instance of {@link WffBMObject} or {@link WffBMArray}. This map
     *         may be null if there is no {@code TagRepository#upsert} operation
     *         has been done at least once in the whole life cycle. Otherwise it
     *         may also be empty.
     * @since 2.1.8
     * @author WFF
     */
    public Map<String, WffBMData> getWffObjects() {
        return Collections.unmodifiableMap(wffBMDatas);
    }

    /**
     * Gets the root level tag of this tag.
     *
     * @return the root parent tag or the current tag if there is no parent for
     *         the given tag
     * @since 2.1.11
     * @author WFF
     */
    public AbstractHtml getRootTag() {
        return sharedObject.getRootTag();
    }

    /**
     * Gets the object which is accessible in all of this tag hierarchy. <br>
     * <br>
     * <br>
     * Eg:-
     *
     * <pre>
     * <code>
     * Html html = new Html(null) {{
     *      new Head(this) {{
     *          new TitleTag(this){{
     *              new NoTag(this, "some title");
     *          }};
     *      }};
     *      new Body(this, new Id("one")) {{
     *          new Div(this);
     *      }};
     *  }};
     *
     *  Div div = TagRepository.findOneTagAssignableToTag(Div.class, html);
     *  Head head = TagRepository.findOneTagAssignableToTag(Head.class, html);
     *
     *  Object sharedData = "some object";
     *
     *  div.setSharedData(sharedData);
     *
     *  System.out.println(sharedData == head.getSharedData());
     *
     *  System.out.println(div.getSharedData() == head.getSharedData());
     *
     *  System.out.println(div.getSharedData().equals(head.getSharedData()));
     *
     *  //prints
     *
     *  true
     *  true
     *  true
     *
     * </code>
     * </pre>
     *
     * @return the sharedData object set by setSharedData method. This object is
     *         same across all of this tag hierarchy.
     * @since 2.1.11
     * @author WFF
     */
    public Object getSharedData() {
        return sharedObject.getSharedData();
    }

    /**
     * Sets the object which will be accessible by getSharedData method in all
     * of this tag hierarchy. {@code setData} sets an object for the specific
     * tag but {@code setSharedData} sets an object for all of the tag
     * hierarchy. <br>
     * <br>
     * <br>
     * Eg:-
     *
     * <pre>
     * <code>
     * Html html = new Html(null) {{
     *      new Head(this) {{
     *          new TitleTag(this){{
     *              new NoTag(this, "some title");
     *          }};
     *      }};
     *      new Body(this, new Id("one")) {{
     *          new Div(this);
     *      }};
     *  }};
     *
     *  Div div = TagRepository.findOneTagAssignableToTag(Div.class, html);
     *  Head head = TagRepository.findOneTagAssignableToTag(Head.class, html);
     *
     *  Object sharedData = "some object";
     *
     *  div.setSharedData(sharedData);
     *
     *  System.out.println(sharedData == head.getSharedData());
     *
     *  System.out.println(div.getSharedData() == head.getSharedData());
     *
     *  System.out.println(div.getSharedData().equals(head.getSharedData()));
     *
     *  //prints
     *
     *  true
     *  true
     *  true
     *
     * </code>
     * </pre>
     *
     * @param sharedData
     *            the object to access through all of this tag hierarchy.
     * @since 2.1.11
     * @author WFF
     */
    public void setSharedData(final Object sharedData) {
        sharedObject.setSharedData(sharedData);
    }

    /**
     * @param sharedData
     * @return true if set or false if it already had a value
     * @since 3.0.1
     */
    public boolean setSharedDataIfNull(final Object sharedData) {
        return sharedObject.setSharedDataIfNull(sharedData);
    }

    /**
     * Resets the hierarchy of this tag so that it can be used in another
     * instance of {@code BrowserPage}. If a tag is used under a
     * {@code BrowserPage} instance and the same instance of tag needs to be
     * used in another instance of {@code BrowserPage} then the tag needs to be
     * reset before use otherwise there could be some strange behaviour in the
     * UI. To avoid compromising performance such usage never throws any
     * exception. <br>
     *
     * NB:- Child tag cannot be reset, i.e. this tag should not be a child of
     * another tag.
     *
     * @throws InvalidTagException
     *             if the tag is already used by another tag, i.e. if this tag
     *             has a parent tag.
     * @since 2.1.13
     * @author WFF
     */
    public final void resetHierarchy() throws InvalidTagException {

        if (parent != null) {
            throw new InvalidTagException("Child tag cannot be reset");
        }

        final Lock lock = sharedObject.getLock(ACCESS_OBJECT).writeLock();
        try {
            lock.lock();

            loopThroughAllNestedChildren(child -> {
                child.removeAttributes(false, DataWffId.TAG_NAME);
                return true;
            }, true, this);

        } finally {
            lock.unlock();
        }
    }

    /**
     * NB: it might lead to StackOverflowException if the tag hierarchy is deep.
     *
     * @return stream of all nested children including this parent object.
     * @since 3.0.0
     * @author WFF
     */
    private Stream<AbstractHtml> buildNestedChildrenIncludingParent() {
        return Stream.concat(Stream.of(this), children.stream()
                .flatMap(AbstractHtml::buildNestedChildrenIncludingParent));
    }

    /**
     * NB: it might lead to StackOverflowException if the tag hierarchy is deep.
     *
     * @param parent
     *            the parent object from which the nested children stream to be
     *            built.
     * @return stream of all nested children including the given parent object.
     * @since 2.1.15
     * @author WFF
     */
    protected static Stream<AbstractHtml> getAllNestedChildrenIncludingParent(
            final AbstractHtml parent) {
        return parent.buildNestedChildrenIncludingParent();
    }

    /**
     * @return the read lock object
     * @since 3.0.1
     */
    protected Lock getReadLock() {
        return sharedObject.getLock(ACCESS_OBJECT).readLock();
    }

    /**
     * @return the write lock object
     * @since 3.0.1
     */
    protected Lock getWriteLock() {
        return sharedObject.getLock(ACCESS_OBJECT).writeLock();
    }
}
