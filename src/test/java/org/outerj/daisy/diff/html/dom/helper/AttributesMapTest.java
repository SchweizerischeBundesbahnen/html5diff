package org.outerj.daisy.diff.html.dom.helper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the normalization of the "style" and "class" attribute values.
 */
public class AttributesMapTest {

    @Test
    public void testNormalizeStyleStringCollapsesSpacesAroundColon() {
        assertEquals("font-size:16pt; margin-left:50px",
                AttributesMap.normalizeStyleString(
                        "    font-size  :  16pt    ;  ;   ;  ; margin-left  : 50px   "));
    }

    @Test
    public void testNormalizeStyleStringKeepsRuleWithoutColon() {
        assertEquals("font-size", AttributesMap.normalizeStyleString("  font-size  "));
    }

    @Test
    public void testEquivalentStylesIgnoresOrderAndSpaces() {
        assertTrue(AttributesMap.equivalentStyles(
                "margin-left:50px;font-size:16pt;",
                "    font-size  :  16pt    ;  ;   ;  ; margin-left  : 50px   "));
    }

    @Test
    public void testEquivalentStylesDetectsDifferentValues() {
        assertFalse(AttributesMap.equivalentStyles(
                "margin-left : 50px", "margin-left : 60px"));
    }
}
