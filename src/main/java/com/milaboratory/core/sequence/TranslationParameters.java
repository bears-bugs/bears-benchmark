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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class TranslationParameters {
    public static final TranslationParameters FromCenter = new TranslationParameters((byte) 0, null, true);
    public static final TranslationParameters FromLeftWithIncompleteCodon = new TranslationParameters((byte) 0, Boolean.TRUE, true);
    public static final TranslationParameters FromLeftWithoutIncompleteCodon = new TranslationParameters((byte) 0, Boolean.TRUE, false);
    public static final TranslationParameters FromRightWithIncompleteCodon = new TranslationParameters((byte) 0, Boolean.FALSE, true);
    public static final TranslationParameters FromRightWithoutIncompleteCodon = new TranslationParameters((byte) 0, Boolean.FALSE, false);

    /**
     * 0, 1 or 2
     */
    final byte frame;
    final Boolean fromLeft;
    final boolean includeIncomplete;

    private TranslationParameters(byte frame, Boolean fromLeft, boolean includeIncomplete) {
        this.frame = frame;
        this.fromLeft = fromLeft;
        this.includeIncomplete = includeIncomplete;
    }

    public byte getFrame() {
        return frame;
    }

    public Boolean getFromLeft() {
        return fromLeft;
    }

    public boolean isIncludeIncomplete() {
        return includeIncomplete;
    }

    private static final TranslationParameters Frame1W = new TranslationParameters((byte) 1, Boolean.TRUE, true);
    private static final TranslationParameters Frame2W = new TranslationParameters((byte) 2, Boolean.TRUE, true);
    private static final TranslationParameters Frame1WO = new TranslationParameters((byte) 1, Boolean.TRUE, false);
    private static final TranslationParameters Frame2WO = new TranslationParameters((byte) 2, Boolean.TRUE, false);

    TranslationParameters convertToLeftBound(int ntSequenceLength) {
        if (fromLeft == null)
            throw new IllegalArgumentException();
        if (fromLeft)
            return this;
        int fr = ntSequenceLength % 3;
        if (includeIncomplete)
            switch (fr) {
                case 0:
                    return FromLeftWithIncompleteCodon;
                case 1:
                    return Frame1W;
                case 2:
                    return Frame2W;
            }
        else
            switch (fr) {
                case 0:
                    return FromLeftWithoutIncompleteCodon;
                case 1:
                    return Frame1WO;
                case 2:
                    return Frame2WO;
            }
        throw new IllegalArgumentException();
    }

    /**
     * E.g. withIncompleteCodon(3) == withIncompleteCodon(6) and withIncompleteCodon(1) == withIncompleteCodon(4)
     *
     * @param inFramePosition position of first nucleotide in any of in-frame triplets
     */
    public static TranslationParameters withIncompleteCodon(int inFramePosition) {
        byte frame = (byte) (inFramePosition % 3);
        switch (frame) {
            case 0:
                return FromLeftWithIncompleteCodon;
            case 1:
                return Frame1W;
            case 2:
                return Frame2W;
        }
        throw new IllegalArgumentException();
    }

    /**
     * E.g. withIncompleteCodon(3) == withIncompleteCodon(6) and withIncompleteCodon(1) == withIncompleteCodon(4)
     *
     * @param inFramePosition position of first nucleotide in any of in-frame triplets
     */
    public static TranslationParameters withoutIncompleteCodon(int inFramePosition) {
        byte frame = (byte) (inFramePosition % 3);
        switch (frame) {
            case 0:
                return FromLeftWithoutIncompleteCodon;
            case 1:
                return Frame1WO;
            case 2:
                return Frame2WO;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TranslationParameters that = (TranslationParameters) o;

        if (frame != that.frame) return false;
        if (includeIncomplete != that.includeIncomplete) return false;
        return !(fromLeft != null ? !fromLeft.equals(that.fromLeft) : that.fromLeft != null);

    }

    @Override
    public int hashCode() {
        int result = (int) frame;
        result = 31 * result + (fromLeft != null ? fromLeft.hashCode() : 0);
        result = 31 * result + (includeIncomplete ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        String shortName = shortNames.get(this);
        if (shortName != null)
            return shortName;

        return "Frame" + frame + (includeIncomplete ? "WithIncomplete" : "WithoutIncomplete");
    }

    private static final Map<TranslationParameters, String> shortNames;

    static {
        shortNames = new HashMap<>();
        shortNames.put(FromCenter, "FromCenter");
        shortNames.put(FromLeftWithIncompleteCodon, "FromLeftWithIncompleteCodon");
        shortNames.put(FromLeftWithoutIncompleteCodon, "FromLeftWithoutIncompleteCodon");
        shortNames.put(FromRightWithIncompleteCodon, "FromRightWithIncompleteCodon");
        shortNames.put(FromRightWithoutIncompleteCodon, "FromRightWithoutIncompleteCodon");
    }

    private static final Collection<TranslationParameters> preDefinedParameters = Collections.unmodifiableCollection(shortNames.keySet());

    public static Collection<TranslationParameters> getPreDefinedParameters() {
        return preDefinedParameters;
    }
}
