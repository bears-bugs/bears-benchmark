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
package com.webfirmframework.wffweb.tag.html.listener;

import java.io.Serializable;
import java.util.Collection;

import com.webfirmframework.wffweb.tag.html.AbstractHtml;

public interface ChildTagAppendListener extends Serializable {

    public static class Event {

        private AbstractHtml appendedChildTag;

        private Collection<? extends AbstractHtml> appendedChildrenTags;

        private AbstractHtml parentTag;

        @SuppressWarnings("unused")
        private Event() {
            throw new AssertionError();
        }

        public Event(final AbstractHtml parentTag,
                final AbstractHtml appendedChildTag) {
            this.parentTag = parentTag;
            this.appendedChildTag = appendedChildTag;
        }

        public Event(final AbstractHtml parentTag,
                final Collection<? extends AbstractHtml> appendedChildrenTags) {
            this.parentTag = parentTag;
            this.appendedChildrenTags = appendedChildrenTags;
        }

        /**
         * @return the appendedChildTag
         */
        public AbstractHtml getAppendedChildTag() {
            return appendedChildTag;
        }

        /**
         * @param appendedChildTag
         *            the appendedChildTag to set
         */
        public void setAppendedChildTag(final AbstractHtml appendedChildTag) {
            this.appendedChildTag = appendedChildTag;
        }

        /**
         * @return the parentTag
         */
        public AbstractHtml getParentTag() {
            return parentTag;
        }

        /**
         * @param parentTag
         *            the parentTag to set
         */
        public void setParentTag(final AbstractHtml parentTag) {
            this.parentTag = parentTag;
        }

        /**
         * @return the appendedChildrenTags
         */
        public Collection<? extends AbstractHtml> getAppendedChildrenTags() {
            return appendedChildrenTags;
        }

        /**
         * @param appendedChildrenTags
         *            the appendedChildrenTags to set
         */
        public void setAppendedChildrenTags(
                final Collection<AbstractHtml> appendedChildrenTags) {
            this.appendedChildrenTags = appendedChildrenTags;
        }

    }

    public static class ChildMovedEvent {

        private AbstractHtml previousParentTag;

        private AbstractHtml movedChildTag;

        private AbstractHtml currentParentTag;

        @SuppressWarnings("unused")
        private ChildMovedEvent() {
            throw new AssertionError();
        }

        public ChildMovedEvent(final AbstractHtml previousParentTag,
                final AbstractHtml currentParentTag,
                final AbstractHtml movedChildTag) {
            super();
            this.previousParentTag = previousParentTag;
            this.currentParentTag = currentParentTag;
            this.movedChildTag = movedChildTag;
        }

        public AbstractHtml getPreviousParentTag() {
            return previousParentTag;
        }

        public void setPreviousParentTag(final AbstractHtml previousParentTag) {
            this.previousParentTag = previousParentTag;
        }

        public AbstractHtml getMovedChildTag() {
            return movedChildTag;
        }

        public void setMovedChildTag(final AbstractHtml movedChildTag) {
            this.movedChildTag = movedChildTag;
        }

        public AbstractHtml getCurrentParentTag() {
            return currentParentTag;
        }

        public void setCurrentParentTag(final AbstractHtml currentParentTag) {
            this.currentParentTag = currentParentTag;
        }

    }

    public void childAppended(Event event);

    public void childrenAppended(Event event);

    /**
     * child removed from another tag and appended to this tag
     *
     * @param event
     * @since 2.0.0
     * @author WFF
     */
    public void childMoved(ChildMovedEvent event);

    public void childrendAppendedOrMoved(Collection<ChildMovedEvent> events);

}
