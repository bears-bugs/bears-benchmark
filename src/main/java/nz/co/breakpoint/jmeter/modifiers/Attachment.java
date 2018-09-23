package nz.co.breakpoint.jmeter.modifiers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.property.ObjectProperty;

/* This represents an entry in the pre/postprocessor's attachments table 
 * of attachments that are to be handed over to the AttachmentCallbackHandler
 * for processing (encrypting/signing or decrypting).
 */
public class Attachment extends AbstractTestElement {

    private static final String 
        CONTENT = "Attachment.Content", 
        MIMETYPE = "Attachment.MimeType", 
        HEADERS = "Attachment.Headers",
        MODE = "Attachment.Mode",
        DESTINATION = "Attachment.Destination";

    private static final String HEADER_REGEX = "(.*?)\\s*:\\s*(.*)";

    public Attachment() {}

    // Mainly for unit tests
    public Attachment(String id, String mimeType, String content, String headers, String mode, String destination) { 
        setName(id); 
        setMimeType(mimeType); 
        setContent(content); 
        setHeaders(headers);
        setMode(mode);
        setDestination(destination);
    }

    // Convenience ctor, not currently used
    public Attachment(org.apache.wss4j.common.ext.Attachment a) throws IOException {
        this(a.getId(), a.getMimeType(), fromSourceStream(a.getSourceStream()), fromHeadersMap(a.getHeaders()), "", "");
    }

    public String getMimeType() { return getPropertyAsString(MIMETYPE); }
    public String getContent() { return getPropertyAsString(CONTENT); }
    public String getHeaders() { return getPropertyAsString(HEADERS); }
    public String getMode() { return getPropertyAsString(MODE); }
    public String getDestination() { return getPropertyAsString(DESTINATION); }
    public void setMimeType(String mimeType) { setProperty(MIMETYPE, mimeType); }
    public void setContent(String content) { setProperty(CONTENT, content); }
    public void setHeaders(String headers) { setProperty(HEADERS, headers); }
    public void setMode(String mode) { setProperty(MODE, mode); }
    public void setDestination(String destination) { setProperty(DESTINATION, destination); }

    // Convenience method for AttachmentCallbackHandler
    public org.apache.wss4j.common.ext.Attachment toWss4jAttachment() {
        return toWss4jAttachment(this);
    }

    public static org.apache.wss4j.common.ext.Attachment toWss4jAttachment(Attachment attachment) {
        org.apache.wss4j.common.ext.Attachment a = new org.apache.wss4j.common.ext.Attachment();
        a.setId(attachment.getName());
        a.setMimeType(attachment.getMimeType());
        a.setSourceStream(toSourceStream(attachment.getContent()));
        a.addHeaders(toHeadersMap(attachment.getHeaders()));
        return a;
    }

    // JMeter StringProperty to wss4j
    public static InputStream toSourceStream(String content) {
        return new ByteArrayInputStream(Base64.getDecoder().decode(content));
    }

    // wss4j to JMeter StringProperty
    public static String fromSourceStream(InputStream source) throws IOException {
        return Base64.getEncoder().encodeToString(IOUtils.toByteArray(source));
    }

    // JMeter StringProperty to wss4j
    public static Map<String, String> toHeadersMap(String headers) {
        Map<String, String> headersMap = new HashMap<String, String>();
        if (headers != null && headers.length() > 0) {
            for (String header : headers.split("\n")) {
                headersMap.put(header.replaceFirst(HEADER_REGEX, "$1"), header.replaceFirst(HEADER_REGEX, "$2"));
            }
        }
        return headersMap;
    }

    // wss4j to JMeter StringProperty
    public static String fromHeadersMap(Map<String, String> headers) {
        StringBuilder sb = new StringBuilder();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                sb.append(entry.getKey()+": "+entry.getValue()+"\n");
            }
        }
        return sb.toString();
    }

    /* Retrieve encrypted bytes from preprocessor and copy them somewhere for the sampler to access,
     * depending on MODE: File, Variable, Context, Property, Base64 String
     */
    public void toJmeter(InputStream fromWss4j) throws IOException {
        if (getMode().length() > 0) { // don't consume stream unless mode specified
            byte[] bytes = IOUtils.toByteArray(fromWss4j);

            if ("File".equalsIgnoreCase(getMode())) {
                FileUtils.writeByteArrayToFile(new File(getDestination()), bytes);
            }
            else if ("Variable".equalsIgnoreCase(getMode())) {
                getThreadContext().getVariables().putObject(getDestination(), bytes);
            }
            else if ("Context".equalsIgnoreCase(getMode())) {
                getThreadContext().getSamplerContext().put(getDestination(), bytes);
            }
            else if ("Property".equalsIgnoreCase(getMode())) {
                getThreadContext().getCurrentSampler().setProperty(new ObjectProperty(getDestination(), bytes));
            }
            else if ("Base64".equalsIgnoreCase(getMode())) {
                getThreadContext().getVariables().put(getDestination(), Base64.getEncoder().encodeToString(bytes));
            }
        }
    }
}
