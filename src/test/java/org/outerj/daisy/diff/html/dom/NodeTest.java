package org.outerj.daisy.diff.html.dom;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.xml.sax.helpers.AttributesImpl;

import java.util.Arrays;

/**
 * Tests (a tiny part of the) functionality of {@link Node}.
 */
public class NodeTest
{
    @Test
    public void testGetParentTree() throws Exception
    {
        TagNode root = new TagNode(null, "root", new AttributesImpl());
        TagNode intermediate = new TagNode(root, "middle", new AttributesImpl());
        root.addChild(intermediate);
        TagNode leaf = new TagNode(intermediate, "leaf", new AttributesImpl());
        intermediate.addChild(leaf);
        assertEquals(Arrays.asList(root, intermediate), leaf.getParentTree());
        // Verify caching: subsequent calls should return the same list object
        assertEquals(leaf.getParentTree(), leaf.getParentTree());
    }

    @Test
    public void testParentTreeInvalidation() throws Exception
    {
        TagNode root = new TagNode(null, "root", new AttributesImpl());
        TagNode intermediate1 = new TagNode(root, "middle1", new AttributesImpl());
        TagNode leaf = new TagNode(intermediate1, "leaf", new AttributesImpl());
        
        assertEquals(Arrays.asList(root, intermediate1), leaf.getParentTree());
        
        TagNode intermediate2 = new TagNode(root, "middle2", new AttributesImpl());
        leaf.setParent(intermediate2);
        
        assertEquals(Arrays.asList(root, intermediate2), leaf.getParentTree());
    }
}
