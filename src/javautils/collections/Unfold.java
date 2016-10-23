package javautils.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * <p>Template method for unfolding, or generating, a collection.</p>
 *
 * <p>Unfolding is related to external iterators like the {@link
 * java.util.Iterator}-interface of the Java Collections Framework. While
 * an external iterator returns elements one-by-one, an unfold produces
 * the complete collection of elements.</p>
 *
 * <p>Unlike the {@link java.util.Iterator}-interface of the Java
 * Collections Framework, this template method for unfolding collections
 * clearly separates non-side-effecting queries and side-effecting
 * commands. As a consequence, it can be less error prone to write an
 * unfold than it is to write an iterator.</p>
 *
 * <h3>Why should I use an unfold instead of a simple loop?</h3>
 *
 * <p>Because an unfold immediately tells that the <em>intention</em> is
 * to produce a new collection or a sequence of elements. A loop only
 * tells that the code performs iteration, but the intention behind the
 * iteration is not immediately obvious from a loop.</p>
 *
 * <h3>Example: Generating integers</h3>
 *
 * <p>The following example method creates an iterator that generates
 * integers in a given range.</p>
 *
 * <pre>
 * public static Iterator iteratorForIntegers(<b>final</b> int min,
 *                                            <b>final</b> int max) {
 *   return <b>new</b> {@link Unfold}() {
 *       int next = min;
 *
 *       <b>protected</b> boolean {@link #more more}() { return next <= max; }
 *       <b>protected</b> Object {@link #value value}() { return new Integer(next); }
 *       <b>protected</b> void {@link #advance advance}() { ++next; }
 *     }.{@link #unfoldAsIterator unfoldAsIterator}();
 * }
 * </pre>
 */
public abstract class Unfold {

  /**
   * <p>Called once before other methods to initialize the unfold.</p>
   */
  protected void init() {}

  /**
   * <p>True if and only if there are more elements to generate. This
   * method should not have side-effects.</p>
   */
  protected abstract boolean more();

  /**
   * <p>The element to be added to the generated list. This method should
   * not have side-effects.</p>
   */
  protected abstract Object value();

  /**
   * <p>Advances the internal state of the generator by using side-effects.</p>
   */
  protected abstract void advance();

  /**
   * <pre>
   * <b>return</b> {@link #unfoldTo(List) unfoldTo}(<b>new</b> ArrayList());
   * </pre>
   */
  public final List unfold() {
    return unfoldTo(new ArrayList());
  }

  /**
   * <pre>
   * <b>return</b> {@link #unfoldTo(Map) unfoldTo}(<b>new</b> HashMap());
   * </pre>
   */
  public final Map unfoldMap() {
    return unfoldTo(new HashMap());
  }

  /**
   * <pre>
   * {@link #init init}();
   * <b>while</b> ({@link #more more}()) {
   *   to.add({@link #value value}());
   *   {@link #advance advance}();
   * }
   * <b>return</b> to;
   * </pre>
   */
  public final Collection unfoldTo(Collection to) {
    init();
    while (more()) {
      to.add(value());
      advance();
    }
    return to;
  }

  /**
   * <pre>
   * {@link #init init}();
   * <b>while</b> ({@link #more more}()) {
   *   Map.Entry entry = (Map.Entry){@link #value value}();
   *   to.put(entry.getKey(), entry.getValue());
   *   {@link #advance advance}();
   * }
   * <b>return</b> to;
   * </pre>
   */
  public final Map unfoldTo(Map to) {
    init();
    while (more()) {
      Map.Entry entry = (Map.Entry)value();
      to.put(entry.getKey(), entry.getValue());
      advance();
    }
    return to;
  }

  /**
   * <pre>
   * <b>return</b> (List){@link #unfoldTo(Collection) unfoldTo}((Collection)to);
   * </pre>
   */
  public final List unfoldTo(List to) {
    return (List)unfoldTo((Collection)to);
  }

  /**
   * <pre>
   * <b>return</b> (Set){@link #unfoldTo(Collection) unfoldTo}((Set)to);
   * </pre>
   */
  public final Set unfoldTo(Set to) {
    return (Set)unfoldTo((Collection)to);
  }

  /**
   * <pre>
   * <b>return</b> {@link #unfoldRightTo unfoldRightTo}(<b>new</b> LinkedList());
   * </pre>
   */
  public final List unfoldRight() {
    return unfoldRightTo(new LinkedList());
  }

  /**
   * <pre>
   * {@link #init init}();
   * <b>while</b> ({@link #more more}()) {
   *   to.addFirst({@link #value value}());
   *   {@link #advance advance}();
   * }
   * <b>return</b> to;
   * </pre>
   */
  public final List unfoldRightTo(LinkedList to) {
    init();
    while (more()) {
      to.addFirst(value());
      advance();
    }
    return to;
  }

  /**
   * <p>An iterator that generates the same sequence of elements. The
   * returned iterator will not support the remove-operation.</p>
   */
  public final Iterator unfoldAsIterator() {
    init();

    return new AbstractIterator() {
        public boolean hasNext() {
          return more();
        }

        public Object next() {
          // See how the Iterator-interface of the Java Collections
          // Framework makes things complicated.

          if (!more())
            throw new NoSuchElementException();

          Object result = value();
          advance();
          return result;
        }
      };
  }
}
