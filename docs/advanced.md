---
layout: page
title: Advanced
---
*This page is under construction*

Please see the [API javadocs](api) for more advanced documentation.

The schema XSD itself provides
a lot of documentation about the details of each allowed element and attribute within the schema xml. A published version
of the schema is located at. 

[http://jsapar.tigris.org/JSaParSchema/2.0/JSaParSchema.xsd](http://jsapar.tigris.org/JSaParSchema/2.0/JSaParSchema.xsd)

If you want to download the XSD as a file, you will probably need to right click on the link above and choose *"Save link as..."* depending on your browser.

#Advanced parsing
## Using XML as input
It is possible to parse an xml document that conforms to the XMLDocumentFormat.xsd (http://jsapar.tigris.org/XMLDocumentFormat/1.0).
Use the class org.jsapar.XmlParser in order to parse an xml file and produce line parsed events.

#Advanced converting
## Text to markup (XML or HTML)
Use the class Text2XmlConverter in order to produce a xml output. You can register a XSLT together with this converter and in
that way you convert the text to any other text output format such as HTML.
