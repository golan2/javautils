package javautils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * <p>Static utility methods for dealing with classes and reflection.</p>
 *
 * <p><b>Note</b>: None of these methods throws checked exceptions.
 * Checked exceptions will be translated to unchecked {@link
 * RuntimeException}-exceptions. This makes these methods far more
 * pleasant to use compared to direct use of the {@link Class}-class.</p>
 */
public class Classes {

  // NOTE: Please keep the methods in this class in alphabetical order.
  // These methods are mostly unrelated. Keeping the methods in
  // alphabetical order makes it easier to locate individual methods.

  /**
   * <p>If the given type is a primitive type (say
   * <code>int.class</code>), returns the corresponding object type (say
   * <code>Integer.class</code>). Otherwise returns the given type.</p>
   */
  public static Class asObjectType(Class type) {
    if (type.isPrimitive())
      for (int i=0; i<PRIMITIVE_TYPE_TABLE.length; ++i)
        if (PRIMITIVE_TYPE_TABLE[i][0].equals(type))
          return PRIMITIVE_TYPE_TABLE[i][1];
    return type;
  }

  /**
   * <pre>
   * <b>return</b> Class.forName(name);
   * </pre>
   */
  public static Class forName(String name) {
    try {
      for (int i=0; i<PRIMITIVE_TYPE_TABLE.length; ++i)
        if (PRIMITIVE_TYPE_TABLE[i][0].getName().equals(name))
          return PRIMITIVE_TYPE_TABLE[i][0];

      return Class.forName(name);
    } catch (Exception e) {
      throw Exceptions.toThrowUnchecked(e);
    }
  }

  private static final Class[][] PRIMITIVE_TYPE_TABLE =
    new Class[][]{{boolean.class, Boolean.class},
                  {byte.class,    Byte.class},
                  {char.class,    Character.class},
                  {double.class,  Double.class},
                  {float.class,   Float.class},
                  {int.class,     Integer.class},
                  {long.class,    Long.class},
                  {short.class,   Short.class},
                  {void.class,    void.class}};

  /**
   * <p>Returns the last part (the part after the last dot) of the type
   * name.</p>
   */
  public static String getBaseName(Class cls) {
    String name = cls.getName();
    return name.substring(name.lastIndexOf('.') + 1);
  }

  /**
   * <pre>
   * <b>return</b> cls.getField(cls, fieldName);
   * </pre>
   */
  public static Field getField(Class cls, String fieldName) {
    try {
      return cls.getField(fieldName);
    } catch (Exception e) {
      throw Exceptions.toThrowUnchecked(e);
    }
  }

  /**
   * <pre>
   * <b>return</b> obj.getClass().getField(fieldName).get(obj);
   * </pre>
   */
  public static Object getFieldValue(Object obj, String fieldName) {
    try {
      return obj.getClass().getField(fieldName).get(obj);
    } catch (Exception e) {
      throw Exceptions.toThrowUnchecked(e);
    }
  }

  /**
   * <pre>
   * <b>return</b> {@link #getMethod(Class,String,Class[]) getMethod}(cls, methodName, null);
   * </pre>
   */
  public static Method getMethod(Class cls, String methodName) {
    return getMethod(cls, methodName, null);
  }

  /**
   * <pre>
   * <b>return</b> cls.getMethod(methodName, argTypes);
   * </pre>
   */
  public static Method getMethod(Class cls, String methodName, Class[] argTypes) {
    try {
      return cls.getMethod(methodName, argTypes);
    } catch (Exception e) {
      throw Exceptions.toThrowUnchecked(e);
    }
  }

  /**
   * <pre>
   * <b>return</b> {@link #getMethod(Class,String,Class[]) getMethod}(obj.getClass(), methodName, argTypes);
   * </pre>
   */
  public static Method getMethod(Object obj, String methodName, Class[] argTypes) {
    return getMethod(obj.getClass(), methodName, argTypes);
  }

  /**
   * <pre>
   * <b>return</b> cls.getField(fieldName).get(null);
   * </pre>
   */
  public static Object getStaticFieldValue(Class cls, String fieldName) {
    try {
      return cls.getField(fieldName).get(null);
    } catch (Exception e) {
      throw Exceptions.toThrowUnchecked(e);
    }
  }

  /**
   * <pre>
   * <b>return</b> method.invoke(self, args);
   * </pre>
   */
  public static Object invoke(Method method, Object self, Object[] args) {
    try {
      return method.invoke(self, args);
    } catch (Exception e) {
      throw Exceptions.toThrowUnchecked(e);
    }
  }

  /**
   * <pre>
   * <b>return</b> cls.newInstance();
   * </pre>
   */
  public static Object newInstance(Class cls) {
    return newInstance(cls, null, null);
  }

  /**
   * <pre>
   * <b>return</b> cls.getConstructor(paramTypes).newInstance(params);
   * </pre>
   */
  public static Object newInstance(Class cls, Class[] paramTypes, Object[] params) {
    try {
      return cls.getConstructor(paramTypes).newInstance(params);
    } catch (Exception e) {
      throw Exceptions.toThrowUnchecked(e);
    }
  }
}
