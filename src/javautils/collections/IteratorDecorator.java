package javautils.collections;

import java.util.Iterator;

/**
 * <p>A basic forwarding Decorator [<a
 * href="{@docRoot}/overview-summary.html#[Gamma1995]">Gamma1995</a>] for
 * iterators.</p>
 */
public class IteratorDecorator extends AbstractIterator {
  public IteratorDecorator(Iterator original) {
    this.original = original;
  }

  public boolean hasNext() {
    return original.hasNext();
  }

  public Object next() {
    return original.next();
  }

  protected final Iterator original;
}
