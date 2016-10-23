package javautils.fun;

import java.lang.reflect.Method;
import javautils.Exceptions;

/**
 * <p><code>Object->Object</code> function.</p>
 */
public abstract class ObjectToObject extends Function {
  public abstract Object with(Object _0);

  public static final ObjectToObject IDENTITY = new ObjectToObject() {
      public Object with(Object o) {
        return o;
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
  public static ObjectToObject from(final Function f) {
    return f instanceof ObjectToObject
      ? (ObjectToObject)f
      : bindSelf(f.getMethod(), f.getSelf());
  }

  /**
   * <p>A wrapper that calls the given arbitrary method on the object
   * given as a parameter to the {@link #with(Object)}-method with the
   * given arguments.</p>
   */
  public static ObjectToObject bindArgs(final Method method, final Object[] args) {
    assert (null == args ? 0 : args.length) == method.getParameterTypes().length;
    method.setAccessible(true);
    return new ObjectToObject() {
        public Object with(Object self) {
          try {
            return method.invoke(self, args);
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
  public static ObjectToObject bindSelf(final Method method, final Object self) {
    assert 1 == method.getParameterTypes().length;
    method.setAccessible(true);
    return new ObjectToObject() {
        public Object with(Object _0) {
          try {
            return method.invoke(self, new Object[]{_0});
          } catch (Exception e) {
            throw Exceptions.toThrowUnchecked(e);
          }
        }};
  }
}
