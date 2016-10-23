package javautils.fun;

import java.lang.reflect.Method;
import javautils.Classes;
import javautils.Exceptions;

/**
 * <p><code>Object->boolean</code> function.</p>
 */
public abstract class ObjectToBoolean extends Function {
  public abstract boolean with(Object _0);

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
  public static ObjectToBoolean from(final Function f) {
    return f instanceof ObjectToBoolean
      ? (ObjectToBoolean)f
      : bindSelf(f.getMethod(), f.getSelf());
  }

  /**
   * <p>A wrapper that calls the given arbitrary method on the given
   * object with the parameter given to the {@link #with(Object)}-method
   * of the wrapper.</p>
   */
  public static ObjectToBoolean bindSelf(final Method method, final Object self) {
    assert 1 == method.getParameterTypes().length;
    assert Classes.asObjectType(method.getReturnType()).equals(Boolean.class);
    method.setAccessible(true);
    return new ObjectToBoolean() {
        public boolean with(Object _0) {
          try {
            return ((Boolean)method.invoke(self, new Object[]{_0})).booleanValue();
          } catch (Exception e) {
            throw Exceptions.toThrowUnchecked(e);
          }
        }};
  }
}
