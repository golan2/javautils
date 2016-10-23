package javautils.fun;

import java.lang.reflect.Method;
import javautils.Exceptions;

/**
 * <p><code>void->void</code> procedure.</p>
 */
public abstract class VoidToVoid extends Function {
  public abstract void with();

  public static final VoidToVoid NOP =
    new VoidToVoid() {
      public void with() {
      }};

  /**
   * <p>A wrapper that calls the given arbitrary function.</p>
   *
   * <p><b>Note</b>:</p>
   * <ul>
   * <li>Careless wrapping and use of arbitrary functions may lead to
   * run-time type errors.</li>
   * <li>The dynamic invocation performed by the created wrapper will be
   * slower than direct invocation of the original {@link Function}
   * object.</li>
   * </ul>
   */
  public static VoidToVoid from(final Function f) {
    return f instanceof VoidToVoid
      ? (VoidToVoid)f
      : bindSelf(f.getMethod(), f.getSelf());
  }

  /**
   * <p>A wrapper that calls the given arbitrary method on the given
   * object.</p>
   */
  public static VoidToVoid bindSelf(final Method method, final Object self) {
    assert 0 == method.getParameterTypes().length;
    method.setAccessible(true);
    return new VoidToVoid() {
        public void with() {
          try {
            method.invoke(self, null);
          } catch (Exception e) {
            throw Exceptions.toThrowUnchecked(e);
          }
        }};
  }
}
