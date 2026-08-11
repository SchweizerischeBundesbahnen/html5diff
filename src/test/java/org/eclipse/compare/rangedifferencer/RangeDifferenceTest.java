package org.eclipse.compare.rangedifferencer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests that {@link RangeDifference#hashCode()} follows its {@link RangeDifference#equals(Object)}.
 */
public class RangeDifferenceTest {

    @Test
    public void testEqualDifferencesShareHashCode() {
        RangeDifference one = new RangeDifference(RangeDifference.CHANGE, 1, 2, 3, 4, 5, 6);
        RangeDifference other = new RangeDifference(RangeDifference.CHANGE, 1, 2, 3, 4, 5, 6);
        assertTrue(one.equals(other));
        assertEquals(one.hashCode(), other.hashCode());
    }

    @Test
    public void testDifferentRangesDifferInHashCode() {
        RangeDifference one = new RangeDifference(RangeDifference.CHANGE, 1, 2, 3, 4, 5, 6);
        RangeDifference other = new RangeDifference(RangeDifference.CHANGE, 1, 2, 3, 4, 5, 7);
        assertNotEquals(one, other);
        assertNotEquals(one.hashCode(), other.hashCode());
    }
}
