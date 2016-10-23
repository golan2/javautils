package javautils.dispensers;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * <p>Implements all the {@link Dispenser}-interface methods except {@link
 * #size size()}, {@link #push push(Object)} and {@link #pop pop()}.</p>
 */
public abstract class AbstractDispenser implements Dispenser {

  public boolean isEmpty() {
    return size() == 0;
  }

  public boolean notEmpty() {
    return size() != 0;
  }

  public void pushAll(Collection c) {
    pushAll(c.iterator());
  }

  public void pushAll(Iterator i) {
    while (i.hasNext())
      push(i.next());
  }

  public void pushAllRight(List l) {
    for (ListIterator i = l.listIterator(l.size()); i.hasPrevious();)
      push(i.previous());
  }
}
