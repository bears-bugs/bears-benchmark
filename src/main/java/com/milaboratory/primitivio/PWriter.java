/*
 * Copyright 2015 MiLaboratory.com
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
package com.milaboratory.primitivio;

import java.io.*;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class PWriter implements AutoCloseable {
    protected final PrimitivO output;
    protected final AtomicBoolean closed = new AtomicBoolean(false);

    protected PWriter(String fileName) throws FileNotFoundException {
        this(new BufferedOutputStream(new FileOutputStream(fileName), 32768));
    }

    protected PWriter(File file) throws FileNotFoundException {
        this(new BufferedOutputStream(new FileOutputStream(file), 32768));
    }

    protected PWriter(OutputStream stream) {
        this(new PrimitivO(stream));
    }

    protected PWriter(PrimitivO output) {
        this.output = output;
    }

    protected void beforeClose() {
    }

    @Override
    public void close() {
        if (closed.compareAndSet(false, true)) {
            beforeClose();
            output.close();
        }
    }
}
