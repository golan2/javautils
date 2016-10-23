package javautils.dispensers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>LIFO stack.</p>
 */
public final class Stack extends AbstractDispenser {

  public Stack() {
    this(new ArrayList());
  }

  public Stack(Collection c) {
    this(new ArrayList(c));
  }

  public static Stack newAllRight(List l) {
    Stack result = new Stack();
    result.pushAllRight(l);
    return result;
  }

  public int size() {
    return list.size();
  }

  public void push(Object o) {
    list.add(o);
  }

  public Object pop() {
    return list.remove(size()-1);
  }

  public Object top() {
    return top(1);
  }

  public Object top(int offset) {
    return list.get(size()-offset);
  }

  public void set(Object o) {
    set(1, o);
  }

  public void set(int offset, Object o) {
    list.set(size()-offset, o);
  }

  private Stack(ArrayList list) {
    this.list = list;
  }

  private ArrayList list;
}
