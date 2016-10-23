package javautils;

import java.util.Map;

/**
 * <p>A simple immutable pair of objects.</p>
 */
public class ImmutablePair implements Map.Entry {

  public ImmutablePair(Object f, Object s) { this.first = f; this.second = s; }
  public ImmutablePair(Object f, boolean s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(Object f, byte s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(Object f, char s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(Object f, double s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(Object f, float s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(Object f, int s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(Object f, long s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(Object f, short s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(boolean f, Object s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(boolean f, boolean s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(boolean f, byte s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(boolean f, char s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(boolean f, double s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(boolean f, float s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(boolean f, int s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(boolean f, long s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(boolean f, short s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(byte f, Object s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(byte f, boolean s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(byte f, byte s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(byte f, char s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(byte f, double s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(byte f, float s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(byte f, int s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(byte f, long s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(byte f, short s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(char f, Object s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(char f, boolean s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(char f, byte s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(char f, char s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(char f, double s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(char f, float s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(char f, int s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(char f, long s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(char f, short s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(double f, Object s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(double f, boolean s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(double f, byte s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(double f, char s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(double f, double s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(double f, float s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(double f, int s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(double f, long s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(double f, short s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(float f, Object s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(float f, boolean s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(float f, byte s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(float f, char s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(float f, double s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(float f, float s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(float f, int s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(float f, long s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(float f, short s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(int f, Object s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(int f, boolean s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(int f, byte s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(int f, char s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(int f, double s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(int f, float s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(int f, int s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(int f, long s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(int f, short s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(long f, Object s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(long f, boolean s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(long f, byte s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(long f, char s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(long f, double s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(long f, float s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(long f, int s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(long f, long s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(long f, short s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(short f, Object s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(short f, boolean s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(short f, byte s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(short f, char s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(short f, double s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(short f, float s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(short f, int s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(short f, long s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }
  public ImmutablePair(short f, short s) { this.first = Objects.asObject(f); this.second = Objects.asObject(s); }

  /**
   * <p>A new immutable pair with the first and second swapped.</p>
   */
  public ImmutablePair swap() {
    return new ImmutablePair(second, first);
  }

  /**
   * <p>The {@link #first}.</p>
   */
  public final Object getKey() {
    return first;
  }

  /**
   * <p>The {@link #second}.</p>
   */
  public final Object getValue() {
    return second;
  }

  /**
   * <pre>
   * throw new UnsupportedOperationException();
   * </pre>
   */
  public final Object setValue(Object value) {
    throw new UnsupportedOperationException();
  }

  /**
   * <p>The hashCode is designed to be asymmetric, so that
   * <i>generally</i> <code>new ImmutablePair(a,b) != new
   * ImmutablePair(b,a)</code>.</p>
   */
  public int hashCode() {
    // Note: The hashCode "required" for Map.Entry objects by the Java API
    // specification is defined to be symmetric, which is not a good idea.
    // It is better to violate the Java API specification here.
    return Objects.hashCode(first) ^
      Objects.rotateLeft(Objects.hashCode(second), 17) ^
      "ImmutablePair".hashCode();
  }

  /**
   * <p>True if and only if
   * <ul>
   * <li>the other object is not-null and</li>
   * <li>this and the other object have the same class and</li>
   * <li>the first fields of this and the other pair are both null or both equal and</li>
   * <li>the second fields of this and the other pair are both null or both equal.</li>
   * </ul></p>
   */
  public boolean equals(Object other) {
    return this == other ||
      Objects.sameClass(this, other) &&
      Objects.equals(this.first, ((ImmutablePair)other).first) &&
      Objects.equals(this.second, ((ImmutablePair)other).second);
  }

  public String toString() {
    return "(" + first + "," + second + ")";
  }

  // TBD: Long names... caar cadr, anyone?

  public final Object firstOfFirst()   { return ((ImmutablePair)first).first; }
  public final Object secondOfFirst()  { return ((ImmutablePair)first).second; }
  public final Object firstOfSecond()  { return ((ImmutablePair)second).first; }
  public final Object secondOfSecond() { return ((ImmutablePair)second).second; }

  /**
   * <p>Immutable field of the pair.</p>
   */
  public final Object first, second;
}
