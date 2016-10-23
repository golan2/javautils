package javautils.graph.testing;

import javautils.ImmutablePair;
import javautils.Objects;

/**
 * <p>A basic implementation of an edge.</p>
 */
public final class BasicEdge extends ImmutablePair {
  public BasicEdge(Object source, Object target, int key) {
    super(source, target);
    this.key = key;
  }

  public int hashCode() {
    return super.hashCode() ^
      Objects.rotateLeft(key, 32-3) ^
      "BasicEdge".hashCode();
  }

  public boolean equals(Object other) {
    return super.equals(other) &&
      this.key == ((BasicEdge)other).key;
  }

  public String toString() {
    return super.toString() + "[" + key + "]";
  }

  private final int key;
}
