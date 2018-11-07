/**
 *
 */
package org.jsapar.convert;

import org.jsapar.Text2TextConverter;
import org.jsapar.error.JSaParException;
import org.jsapar.error.RecordingErrorEventListener;
import org.jsapar.schema.Schema;
import org.jsapar.schema.Xml2SchemaBuilder;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Properties;

/**
 * Command line utility that converts one file to another using provided input and output schemas.
 * <p>
 * Usage:
 * <pre>{@code
 * java -jar jsapar-<version>.jar -in.schema <input schema name> -out.schema <output schema name>
 * -in.file <input file name> [-out.file <output file name>]
 * [-in.file.encoding <input file encoding (or system default is used)>]
 * [-out.file.encoding <output file encoding (or system default is used)>]
 * }</pre>
 */
public class Text2TextConverterMain {
    private static final String APP_NAME = "jsapar.jar";

    private String applicationName = APP_NAME;

    public void run(String[] args) {
        Properties properties;
        try {
            properties = readConfig(args);
        } catch (Exception e) {
            printUsage(e, System.out);
            return;
        }

        try {
            Schema inputSchema = Xml2SchemaBuilder.loadSchemaFromXmlFile(new File(properties.getProperty("in.schema")));
            Schema outputSchema = Xml2SchemaBuilder
                    .loadSchemaFromXmlFile(new File(properties.getProperty("out.schema")));

            String inFileName = properties.getProperty("in.file");
            String inFileEncoding = properties.getProperty("in.file.encoding", Charset.defaultCharset().name());

            String outFileEncoding = properties.getProperty("out.file.encoding", Charset.defaultCharset().name());
            String outFileName = properties.getProperty("out.file", inFileName + ".out");

            try (Reader inputFileReader = new InputStreamReader(
                    new FileInputStream(inFileName), inFileEncoding );
                 Writer writer = new OutputStreamWriter(
                         new FileOutputStream(outFileName), outFileEncoding )) {
                Text2TextConverter converter = makeConverter(inputSchema, outputSchema);
                RecordingErrorEventListener errorEventListener = new RecordingErrorEventListener();
                converter.setErrorEventListener(errorEventListener);
                converter.convert(inputFileReader, writer);
                List<JSaParException> parseErrors = errorEventListener.getErrors();
                if (parseErrors.size() > 0)
                    System.out.println("===> Found errors while converting file " + inFileName + ": "
                            + System.getProperty("line.separator") + parseErrors);
                else
                    System.out.println("Successfully converted file " + inFileName);

            }
        } catch (Throwable t) {
            System.err.println("Failed to convert file.");
            t.printStackTrace(System.err);
        }
    }

    private void printUsage(Exception e, PrintStream out) {
        out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
        out.println();
        out.println("Usage:");
        out.println(" 1. " + getApplicationName() + " <property file name> ");
        out.println(" 2. " + getApplicationName()
                + " -in.schema <input schem name> -out.schema <output schema name>");
        out.println("               -in.file <input file name> [-out.file <output file name>]");
        out.println("               [-in.file.encoding <input file encoding (or system default is used)>] ");
        out.println("               [-out.file.encoding <output file encoding (or system default is used)>] ");
        out.println();
        out.println("Alternative 1. above reads the arguments from a property file.");
    }

    private Text2TextConverter makeConverter(Schema inputSchema, Schema outputSchema) {
        return new Text2TextConverter(inputSchema, outputSchema);
    }

    private void readArgs(Properties properties, String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.length() > 1 && arg.charAt(0) == '-') {
                if (args.length > i + 1) {
                    properties.setProperty(arg.substring(1, arg.length()), args[i + 1]);
                    i++; // Skip next.
                }
            }
        }
    }

    private void checkMandatory(Properties properties, String key) {
        if (null == properties.getProperty(key))
            throw new IllegalArgumentException("Mandatory argument -" + key + " is missing.");
    }

    private Properties readConfig(String[] args) throws IOException {
        Properties properties = new Properties();
        if (args.length == 1) {
            try (FileReader reader = new FileReader(args[0])) {
                properties.load(reader);
            }
        } else if (args.length > 1) {
            readArgs(properties, args);
        } else {
            throw new IllegalArgumentException("Too few arguments");
        }

        // Check mandatory arguments
        checkMandatory(properties, "in.schema");
        checkMandatory(properties, "out.schema");
        checkMandatory(properties, "in.file");
        return properties;
    }


    /**
     * @param applicationName the applicationName to set
     */
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    /**
     * @return the applicationName
     */
    public String getApplicationName() {
        return applicationName;
    }

}
