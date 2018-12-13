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

import io.lettuce.core.protocol.CommandArgs;

/**
 * Argument list builder for the improved Redis <a href="http://redis.io/commands/zadd">ZADD</a> command starting from Redis
 * 3.0.2. Static import the methods from {@link Builder} and call the methods: {@code xx()} or {@code nx()} .
 * <p/>
 * {@link ZAddArgs} is a mutable object and instances should be used only once to avoid shared mutable state.
 *
 * @author Mark Paluch
 */
public class ZAddArgs implements CompositeArgument {

    private boolean nx = false;
    private boolean xx = false;
    private boolean ch = false;

    /**
     * Builder entry points for {@link ScanArgs}.
     */
    public static class Builder {

        /**
         * Utility constructor.
         */
        private Builder() {
        }

        /**
         * Creates new {@link ZAddArgs} and enabling {@literal NX}.
         *
         * @return new {@link ZAddArgs} with {@literal NX} enabled.
         * @see ZAddArgs#nx()
         */
        public static ZAddArgs nx() {
            return new ZAddArgs().nx();
        }

        /**
         * Creates new {@link ZAddArgs} and enabling {@literal XX}.
         *
         * @return new {@link ZAddArgs} with {@literal XX} enabled.
         * @see ZAddArgs#xx()
         */
        public static ZAddArgs xx() {
            return new ZAddArgs().xx();
        }

        /**
         * Creates new {@link ZAddArgs} and enabling {@literal CH}.
         *
         * @return new {@link ZAddArgs} with {@literal CH} enabled.
         * @see ZAddArgs#ch()
         */
        public static ZAddArgs ch() {
            return new ZAddArgs().ch();
        }
    }

    /**
     * Don't update already existing elements. Always add new elements.
     *
     * @return {@code this} {@link ZAddArgs}.
     */
    public ZAddArgs nx() {

        this.nx = true;
        return this;
    }

    /**
     * Only update elements that already exist. Never add elements.
     *
     * @return {@code this} {@link ZAddArgs}.
     */
    public ZAddArgs xx() {

        this.xx = true;
        return this;
    }

    /**
     * Modify the return value from the number of new elements added, to the total number of elements changed.
     *
     * @return {@code this} {@link ZAddArgs}.
     */
    public ZAddArgs ch() {

        this.ch = true;
        return this;
    }

    public <K, V> void build(CommandArgs<K, V> args) {

        if (nx) {
            args.add("NX");
        }

        if (xx) {
            args.add("XX");
        }

        if (ch) {
            args.add("CH");
        }
    }
}
