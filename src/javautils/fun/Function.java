package javautils.fun;

import java.lang.reflect.Method;
import javautils.Classes;
import javautils.Exceptions;

/**
 * <p>An arbitrary function or procedure.</p>
 *
 * <p>The intention of this class is to help emulate anonymous functions,
 * also known as lambda-expressions, using anonymous inner classes. The
 * user derives from this class and defines a public
 * <code>with()</code>-method. The <code>with()</code>-method can then be
 * accessed using the services of this base class.</p>
 *
 * <p>It usually makes sense to derive a more specific abstract function
 * class from this class, so that the function signature can be type
 * checked more thoroughly and that calling the function becomes more
 * convenient and efficient.</p>
 *
 * <p>Among many other things, [<a
 * href="{@docRoot}/overview-summary.html#[Abelson1995]">Abelson1995</a>]
 * introduces functional programming and lambda-expressions in the context
 * of Scheme. Anonymous functions are also related to the Strategy-design
 * pattern described in [<a
 * href="{@docRoot}/overview-summary.html#[Gamma1995]">Gamma1995</a>].</p>
 */
public class Function {

  /**
   * <p>A new function bound to this object and the
   * <code>with()</code>-method defined by the derived class.</p>
   */
  protected Function() {
    this.self = this;

    Method[] methods = getClass().getMethods();

    for (int i=0; i<methods.length; ++i) {
      if ("with".equals(methods[i].getName())) {
        this.method = methods[i];
        this.method.setAccessible(true);
        return;
      }
    }
    throw new RuntimeException("No public `with()' method defined in the derived Function class.");
  }

  /**
   * <p>A new function bound to the specified object and named public
   * method.</p>
   */
  public Function(Object self, String methodName) {
    this(self, methodName, (Class[])null);
  }

  /**
   * <p>A new function bound to the specified object and named public
   * method.</p>
   */
  public Function(Object self, String methodName, Class argType) {
    this(self, methodName, new Class[]{argType});
  }

  /**
   * <p>A new function bound to the specified object and named public
   * method.</p>
   */
  public Function(Object self, String methodName, Class[] argTypes) {
    this(self, Classes.getMethod(self, methodName, argTypes));
  }

  /**
   * <p>A new function bound to the specified object and method.</p>
   */
  public Function(Object self, Method method) {
    this.self = self;
    this.method = method;

    method.setAccessible(true);
  }

  /**
   * <p>Invokes the bound method on the bound object.</p>
   *
   * <p>Any checked exceptions thrown by the bound method will be thrown
   * wrapped inside {@link java.lang.RuntimeException}-exceptions.</p>
   */
  public Object invoke(Object[] args) {
    try {
      return method.invoke(self, args);
    } catch (Exception e) {
      throw Exceptions.toThrowUnchecked(e);
    }
  }

  /**
   * <p>The bound method.</p>
   */
  public final Method getMethod() {
    return method;
  }

  /**
   * <p>The bound object.</p>
   */
  public final Object getSelf() {
    return self;
  }

  private final Object self;
  private final Method method;
}
