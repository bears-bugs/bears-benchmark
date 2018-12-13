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
package io.lettuce.core.protocol;

import java.nio.ByteBuffer;

import io.lettuce.core.protocol.CommandArgs.CharArrayArgument;
import io.lettuce.core.protocol.CommandArgs.SingularArgument;
import io.lettuce.core.protocol.CommandArgs.StringArgument;

/**
 * Accessor for first encoded key, first string and first {@link Long integer} argument of {@link CommandArgs}. This class is
 * part of the internal API and may change without further notice.
 *
 * @author Mark Paluch
 * @since 4.4
 */
public class CommandArgsAccessor {

    /**
     * Get the first encoded key for cluster command routing.
     *
     * @param commandArgs must not be null.
     * @return the first encoded key or {@literal null}.
     */
    @SuppressWarnings("unchecked")
    public static <K, V> ByteBuffer encodeFirstKey(CommandArgs<K, V> commandArgs) {

        for (SingularArgument singularArgument : commandArgs.singularArguments) {

            if (singularArgument instanceof CommandArgs.KeyArgument) {
                return commandArgs.codec.encodeKey(((CommandArgs.KeyArgument<K, V>) singularArgument).key);
            }
        }

        return null;
    }

    /**
     * Get the first {@link String} argument.
     *
     * @param commandArgs must not be null.
     * @return the first {@link String} argument or {@literal null}.
     */
    @SuppressWarnings("unchecked")
    public static <K, V> String getFirstString(CommandArgs<K, V> commandArgs) {

        for (SingularArgument singularArgument : commandArgs.singularArguments) {

            if (singularArgument instanceof StringArgument) {
                return ((StringArgument) singularArgument).val;
            }
        }

        return null;
    }

    /**
     * Get the first {@link char}-array argument.
     *
     * @param commandArgs must not be null.
     * @return the first {@link String} argument or {@literal null}.
     */
    @SuppressWarnings("unchecked")
    public static <K, V> char[] getFirstCharArray(CommandArgs<K, V> commandArgs) {

        for (SingularArgument singularArgument : commandArgs.singularArguments) {

            if (singularArgument instanceof CharArrayArgument) {
                return ((CharArrayArgument) singularArgument).val;
            }
        }

        return null;
    }

    /**
     * Get the first {@link Long integer} argument.
     *
     * @param commandArgs must not be null.
     * @return the first {@link Long integer} argument or {@literal null}.
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Long getFirstInteger(CommandArgs<K, V> commandArgs) {

        for (SingularArgument singularArgument : commandArgs.singularArguments) {

            if (singularArgument instanceof CommandArgs.IntegerArgument) {
                return ((CommandArgs.IntegerArgument) singularArgument).val;
            }
        }

        return null;
    }
}
