/**
 * The JSaPar package provides parser and composer for flat and CSV files (or any type of delimited file). It also provides
 * converter classes to convert from text source into java beans and from java beans into text output plus some
 * additional converters that handles xml and java strings.
 * <p>
 * The classes in this package provides the highest level of abstractions and are thus the easiest to use. If you want
 * to solve more complex scenarios you may choose to use the lower level {@link org.jsapar.parse.ParseTask} and
 * {@link org.jsapar.convert.ConvertTask}
 * implementations.
 * <p>
 * Example of reading <b>CSV file</b> into a {@link org.jsapar.model.Document} object according to an xml-schema:
 * <pre>{@code
 * try (Reader schemaReader = new FileReader("examples/01_CsvSchema.xml");
 *    Reader fileReader = new FileReader("examples/01_Names.csv")) {
 *    Schema schema = Schema.ofXml(schemaReader);
 *    TextParser parser = new TextParser(schema);
 *    Document document = new Document();
 *    DocumentBuilderLineEventListener listener = new DocumentBuilderLineEventListener(document);
 *    parser.parse(fileReader, listener);
 * }
 * }</pre>
 * <p>
 * Example of converting a <b>CSV file</b> into a <b>Fixed width file</b> according to two xml-schemas:
 * <pre>{@code
 * File outFile = new File("examples/02_Names_out.txt");
 * try (Reader inSchemaReader = new FileReader("examples/01_CsvSchema.xml");
 *    Reader outSchemaReader = new FileReader("examples/02_FixedWidthSchema.xml");
 *    Reader inReader = new FileReader("examples/01_Names.csv");
 *    Writer outWriter = new FileWriter(outFile)) {
 *    Text2TextConverter converter = new Text2TextConverter(Schema.ofXml(inSchemaReader),
 *    Schema.ofXml(outSchemaReader));
 *    converter.convert(inReader, outWriter);
 * }
 * }</pre>
 * <p>
 * Example of converting a <b>CSV file</b> into a list of <b>Java objects</b> according to an xml-schema:
 * <pre>{@code
 * try (Reader schemaReader = new FileReader("examples/07_CsvSchemaToJava.xml");
 *    Reader fileReader = new FileReader("examples/07_Names.csv")) {
 *    Text2BeanConverter converter = new Text2BeanConverter(Schema.ofXml(schemaReader));
 *    RecordingBeanEventListener<TstPerson> beanEventListener = new RecordingBeanEventListener<>();
 *    converter.convert(fileReader, beanEventListener);
 *    List<TstPerson> people = beanEventListener.getLines();
 * }
 * }</pre>
 *
 * @see <a href="https://org-tigris-jsapar.github.io/jsapar">Documentation pages</a> for further documentation.
 * @see <a href="https://github.com/org-tigris-jsapar/jsapar-examples">Examples project</a> for complete examples.
 */
package org.jsapar;