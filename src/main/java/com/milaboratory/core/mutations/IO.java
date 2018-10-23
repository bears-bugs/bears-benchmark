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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.milaboratory.core.sequence.Alphabet;
import com.milaboratory.core.sequence.Alphabets;
import com.milaboratory.core.sequence.AminoAcidSequence;
import com.milaboratory.core.sequence.NucleotideSequence;
import com.milaboratory.primitivio.PrimitivI;
import com.milaboratory.primitivio.PrimitivO;
import com.milaboratory.primitivio.Serializer;

import java.io.IOException;

class IO {
    private IO() {
    }

    public static class MutationsSerializer implements Serializer<Mutations> {
        @Override
        public void write(PrimitivO output, Mutations object) {
            output.writeObject(object.alphabet);
            output.writeObject(object.mutations);
        }

        @Override
        public Mutations read(PrimitivI input) {
            Alphabet alphabet = input.readObject(Alphabet.class);
            return new Mutations(alphabet, input.readObject(int[].class), true);
        }

        @Override
        public boolean isReference() {
            return false;
        }

        @Override
        public boolean handlesReference() {
            return false;
        }
    }

    public static final class JsonMutationsDeserializer extends JsonDeserializer<Mutations> implements ContextualDeserializer {
        @Override
        @SuppressWarnings("unchecked")
        public Mutations deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String val = p.getValueAsString();
            Alphabet alphabet = Alphabets.getByFirstLetterOfName(val.charAt(0));
            return new Mutations(alphabet, val.substring(1));
        }

        @Override
        public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
            if (ctxt.getContextualType().getBindings().getTypeParameters().size() == 0)
                return this;
            Class<?> seqClass = ctxt.getContextualType().getBindings().getTypeParameters().get(0).getRawClass();
            if (seqClass == NucleotideSequence.class)
                return N_MUTATIONS_DESERIALIZER;
            if (seqClass == AminoAcidSequence.class)
                return AA_MUTATIONS_DESERIALIZER;
            return this;
        }
    }

    public static final class JsonMutationsSerializer extends JsonSerializer<Mutations> implements ContextualSerializer {
        @Override
        public void serialize(Mutations value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
            gen.writeString(Character.toUpperCase(value.getAlphabet().getAlphabetName().charAt(0)) + value.encode());
        }

        @Override
        public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
            if (property == null)
                return this;
            Class<?> seqClass = property.getType().getBindings().getTypeParameters().get(0).getRawClass();
            if (seqClass == NucleotideSequence.class)
                return N_MUTATIONS_SERIALIZER;
            if (seqClass == AminoAcidSequence.class)
                return AA_MUTATIONS_SERIALIZER;
            return this;
        }
    }

    private static final NMutationsDeserializer N_MUTATIONS_DESERIALIZER = new NMutationsDeserializer();
    private static final NMutationsSerializer N_MUTATIONS_SERIALIZER = new NMutationsSerializer();
    private static final AAMutationsDeserializer AA_MUTATIONS_DESERIALIZER = new AAMutationsDeserializer();
    private static final AAMutationsSerializer AA_MUTATIONS_SERIALIZER = new AAMutationsSerializer();

    private static final class NMutationsDeserializer extends JsonDeserializer<Mutations<NucleotideSequence>> {
        @Override
        public Mutations<NucleotideSequence> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            String val = jp.getValueAsString();
            if (val.charAt(0) == 'N' || val.charAt(0) == 'n')
                val = val.substring(1);
            return Mutations.decode(val, NucleotideSequence.ALPHABET);
        }
    }

    private static final class NMutationsSerializer extends JsonSerializer<Mutations<NucleotideSequence>> {
        @Override
        public void serialize(Mutations<NucleotideSequence> value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeString(value.encode());
        }
    }

    private static final class AAMutationsDeserializer extends JsonDeserializer<Mutations<AminoAcidSequence>> {
        @Override
        public Mutations<AminoAcidSequence> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            String val = jp.getValueAsString();
            if (val.charAt(0) == 'A' || val.charAt(0) == 'a')
                val = val.substring(1);
            return Mutations.decode(val, AminoAcidSequence.ALPHABET);
        }
    }

    private static final class AAMutationsSerializer extends JsonSerializer<Mutations<AminoAcidSequence>> {
        @Override
        public void serialize(Mutations<AminoAcidSequence> value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeString(value.encode());
        }
    }
}
