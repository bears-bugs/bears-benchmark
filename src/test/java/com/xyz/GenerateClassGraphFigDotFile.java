package com.xyz;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

public class GenerateClassGraphFigDotFile {
    public static void main(final String[] args) {
        try (ScanResult scanResult = new ClassGraph() //
                .whitelistPackages("com.xyz.fig") //
                .ignoreFieldVisibility() //
                .enableFieldInfo() //
                .ignoreMethodVisibility() //
                .enableMethodInfo() //
                .enableAnnotationInfo() //
                .scan()) {
            System.out.println(scanResult.getAllClasses().generateGraphVizDotFile(9.2f, 8.0f));
            // System.out.println(scanResult.toJSON(2));
        }
    }
}
