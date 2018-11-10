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
package com.webfirmframework.wffweb;

/**
 *
 * @author WFF
 * @since 1.0.0
 */
public class InvalidValueException extends WffRuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1_0_0L;

    public InvalidValueException() {
        super();
    }

    public InvalidValueException(final String message, final Throwable cause,
            final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public InvalidValueException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidValueException(final String message) {
        super(message);
    }

    public InvalidValueException(final Throwable cause) {
        super(cause);
    }

}
