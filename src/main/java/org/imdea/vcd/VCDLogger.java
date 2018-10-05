package org.imdea.vcd;

import java.util.logging.Logger;

/**
 *
 * @author Vitor Enes
 */
public class VCDLogger {

    public static Logger init(Class<?> c) {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tF %1$tT] [%4$-7s] %5$s %n");
        return Logger.getLogger(c.getName());
    }
}
