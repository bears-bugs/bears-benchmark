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
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.webfirmframework.wffweb.tag.html.AbstractHtml;
import com.webfirmframework.wffweb.tag.html.attribute.listener.AttributeValueChangeListener;
import com.webfirmframework.wffweb.tag.html.html5.attribute.global.DataWffId;
import com.webfirmframework.wffweb.util.data.NameValue;

class AttributeValueChangeListenerImpl implements AttributeValueChangeListener {

    private static final long serialVersionUID = 1L;

    public static final Logger LOGGER = Logger
            .getLogger(AttributeValueChangeListenerImpl.class.getName());

    private BrowserPage browserPage;

    private Map<String, AbstractHtml> tagByWffId;

    @SuppressWarnings("unused")
    private AttributeValueChangeListenerImpl() {
        throw new AssertionError();
    }

    AttributeValueChangeListenerImpl(final BrowserPage browserPage,
            final Map<String, AbstractHtml> tagByWffId) {
        this.browserPage = browserPage;
        this.tagByWffId = tagByWffId;
    }

    @Override
    public void valueChanged(final Event event) {

        // in this listener, pushing value change of attribute to the
        // client
        try {
            //@formatter:off
            // update attribute task format :-
            // { "name": task_byte, "values" : [update_attribute_byte_from_Task_enum]}, { "name": attribute_name, "values" : [ data-wff-id, data-wff-id ]}
            // { "name": 2, "values" : [[1]]}, { "name":"style=color:green", "values" : ["C55", "S555"]}
            //@formatter:on

            final NameValue task = Task.ATTRIBUTE_UPDATED.getTaskNameValue();
            final NameValue nameValue = new NameValue();

            // should be name=somevalue
            String attrNameValue = event.getSourceAttribute()
                    .toHtmlString("UTF-8").replaceFirst("[=][\"]", "=");

            if (attrNameValue.charAt(attrNameValue.length() - 1) == '"') {
                attrNameValue = attrNameValue.substring(0,
                        attrNameValue.length() - 1);
            }

            nameValue.setName(attrNameValue.getBytes("UTF-8"));

            final Set<AbstractHtml> ownerTags = new HashSet<>(
                    event.getOwnerTags());

            final Collection<AbstractHtml> uiTags = tagByWffId.values();
            if (uiTags.size() > 10) {
                // to remove ownerTags which don't exist in ui
                // to improve performance HashSet object passed as an argument
                ownerTags.retainAll(new HashSet<>(uiTags));
            } else {
                ownerTags.retainAll(uiTags);
            }

            // for (AbstractHtml ownerTag : event.getOwnerTags()) {
            // AbstractAttribute dataWffIdAttr = ownerTag
            // .getAttributeByName("data-wff-id");
            //
            // if (dataWffIdAttr == null || !tagByWffId
            // .containsKey(dataWffIdAttr.getAttributeValue())) {
            // ownerTags.remove(ownerTag);
            // }
            // }

            final byte[][] dataWffIds = new byte[ownerTags.size()][0];

            int count = 0;

            for (final AbstractHtml owner : ownerTags) {

                final DataWffId dataWffId = owner.getDataWffId();

                if (dataWffId != null) {

                    final byte[] dataWffIdBytes = DataWffIdUtil
                            .getDataWffIdBytes(dataWffId.getValue());

                    dataWffIds[count] = dataWffIdBytes;
                    count++;

                } else {
                    LOGGER.severe("Could not find data-wff-id from owner tag");
                }
            }

            byte[][] values = dataWffIds;

            if (ownerTags.size() > count) {
                values = new byte[count][0];
                System.arraycopy(dataWffIds, 0, values, 0, values.length);
            }

            nameValue.setValues(values);

            browserPage.push(task, nameValue);
        } catch (final UnsupportedEncodingException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }

}
