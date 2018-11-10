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
package com.webfirmframework.wffweb.server.page;

import java.io.UnsupportedEncodingException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.webfirmframework.wffweb.InvalidTagException;
import com.webfirmframework.wffweb.tag.html.AbstractHtml;
import com.webfirmframework.wffweb.tag.html.html5.attribute.global.DataWffId;
import com.webfirmframework.wffweb.tag.html.listener.InsertBeforeListener;
import com.webfirmframework.wffweb.util.data.NameValue;

/**
 * @author WFF
 * @since 2.1.1
 */
class InsertBeforeListenerImpl implements InsertBeforeListener {

    private static final long serialVersionUID = 1L;

    public static final Logger LOGGER = Logger
            .getLogger(InsertBeforeListenerImpl.class.getName());

    private BrowserPage browserPage;

    private Object accessObject;

    private Map<String, AbstractHtml> tagByWffId;

    @SuppressWarnings("unused")
    private InsertBeforeListenerImpl() {
        throw new AssertionError();
    }

    InsertBeforeListenerImpl(final BrowserPage browserPage,
            final Object accessObject,
            final Map<String, AbstractHtml> tagByWffId) {
        this.browserPage = browserPage;
        this.accessObject = accessObject;
        this.tagByWffId = tagByWffId;

    }

    /**
     * adds to wffid map
     *
     * @param tag
     * @since 2.0.0
     * @author WFF
     */
    private void addInWffIdMap(final AbstractHtml tag) {

        final Deque<Set<AbstractHtml>> childrenStack = new ArrayDeque<>();
        final Set<AbstractHtml> initialSet = new HashSet<>(1);
        initialSet.add(tag);
        childrenStack.push(initialSet);

        Set<AbstractHtml> children;
        while ((children = childrenStack.poll()) != null) {
            for (final AbstractHtml child : children) {

                final DataWffId wffIdAttr = child.getDataWffId();

                if (wffIdAttr != null) {
                    tagByWffId.put(wffIdAttr.getValue(), child);
                }

                final Set<AbstractHtml> subChildren = child
                        .getChildren(accessObject);
                if (subChildren != null && subChildren.size() > 0) {
                    childrenStack.push(subChildren);
                }

            }
        }

    }

    @Override
    public void insertedBefore(final Event... events) {

        //@formatter:off
        // removed all children tags task format :-
        // { "name": task_byte, "values" : [INSERTED_BEFORE_TAG_byte_from_Task_enum]}, { "name": parent_data-wff-id, "values" : [ parent_tag_name, inserted_tag_html, before_tag_name, before_tag_data-wff-id, 1_if_there_was_a_previous_parent ]}
        // { "name": 2, "values" : [[3]]}, { "name":"C55", "values" : ["div", "<span></span>", 1]}
        //@formatter:on

        try {
            final NameValue task = Task.INSERTED_BEFORE_TAG.getTaskNameValue();

            final Deque<NameValue> nameValues = new ArrayDeque<>();
            nameValues.add(task);

            for (final Event event : events) {

                final AbstractHtml parentTag = event.getParentTag();

                final AbstractHtml insertedTag = event.getInsertedTag();

                final AbstractHtml beforeTag = event.getBeforeTag();

                final AbstractHtml previousParentTag = event
                        .getPreviousParentTag();

                final DataWffId dataWffId = parentTag.getDataWffId();

                if (dataWffId != null) {

                    final NameValue nameValue = new NameValue();

                    final byte[][] parentTagNameAndWffId = DataWffIdUtil
                            .getTagNameAndWffId(parentTag);

                    final byte[] parentWffIdBytes = parentTagNameAndWffId[1];

                    nameValue.setName(parentWffIdBytes);

                    final byte[] parentTagName = parentTagNameAndWffId[0];

                    final byte[][] beforeTagNameAndWffId = DataWffIdUtil
                            .getTagNameAndWffId(beforeTag);

                    try {
                        if (previousParentTag != null) {
                            nameValue.setValues(parentTagName,
                                    insertedTag.toWffBMBytes("UTF-8"),
                                    beforeTagNameAndWffId[0],
                                    beforeTagNameAndWffId[1], new byte[] { 1 });
                        } else {
                            nameValue.setValues(parentTagName,
                                    insertedTag.toWffBMBytes("UTF-8"),
                                    beforeTagNameAndWffId[0],
                                    beforeTagNameAndWffId[1]);
                        }
                    } catch (final InvalidTagException e) {
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.log(Level.WARNING,
                                    "Do not append/add an empty NoTag as child tag, eg: new NoTag(null, \"\").\n"
                                            .concat("To make a tag's children as empty then invoke removeAllChildren() method in it."),
                                    e);
                        }
                        continue;
                    }

                    addInWffIdMap(insertedTag);
                    nameValues.add(nameValue);

                } else {
                    LOGGER.severe("Could not find data-wff-id from owner tag");
                }
            }

            browserPage
                    .push(nameValues.toArray(new NameValue[nameValues.size()]));

        } catch (final UnsupportedEncodingException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }

    }

}