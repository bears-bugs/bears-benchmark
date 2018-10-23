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
package com.milaboratory.core.alignment.batch;

import com.milaboratory.core.alignment.Alignment;
import com.milaboratory.core.sequence.Sequence;

/**
 * @param <S> sequence type
 * @param <P> type of record payload, used to identify particular sequence from base that query was aligned with
 */
public interface AlignmentHit<S extends Sequence<S>, P> {
    Alignment<S> getAlignment();

    P getRecordPayload();
}
