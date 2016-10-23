package javautils.collections;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import javautils.Exceptions;
import javautils.collections.AbstractIterator;
import javautils.dispensers.Stack;
import javautils.fun.Function;
import javautils.fun.ObjectToBoolean;
import javautils.fun.ObjectToObject;
import javautils.fun.ObjectToObjectToBoolean;
import javautils.fun.ObjectToObjectToObject;
import javautils.fun.ObjectToObjectToVoid;
import javautils.fun.ObjectToVoid;

/**
 * <p>Utility methods for manipulating sequences of elements.</p>
 *
 * <h3>Higher-order methods</h3>
 *
 * <p>Many of the methods in this class are higher-order methods. The
 * intention of higher-order methods is to capture common computational
 * patterns into concrete implementations that are parameterized by
 * anonymous functional objects. Careful use of higher-order order methods
 * can raise the abstraction level of a program significantly. Among many
 * other things, [<a
 * href="{@docRoot}/overview-summary.html#[Abelson1995]">Abelson1995</a>]
 * discusses a number of higher-order functions in the context of
 * Scheme.</p>
 *
 * <p>Higher-order methods of the following kind are defined:</p>
 * <dl>
 * <dt>{@link #exists(Iterator,Function) exists}</dt>
 * <dd>Tests whether a predicate holds for at least one element of a
 * sequence.</dd>
 * <dt>{@link #filter(Iterator,Function) filter}</dt>
 * <dd>Filters elements from a sequence to form a new sequence.</dd>
 * <dt>{@link #find(Iterator,Function) find}</dt>
 * <dd>Finds an element from a sequence.</dd>
 * <dt>{@link #fold(Object,Iterator,Function) fold}</dt>
 * <dd>Perform an arbitrary primitive recursive computation on the
 * elements of a sequence.</dd>
 * <dt>{@link #foldRight(ListIterator,Object,Function) foldRight}</dt>
 * <dd>Perform an arbitrary primitive recursive computation on the
 * elements of a sequence considering values in reverse order.</dd>
 * <dt>{@link #forAll(Iterator,Function) forAll}</dt>
 * <dd>Tests whether a predicate holds for all elements of a sequence.</dd>
 * <dt>{@link #forEach(Iterator,Function) forEach}</dt>
 * <dd>Perform a procedure for each element of a sequence.</dd>
 * <dt>{@link #forEach(Iterator,Iterator,Function) forEach}</dt>
 * <dd>Perform a procedure simultaneously for each element of two
 * collections.</dd>
 * <dt>{@link #genForEach(Iterator[],Function) genForEach}</dt>
 * <dd>Generalized version of forEach that handles arbitrarily many
 * collections.</dd>
 * <dt>{@link #forEachInProduct(Collection,Collection,Function) forEachInProduct}</dt>
 * <dd>Perform a procedure for each member (ordered pair) of the cartesian
 * product of two collections.</dd>
 * <dt>{@link #map(Iterator,Function) map}</dt>
 * <dd>Map each element of a collection to form a new collection.</dd>
 * <dt>{@link #mapMorphism mapMorphism}</dt>
 * <dd>Make a morphic copy of the map, with keys and values
 * transformed.</dd>
 * <dt>{@link #mapMorphismTo mapMorphismTo}</dt>
 * <dd>Make a morphic copy of the map, with keys and values
 * transformed, to another map.</dd>
 * <dt>{@link #mapTransform mapTransform}</dt>
 * <dd>Transform each value of a map in place.
 * (The keys are left untouched.)</dd>
 * <dt>{@link #select(Object,Iterator,Function) select}</dt>
 * <dd>Select an element from a collection.</dd>
 * <dt>{@link #sort(List,Function) sort}</dt>
 * <dd>Sort a list in place.</dd>
 * <dt>{@link #sorted(Iterator,Function) sorted}</dt>
 * <dd>Produce a sorted copy of a sequence.</dd>
 * <dt>{@link #transform(ListIterator,Function) transform}</dt>
 * <dd>Transform each element of a list in place.</dd>
 * </dl>
 *
 * <h3>Laziness</h3>
 *
 * <p>Many of the methods in this class are designed to be lazy. For
 * example, the {@link #map(Iterator,Function) map}-method does not
 * immediately call the specified function on every element of the given
 * sequence, but it instead returns a new sequence, as an {@link
 * java.util.Iterator}, whose traversal performs the operations. The
 * intention behind the laziness is to avoid instantiating collections
 * until it is actually necessary. In many cases it isn't necessary to
 * instantiate a collection at all.</p>
 *
 * <p>To actually instantiate a sequence into a collection, you can use
 * one of the following methods.</p>
 * <ul>
 * <li>{@link #collect(Iterator)}</li>
 * <li>{@link #collectMap(Iterator)}</li>
 * <li>{@link #collectSet(Iterator)}</li>
 * <li>{@link #addAll(Iterator,Collection)}</li>
 * <li>{@link #putAll(Iterator,Map)}</li>
 * </ul>
 *
 * <p>Laziness also makes it possible to manipulate infinite sequences of
 * elements. The lazy iterators used here are similar to lazy streams
 * described in [<a
 * href="{@docRoot}/overview-summary.html#[Abelson1995]">Abelson1995</a>].
 * The main difference is that Java {@link java.util.Iterator}s are very
 * transparently imperative, which can make their use more difficult.</p>
 *
 * <h3>Side-effects</h3>
 *
 * <p>Most of the higher-order methods in this class are designed to work
 * with side-effect free <i>functions</i> (<code>?->Object</code>) and
 * <i>predicates</i> (<code>?->Boolean</code>). The higher-order methods
 * that are designed to work with side-effecting <i>procedures</i>
 * (<code>?->Void</code>) are easy to spot, because their result type is
 * <code>void</code> and the functional argument is named
 * <code>proc</code>.</p>
 *
 * <h3>Why should I use higher-order methods instead of loops?</h3>
 *
 * <p>Because the name of the higher-order method immediately communicates
 * the <em>intention</em> of the code at a higher level. For example, the
 * name {@link #filter filter} immediately tells that the intention is to
 * select elements from a sequence based on a predicate. Similarly, the
 * name {@link #exists exists} immediately tells that the intention is to
 * test whether some property holds for at least one element of a
 * sequence. A loop tells that the code performs iteration, but it does
 * not immediately communicate the intention behind the loop.</p>
 *
 * <h3>Example: Finding the first {@link java.lang.String} in a list that
 * begins with <code>"prefix"</code></h3>
 *
 * <pre>
 * String firstWithPrefix = (String)
 *   {@link #find(Iterator,Function) find}(listOfStrings,
 *        <b>new</b> {@link javautils.fun.Function}() {
 *          <b>public</b> boolean with(String some) {
 *            <b>return</b> some.startsWith("prefix");
 *          }});
 * </pre>
 *
 * <h3>Example: A method for filtering all classes from a list that
 * implement the given interface</h3>
 *
 * <pre>
 * public static List allClassesImplementing(List listOfClasses, <b>final</b> Class <i>anInterfaceClass</i>) {
 *   return {@link #collect(Iterator) collect}({@link #filter(Iterator,Function) filter}(listOfClasses,
 *                         <b>new</b> {@link javautils.fun.Function}() {
 *                           <b>public</b> boolean with(Class someClass) {
 *                             <b>return</b> null != someClass &amp;&amp;
 *                               (someClass.equals(<i>anInterfaceClass</i>) ||
 *                                with(someClass.getSuperclass()) ||
 *                                {@link #exists(Iterator,Function) exists}(someClass.getInterfaces(),
 *                                       <b>new</b> {@link javautils.fun.Function#Function(Object,String,Class) Function}(this, "with", Object.class)));
 *                           }}));
 * }
 * </pre>
 *
 * <h3>Example: Summing a list of {@link java.lang.Integer}s</h3>
 *
 * <pre>
 * Integer sum = (Integer)
 *   {@link #fold(Object,Iterator,Function) fold}(new Integer(0),
 *        listOfIntegers,
 *        <b>new</b> {@link javautils.fun.Function}() {
 *          <b>public</b> int with(int sum, int some) {
 *            <b>return</b> sum + some;
 *          }});
 * </pre>
 *
 * <h3>Example: Testing whether all {@link java.lang.Integer}s in a list
 * are positive</h3>
 *
 * <pre>
 * boolean allPositive = {@link #forAll(Iterator,Function) forAll}(listOfIntegers,
 *                              <b>new</b> {@link javautils.fun.Function}() {
 *                                <b>public</b> boolean with(int some) {
 *                                  <b>return</b> some &gt; 0;
 *                                }});
 * </pre>
 *
 * <h3>Example: Rendering lines between all consecutive pairs of points in
 * an array</h3>
 *
 * <pre>
 * {@link #forEach(Iterator,Iterator,Function) forEach}({@link #iterator(Object[],int) iterator}(points, 0),
 *         {@link #iterator(Object[],int) iterator}(points, 1),
 *         <b>new</b> {@link javautils.fun.Function}() {
 *           <b>public</b> void with(Point x0, Point x1) {
 *             g.drawLine(x0.x, x0.y, x1.x, x1.y);
 *           }});
 * </pre>
 *
 * @see javautils.fun.Function
 */
