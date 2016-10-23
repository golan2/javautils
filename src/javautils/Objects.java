package javautils;

import java.lang.reflect.Method;

/**
 * <p>Static utility methods for dealing with objects.</p>
 */
public class Objects {

  // NOTE: Please keep the methods in this class in alphabetical order.
  // These methods are mostly unrelated. Keeping the methods in
  // alphabetical order makes it easier to locate individual methods.

  /**
   * <p>Just returns the specified object.</p>
   */
  public static Object    asObject(Object  x) { return x; }

  public static Boolean   asObject(boolean x) { return x ? Boolean.TRUE : Boolean.FALSE; }
  public static Byte      asObject(byte    x) { return new Byte(x); }
  public static Character asObject(char    x) { return new Character(x); }
  public static Double    asObject(double  x) { return new Double(x); }
  public static Float     asObject(float   x) { return new Float(x); }
  public static Integer   asObject(int     x) { return new Integer(x); }
  public static Long      asObject(long    x) { return new Long(x); }
  public static Short     asObject(short   x) { return new Short(x); }

  /**
   * <p>Clones the specified object.</p>
   */
  public static Object clone(Object o) {
    try {
      if (null == o)
        return null;

      Method m = Object.class.getDeclaredMethod("clone", null);
      m.setAccessible(true);
      return m.invoke(o, null);
    } catch (Exception e) {
      throw Exceptions.toThrowUnchecked(e);
    }
  }

  public static boolean clone(boolean x) { return x; }
  public static byte    clone(byte    x) { return x; }
  public static char    clone(char    x) { return x; }
  public static double  clone(double  x) { return x; }
  public static float   clone(float   x) { return x; }
  public static int     clone(int     x) { return x; }
  public static long    clone(long    x) { return x; }
  public static short   clone(short   x) { return x; }

  /**
   * <pre>
   * <b>return</b> lhs == rhs || null &#33;= lhs &amp;&amp; lhs.equals(rhs);
   * </pre>
   */
  public static boolean equals(Object lhs, Object rhs) {
    return lhs == rhs || null != lhs && lhs.equals(rhs);
  }

  /**
   * <pre>
   * <b>return</b> null == o &#63; 0 : o.hashCode();
   * </pre>
   */
  public static int hashCode(Object o) {
    return null == o ? 0 : o.hashCode();
  }

  /**
   * <pre>
   * <b>return</b> value &lt;&lt; n | value &gt;&gt;&gt; 32-n;
   * </pre>
   */
  public static int rotateLeft(int value, int n) {
    return value << n | value >>> 32-n;
  }

  /**
   * <pre>
   * <b>return</b> lhs == rhs;
   * </pre>
   */
  public static boolean same(Object lhs, Object rhs) {
    return lhs == rhs;
  }

  /**
   * <pre>
   * <b>return</b> null &#33;= lhs &amp;&amp; null &#33;= rhs &amp;&amp; lhs.getClass().equals(rhs.getClass());
   * </pre>
   */
  public static boolean sameClass(Object lhs, Object rhs) {
    return null != lhs && null != rhs && lhs.getClass().equals(rhs.getClass());
  }

  /**
   * <pre>
   * <b>return</b> null &#33;= o &#63; o.toString() : "null";
   * </pre>
   */
  public static String toString(Object o) {
    return null != o ? o.toString() : "null";
  }
}
