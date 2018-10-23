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
package com.milaboratory.core.sequence;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import gnu.trove.impl.Constants;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Registry of all alphabets.
 */
public final class Alphabets {
    private final static Map<String, Alphabet> alphabetsByName = new HashMap<>();
    private final static TIntObjectHashMap<Alphabet> alphabetsById = new TIntObjectHashMap<>(Constants.DEFAULT_CAPACITY,
            Constants.DEFAULT_LOAD_FACTOR, Byte.MIN_VALUE);

    private Alphabets() {
    }

    /**
     * Register new alphabet
     *
     * @param alphabet alphabet
     */
    public static void register(Alphabet alphabet) {
        if (alphabetsByName.put(alphabet.getAlphabetName(), alphabet) != null)
            throw new IllegalStateException("Alphabet with this name is already registered.");

        if (alphabetsById.put(alphabet.getId(), alphabet) != null)
            throw new IllegalStateException("Alphabet with this id is already registered.");
    }

    static {
        register(NucleotideAlphabet.INSTANCE);
        register(AminoAcidAlphabet.INSTANCE);
    }

    /**
     * Returns instance of {@code Alphabet} from its string name.
     *
     * @param name string name of alphabet
     * @return instance of {@code Alphabet} from its string name
     */
    public static Alphabet getByName(String name) {
        return alphabetsByName.get(name);
    }

    /**
     * Searches for instance of {@code Alphabet} using first letter of it's name
     *
     * @param letter first letter of alphabet's name
     * @return instance of {@code Alphabet}
     */
    public static Alphabet getByFirstLetterOfName(char letter) {
        letter = Character.toLowerCase(letter);
        for (Alphabet alphabet : alphabetsByName.values())
            if (alphabet.getAlphabetName().charAt(0) == letter)
                return alphabet;
        throw new IllegalArgumentException("No alphabet starting with letter: " + letter);
    }

    /**
     * Returns instance of {@code Alphabet} from its byte id.
     *
     * @param id byte id of alphabet
     * @return instance of {@code Alphabet} from its byte id
     */
    public static Alphabet getById(byte id) {
        return alphabetsById.get(id);
    }

    /**
     * Returns unmodifiable collection of all registered alphabets.
     *
     * @return unmodifiable collection of all registered alphabets
     */
    public static Collection<Alphabet> getAll() {
        return Collections.unmodifiableCollection(alphabetsByName.values());
    }

    /* JSON serializers / deserializers */

    public static final class Deserializer extends JsonDeserializer<Alphabet> {
        @Override
        public Alphabet deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return Alphabets.getByName(jp.readValueAs(String.class));
        }

        @Override
        public Alphabet getEmptyValue(DeserializationContext context) {
            return NucleotideAlphabet.INSTANCE;
        }

        @Override
        public Alphabet getNullValue(DeserializationContext context) {
            return NucleotideAlphabet.INSTANCE;
        }
    }

    public static final class Serializer extends JsonSerializer<Alphabet> {
        @Override
        public void serialize(Alphabet value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeString(value.getAlphabetName());
        }
    }
}
