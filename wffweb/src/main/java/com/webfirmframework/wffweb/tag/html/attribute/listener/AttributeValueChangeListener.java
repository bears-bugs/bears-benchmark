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
package com.webfirmframework.wffweb.tag.html.attribute.listener;

import java.io.Serializable;
import java.util.Set;

import com.webfirmframework.wffweb.tag.html.AbstractHtml;
import com.webfirmframework.wffweb.tag.html.attribute.core.AbstractAttribute;

public interface AttributeValueChangeListener extends Serializable {

    public static class Event {

        private AbstractAttribute sourceAttribute;

        private Set<AbstractHtml> ownerTags;

        private boolean changedByClient;

        public Event() {
        }

        public Event(final AbstractAttribute sourceAttribute,
                final Set<AbstractHtml> ownerTags) {
            this.sourceAttribute = sourceAttribute;
            this.ownerTags = ownerTags;
        }

        public Event(final AbstractAttribute sourceAttribute,
                final Set<AbstractHtml> ownerTags,
                final boolean changedByClient) {
            this.sourceAttribute = sourceAttribute;
            this.ownerTags = ownerTags;
            this.changedByClient = changedByClient;
        }

        /**
         * @return the sourceAttribute
         */
        public AbstractAttribute getSourceAttribute() {
            return sourceAttribute;
        }

        /**
         * @param sourceAttribute
         *            the sourceAttribute to set
         */
        public void setSourceAttribute(
                final AbstractAttribute sourceAttribute) {
            this.sourceAttribute = sourceAttribute;
        }

        /**
         * @return the ownerTags
         */
        public Set<AbstractHtml> getOwnerTags() {
            return ownerTags;
        }

        /**
         * @param ownerTags
         *            the ownerTags to set
         */
        public void setOwnerTags(final Set<AbstractHtml> ownerTags) {
            this.ownerTags = ownerTags;
        }

        /**
         * @return the changedByClient
         */
        public boolean isChangedByClient() {
            return changedByClient;
        }

        /**
         * @param changedByClient
         *            the changedByClient to set
         */
        public void setChangedByClient(final boolean changedByClient) {
            this.changedByClient = changedByClient;
        }
    }

    public void valueChanged(Event event);

}
