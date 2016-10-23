package javautils.dispensers;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>A Decorator [<a
 * href="{@docRoot}/overview-summary.html#[Gamma1995]">Gamma1995</a>] that
 * filters out duplicates.</p>
 */
public class DispenserWithoutDuplicatesDecorator extends AbstractDispenser {

  /**
   * <p>A new dispenser without duplicates. The client should never use
   * the original dispenser directly.</p>
   */
  public DispenserWithoutDuplicatesDecorator(Dispenser original) {
    assert original.isEmpty();
    this.original = original;
  }

  public int size() {
    return elementSet.size();
  }

  public Object pop() {
    Object element = original.pop();
    elementSet.remove(element);
    return element;
  }

  public void push(Object element) {
    if (elementSet.add(element))
      original.push(element);
  }

  private final Dispenser original;
  private final Set elementSet = new HashSet();
}
