package org.jsapar.parse.bean;

import org.jsapar.error.BeanException;
import org.jsapar.error.JSaParException;
import org.jsapar.schema.SchemaException;
import org.jsapar.schema.Xml2SchemaBuilder;
import org.jsapar.utils.XmlTypes;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.beans.IntrospectionException;
import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class for building {@link BeanMap} instances based on xml.
 */
public class Xml2BeanMapBuilder implements XmlTypes {
    private final static String NAMESPACE = "http://jsapar.tigris.org/BeanMapSchema/2.0";

    /**
     * Loads a {@link BeanMap} instance from xml that is read from the supplied reader.
     * @param reader The reader to read xml from.
     * @return A newly created {@link BeanMap} instance.
     * @throws ClassNotFoundException In case one of the classes referenced from the bean map xml could not be created.
     * @throws IOException In case there was an io error while reading xml.
     */
    public BeanMap build(Reader reader) throws ClassNotFoundException, IOException {
        String schemaFileName = "/xml/schema/BeanMapSchema.xsd";

        try(InputStream schemaStream = Xml2SchemaBuilder.class.getResourceAsStream(schemaFileName)) {
            if (schemaStream == null)
                throw new FileNotFoundException("Could not find schema file: " + schemaFileName);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringElementContentWhitespace(true);
            factory.setIgnoringComments(true);
            factory.setCoalescing(true);
            factory.setNamespaceAware(true);
            factory.setValidating(true);
            factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
            factory.setAttribute(JAXP_SCHEMA_SOURCE, schemaStream);

            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(makeDefaultErrorHandler());
            org.xml.sax.InputSource is = new org.xml.sax.InputSource(reader);
            org.w3c.dom.Document xmlDocument = builder.parse(is);

            Element xmlRoot = xmlDocument.getDocumentElement();

            List<BeanPropertyMap> beanPropertyMaps = getChildrenStream(NAMESPACE, xmlRoot, "bean")
                    .map(this::buildPropertyMap)
                    .collect(Collectors.toList());

            return BeanMap.ofBeanPropertyMaps(beanPropertyMaps);
        } catch (ParserConfigurationException | SAXException e) {
            throw new JSaParException("Failed to load bean map from xml ", e);
        }

    }

    private BeanPropertyMap buildPropertyMap(Element xmlBean) {
        String className = xmlBean.getAttribute("name");
        String lineType = xmlBean.getAttribute("linetype");

        Map<String, String> cellNamesOfProperty = getChildrenStream(xmlBean, "property")
                .collect(Collectors.toMap(e -> e.getAttribute("name"), e -> e.getAttribute("cellname")));
        try {
            return BeanPropertyMap.ofPropertyNames(className, lineType, cellNamesOfProperty);
        } catch (ClassNotFoundException | IntrospectionException e) {
            throw new JSaParException("Failed to build property map", e);
        }
    }

    /**
     * Loads a bean map from specified resource using default character encoding.
     *
     * @param resourceBaseClass
     *            A class that specifies the base for the relative location of the resource. If this parameter is null,
     *            the resource name has to specify the resource with an absolute path.
     * @param resourceName
     *            The name of the resource to load.
     * @return A newly created bean map from the supplied xml resource.
     * @throws SchemaException  When there is an error in the bean map
     * @throws UncheckedIOException      When there is an error reading from input
     */
    @SuppressWarnings("unused")
    public static BeanMap loadBeanMapFromXmlResource(Class<?> resourceBaseClass, String resourceName)
            throws UncheckedIOException{
        return loadBeanMapFromXmlResource(resourceBaseClass, resourceName, Charset.defaultCharset().name());
    }

    /**
     * Loads a bean map from specified resource using supplied character encoding.
     *
     * @param resourceBaseClass
     *            A class that specifies the base for the relative location of the resource. If this parameter is null,
     *            the resource name has to specify the resource with an absolute path.
     * @param resourceName
     *            The name of the resource to load.
     * @param encoding
     *            The character encoding to use while reading resource.
     * @return A newly created bean map from the supplied xml resource.
     * @throws UncheckedIOException      When there is an error reading from input
     */
    @SuppressWarnings("WeakerAccess")
    public static BeanMap loadBeanMapFromXmlResource(Class<?> resourceBaseClass, String resourceName, String encoding)
            throws UncheckedIOException{
        if (resourceBaseClass == null)
            resourceBaseClass = Xml2BeanMapBuilder.class;
        try (InputStream is = resourceBaseClass.getResourceAsStream(resourceName)) {
            if (is == null) {
                throw new IOException(
                        "Failed to load resource [" + resourceName + "] from class " + resourceBaseClass.getName());
            }
            Xml2BeanMapBuilder beanMapBuilder = new Xml2BeanMapBuilder();
            return beanMapBuilder.build(new InputStreamReader(is, encoding));
        } catch (IOException  e) {
            throw new UncheckedIOException("Failed to load bean map from xml resource", e);
        } catch (ClassNotFoundException e) {
            throw new BeanException("Failed to load bean map", e);
        }
    }

}
