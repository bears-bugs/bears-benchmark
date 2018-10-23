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

import java.io.IOException;
import java.io.InputStream;

import static com.milaboratory.core.alignment.ScoringMatrixIO.readAABlastMatrix;

/**
 * BLASTMatrix - enum of available BLAST AminoAcid substitution matrices
 */
public enum BLASTMatrix implements java.io.Serializable {
    BLOSUM45, BLOSUM50, BLOSUM62, BLOSUM80, BLOSUM90, PAM30, PAM70, PAM250;
    private volatile int[] iMatrix = null, matrix = null;

    public int[] getMatrix() {
        if (matrix == null) {
            synchronized (this) {
                if (matrix == null) {
                    try (InputStream stream = BLASTMatrix.class.getClassLoader().getResourceAsStream("matrices/" + this.name())) {
                        matrix = readAABlastMatrix(stream);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return matrix;
    }
}