public class Algs {

  // NOTE: Please keep the methods in this class in alphabetical order.
  // These methods are mostly unrelated. Keeping the methods in
  // alphabetical order makes it easier to locate individual methods.

  /**
   * <p>Empty, unmodifiable list.</p>
   */
  public static final List EMPTY_LIST = Collections.EMPTY_LIST;

  /**
   * <p>Empty, unmodifiable map.</p>
   */
  public static final Map EMPTY_MAP = Collections.EMPTY_MAP;

  /**
   * <p>Empty, unmodifiable array.</p>
   */
  public static final Object[] EMPTY_ARRAY = new Object[0];

  /**
   * <p>Empty, unmodifiable sequences.</p>
   */
  public static final Iterator EMPTY_SEQUENCE = new AbstractIterator() {
      public boolean hasNext() { return false; }
      public Object next() { throw new NoSuchElementException(); }
    };

  /**
   * <p>Empty, unmodifiable set.</p>
   */
  public static final Set EMPTY_SET = Collections.EMPTY_SET;

  /**
   * <p>Adds all elements from the sequence to the collection.</p>
   */
  public static void addAll(Iterator from, Collection to) {
    while (from.hasNext())
      to.add(from.next());
  }

  /**
   * <p>An iterator over <i>all</i> super interfaces of the specified class.</p>
   */
  public static Iterator allSuperInterfaces(Class cls) {
    return null == cls
      ? EMPTY_SEQUENCE
      : flatten(new Object[]{map(cls.getInterfaces(),
                                 new Function() {
                                   public Object[] with(Class ifc) {
                                     return new Object[]{ifc, allSuperInterfaces(ifc)};
                                   }}),
                             allSuperInterfaces(cls.getSuperclass())});
  }

