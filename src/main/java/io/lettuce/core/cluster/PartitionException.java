/*
 * Copyright 2018 the original author or authors.
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
package io.lettuce.core.cluster;

import io.lettuce.core.RedisException;

/**
 * Partition access exception thrown when a partition-specific operations fails.
 *
 * @author Mark Paluch
 * @since 5.1
 */
@SuppressWarnings("serial")
public class PartitionException extends RedisException {

    /**
     * Create a {@code PartitionException} with the specified detail message.
     *
     * @param msg the detail message.
     */
    public PartitionException(String msg) {
        super(msg);
    }

    /**
     * Create a {@code PartitionException} with the specified detail message and nested exception.
     *
     * @param msg the detail message.
     * @param cause the nested exception.
     */
    public PartitionException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Create a {@code PartitionException} with the specified nested exception.
     *
     * @param cause the nested exception.
     */
    public PartitionException(Throwable cause) {
        super(cause);
    }
}
