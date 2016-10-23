package javautils.jdbc;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javautils.Classes;
import javautils.Exceptions;
import javautils.collections.Algs;
import javautils.fun.Function;
import javautils.fun.VoidToVoid;

/**
 * <p>Static utility methods for dealing with JDBC.</p>
 */
public class JDBC extends Algs {

  /**
   * <p>Performs a transaction. The intention of this method is to
   * simplify the implementation of transactions.</p>
   *
   * <p>This method:</p>
   * <ol>
   * <li>Checks that auto commit (of the given connection) is <em>on</em>.
   * If auto commit is <em>off</em>, throws a runtime exception, because
   * it may indicate a nested transaction.</li>
   * <li>Sets auto commit <em>off</em>.</li>
   * <li>Calls the given procedure. The intention is that the procedure
   * performs the necessary SQL operations on the (same) connection. If
   * the transaction fails, the procedure should throw an exception.</li>
   * <li>If the procedure returns normally, the transaction is committed.
   * Otherwise the procedure has thrown an exception. In that case, the
   * transaction is rolled back and the exception is rethrown.</li>
   * <li>Finally auto commit is set <em>on</em>.</li>
   * </ol>
   *
   * <p>Note that this method does not automatically roll back any
   * mutations made to the application state. The mutations made to the
   * database through the connection are rolled back in case the
   * transaction procedure throws an exception.</p>
   */
  public static void executeTransaction(Connection connection, Function nullaryProc) {
    VoidToVoid proc = VoidToVoid.from(nullaryProc);

    if (!getAutoCommit(connection))
      throw new RuntimeException("Attempted to perform a nested transactions.");

    setAutoCommit(connection, false);

    try {
      proc.with();
      commit(connection);
    } catch (Exception e) {
      rollback(connection);
      throw Exceptions.toThrowUnchecked(e);
    } finally {
      setAutoCommit(connection, true);
    }
  }

  /**
   * <p>Folds a function into the row sequence of the result set.</p>
   *
   * <p>The accumulator is passed as the first argument to the function.
   * The return value from the function then replaces the accumulator.
   * Finally the value of the accumulator is returned from fold.</p>
   *
   * <p>The columns of the result set are matched to the arguments of the
   * function. The second argument of the function gets the value of the
   * first column, the third arguments gets the second column, etc... The
   * type of the argument determines the getter used to get the value of
   * column from the result set. Basically, if the type of an argument is
   * a primitive type, then a getter with the same primitive type will be
   * used. Otherwise either the <code>getObject(int i)</code> or the
   * getter of the specific non-primitive type will be used.</p>
   *
   * <p>Suppose that you have a table that maps names (strings) to ages
   * (integers). To make a simple list of the table, you could use code
   * like this:</p>
   *
   * <pre>
   * String result =
   *   {@link javautils.jdbc.JDBC#fold fold}(new <b>StringBuffer</b>("&lt;dl&gt;\n"),
   *        stmt.executeQuery(" SELECT name " +
   *                          "      , age " +
   *                          " FROM players " +
   *                          " ORDER BY name DESC "),
   *        new {@link javautils.fun.Function}() {
   *          public <b>StringBuffer</b> with(<b>StringBuffer</b> accumulator,
   *                                   <b>String</b> name,
   *                                   <b>int</b> age) {
   *            accumulator.append("  &lt;dt&gt;" + name + "&lt;/dt&gt;\n" +
   *                               "  &lt;dd&gt;" + age + "&lt;/dd&gt;\n");
   *            <b>return</b> accumulator;
   *          }}) + "&lt;/dl&gt;\n";
   * </pre>
   */
  public static Object fold(Object accumulator, ResultSet rs, Function fun) {
    Method method = fun.getMethod();
    Object self = fun.getSelf();

    Class[] parameterTypes = method.getParameterTypes();

    Method[] getMethods = getMethods(parameterTypes);
    Object[] actuals = new Object[parameterTypes.length];

    Integer[][] indice = indice(parameterTypes.length - 1);

    actuals[0] = accumulator;

    try {
      while (rs.next()) {
        for (int i=1; i<actuals.length; ++i)
          actuals[i] = getMethods[i].invoke(rs, indice[i]);

        actuals[0] = method.invoke(self, actuals);
      }
    } catch (Exception e) {
      throw Exceptions.toThrowUnchecked(e);
    }
    return actuals[0];
  }