  /**
   * <p>Converts a collection of {@link java.lang.Integer}-objects to an
   * <code>int</code>-array.</p>
   */
  public static int[] asArray(Collection from, int[] to) {
    return asArray((Integer[])from.toArray(new Integer[from.size()]), to);
  }

  /**
   * <p>Converts an array of {@link java.lang.Integer}-objects to an
   * <code>int</code>-array.</p>
   */
  public static int[] asArray(Integer[] from, int[] to) {
    if (to.length != from.length)
      to = new int[from.length];

    for (int i=0; i<from.length; ++i)
      to[i] = from[i].intValue();

    return to;
  }

  /**
   * <p>Converts the binary predicate to a <code>Comparator</code>.</p>
   */
  public static Comparator asComparator(Function lessPred) {
    final ObjectToObjectToBoolean lessPrd = ObjectToObjectToBoolean.from(lessPred);
    return new Comparator() {
        public int compare(Object lhs, Object rhs) {
          return (lessPrd.with(lhs, rhs)
                  ? -1
                  : (lessPrd.with(rhs, lhs)
                     ? 1
                     : 0));
        }};
  }

  /**
   * <p>An unmodifiable list that references the specified arrays.
   * Modifications to the given array will be visible through the
   * list.</p>
   */
  public static List asUnmodifiableList(final Object[] array) {
    return new AbstractList() {
        public Object get(int i) { return array[i]; }
        public int    size()     { return array.length; }
      };
  }

  /**
   * <p>Collects all elements from the sequence to a new list.</p>
   */
  public static List collect(Iterator from) {
    List result = new ArrayList();
    addAll(from, result);
    return result;
  }

  /**
   * <p>Collects a sequence of <code>Map.Entry</code>-objects into a new
   * map.</p>
   */
  public static Map collectMap(Iterator mapEntries) {
    Map result = new HashMap();
    putAll(mapEntries, result);
    return result;
  }

  /**
   * <p>Collects all elements from the sequence to a new set.</p>
   */
  public static Set collectSet(Iterator from) {
    Set result = new HashSet();
    addAll(from, result);
    return result;
  }

  /**
   * <p>Collects all elements into a new unmodifiable list.</p>
   */
  public static List collectUnmodifiable(Iterator from) {
    return asUnmodifiableList(collect(from).toArray());
  }

  /**
   * <p>A sequences that first iterates over the <code>lhs</code> sequence
   * and then over the <code>rhs</code> sequence.</p>
   */
  public static Iterator concat(final Iterator lhs, final Iterator rhs) {
    return new AbstractIterator() {
        public boolean hasNext() { return lhs.hasNext() || rhs.hasNext(); }
        public Object next() { return lhs.hasNext() ? lhs.next() : rhs.next(); }
      };
  }

  public static Iterator concat(Collection lhs, Collection rhs) { return concat(iterator(lhs), iterator(rhs)); }
  public static Iterator concat(Object[]   lhs, Object[]   rhs) { return concat(iterator(lhs), iterator(rhs)); }

  public static Object[]  copyOf(Object[]  array) { return (Object[]) copyOfArray(array); }
  public static boolean[] copyOf(boolean[] array) { return (boolean[])copyOfArray(array); }
  public static byte[]    copyOf(byte[]    array) { return (byte[])   copyOfArray(array); }
  public static char[]    copyOf(char[]    array) { return (char[])   copyOfArray(array); }
  public static double[]  copyOf(double[]  array) { return (double[]) copyOfArray(array); }
  public static float[]   copyOf(float[]   array) { return (float[])  copyOfArray(array); }
  public static int[]     copyOf(int[]     array) { return (int[])    copyOfArray(array); }
  public static long[]    copyOf(long[]    array) { return (long[])   copyOfArray(array); }
  public static short[]   copyOf(short[]   array) { return (short[])  copyOfArray(array); }

  /**
   * <p>A new copy of the given array.</p>
   */
  public static Object copyOfArray(Object array) {
    assert array.getClass().isArray();
    Object result = Array.newInstance(array.getClass().getComponentType(), Array.getLength(array));
    System.arraycopy(array, 0, result, 0, Array.getLength(array));
    return result;
  }

  /**
   * <p>Drops the first <code>n</code> elements from the sequence.</p>
   */
  public static Iterator drop(Iterator in, int n) {
    assert 0 <= n;
    while (n-- > 0 && in.hasNext())
      in.next();
    return in;
  }

