package javautils.fun;

import java.lang.reflect.Method;
import javautils.Exceptions;

/**
 * <p><code>Object->Object->void</code> procedure.</p>
 */
public abstract class ObjectToObjectToVoid extends Function {
  public abstract void with(Object _0, Object _1);

  public static final ObjectToObjectToVoid NOP =
    new ObjectToObjectToVoid() {
      public void with(Object _0, Object _1) {
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
  public static ObjectToObjectToVoid from(final Function f) {
    return f instanceof ObjectToObjectToVoid
      ? (ObjectToObjectToVoid)f
      : bindSelf(f.getMethod(), f.getSelf());
  }

  /**
   * <p>A wrapper that calls the given arbitrary method on the given
   * object with the parameter given to the {@link #with}-method of the
   * wrapper.</p>
   */
  public static ObjectToObjectToVoid bindSelf(final Method method, final Object self) {
    assert 2 == method.getParameterTypes().length;
    method.setAccessible(true);
    return new ObjectToObjectToVoid() {
        public void with(Object _0, Object _1) {
          try {
            method.invoke(self, new Object[]{_0,_1});
          } catch (Exception e) {
            throw Exceptions.toThrowUnchecked(e);
          }
        }};
  }
}
