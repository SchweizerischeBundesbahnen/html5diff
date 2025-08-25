package org.outerj.daisy.diff;

import org.junit.Test;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.XMLConstants;
import java.io.StringReader;
import java.util.Locale;

import static org.junit.Assert.*;

public class DaisyDiffSecurityTest {

    @Test
    public void disallowDoctypeDecl_shouldThrow_onDoctype() throws Exception {
        String xml = "<?xml version=\"1.0\"?>\n" +
                "<!DOCTYPE root [\n" +
                "  <!ELEMENT root ANY>\n" +
                "]>\n" +
                "<root>test</root>";

        InputSource is = new InputSource(new StringReader(xml));

        try {
            XMLReader xr = invokeNewXmlReader();
            xr.setContentHandler(new DefaultHandler());
            xr.parse(is);
            fail("Expected SAXException/SAXParseException due to disallow-doctype-decl");
        } catch (SAXException ex) {
            assertTrue(ex instanceof SAXParseException || ex.getCause() instanceof SAXParseException);
        }
    }

    @Test
    public void externalGeneralEntities_shouldBeDisabled() throws Exception {
        String xml = "<?xml version=\"1.0\"?>\n" +
                "<!DOCTYPE root [\n" +
                "  <!ENTITY ext SYSTEM \"http://example.com/evil\">\n" +
                "]>\n" +
                "<root>&ext;</root>";

        InputSource is = new InputSource(new StringReader(xml));

        try {
            XMLReader xr = invokeNewXmlReader();
            xr.setContentHandler(new DefaultHandler());
            xr.parse(is);
            fail("Expected SAXException due to disabled DOCTYPE/external entities");
        } catch (SAXException ex) {
            assertTrue(ex instanceof SAXParseException || ex.getCause() instanceof SAXParseException);
        }
    }

    @Test
    public void features_shouldBeSet_whenSupported() throws Exception {
        XMLReader xr = invokeNewXmlReader();

        assertFeatureIfSupported(xr, "http://apache.org/xml/features/disallow-doctype-decl", true);
        assertFeatureIfSupported(xr, "http://xml.org/sax/features/external-general-entities", false);
        assertFeatureIfSupported(xr, "http://xml.org/sax/features/external-parameter-entities", false);
        assertFeatureIfSupported(xr, "http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        assertFeatureIfSupported(xr, XMLConstants.FEATURE_SECURE_PROCESSING, true);
    }

    @Test
    public void diffHTML_shouldWork_onSimpleHtml() throws Exception {
        String oldHtml = "<html><body><p>Hello</p></body></html>";
        String newHtml = "<html><body><p>Hello, world!</p></body></html>";

        InputSource oldSrc = new InputSource(new StringReader(oldHtml));
        InputSource newSrc = new InputSource(new StringReader(newHtml));

        class CountingHandler extends DefaultHandler {
            int startElementCount = 0;
            @Override
            public void startElement(String uri, String localName, String qName, Attributes atts) {
                startElementCount++;
            }
        }
        CountingHandler sink = new CountingHandler();

        DaisyDiff.diffHTML(oldSrc, newSrc, sink, "diff-", Locale.ENGLISH);

        assertTrue("Expected diff to produce SAX events", sink.startElementCount > 0);
    }

    private XMLReader invokeNewXmlReader() throws Exception {
        java.lang.reflect.Method m = DaisyDiff.class.getDeclaredMethod("newXmlReader");
        m.setAccessible(true);
        return (XMLReader) m.invoke(null);
    }

    private void assertFeatureIfSupported(XMLReader xr, String feature, boolean expected) {
        try {
            boolean actual = xr.getFeature(feature);
            assertEquals("Feature mismatch: " + feature, expected, actual);
        } catch (SAXNotRecognizedException | SAXNotSupportedException ignored) {
        }
    }

}
