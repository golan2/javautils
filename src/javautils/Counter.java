package javautils;

/**
 * <p>A simple counter for generating identifiers.</p>
 */
public class Counter {

  /**
   * <p>Increments the counter and returns the incremented value.</p>
   */
  public long next() {
    return ++value;
  }

  private long value = 0;
}
