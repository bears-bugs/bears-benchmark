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
package com.webfirmframework.wffweb.js;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Utility methods to generate JavaScript code.
 *
 * @author WFF
 * @since 2.1.1
 */
public final class JsUtil {

    private JsUtil() {
        throw new AssertionError();
    }

    /**
     * @param jsKeyAndElementId
     *            The map containing key values. The key in the map will be used
     *            as the key in the generated js object. The value in the map
     *            should be the id of the field.
     * @return the JavaScript object for the fields value. Sample :
     *         <code>{username:document.getElementById('uId').value}</code>
     * @since 2.1.1
     * @author WFF
     */
    public static String getJsObjectForFieldsValue(
            final Map<String, Object> jsKeyAndElementId) {

        final StringBuilder builder = new StringBuilder(38);

        builder.append('{');

        for (final Entry<String, Object> entry : jsKeyAndElementId.entrySet()) {

            builder.append(entry.getKey()).append(":document.getElementById('")
                    .append(entry.getValue().toString()).append("').value,");

        }

        builder.replace(builder.length() - 1, builder.length(), "}");

        return builder.toString();
    }

    /**
     * @param ids
     *            The set containing element ids. The id will be used as the key
     *            in the generated js object. The value in the set should be the
     *            id of the field. The id in the set should be a valid
     *            JavaScript object key.
     * @return the JavaScript object for the fields value. Sample :
     *         <code>{uId:document.getElementById('uId').value}</code>
     * @since 2.1.1
     * @author WFF
     */
    public static String getJsObjectForFieldsValue(final Set<Object> ids) {

        final StringBuilder builder = new StringBuilder(75);

        builder.append('{');

        for (final Object id : ids) {

            builder.append(id.toString()).append(":document.getElementById('")
                    .append(id.toString()).append("').value,");

        }

        builder.replace(builder.length() - 1, builder.length(), "}");

        return builder.toString();
    }

    /**
     * @param ids
     *            The string array containing element ids. The id will be used
     *            as the key in the generated js object. The value in the array
     *            should be the id of the field. The id in the array should be a
     *            valid JavaScript object key.
     * @return the JavaScript object for the fields value. Sample :
     *         <code>{uId:document.getElementById('uId'.value)}</code>
     * @since 2.1.3
     * @author WFF
     */
    public static String getJsObjectForFieldsValue(final String... ids) {

        final StringBuilder builder = new StringBuilder(38);

        builder.append('{');

        for (final Object id : ids) {

            builder.append(id.toString()).append(":document.getElementById('")
                    .append(id.toString()).append("').value,");

        }

        builder.replace(builder.length() - 1, builder.length(), "}");

        return builder.toString();
    }

    /**
     * @param inputIds
     *            input field ids such as input type text etc..
     * @param checkboxIds
     *            checkbox field ids such as input type checkbox
     * @return the JavaScript object for the fields value. Sample :
     *         <code>{usernameInputId:document.getElementById('usernameInputId').value,dateExpiredCheckboxId:document.getElementById('dateExpiredCheckboxId').checked}</code>
     * @since 3.0.1
     */
    public static String getJsObjectForFieldsValue(final Set<Object> inputIds,
            final Set<Object> checkboxIds) {

        final StringBuilder builder = new StringBuilder(75);

        builder.append('{');

        for (final Object id : inputIds) {

            builder.append(id.toString()).append(":document.getElementById('")
                    .append(id.toString()).append("').value,");

        }

        for (final Object id : checkboxIds) {

            builder.append(id.toString()).append(":document.getElementById('")
                    .append(id.toString()).append("').checked,");

        }

        builder.replace(builder.length() - 1, builder.length(), "}");

        return builder.toString();
    }

}
