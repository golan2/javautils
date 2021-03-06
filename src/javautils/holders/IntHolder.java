package javautils.holders;

import javautils.Objects;


/**
 * <p>A simple mutable holder for <code>int</code>-values. This class
 * is designed to be used in situations in which you need to store
 * ints in collections or when you want to be able to mutate an
 * int from an anonymous inner class, for example.</p>
 */
public class IntHolder extends AbstractHolder {

  // Warning: This file was generated by the javautils-create-holder
  // script. It is probably more productive to extend the script rather
  // than edit this code directly.

  /**
   * <p>Default constructor for convenience.</p>
   */
  public IntHolder() {}

  /**
   * <p>A new holder with specified initial value.</p>
   */
  public IntHolder(int value) {
    this.value = value;
  }

  /**
   * <p>Overrides the base method for efficiency.</p>
   */
  protected Object get() {
    return Objects.asObject(value);
  }

  /**
   * <p>The mutable value of this holder.</p>
   */
  public int value;
}
