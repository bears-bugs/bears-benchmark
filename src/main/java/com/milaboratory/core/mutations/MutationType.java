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
package com.milaboratory.core.mutations;

import static com.milaboratory.core.mutations.Mutation.*;

public enum MutationType {
    Substitution(RAW_MUTATION_TYPE_SUBSTITUTION),
    Deletion(RAW_MUTATION_TYPE_DELETION),
    Insertion(RAW_MUTATION_TYPE_INSERTION);
    public final int rawType;

    private MutationType(int rawType) {
        this.rawType = rawType;
    }

    private static MutationType[] types = new MutationType[4];

    static {
        for (MutationType mutationType : values())
            types[mutationType.rawType >> MUTATION_TYPE_OFFSET] = mutationType;
    }

    public static MutationType getTypeFromRaw(int rawType) {
        return types[rawType >> MUTATION_TYPE_OFFSET];
    }

    /**
     * Returns {@link #Substitution} for 0, {@link #Deletion} for 1 and {@link #Insertion} for 2.
     *
     * @param type int type
     * @return {@link #Substitution} for 0, {@link #Deletion} for 1 and {@link #Insertion} for 2.
     */
    public static MutationType getType(int type) {
        return types[type + 1];
    }
}
