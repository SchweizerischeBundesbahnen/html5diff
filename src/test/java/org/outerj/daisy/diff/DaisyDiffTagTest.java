package org.outerj.daisy.diff;

import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.junit.Assert.*;

public class DaisyDiffTagTest {

    private static class RecordingHandler implements ContentHandler {
        int startDocumentCount = 0;
        int endDocumentCount = 0;
        int startElementCount = 0;
        int endElementCount = 0;
        int charactersEventCount = 0;
        int totalChars = 0;

        @Override
        public void setDocumentLocator(Locator locator) {
            // not required
        }
        @Override
        public void startDocument() {
            startDocumentCount++;
        }
        @Override
        public void endDocument() {
            endDocumentCount++;
        }
        @Override
        public void startPrefixMapping(String prefix, String uri) {
            // not required
        }
        @Override
        public void endPrefixMapping(String prefix) {
            // not required
        }
        @Override
        public void startElement(String uri, String localName, String qName, Attributes atts) {
            startElementCount++;
        }
        @Override
        public void endElement(String uri, String localName, String qName) {
            endElementCount++;
        }
        @Override
        public void characters(char[] ch, int start, int length) {
            charactersEventCount++;
            totalChars += length;
        }
        @Override
        public void ignorableWhitespace(char[] ch, int start, int length) {
            // not required
        }
        @Override
        public void processingInstruction(String target, String data) {
            // not required
        }
        @Override
        public void skippedEntity(String name) {
            // not required
        }
    }

    @Test
    public void diffTag_String_withHtmlTags_detectsChanges() throws Exception {
        String oldText = "<p>Hello <b>world</b></p>";
        String newText = "<p>Hello <i>WORLD</i>!</p>";

        RecordingHandler handler = new RecordingHandler();

        DaisyDiff.diffTag(oldText, newText, handler);

        assertEquals(1, handler.startDocumentCount);
        assertEquals(1, handler.endDocumentCount);

        assertTrue(handler.startElementCount > 0);
        assertTrue(handler.charactersEventCount > 0);
    }

    @Test
    public void diffTag_BufferedReader_withHtmlTags_detectsChanges() throws Exception {
        String oldText = "<div>foo</div>";
        String newText = "<div>foo <span>bar</span></div>";

        BufferedReader oldReader = new BufferedReader(new StringReader(oldText));
        BufferedReader newReader = new BufferedReader(new StringReader(newText));

        RecordingHandler handler = new RecordingHandler();

        DaisyDiff.diffTag(oldReader, newReader, handler);

        assertEquals(0, handler.startDocumentCount);
        assertEquals(0, handler.endDocumentCount);

        assertTrue(handler.startElementCount > 0);
        assertTrue(handler.charactersEventCount > 0);
    }

}
