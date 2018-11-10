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

import com.webfirmframework.wffweb.tag.html.AbstractHtml;

public interface InnerHtmlAddListener extends Serializable {

    public static class Event {

        private AbstractHtml innerHtmlTag;

        private AbstractHtml parentTag;

        /**
         * previous parent tag of inner html
         */
        private AbstractHtml previousParentTag;

        @SuppressWarnings("unused")
        private Event() {
            throw new AssertionError();
        }

        public Event(final AbstractHtml parentTag,
                final AbstractHtml innerHtmlTag,
                final AbstractHtml previousParentTag) {
            super();
            this.parentTag = parentTag;
            this.innerHtmlTag = innerHtmlTag;
            this.previousParentTag = previousParentTag;
        }

        public AbstractHtml getInnerHtmlTag() {
            return innerHtmlTag;
        }

        public void setInnerHtmlTag(final AbstractHtml innerHtmlTag) {
            this.innerHtmlTag = innerHtmlTag;
        }

        public AbstractHtml getParentTag() {
            return parentTag;
        }

        public void setParentTag(final AbstractHtml parentTag) {
            this.parentTag = parentTag;
        }

        /**
         * @return the previousParentTag
         */
        public AbstractHtml getPreviousParentTag() {
            return previousParentTag;
        }

        /**
         * @param previousParentTag
         *            the previousParentTag to set
         */
        public void setPreviousParentTag(final AbstractHtml previousParentTag) {
            this.previousParentTag = previousParentTag;
        }

    }

    public void innerHtmlAdded(Event event);

    public void innerHtmlsAdded(AbstractHtml parentTag, Event... events);

}
