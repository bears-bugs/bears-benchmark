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
package com.milaboratory.core.io.sequence;

/**
 * Created by dbolotin on 23/06/14.
 */
public class PairedReader extends AbstractMultiReader<PairedRead> {
    public PairedReader(SingleReader... readers) {
        super(readers);
    }

    @Override
    public PairedRead take() {
        SingleRead[] singleReads = takeReads();

        if (singleReads == null)
            return null;

        return new PairedRead(singleReads);
    }
}
