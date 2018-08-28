package hu.oe.nik.szfmv.common;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConfigProviderTest {

    @Test
    public void provideTest() {
        // This test - besides testing the code - also provide a sample usage of the ConfigProvider and the TOML parser
        // itself. The get* methods can be used for accessing the configuration options with a key in a slightly
        // similar way as in XPATH for XML.
        assertEquals(false, ConfigProvider.provide().getBoolean("general.debug"));
    }
}