  /**
   * <p>Executes a procedure for each row of the result set.</p>
   *
   * <p>The columns of the result set are matched to the arguments of the
   * procedure. The first argument of the procedure gets the value of the
   * first column, the second arguments gets the second column, etc... The
   * type of the argument determines the getter used to get the value of
   * column from the result set. Basically, if the type of an argument is
   * a primitive type, then a getter with the same primitive type will be
   * used. Otherwise either the <code>getObject(int i)</code> or the
   * getter of the specific non-primitive type will be used.</p>
   *
   * <p>Suppose that you have a table that maps names (strings) to ages
   * (integers). To make a simple list of the table, you could use code
   * like this:</p>
   *
   * <pre>
   * <b>final</b> StringBuffer result = new StringBuffer("&lt;dl&gt;\n");
   *
   * {@link javautils.jdbc.JDBC#forEach forEach}(stmt.executeQuery(" SELECT name, age " +
   *                           " FROM players " +
   *                           " ORDER BY name DESC "),
   *         new {@link javautils.fun.Function}() {
   *           public <b>void</b> with(<b>String</b> name, <b>int</b> age) {
   *             result.append("  &lt;dt&gt;" + name + "&lt;/dt&gt;\n" +
   *                           "  &lt;dd&gt;" + age + "&lt;/dd&gt;\n");
   *           }});
   *
   * result.append("&lt;/dl&gt;\n");
   * </pre>
   */
  public static void forEach(ResultSet rs, Function proc) {
    Method method = proc.getMethod();
    Object self = proc.getSelf();

    Class[] parameterTypes = method.getParameterTypes();

    Method[] getMethods = getMethods(parameterTypes);
    Object[] actuals = new Object[parameterTypes.length];

    Integer[][] indice = indice(parameterTypes.length);

    try {
      while (rs.next()) {
        for (int i=0; i<actuals.length; ++i)
          actuals[i] = getMethods[i].invoke(rs, indice[1 + i]);

        method.invoke(self, actuals);
      }
    } catch (Exception e) {
      throw Exceptions.toThrowUnchecked(e);
    }
  }

  private static void commit(Connection connection) {
    try {
      connection.commit();
    } catch (SQLException e) {
      throw Exceptions.toThrowUnchecked(e);
    }
  }

  private static boolean getAutoCommit(Connection connection) {
    try {
      return connection.getAutoCommit();
    } catch (SQLException e) {
      throw Exceptions.toThrowUnchecked(e);
    }
  }

  private static void rollback(Connection connection) {
    try {
      connection.rollback();
    } catch (SQLException e) {
      throw Exceptions.toThrowUnchecked(e);
    }
  }

  private static void setAutoCommit(Connection connection, boolean state) {
    try {
      connection.setAutoCommit(state);
    } catch (SQLException e) {
      throw Exceptions.toThrowUnchecked(e);
    }
  }

  private static Method[] getMethods(Class[] types) {
    Method[] result = new Method[types.length];
    for (int i=0; i<types.length; ++i)
      result[i] = (Method)TYPE_TO_GET_METHOD_MAP.get(TYPE_TO_GET_METHOD_MAP.containsKey(types[i])
                                                     ? types[i]
                                                     : Object.class);
    return result;
  }

  private static final Map TYPE_TO_GET_METHOD_MAP = new HashMap();

  static {
    forEach(ResultSet.class.getMethods(),
            new Function() {
              public void with(Method m) {
                if (m.getName().startsWith("get") &&
                    1 == m.getParameterTypes().length &&
                    int.class.equals(m.getParameterTypes()[0]))
                  TYPE_TO_GET_METHOD_MAP.put(m.getReturnType(), m);
              }});
  }

  // TBD: Optimize using double checked locking?
  private static synchronized Integer[][] indice(int max) {
    if (indiceCache.length <= max) {
      indiceCache = new Integer[max + 1][];

      for (int i=0; i<=max; ++i)
        indiceCache[i] = new Integer[]{new Integer(i)};
    }
    return indiceCache;
  }

  private static Integer[][] indiceCache = new Integer[][]{};
}
