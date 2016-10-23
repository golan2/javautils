package javautils.collections;

import java.util.Iterator;

/**
 * <p>Implements the {@link #remove remove}-method.</p>
 */
public abstract class AbstractIterator implements Iterator {

  /**
   * <pre>
   * <b>throw new</b> {@link java.lang.UnsupportedOperationException}();
   * </pre>
   */
  public void remove() {
    throw new UnsupportedOperationException();
  }
}
