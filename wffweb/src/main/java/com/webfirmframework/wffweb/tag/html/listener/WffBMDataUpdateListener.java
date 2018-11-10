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
import com.webfirmframework.wffweb.wffbm.data.WffBMData;

public interface WffBMDataUpdateListener extends Serializable {

    public static final class UpdateEvent {

        private final String key;

        private final WffBMData wffBMData;

        private final AbstractHtml tag;

        public UpdateEvent(final AbstractHtml tag, final String key,
                final WffBMData wffBMData) {
            super();
            this.tag = tag;
            this.key = key;
            this.wffBMData = wffBMData;
        }

        public String getKey() {
            return key;
        }

        public WffBMData getWffData() {
            return wffBMData;
        }

        public AbstractHtml getTag() {
            return tag;
        }

    }

    public void updatedWffData(UpdateEvent event);

}
