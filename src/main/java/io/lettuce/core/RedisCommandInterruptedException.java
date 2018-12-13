/*
 * Copyright 2011-2018 the original author or authors.
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
package io.lettuce.core;

/**
 * Exception thrown when the thread executing a redis command is interrupted.
 *
 * @author Will Glozer
 * @author Mark Paluch
 */
@SuppressWarnings("serial")
public class RedisCommandInterruptedException extends RedisException {

    /**
     * Create a {@code RedisCommandInterruptedException} with the specified nested exception.
     *
     * @param cause the nested exception.
     */
    public RedisCommandInterruptedException(Throwable cause) {
        super("Command interrupted", cause);
    }
}
