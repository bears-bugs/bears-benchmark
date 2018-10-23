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

import cc.redberry.pipe.InputPort;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;

public class PipeWriter<O> extends PWriter implements InputPort<O>, AutoCloseable {
    public PipeWriter(String fileName) throws FileNotFoundException {
        super(fileName);
    }

    public PipeWriter(File file) throws FileNotFoundException {
        super(file);
    }

    public PipeWriter(OutputStream stream) {
        super(stream);
    }

    @Override
    public synchronized void put(O o) {
        if (o == null){
            close();
            return;
        }

        if (closed.get())
            throw new IllegalStateException("Already closed.");

        output.writeObject(o);
    }

    @Override
    protected void beforeClose() {
        output.writeObject(null);
    }
}