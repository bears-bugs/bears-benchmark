/**
 * Copyrigth: Jonas Stenberg
 */
package org.jsapar.schema;

import org.jsapar.error.JSaParException;

/**
 * Exception used for when there are errors within a schema or while building a schema.
 */
public class SchemaException extends JSaParException {

    private static final long serialVersionUID = 636516160510599949L;

    public SchemaException(String s) {
        super(s);
    }

    public SchemaException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SchemaException(Throwable throwable) {
        super(throwable);
    }

}
