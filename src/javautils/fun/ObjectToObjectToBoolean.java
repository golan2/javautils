package javautils.fun;

import java.lang.reflect.Method;
import javautils.Classes;
import javautils.Exceptions;

/**
 * <p><code>Object->Object->boolean</code> function.</p>
 */
public abstract class ObjectToObjectToBoolean extends Function {
  public abstract boolean with(Object _0, Object _1);

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
  public static ObjectToObjectToBoolean from(final Function f) {
    return f instanceof ObjectToObjectToBoolean
      ? (ObjectToObjectToBoolean)f
      : bindSelf(f.getMethod(), f.getSelf());
  }

  /**
   * <p>A wrapper that calls the given arbitrary method on the given
   * object with the parameter given to the {@link #with}-method of the
   * wrapper.</p>
   */
  public static ObjectToObjectToBoolean bindSelf(final Method method, final Object self) {
    assert 2 == method.getParameterTypes().length;
    assert Classes.asObjectType(method.getReturnType()).equals(Boolean.class);
    method.setAccessible(true);
    return new ObjectToObjectToBoolean() {
        public boolean with(Object _0, Object _1) {
          try {
            return ((Boolean)method.invoke(self, new Object[]{_0, _1})).booleanValue();
          } catch (Exception e) {
            throw Exceptions.toThrowUnchecked(e);
          }
        }};
  }
}
