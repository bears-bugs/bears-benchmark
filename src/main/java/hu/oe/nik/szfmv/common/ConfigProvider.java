package hu.oe.nik.szfmv.common;

import com.moandjiezana.toml.Toml;

/**
 * ConfigProvider gives access to the <i>config.toml</i> configuration file, that can store configuration options such
 * as debug mode or any other settings without recompiling the code.
 *
 * @author Gergő Pintér
 */
public final class ConfigProvider {

    /**
     * Reads the <i>resources/config.toml</i> configuration file.
     *
     * @return a new com.moandjiezana.toml.Toml object with the content of the <i>config.toml</i> file.
     */
    public static Toml provide() {
        return new Toml().read(ClassLoader.getSystemResourceAsStream("config.toml"));
    }

}
