package org.outerj.daisy.diff.html.dom;

import org.junit.Test;
import org.xml.sax.helpers.AttributesImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests that {@link SeparatingNode#hashCode()} follows its {@link SeparatingNode#equals(Object)}.
 */
public class SeparatingNodeTest {

    @Test
    public void testSeparatorsAreNotEqual() {
        TagNode parent = new TagNode(null, "td", new AttributesImpl());
        assertNotEquals(new SeparatingNode(parent), new SeparatingNode(parent));
    }

    @Test
    public void testSeparatorsShareTheirHashCode() {
        // Two separators can still be equal when they sit in paired Polarion RTE links,
        // which no single node can derive, so every separator hashes the same.
        TagNode parent = new TagNode(null, "td", new AttributesImpl());
        assertEquals(new SeparatingNode(parent).hashCode(), new SeparatingNode(parent).hashCode());
    }
}
