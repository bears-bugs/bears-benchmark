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

import cc.redberry.pipe.OutputPortCloseable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PipeReader<O> extends PReader implements OutputPortCloseable<O> {
    final Class type;

    public PipeReader(Class<? super O> type, String fileName) throws IOException {
        super(fileName);
        this.type = type;
    }

    public PipeReader(Class<? super O> type, File file) throws IOException {
        super(file);
        this.type = type;
    }

    private PipeReader(Class<? super O> type, FileInputStream stream) throws IOException {
        super(stream);
        this.type = type;
    }

    public PipeReader(Class<? super O> type, InputStream stream) {
        super(stream);
        this.type = type;
    }

    @Override
    public synchronized O take() {
        if (closed.get())
            return null;

        return (O) input.readObject(type);
    }
}