  /**
   * <p>A new array that has the same type as the given array and whose
   * length is exactly the given length or the given array if it has the
   * given length.</p>
   */
  public static Object[] ensureLength(Object[] a, int length) {
    assert 0 <= length;
    return a.length == length
      ? a
      : (Object[])Array.newInstance(a.getClass().getComponentType(), length);
  }

  /**
   * <pre>
   * <b>while</b> (in.hasNext())
   *   <b>if</b> (pred.with(in.next()))
   *     <b>return true</b>;
   * <b>return false</b>;
   * </pre>
   */
  public static boolean exists(Iterator in, Function pred) {
    ObjectToBoolean prd = ObjectToBoolean.from(pred);
    while (in.hasNext())
      if (prd.with(in.next()))
        return true;
    return false;
  }

  public static boolean exists(Collection in, Function pred) { return exists(iterator(in), pred); }
  public static boolean exists(Object[]   in, Function pred) { return exists(iterator(in), pred); }

  /**
   * <p>A sequence of elements that contains the elements from the given
   * sequence for which <code>pred.with(element)</code> is true.</p>
   */
  public static Iterator filter(final Iterator from, Function pred) {
    final ObjectToBoolean prd = ObjectToBoolean.from(pred);

    return new Unfold() {
        private boolean more = true;
        private Object next;

        protected void    init() { advance(); }
        protected boolean more() { return more; }
        protected Object value() { return next; }

        protected void advance() {
          while (from.hasNext()) {
            next = from.next();
            if (prd.with(next))
              return;
          }
          more = false;
        }
      }.unfoldAsIterator();
  }

  public static Iterator filter(Collection from, Function pred) { return filter(iterator(from), pred); }
  public static Iterator filter(Object[]   from, Function pred) { return filter(iterator(from), pred); }

  /**
   * <pre>
   * <b>while</b> (in.hasNext()) {
   *   Object o = in.next();
   *   <b>if</b> (pred.with(o))
   *     <b>return</b> o;
   * }
   * <b>return null</b>;
   * </pre>
   */
  public static Object find(Iterator in, Function pred) {
    ObjectToBoolean prd = ObjectToBoolean.from(pred);
    while (in.hasNext()) {
      Object o = in.next();
      if (prd.with(o))
        return o;
    }
    return null;
  }

  public static Object find(Collection in, Function pred) { return find(iterator(in), pred); }
  public static Object find(Object[]   in, Function pred) { return find(iterator(in), pred); }

  /**
   * <p>Flattens the sequence by recursively flattening all collections
   * (of type <code>Collection</code>), arrays (including arrays of
   * primitive types) and sequences (of type <code>Iterator</code>).</p>
   *
   * <p><b>Note</b>: This method will not work if the given sequence
   * structure contains a cycle. Cycle detection would make this method
   * significantly slower.</p>
   */
  public static Iterator flatten(Iterator i) {
    final Stack stack = new Stack();
    stack.push(i);

    return new Unfold() {
        private boolean more = true;
        private Object next;

        protected void    init() { advance(); }
        protected boolean more() { return more; }
        protected Object value() { return next; }

        protected void advance() {
          while (stack.notEmpty()) {
            Iterator top = (Iterator)stack.top();

            if (top.hasNext()) {
              next = top.next();

              if (next instanceof Collection)
                stack.push(iterator(((Collection)next)));
              else if (next instanceof Iterator)
                stack.push(next);
              else if (next.getClass().isArray())
                stack.push(iteratorOverArray(next, 0, Array.getLength(next)));
              else
                return;
            } else {
              stack.pop();
            }
          }
          more = false;
        }
      }.unfoldAsIterator();
  }

  public static Iterator flatten(Collection c) { return flatten(iterator(c)); }
  public static Iterator flatten(Object[]   a) { return flatten(iterator(a)); }

  /**
   * <pre>
   * <b>while</b> (rhs.hasNext())
   *   lhs = fun.with(lhs, rhs.next());
   * <b>return</b> lhs;
   * </pre>
   */
  public static Object fold(Object lhs, Iterator rhs, Function fun) {
    ObjectToObjectToObject fn = ObjectToObjectToObject.from(fun);
    while (rhs.hasNext())
      lhs = fn.with(lhs, rhs.next());
    return lhs;
  }

  public static Object fold(Object lhs, Collection rhs, Function fun) { return fold(lhs, iterator(rhs), fun); }
  public static Object fold(Object lhs, Object[]   rhs, Function fun) { return fold(lhs, iterator(rhs), fun); }

  /**
   * <pre>
   * <b>while</b> (lhs.hasPrevious())
   *   rhs = fun.with(lhs.previous(), rhs);
   * <b>return</b> rhs;
   * </pre>
   */
  public static Object foldRight(ListIterator lhs, Object rhs, Function fun) {
    ObjectToObjectToObject fn = ObjectToObjectToObject.from(fun);

    while (lhs.hasPrevious())
      rhs = fn.with(lhs.previous(), rhs);
    return rhs;
  }

  public static Object foldRight(List lhs, Object rhs, Function fun) { return foldRight(lhs.listIterator(lhs.size()), rhs, fun); }

