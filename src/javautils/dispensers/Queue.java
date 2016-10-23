package javautils.dispensers;

import java.util.Collection;
import java.util.LinkedList;

/**
 * <p>FIFO queue.</p>
 */
public class Queue extends AbstractDispenser {
  public Queue() {}

  public Queue(Collection c) {
    pushAll(c);
  }

  public int size() {
    return list.size();
  }

  public Object pop() {
    return list.removeFirst();
  }

  public void push(Object element) {
    list.addLast(element);
  }

  private LinkedList list = new LinkedList();
}
