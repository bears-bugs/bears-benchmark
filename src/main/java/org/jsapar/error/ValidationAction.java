package org.jsapar.error;

import org.jsapar.compose.bean.BeanComposeConfig;
import org.jsapar.parse.text.TextParseConfig;

/**
 * Enum describing different actions to take upon validation.
 * @see TextParseConfig
 * @see BeanComposeConfig
 */
public enum ValidationAction {
    /**
     * Generate an error event.
     */
    ERROR,

    /**
     * Throw an exception
     */
    EXCEPTION,

    /**
     * Silently ignore the current line
     */
    OMIT_LINE,

    /**
     * Do nothing, which in most cases is the same as silently ignoring the current line.
     */
    NONE
}
