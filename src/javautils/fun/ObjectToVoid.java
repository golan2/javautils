package javautils.fun;

import java.lang.reflect.Method;
import javautils.Exceptions;

/**
 * <p><code>Object->void</code> procedure.</p>
 */
public abstract class ObjectToVoid extends Function {
  public abstract void with(Object _0);

  public static final ObjectToVoid NOP =
    new ObjectToVoid() {
      public void with(Object _0) {
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
  public static ObjectToVoid from(final Function f) {
    return f instanceof ObjectToVoid
      ? (ObjectToVoid)f
      : bindSelf(f.getMethod(), f.getSelf());
  }

  /**
   * <p>A wrapper that calls the given arbitrary method on the {@link
   * java.lang.Object} given as a parameter to the {@link
   * #with(Object)}-method with the given arguments.</p>
   */
  public static ObjectToVoid bindArgs(final Method method, final Object[] args) {
    assert (null == args ? 0 : args.length) == method.getParameterTypes().length;
    method.setAccessible(true);
    return new ObjectToVoid() {
        public void with(Object self) {
          try {
            method.invoke(self, args);
          } catch (Exception e) {
            throw Exceptions.toThrowUnchecked(e);
          }
        }};
  }

  /**
   * <p>A wrapper that calls the given arbitrary method on the given
   * object with the parameter given to the {@link #with(Object)}-method
   * of the wrapper.</p>
   */
  public static ObjectToVoid bindSelf(final Method method, final Object self) {
    assert 1 == method.getParameterTypes().length;
    method.setAccessible(true);
    return new ObjectToVoid() {
        public void with(Object _0) {
          try {
            method.invoke(self, new Object[]{_0});
          } catch (Exception e) {
            throw Exceptions.toThrowUnchecked(e);
          }
        }};
  }
}
