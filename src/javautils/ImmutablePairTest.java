package javautils;

import junit.framework.TestCase;

/**
 * <p>A [<a href="{@docRoot}/overview-summary.html#[JUnit]">JUnit</a>]
 * test for {@link ImmutablePair}.</p>
 */
public class ImmutablePairTest extends TestCase {

  /**
   * <p>Tests that the hash code of {@link ImmutablePair}-objects is not
   * symmetric and that the hash code is the same on a couple of
   * separately constructed pairs with equal values.</p>
   */
  public void testHashCode() {
    assertTrue(new ImmutablePair(new Integer(1), new Integer(2)).hashCode() !=
               new ImmutablePair(new Integer(2), new Integer(1)).hashCode());

    assertTrue(new ImmutablePair(new Integer(1), new Integer(2)).hashCode() ==
               new ImmutablePair(new Integer(1), new Integer(2)).hashCode());

    assertTrue(new ImmutablePair(null, null).hashCode() ==
               new ImmutablePair(null, null).hashCode());
  }

  /**
   * <p>Tests that the {@link ImmutablePair#equals}-method gives the
   * expected result on a couple of test cases.</p>
   */
  public void testEquals() {
    assertFalse(new ImmutablePair(new Integer(1), new Integer(2))
                .equals(null));

    assertEquals(new ImmutablePair(null, null),
                 new ImmutablePair(null, null));

    assertFalse(new ImmutablePair(null, null)
                .equals(new ImmutablePair(null, null) {}));

    assertFalse(new ImmutablePair(new Integer(1), null)
                .equals(new ImmutablePair(new Integer(1), new Integer(2))));
  }
}
