package javautils;

import junit.framework.TestCase;

/**
 * <p>A [<a href="{@docRoot}/overview-summary.html#[JUnit]">JUnit</a>]
 * test for {@link Objects}.</p>
 */
public class ObjectsTest extends TestCase {

  /**
   * <p>Tests that the {@link Objects#rotateLeft}-method gives the
   * expected result on a number of test cases designed to cover most
   * essential input equivalence classes.</p>
   */
  public void testRotateLeft() {
    assertEquals(0x76543218, Objects.rotateLeft(0x87654321, 4));
    assertEquals(0x87654321, Objects.rotateLeft(0x87654321, 0));
    assertEquals(0x00000000, Objects.rotateLeft(0x00000000, 4));
    assertEquals(0x00000000, Objects.rotateLeft(0x00000000, 0));
    assertEquals(0xFFFFFF7F, Objects.rotateLeft(0xFFFFFFF7, 4));
    assertEquals(0xFFFFFFFE, Objects.rotateLeft(0x7FFFFFFF, 33));
    assertEquals(0xFFFFFFFF, Objects.rotateLeft(0xFFFFFFFF, 0));
    assertEquals(0xFFFFFFFF, Objects.rotateLeft(0xFFFFFFFF, 55));
  }
}
