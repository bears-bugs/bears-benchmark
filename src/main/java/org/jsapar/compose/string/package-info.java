/**
 * Classes for composing a stream of Java String as output for each line.
 * <p>
 * The {@link org.jsapar.compose.string.StringComposer} is the class that composes {@link org.jsapar.model.Document} or
 * {@link org.jsapar.model.Line} into
 * {@link java.util.stream.Stream} of {@link java.lang.String} for each {@link org.jsapar.model.Line} where each string is matches the cell in a schema. Each cell is formatted according to provided
 * {@link org.jsapar.schema.Schema}.
 * <p>
 * This can be useful if you want to
 * use the cell values for some other purpose than to create a text output. E.g. inserting the line into a database.
 */
package org.jsapar.compose.string;