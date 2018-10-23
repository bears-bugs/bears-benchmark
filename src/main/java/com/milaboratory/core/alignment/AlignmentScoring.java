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
package com.milaboratory.core.alignment;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.milaboratory.core.sequence.Alphabet;
import com.milaboratory.core.sequence.Sequence;
import com.milaboratory.primitivio.annotations.Serializable;

/**
 * AlignmentScoring - interface which is to be implemented by any scoring system
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({@JsonSubTypes.Type(value = LinearGapAlignmentScoring.class, name = "linear"),
        @JsonSubTypes.Type(value = AffineGapAlignmentScoring.class, name = "affine")})
@Serializable(asJson = true)
public interface AlignmentScoring<S extends Sequence<S>> extends java.io.Serializable {
    int getScore(byte from, byte to);

    Alphabet<S> getAlphabet();

    int getMinimalMatchScore();

    int getMaximalMatchScore();

    int getMinimalMismatchScore();

    int getMaximalMismatchScore();
}
