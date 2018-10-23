/*
 * Copyright 2016 MiLaboratory.com
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
package com.milaboratory.util;

import gnu.trove.map.hash.TCharIntHashMap;

public final class ParseUtil {
    private ParseUtil() {
    }

    public static String[] splitWithBrackets(String string, char splitChar, String brackets) {
        return splitWithBrackets(string, splitChar, new BracketsInfo(brackets));
    }

    public static String[] splitWithBrackets(String string, char splitChar, BracketsInfo brackets) {
        IntArrayList splitPoints = new IntArrayList();
        splitPoints.push(-1);
        BracketsProcessor bracketsProcessor = brackets.createBracketsProcessor();
        for (int i = 0; i < string.length(); ++i) {
            char c = string.charAt(i);
            if (!bracketsProcessor.process(c)) {
                if (c == splitChar && bracketsProcessor.getDepth() == 0)
                    splitPoints.push(i);
            }
        }
        bracketsProcessor.finish();
        splitPoints.push(string.length());
        int size = splitPoints.size() - 1;
        String[] result = new String[size];
        for (int i = 0; i < size; ++i)
            result[i] = string.substring(splitPoints.get(i) + 1, splitPoints.get(i + 1));
        return result;
    }

    public static final class BracketsInfo {
        private final TCharIntHashMap bracketsMap = new TCharIntHashMap();

        public BracketsInfo(String brackets) {
            if (brackets.length() % 2 != 0)
                throw new IllegalArgumentException();
            for (int i = brackets.length() / 2 - 1; i >= 0; --i) {
                bracketsMap.put(brackets.charAt(i * 2), i + 1);
                bracketsMap.put(brackets.charAt(i * 2 + 1), -i - 1);
            }
        }

        BracketsProcessor createBracketsProcessor() {
            return new BracketsProcessor(bracketsMap);
        }
    }

    private static final class BracketsProcessor {
        private final TCharIntHashMap bracketsMap;
        IntArrayList types = new IntArrayList();

        private BracketsProcessor(TCharIntHashMap bracketsMap) {
            this.bracketsMap = bracketsMap;
        }

        public boolean process(char c) {
            int v = bracketsMap.get(c);
            if (v == 0)
                return false;
            else {
                if (v < 0) {
                    if (types.size() == 0)
                        throw new ParserException("Closing bracket '" + c + "' before any opening bracket.");
                    if (types.pop() != -v)
                        throw new ParserException("Unbalanced bracket '" + c + "'.");
                    return true;
                } else {
                    types.push(v);
                    return true;
                }
            }
        }

        public void finish() {
            if (getDepth() != 0)
                throw new ParserException("Unbalanced brackets.");
        }

        public int getDepth() {
            return types.size();
        }
    }
}