  /**
   * <pre>
   * <b>for</b> (int i=lhs.length-1; 0<=i; --i)
   *   rhs = fun.with(lhs[i], rhs);
   * <b>return</b> rhs;
   * </pre>
   */
  public static Object foldRight(Object[] lhs, Object rhs, Function fun) {
    ObjectToObjectToObject fn = ObjectToObjectToObject.from(fun);
    for (int i=lhs.length-1; 0<=i; --i)
      rhs = fn.with(lhs[i], rhs);
    return rhs;
  }

  /**
   * <pre>
   * <b>while</b> (in.hasNext())
   *   <b>if</b> (&#33;pred.with(in.next()))
   *     <b>return false</b>;
   * <b>return true</b>;
   * </pre>
   */
  public static boolean forAll(Iterator in, Function pred) {
    ObjectToBoolean prd = ObjectToBoolean.from(pred);
    while (in.hasNext())
      if (!prd.with(in.next()))
        return false;
    return true;
  }

  public static boolean forAll(Collection in, Function pred) { return forAll(iterator(in), pred); }
  public static boolean forAll(Object[]   in, Function pred) { return forAll(iterator(in), pred); }

  /**
   * <pre>
   * <b>while</b> (in.hasNext())
   *   proc.with(in.next());
   * </pre>
   */
  public static void forEach(Iterator in, Function proc) {
    ObjectToVoid prc = ObjectToVoid.from(proc);
    while (in.hasNext())
      prc.with(in.next());
  }

  public static void forEach(Collection in, Function proc) { forEach(iterator(in), proc); }
  public static void forEach(Object[]   in, Function proc) { forEach(iterator(in), proc); }

  /**
   * <pre>
   * <b>while</b> (lhs.hasNext() &amp;&amp; rhs.hasNext())
   *   proc.with(lhs.next(), rhs.next());
   * </pre>
   */
  public static void forEach(Iterator lhs, Iterator rhs, Function proc) {
    ObjectToObjectToVoid prc = ObjectToObjectToVoid.from(proc);
    while (lhs.hasNext() && rhs.hasNext())
      prc.with(lhs.next(), rhs.next());
  }

  public static void forEach(Collection lhs, Collection rhs, Function proc) { forEach(iterator(lhs), iterator(rhs), proc); }
  public static void forEach(Object[]   lhs, Object[]   rhs, Function proc) { forEach(iterator(lhs), iterator(rhs), proc); }

  /**
   * <pre>
   * proc.with(lhs[0], rhs[0]);            proc.with(lhs[0], rhs[1]);            ... proc.with(lhs[0], rhs[rhs.size()-1]);
   * proc.with(lhs[1], rhs[0]);            proc.with(lhs[1], rhs[1]);            ... proc.with(lhs[1], rhs[rhs.size()-1]);
   * ...
   * proc.with(lhs[lhs.size()-1], rhs[0]); proc.with(lhs[lhs.size()-1], rhs[1]); ... proc.with(lhs[lhs.size()-1], rhs[rhs.size()-1]);
   * </pre>
   */
  public static void forEachInProduct(Collection lhs, Collection rhs, Function proc) { forEachInProduct(iterator(lhs), rhs.toArray(), proc); }
  public static void forEachInProduct(Iterator   lhs, Iterator   rhs, Function proc) { forEachInProduct(lhs, collect(rhs).toArray(), proc); }
  public static void forEachInProduct(Object[]   lhs, Object[]   rhs, Function proc) { forEachInProduct(iterator(lhs), rhs, proc); }

  public static void forEachInProduct(Iterator lhsI, Object[] rhs, Function proc) {
    ObjectToObjectToVoid prc = ObjectToObjectToVoid.from(proc);

    while (lhsI.hasNext()) {
      Object lhsItem = lhsI.next();

      for (int i=0; i<rhs.length; ++i)
        prc.with(lhsItem, rhs[i]);
    }
  }

  public static Collection genAddAll(Iterator[] is, Collection to) {
    for (int i=0; i<is.length; ++i)
      addAll(is[i], to);
    return to;
  }

  public static List genConcat(Collection[] cs) {
    return (List)genAddAll(iterators(cs), new ArrayList(size(cs)));
  }

  public static List genConcat(Object[][] cs) {
    return (List)genAddAll(iterators(cs), new ArrayList(size(cs)));
  }

  /**
   * <pre>
   * proc.invoke(new Object[]{cs[0][0],              cs[1][0],              ..., cs[cs.length-1][0]});
   * proc.invoke(new Object[]{cs[0][1],              cs[1][1],              ..., cs[cs.length-1][1]});
   * ...
   * proc.invoke(new Object[]{cs[0][cs[min].size()], cs[1][cs[min].size()], ..., cs[cs.length-1][cs[min].size()]});
   * </pre>
   */
  public static void genForEach(Collection[] cs, Function proc) { genForEach(iterators(cs), proc); }

  public static void genForEach(Iterator[] is, Function proc) {
    assert is.length == proc.getMethod().getParameterTypes().length;

    try {
      Object[] ps = new Object[is.length];
      Method m = proc.getMethod();
      Object s = proc.getSelf();

      while (hasNext(is)) {
        nextTo(is, ps);
        m.invoke(s, ps);
      }
    } catch (Exception e) {
      throw Exceptions.toThrowUnchecked(e);
    }
  }

