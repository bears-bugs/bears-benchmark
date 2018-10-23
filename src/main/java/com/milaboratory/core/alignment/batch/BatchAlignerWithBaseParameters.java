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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.milaboratory.core.alignment.AlignmentScoring;
import com.milaboratory.core.alignment.kaligner1.KAlignerParameters;
import com.milaboratory.core.alignment.kaligner2.KAlignerParameters2;
import com.milaboratory.core.sequence.NucleotideSequence;
import com.milaboratory.primitivio.annotations.Serializable;

/**
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = KAlignerParameters.class, name = "kaligner"),
        @JsonSubTypes.Type(value = KAlignerParameters2.class, name = "kaligner2")
})
@Serializable(asJson = true)
public interface BatchAlignerWithBaseParameters {
    <P> BatchAlignerWithBaseWithFilter<NucleotideSequence, P, ? extends AlignmentHit<NucleotideSequence, P>> createAligner();

    AlignmentScoring<NucleotideSequence> getScoring();

    BatchAlignerWithBaseParameters clone();
}
