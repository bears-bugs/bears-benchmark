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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.milaboratory.primitivio.PrimitivI;
import com.milaboratory.primitivio.PrimitivO;
import com.milaboratory.primitivio.Serializer;

import java.io.IOException;

final class IO {
    private IO() {
    }

    public static class AlphabetSerializer implements Serializer<Alphabet> {
        @Override
        public void write(PrimitivO output, Alphabet object) {
            output.writeByte(object.getId());
        }

        @Override
        public Alphabet read(PrimitivI input) {
            return Alphabets.getById(input.readByte());
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

    public static class SequenceSerializer implements Serializer<Sequence> {
        @Override
        public void write(PrimitivO output, Sequence object) {
            output.writeObject(object.getAlphabet());
            output.writeObject(object.asArray());
        }

        @Override
        public Sequence read(PrimitivI input) {
            Alphabet alphabet = input.readObject(Alphabet.class);
            return alphabet.createBuilder().append(input.readObject(byte[].class)).createAndDestroy();
        }

        @Override
        public boolean isReference() {
            return true;
        }

        @Override
        public boolean handlesReference() {
            return false;
        }
    }

    //public static class NucleotideSequenceSerializer implements Serializer<NucleotideSequence> {
    //    @Override
    //    public void write(PrimitivO output, NucleotideSequence object) {
    //        try {
    //            object.data.writeTo(output);
    //        } catch (IOException e) {
    //            throw new RuntimeException(e);
    //        }
    //    }
    //
    //    @Override
    //    public NucleotideSequence read(PrimitivI input) {
    //        try {
    //            return new NucleotideSequence(Bit2Array.readFrom(input), true);
    //        } catch (IOException e) {
    //            throw new RuntimeException(e);
    //        }
    //    }
    //
    //    @Override
    //    public boolean isReference() {
    //        return true;
    //    }
    //
    //    @Override
    //    public boolean handlesReference() {
    //        return false;
    //    }
    //}

    public static class SequenceQualitySerializer implements Serializer<SequenceQuality> {
        @Override
        public void write(PrimitivO output, SequenceQuality object) {
            output.writeObject(object.data);
        }

        @Override
        public SequenceQuality read(PrimitivI input) {
            return new SequenceQuality(input.readObject(byte[].class));
        }

        @Override
        public boolean isReference() {
            return true;
        }

        @Override
        public boolean handlesReference() {
            return false;
        }
    }

    public static class NSequenceWithQualitySerializer implements Serializer<NSequenceWithQuality> {
        @Override
        public void write(PrimitivO output, NSequenceWithQuality object) {
            output.writeObject(object.sequence);
            output.writeObject(object.quality);
        }

        @Override
        public NSequenceWithQuality read(PrimitivI input) {
            NucleotideSequence seq = input.readObject(NucleotideSequence.class);
            SequenceQuality qual = input.readObject(SequenceQuality.class);
            return new NSequenceWithQuality(seq, qual);
        }

        @Override
        public boolean isReference() {
            return true;
        }

        @Override
        public boolean handlesReference() {
            return false;
        }
    }

    public static final class NSeqSerializer extends JsonSerializer<NucleotideSequence> {
        @Override
        public void serialize(NucleotideSequence value,
                              JsonGenerator jgen,
                              SerializerProvider provider) throws IOException, JsonProcessingException {
            jgen.writeString(value.toString());
        }
    }

    public static final class NSeqDeserializer extends JsonDeserializer<NucleotideSequence> {
        @Override
        public NucleotideSequence deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return new NucleotideSequence(jp.readValueAs(String.class));
        }
    }

    public static final class AASeqSerializer extends JsonSerializer<AminoAcidSequence> {
        @Override
        public void serialize(AminoAcidSequence value,
                              JsonGenerator jgen,
                              SerializerProvider provider) throws IOException, JsonProcessingException {
            jgen.writeString(value.toString());
        }
    }

    public static final class AASeqDeserializer extends JsonDeserializer<AminoAcidSequence> {
        @Override
        public AminoAcidSequence deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return new AminoAcidSequence(jp.readValueAs(String.class));
        }
    }


    public static final class SQSeqSerializer extends JsonSerializer<SequenceQuality> {
        @Override
        public void serialize(SequenceQuality value,
                              JsonGenerator jgen,
                              SerializerProvider provider) throws IOException, JsonProcessingException {
            jgen.writeString(value.toString());
        }
    }

    public static final class SQSeqDeserializer extends JsonDeserializer<SequenceQuality> {
        @Override
        public SequenceQuality deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return new SequenceQuality(jp.readValueAs(String.class));
        }
    }
}