  public static Object getOrIfNull(Map map, Object key, Object alternative) {
    Object result = map.get(key);

    return null != result
      ? result
      : alternative;
  }

  private static boolean hasNext(Iterator[] is) {
    for (int i=0; i<is.length; ++i)
      if (!is[i].hasNext())
        return false;
    return true;
  }

  /**
   * <p>Sequence of consecutive integers that starts at <code>from</code> and
   * ends just short of <code>to</code>. For example:</p>
   *
   * <pre>
   * integersInRange(75,75) => []
   * integersInRange(3,7) => [3,4,5,6]
   * integersInRange(1,-3) => [1,0,-1,-2]
   * </pre>
   */
  public static Iterator integersInRange(final int from, final int to) {
    final int sign = sign(to - from);

    return new Unfold() {
        private int next = from;

        public boolean more() { return next != to; }
        public Object value() { return new Integer(next); }
        public void advance() { next += sign; }
      }.unfoldAsIterator();
  }

  public static Iterator iterator(Collection c) {
    return c.iterator();
  }

  public static Iterator iterator(Object[]  array)                     { return iterator(array, 0, array.length); }
  public static Iterator iterator(Object[]  array, int begin)          { return iterator(array, begin, array.length); }
  public static Iterator iterator(Object[]  array, int begin, int end) { return iteratorOverArray(array, begin, end); }
  public static Iterator iterator(boolean[] array)                     { return iterator(array, 0, array.length); }
  public static Iterator iterator(boolean[] array, int begin)          { return iterator(array, begin, array.length); }
  public static Iterator iterator(boolean[] array, int begin, int end) { return iteratorOverArray(array, begin, end); }
  public static Iterator iterator(byte[]    array)                     { return iterator(array, 0, array.length); }
  public static Iterator iterator(byte[]    array, int begin)          { return iterator(array, begin, array.length); }
  public static Iterator iterator(byte[]    array, int begin, int end) { return iteratorOverArray(array, begin, end); }
  public static Iterator iterator(char[]    array)                     { return iterator(array, 0, array.length); }
  public static Iterator iterator(char[]    array, int begin)          { return iterator(array, begin, array.length); }
  public static Iterator iterator(char[]    array, int begin, int end) { return iteratorOverArray(array, begin, end); }
  public static Iterator iterator(double[]  array)                     { return iterator(array, 0, array.length); }
  public static Iterator iterator(double[]  array, int begin)          { return iterator(array, begin, array.length); }
  public static Iterator iterator(double[]  array, int begin, int end) { return iteratorOverArray(array, begin, end); }
  public static Iterator iterator(float[]   array)                     { return iterator(array, 0, array.length); }
  public static Iterator iterator(float[]   array, int begin)          { return iterator(array, begin, array.length); }
  public static Iterator iterator(float[]   array, int begin, int end) { return iteratorOverArray(array, begin, end); }
  public static Iterator iterator(int[]     array)                     { return iterator(array, 0, array.length); }
  public static Iterator iterator(int[]     array, int begin)          { return iterator(array, begin, array.length); }
  public static Iterator iterator(int[]     array, int begin, int end) { return iteratorOverArray(array, begin, end); }
  public static Iterator iterator(long[]    array)                     { return iterator(array, 0, array.length); }
  public static Iterator iterator(long[]    array, int begin)          { return iterator(array, begin, array.length); }
  public static Iterator iterator(long[]    array, int begin, int end) { return iteratorOverArray(array, begin, end); }
  public static Iterator iterator(short[]   array)                     { return iterator(array, 0, array.length); }
  public static Iterator iterator(short[]   array, int begin)          { return iterator(array, begin, array.length); }
  public static Iterator iterator(short[]   array, int begin, int end) { return iteratorOverArray(array, begin, end); }

  public static Iterator iteratorOverArray(final Object array, final int begin, final int end) {
    assert 0 <= begin && end <= Array.getLength(array);
    assert begin <= end;

    return new AbstractIterator() {
        private int i = begin;

        public boolean hasNext() {
          return i < end;
        }

        public Object next() {
          if (hasNext())
            return Array.get(array, i++);
          else
            throw new NoSuchElementException();
        }
      };
  }

  private static Iterator[] iterators(Collection[] cs) {
    Iterator[] result = new Iterator[cs.length];
    for (int i=0; i<cs.length; ++i)
      result[i] = iterator(cs[i]);
    return result;
  }

  private static Iterator[] iterators(Object[][] cs) {
    Iterator[] result = new Iterator[cs.length];
    for (int i=0; i<cs.length; ++i)
      result[i] = iterator(cs[i]);
    return result;
  }

  /**
   * <p>Maps the function to each element of the sequence producing a new
   * sequence.</p>
   */
  public static Iterator map(Iterator from, Function fun) {
    final ObjectToObject fn = ObjectToObject.from(fun);
    return new IteratorDecorator(from) {
        public Object next() {
          return fn.with(original.next());
        }};
  }

  public static Iterator map(Collection from, Function fun) { return map(iterator(from), fun); }
  public static Iterator map(Object[]   from, Function fun) { return map(iterator(from), fun); }

