package javautils;

import java.lang.reflect.InvocationTargetException;

/**
 * <p>Static utility methods for dealing with exceptions.</p>
 */
public class Exceptions {

  // NOTE: Please keep the methods in this class in alphabetical order.
  // These methods are mostly unrelated. Keeping the methods in
  // alphabetical order makes it easier to locate individual methods.

  /**
   * <p>The root cause of the given throwable.</p>
   */
  public static Throwable getRootCause(Throwable t) {
    return t.getCause() == null
      ? t
      : getRootCause(t.getCause());
  }

  /**
   * <p>Throws the <code>Throwable</code> as an uncheched exception or
   * wraps it inside a <code>RuntimeException</code> to be thrown by the
   * caller.</p>
   *
   * <p>This method is intended to be used for translating
   * <em>checked</em> exceptions to <em>unchecked</em> exceptions using
   * code like this:</p>
   *
   * <pre>
   * <b>try</b> {
   *   // ...an operation that may throw a checked exception...
   * } <b>catch</b> (Exception e) {
   *   <b>throw</b> {@link javautils.Exceptions#toThrowUnchecked(Throwable) toThrowUnchecked}(e);
   * }
   * </pre>
   */
  public static RuntimeException toThrowUnchecked(Throwable t) {
    if (t instanceof RuntimeException)
      throw (RuntimeException)t;
    else if (t instanceof Error)
      throw (Error)t;
    else if (t instanceof InvocationTargetException)
      return toThrowUnchecked(t.getCause());
    else
      return new RuntimeException(t);
  }
}
