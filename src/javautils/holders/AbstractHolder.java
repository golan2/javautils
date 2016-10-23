package javautils.holders;

import java.io.Serializable;
import javautils.Classes;
import javautils.Objects;

/**
 * <p>A base class for holders providing default implementations of {@link
 * #equals}, {@link #hashCode} and {@link #toString}-methods.</p>
 *
 * <p><b>Note</b>: The derived holder class is assumed to contain a public
 * field named <code>value</code>.</p>
 */
public abstract class AbstractHolder implements Cloneable, Serializable {

  /**
   * <p>Gets the value of the <code>value</code>-field of this object. You
   * can override this method in the derived class to improve performance,
   * but it is not strictly necessary.</p>
   *
   * <p><b>Node</b>: This method is <i>not</i> intended for public use.
   * The whole idea is that concrete holders publicly reveal their
   * <code>value</code>-field for ease of use.</p>
   */
  protected Object get() {
    return Classes.getFieldValue(this, "value");
  }

  public boolean equals(Object other) {
    return Objects.sameClass(this, other) &&
      Objects.equals(this.get(), ((AbstractHolder)other).get());
  }

  public int hashCode() {
    return Objects.hashCode(get()) ^ HASH_CODE_XOR;
  }

  private static final int HASH_CODE_XOR = "AbstractHolder".hashCode();

  public String toString() {
    return Objects.toString(get());
  }
}