  /**
   * <p>Maps the function in parallel to elements from the given sequences
   * producing a new sequence.</p>
   */
  public static Iterator map(final Iterator lhs, final Iterator rhs, Function fun) {
    final ObjectToObjectToObject fn = ObjectToObjectToObject.from(fun);
    return new AbstractIterator() {
        public boolean hasNext() { return lhs.hasNext() && rhs.hasNext(); }
        public Object  next()    { return fn.with(lhs.next(), rhs.next()); }
      };
  }

  public static Iterator map(Collection lhs, Collection rhs, Function fun) { return map(iterator(lhs), iterator(rhs), fun); }
  public static Iterator map(Object[]   lhs, Object[]   rhs, Function fun) { return map(iterator(lhs), iterator(rhs), fun); }

  public static Map mapMorphism(Map map, Function fun) {
    ObjectToObjectToObject fn = ObjectToObjectToObject.from(fun);
    Map result = new HashMap();
    mapMorphismTo(map, fn, result);
    return result;
  }

  public static void mapMorphismTo(Map from, Function fun, Map to) {
    ObjectToObjectToObject fn = ObjectToObjectToObject.from(fun);
    Iterator i = iterator(from.entrySet());
    while (i.hasNext()) {
      Map.Entry original = (Map.Entry)i.next();
      Map.Entry result = (Map.Entry)fn.with(original.getKey(), original.getValue());
      to.put(result.getKey(), result.getValue());
    }
  }

  public static void mapTransform(Map map, Function fun) {
    ObjectToObjectToObject fn = ObjectToObjectToObject.from(fun);
    Iterator i = iterator(map.entrySet());
    while (i.hasNext()) {
      Map.Entry entry = (Map.Entry)i.next();
      entry.setValue(fn.with(entry.getKey(), entry.getValue()));
    }
  }

  /**
   * <p>The largest value of a sequence of {@link
   * java.lang.Comparable}-objects larger than the given initial value or
   * the initial value if the collection does not contain larger
   * values.</p>
   */

  public static Comparable max(Comparable initial, Iterator s) {
    while (s.hasNext()) {
      Comparable some = (Comparable)s.next();
      if (initial.compareTo(some) < 0)
        initial = some;
    }
    return initial;
  }

  public static Comparable max(Comparable initial, Collection s) { return max(initial, iterator(s)); }
  public static Comparable max(Comparable initial, Object[]   s) { return max(initial, iterator(s)); }
  public static double     max(double     initial, Collection s) { return ((Double)max(new Double(initial), iterator(s))).doubleValue(); }
  public static double     max(double     initial, Iterator   s) { return ((Double)max(new Double(initial), s)).doubleValue(); }
  public static int        max(int        initial, Collection s) { return ((Integer)max(new Integer(initial), iterator(s))).intValue(); }
  public static int        max(int        initial, Iterator   s) { return ((Integer)max(new Integer(initial), s)).intValue(); }

  /**
   * <p>The smallest value of a sequence of {@link
   * java.lang.Comparable}-objects smaller than the given initial value or
   * the initial value if the collection does not contain smaller
   * values.</p>
   */
  public static Comparable min(Comparable initial, Iterator s) {
    while (s.hasNext()) {
      Comparable some = (Comparable)s.next();
      if (initial.compareTo(some) > 0)
        initial = some;
    }
    return initial;
  }

  public static Comparable min(Comparable initial, Collection s) { return min(initial, iterator(s)); }
  public static Comparable min(Comparable initial, Object[]   s) { return min(initial, iterator(s)); }
  public static double     min(double     initial, Collection s) { return ((Double)min(new Double(initial), iterator(s))).doubleValue(); }
  public static double     min(double     initial, Iterator   s) { return ((Double)min(new Double(initial), s)).doubleValue(); }
  public static int        min(int        initial, Collection s) { return ((Integer)min(new Integer(initial), iterator(s))).intValue(); }
  public static int        min(int        initial, Iterator   s) { return ((Integer)min(new Integer(initial), s)).intValue(); }

  /**
   * <p>A new map with all bindings of the association table.</p>
   *
   * <p>The last element of each row of the association table is the value
   * to which all the other elements on the row are mapped to.</p>
   */
  public static Map newMap(Object[][] table) {
    Map result = new HashMap();
    putAll(table, result);
    return result;
  }

  /**
   * <p>A new 2D-array with the specified shape.</p>
   */
  public static Object[] newShapedArray(int[] shape, Class componentType) {
    Object[] result = (Object[])Array.newInstance(componentType, new int[]{shape.length, 0});
    for (int i=0; i<shape.length; ++i)
      result[i] = Array.newInstance(componentType, shape[i]);
    return result;
  }

  /**
   * <p>A new unmodifiable list that contains the same elements as the
   * given collection.</p>
   */
  public static List newUnmodifiableList(Collection c) {
    return asUnmodifiableList(c.toArray(new Object[c.size()]));
  }

  private static void nextTo(Iterator[] is, Object[] vs) {
    for (int i=0; i<is.length; ++i)
      vs[i] = is[i].next();
  }

