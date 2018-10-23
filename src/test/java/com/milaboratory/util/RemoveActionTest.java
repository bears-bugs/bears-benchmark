package com.milaboratory.util;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.nio.charset.Charset;

public class RemoveActionTest {
    @Test
    public void test1() throws Exception {
        File tempDir = TempFileManager.getTempDir();
        FileUtils.write(tempDir.toPath().resolve("figure").toFile(), "sadfasf", Charset.forName("US-ASCII"));
        System.out.println(tempDir);
    }

    @Test
    public void test2() throws Exception {
        File tempFile = TempFileManager.getTempFile();
        FileUtils.write(tempFile, "sadfasf", Charset.forName("US-ASCII"));
        System.out.println(tempFile);
    }
}