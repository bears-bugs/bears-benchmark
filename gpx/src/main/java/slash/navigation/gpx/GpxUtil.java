/*
    This file is part of RouteConverter.

    RouteConverter is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    RouteConverter is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with RouteConverter; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

    Copyright (C) 2007 Christian Pesch. All Rights Reserved.
*/

package slash.navigation.gpx;

import slash.navigation.gpx.binding10.Gpx;
import slash.navigation.gpx.binding11.GpxType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import static slash.common.helpers.JAXBHelper.newContext;
import static slash.common.helpers.JAXBHelper.newMarshaller;
import static slash.common.helpers.JAXBHelper.newUnmarshaller;

public class GpxUtil {
    public static final String GPX_10_NAMESPACE_URI = "http://www.topografix.com/GPX/1/0";
    public static final String GPX_11_NAMESPACE_URI = "http://www.topografix.com/GPX/1/1";
    public static final String GARMIN_EXTENSIONS_3_NAMESPACE_URI = "http://www.garmin.com/xmlschemas/GpxExtensions/v3";
    public static final String GARMIN_TRACKPOINT_EXTENSIONS_1_NAMESPACE_URI = "http://www.garmin.com/xmlschemas/TrackPointExtension/v1";
    public static final String GARMIN_TRACKPOINT_EXTENSIONS_2_NAMESPACE_URI = "http://www.garmin.com/xmlschemas/TrackPointExtension/v2";
    public static final String GARMIN_TRIP_EXTENSIONS_1_NAMESPACE_URI = "http://www.garmin.com/xmlschemas/TripExtensions/v1";
    public static final String TREKBUDDY_EXTENSIONS_0984_NAMESPACE_URI = "http://trekbuddy.net/2009/01/gpx/nmea";

    public static Unmarshaller newUnmarshaller10() {
        return newUnmarshaller(newContext(slash.navigation.gpx.binding10.ObjectFactory.class));
    }

    private static Marshaller newMarshaller10() {
        return newMarshaller(newContext(slash.navigation.gpx.binding10.ObjectFactory.class));
    }

    private static JAXBContext newContext11() {
        return newContext(slash.navigation.gpx.binding11.ObjectFactory.class,
                slash.navigation.gpx.garmin3.ObjectFactory.class,
                slash.navigation.gpx.trackpoint1.ObjectFactory.class,
                slash.navigation.gpx.trackpoint2.ObjectFactory.class,
                slash.navigation.gpx.trip1.ObjectFactory.class);
    }

    private static Unmarshaller newUnmarshaller11() {
        return newUnmarshaller(newContext11());
    }

    private static Marshaller newMarshaller11() {
        return newMarshaller(newContext11());
    }


    public static Gpx unmarshal10(Reader reader) throws JAXBException {
        Gpx result;
        try {
            result = (Gpx) newUnmarshaller10().unmarshal(reader);
        } catch (ClassCastException e) {
            throw new JAXBException("Parse error: " + e);
        }
        return result;
    }

    public static Gpx unmarshal10(InputStream inputStream) throws JAXBException {
        Gpx result;
        try {
            result = (Gpx) newUnmarshaller10().unmarshal(inputStream);
        } catch (ClassCastException e) {
            throw new JAXBException("Parse error: " + e);
        }
        return result;
    }

    public static void marshal10(Gpx gpx, OutputStream outputStream) throws JAXBException {
        try {
            try {
                newMarshaller10().marshal(new JAXBElement<>(new QName(GPX_10_NAMESPACE_URI, "gpx"), Gpx.class, gpx), outputStream);
            }
            finally {
                outputStream.flush();
                outputStream.close();
            }
        } catch (IOException e) {
            throw new JAXBException("Error while marshalling: " + e, e);
        }
    }


    public static GpxType unmarshal11(String string) throws JAXBException {
        return unmarshal11(new StringReader(string));
    }

    public static GpxType unmarshal11(Reader reader) throws JAXBException {
        GpxType result;
        try {
            JAXBElement element = (JAXBElement) newUnmarshaller11().unmarshal(reader);
            result = (GpxType) element.getValue();
        } catch (ClassCastException e) {
            throw new JAXBException("Parse error: " + e);
        }
        return result;
    }

    public static GpxType unmarshal11(InputStream in) throws JAXBException {
        GpxType result;
        try {
            JAXBElement element = (JAXBElement) newUnmarshaller11().unmarshal(in);
            result = (GpxType) element.getValue();
        } catch (ClassCastException e) {
            throw new JAXBException("Parse error: " + e);
        }
        return result;
    }

    public static void marshal11(GpxType gpxType, Writer writer) throws JAXBException {
        newMarshaller11().marshal(new slash.navigation.gpx.binding11.ObjectFactory().createGpx(gpxType), writer);
    }

    public static void marshal11(GpxType gpxType, OutputStream outputStream) throws JAXBException {
        try {
            try {
                newMarshaller11().marshal(new slash.navigation.gpx.binding11.ObjectFactory().createGpx(gpxType), outputStream);
            }
            finally {
                outputStream.flush();
                outputStream.close();
            }
        } catch (IOException e) {
            throw new JAXBException("Error while marshalling: " + e, e);
        }
    }

    public static String toXml(GpxType gpxType) throws IOException {
        StringWriter writer = new StringWriter();
        try {
            marshal11(gpxType, writer);
        } catch (JAXBException e) {
            throw new IOException("Cannot marshall " + gpxType + ": " + e, e);
        }
        return writer.toString();
    }
}
