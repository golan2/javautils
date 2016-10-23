package javautils;

import junit.framework.TestCase;

/**
 * <p>A [<a href="{@docRoot}/overview-summary.html#[JUnit]">JUnit</a>]
 * test for {@link Classes}.</p>
 */
public class ClassesTest extends TestCase {

  /**
   * <p>Tests that the {@link java.lang.Class#forName}-method does not
   * recognize the primitive type <code>boolean</code>. The intention is
   * to make sure that the complexity of the {@link
   * Classes#forName}-method is justified.</p>
   */
  public void testClassForName() {
    try {
      Class.forName("boolean");
      fail("Class.forName(\"boolean\") didn't fail!");
    } catch (Exception e) {
      // ignore
    }
  }

  /**
   * <p>Tests that the {@link Classes#forName}-method also recognizes the
   * primitives types.</p>
   */
  public void testForName() {
    assertEquals(boolean.class, Classes.forName("boolean"));
    assertEquals(byte.class,    Classes.forName("byte"));
    assertEquals(char.class,    Classes.forName("char"));
    assertEquals(double.class,  Classes.forName("double"));
    assertEquals(float.class,   Classes.forName("float"));
    assertEquals(int.class,     Classes.forName("int"));
    assertEquals(long.class,    Classes.forName("long"));
    assertEquals(short.class,   Classes.forName("short"));
    assertEquals(void.class,    Classes.forName("void"));

    assertEquals(ClassesTest.class, Classes.forName("javautils.ClassesTest"));
  }

  /**
   * <p>Tests that the {@link Classes#asObjectType}-method recognizes the
   * primitive types.</p>
   */
  public void testAsObjectType() {
    assertEquals(Boolean.class,   Classes.asObjectType(boolean.class));
    assertEquals(Byte.class,      Classes.asObjectType(byte.class));
    assertEquals(Character.class, Classes.asObjectType(char.class));
    assertEquals(Double.class,    Classes.asObjectType(double.class));
    assertEquals(Float.class,     Classes.asObjectType(float.class));
    assertEquals(Integer.class,   Classes.asObjectType(int.class));
    assertEquals(Long.class,      Classes.asObjectType(long.class));
    assertEquals(Short.class,     Classes.asObjectType(short.class));
    assertEquals(void.class,      Classes.asObjectType(void.class));

    assertEquals(ClassesTest.class, Classes.asObjectType(javautils.ClassesTest.class));
  }
}
