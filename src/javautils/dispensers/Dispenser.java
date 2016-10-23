package javautils.dispensers;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * <p>A dispenser, see [<a
 * href="{@docRoot}/overview-summary.html#[Meyer1997]">Meyer1997</a>], is
 * a Strategy, see [<a
 * href="{@docRoot}/overview-summary.html#[Gamma1995]">Gamma1995</a>],
 * that decides the order in which elements governed by the dispenser will
 * be processed. Some well known strategies are:</p>
 * <dl>
 * <dt>Stack or LIFO</dt>
 * <dd>The most recent pushed element will be processed next.</dd>
 * <dt>Queue or FIFO</dt>
 * <dd>The least recent pushed element will be processed next.</dd>
 * <dt>Priority queue</dt>
 * <dd>The element with the highest priority will be processed next.</dd>
 * <dt>Random permutation</dt>
 * <dd>The element to be processed next will be chosen randomly.</dd>
 * </dl>
 */
public interface Dispenser {

  /**
   * <pre>
   * <b>return</b> size() == 0;
   * </pre>
   */
  boolean isEmpty();

  /**
   * <pre>
   * <b>return</b> &#33;isEmpty();
   * </pre>
   */
  boolean notEmpty();

  /**
   * <p>Number of elements in the dispenser.</p>
   */
  int size();

  /**
   * <p>Chooses the next element to be processed, removes it from the
   * dispenser, and returns the element.</p>
   */
  Object pop();

  /**
   * <p>Adds a new element into the dispenser.</p>
   */
  void push(Object element);

  /**
   * <p>Adds all elements in the collection into the dispenser in the
   * order that the iterator of the collection returns them.</p>
   */
  void pushAll(Collection c);

  /**
   * <p>Adds all elements returned by the iterator into the dispenser in
   * the order that the iterator returns them.</p>
   */
  void pushAll(Iterator i);

  /**
   * <p>Adds all elements in the list into the dispenser starting from the
   * last element in the list.</p>
   */
  void pushAllRight(List l);
}