  /**
   * <p>Puts all key-value bindings from the association table to the
   * map.</p>
   *
   * <p>The last element of each row of the association table is the value
   * to which all the other elements on the row are mapped to.</p>
   */
  public static void putAll(Object[][] table, Map to) {
    for (int i = 0; i < table.length; i++)
      for (int j = 0; j < table[i].length - 1; j++)
        to.put(table[i][j], table[i][table[i].length - 1]);
  }

  /**
   * <p>Puts a sequence of <code>Map.Entry</code>-objects to the map.</p>
   */
  public static void putAll(Iterator mapEntries, Map to) {
    while (mapEntries.hasNext()) {
      Map.Entry entry = (Map.Entry)mapEntries.next();
      to.put(entry.getKey(), entry.getValue());
    }
  }

  /**
   * <pre>
   * return reverse(l.listIterator(l.size()));
   * </pre>
   */
  public static ListIterator reverseIterator(List l) {
    return reverseIterator(l.listIterator(l.size()));
  }

  /**
   * <p>An iterator whose <code>previous</code> is the <code>next</code>
   * of the original iterator.</p>
   */
  public static ListIterator reverseIterator(final ListIterator original) {
    return new ListIterator() {
        public void    add(Object o)   { original.add(o); original.previous(); }
        public boolean hasNext()       { return original.hasPrevious(); }
        public boolean hasPrevious()   { return original.hasNext(); }
        public Object  next()          { return original.previous(); }
        public int     nextIndex()     { return original.previousIndex(); }
        public Object  previous()      { return original.next(); }
        public int     previousIndex() { return original.nextIndex(); }
        public void    remove()        { original.remove(); }
        public void    set(Object o)   { original.set(o); }
      };
  }

  /**
   * <pre>
   * <b>while</b> (rhs.hasNext()) {
   *   Object o = rhs.next();
   *   <b>if</b> (pred.with(lhs,o))
   *     lhs = o;
   * }
   * <b>return</b> lhs;
   * </pre>
   */
  public static Object select(Object lhs, Iterator rhs, Function pred) {
    ObjectToObjectToBoolean prd = ObjectToObjectToBoolean.from(pred);
    while (rhs.hasNext()) {
      Object o = rhs.next();
      if (prd.with(lhs,o))
        lhs = o;
    }
    return lhs;
  }

  public static Object select(Object lhs, Collection rhs, Function pred) { return select(lhs, iterator(rhs), pred); }
  public static Object select(Object lhs, Object[]   rhs, Function pred) { return select(lhs, iterator(rhs), pred); }

  /**
   * <p>The sign, <code>-1</code> or <code>1</code> of <code>x</code> or
   * <code>0</code>.</p>
   */
  public static int sign(int x) {
    return x < 0 ? -1 : 0 < x ? 1 : 0;
  }

  public static Iterator singletonIterator(final Object o) {
    return new AbstractIterator() {
        private boolean hasNext = true;

        public boolean hasNext() {
          return hasNext;
        }

        public Object next() {
          if (hasNext) {
            hasNext = false;

            return o;
          } else {
            throw new NoSuchElementException();
          }
        }
      };
  }

  private static int size(Collection[] cs) {
    int result = 0;
    for (int i=0; i<cs.length; ++i)
      result += cs[i].size();
    return result;
  }

  private static int size(Object[][] cs) {
    int result = 0;
    for (int i=0; i<cs.length; ++i)
      result += cs[i].length;
    return result;
  }

  /**
   * <p>Sorts the collection in-place according to the ordering imposed
   * by the predicate.</p>
   */
  public static void sort(List l, Function lessPred) { Collections.sort(l, asComparator(lessPred)); }

  /**
   * <p>Sorts the array in-place according to the ordering imposed
   * by the predicate.</p>
   */
  public static void sort(Object[] a, Function lessPred) { Arrays.sort(a, asComparator(lessPred)); }

  /**
   * <p>A sorted copy of the collection according to the ordering imposed
   * by the predicate.</p>
   */
  public static List sorted(Collection c, Function lessPred) {
    List result = new ArrayList(c);
    sort(result, lessPred);
    return result;
  }

  public static List sorted(Iterator i, Function lessPred) {
    List result = collect(i);
    sort(result, lessPred);
    return result;
  }

  /**
   * <p>A new sequence that only has the first
   * <code>min(n,length(in))</code> elements of the given sequence.</p>
   */
  public static Iterator take(final Iterator in, final int n) {
    assert 0 <= n;

    return new AbstractIterator() {
        private int i = 0;

        public boolean hasNext() {
          return i < n && in.hasNext();
        }

        public Object next() {
          if (hasNext()) {
            ++i;
            return in.next();
          } else {
            throw new NoSuchElementException();
          }
        }
      };
  }

  /**
   * <pre>
   * <b>while</b> (i.hasNext())
   *   i.set(fun.with(i.next()));
   * </pre>
   */
  public static void transform(ListIterator i, Function fun) {
    ObjectToObject fn = ObjectToObject.from(fun);
    while (i.hasNext())
      i.set(fn.with(i.next()));
  }

  public static void transform(List     l, Function fun) { transform(l.listIterator(), fun); }
  public static void transform(Object[] a, Function fun) { transform(Arrays.asList(a), fun); }
}
