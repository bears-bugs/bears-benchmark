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

import java.util.logging.Level;
import java.util.logging.Logger;

import com.webfirmframework.wffweb.tag.html.AbstractHtml;
import com.webfirmframework.wffweb.tag.html.listener.WffBMDataUpdateListener;
import com.webfirmframework.wffweb.util.data.NameValue;
import com.webfirmframework.wffweb.wffbm.data.BMType;
import com.webfirmframework.wffweb.wffbm.data.WffBMData;

/**
 * @author WFF
 * @since 2.1.8
 */
class WffBMDataUpdateListenerImpl implements WffBMDataUpdateListener {

    private static final long serialVersionUID = 1L;

    public static final Logger LOGGER = Logger
            .getLogger(WffBMDataUpdateListenerImpl.class.getName());

    private BrowserPage browserPage;

    @SuppressWarnings("unused")
    private WffBMDataUpdateListenerImpl() {
        throw new AssertionError();
    }

    WffBMDataUpdateListenerImpl(final BrowserPage browserPage) {
        this.browserPage = browserPage;
    }

    @Override
    public void updatedWffData(final UpdateEvent event) {

        try {

            //@formatter:off
            // SET_BM_OBJ_ON_TAG/SET_BM_ARR_ON_TAG task format :-
            // { "name": task_byte, "values" : [SET_BM_OBJ_ON_TAG/SET_BM_ARR_ON_TAG_byte_from_Task_enum]},
            // { "name": [tag name bytes], "values" : [[wff id bytes], [key bytes], [bm bytes] }
            //@formatter:on

            final AbstractHtml tag = event.getTag();
            final WffBMData wffBMData = event.getWffData();
            final NameValue task;

            if (BMType.OBJECT.equals(wffBMData.getBMType())) {
                task = Task.SET_BM_OBJ_ON_TAG.getTaskNameValue();
            } else if (BMType.ARRAY.equals(wffBMData.getBMType())) {
                task = Task.SET_BM_ARR_ON_TAG.getTaskNameValue();
            } else {
                return;
            }

            final NameValue nameValue = new NameValue();

            final NameValue[] nameValues = { task, nameValue };

            nameValue.setName(tag.getTagName().getBytes("UTF-8"));

            final byte[] dataWffIdBytes = DataWffIdUtil
                    .getDataWffIdBytes(tag.getDataWffId().getValue());

            nameValue.setValues(new byte[][] { dataWffIdBytes,
                    event.getKey().getBytes("UTF-8"), wffBMData.build(true) });

            browserPage.push(nameValues);

        } catch (final Exception e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }

    }

}