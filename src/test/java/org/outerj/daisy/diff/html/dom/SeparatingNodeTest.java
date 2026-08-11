package org.outerj.daisy.diff.html.dom;

import org.junit.Test;
import org.xml.sax.helpers.AttributesImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests that {@link SeparatingNode#hashCode()} follows its {@link SeparatingNode#equals(Object)}.
 */
public class SeparatingNodeTest {

    @Test
    public void testSeparatorEqualsOnlyItself() {
        TagNode parent = new TagNode(null, "td", new AttributesImpl());
        SeparatingNode separator = new SeparatingNode(parent);
        SeparatingNode otherSeparator = new SeparatingNode(parent);
        assertTrue(separator.equals(separator));
        assertFalse(separator.equals(otherSeparator));
    }

    @Test
    public void testHashCodeIsStable() {
        TagNode parent = new TagNode(null, "td", new AttributesImpl());
        SeparatingNode separator = new SeparatingNode(parent);
        assertEquals(separator.hashCode(), separator.hashCode());
    }
}
