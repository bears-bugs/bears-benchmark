/*
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.lettuce.core.masterslave;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import io.lettuce.core.RedisCommandInterruptedException;
import io.lettuce.core.RedisCommandTimeoutException;
import io.lettuce.core.RedisException;

/**
 * Exception handling and utils to operate on.
 *
 * @author Mark Paluch
 */
class Exceptions {

    /**
     * Prepare an unchecked {@link RuntimeException} that will bubble upstream if thrown by an operator.
     *
     * @param t the root cause
     * @return an unchecked exception that should choose bubbling up over error callback path.
     */
    public static RuntimeException bubble(Throwable t) {

        if (t instanceof ExecutionException) {
            return bubble(t.getCause());
        }

        if (t instanceof TimeoutException) {
            return new RedisCommandTimeoutException(t);
        }

        if (t instanceof InterruptedException) {

            Thread.currentThread().interrupt();
            return new RedisCommandInterruptedException(t);
        }

        if (t instanceof RedisException) {
            return (RedisException) t;
        }

        return new RedisException(t);
    }
}
