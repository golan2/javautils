package javautils.maps;

import java.util.Map;
import javautils.holders.IntHolder;


/**
 * <p>Map from objects to int-values. {@link IntHolder}-objects are
 * used for storing the values in the map.</p>
 */
public final class ObjectToIntMap extends AbstractTypedMap {

  // Warning: This file was generated by the javautils-create-primitive-typed-map
  // script. It is probably more productive to extend the script rather
  // than edit this code directly.

  /**
   * <p>A new map based on the map type used by {@link
   * AbstractTypedMap#AbstractTypedMap}.</p>
   */
  public ObjectToIntMap() {}

  /**
   * <p>A new <code>Object->int</code> map using the given map. The
   * given map must not contain any values that are not {@link
   * IntHolder}-objects. The given map should also never be modified
   * except by using the services of this class.</p>
   */
  public ObjectToIntMap(Map map) {
    super(map);
  }

  /**
   * <p>Tests that the value is a non <code>null</code> {@link
   * IntHolder}-object.</p>
   */
  protected boolean valuePredicate(Object value) {
    return null != value && value instanceof IntHolder;
  }

  /**
   * <p>Associates the key with the specified value. Will not create a new
   * holder if the key is already associated with a value.</p>
   */
  public void put(Object key, int value) {
    IntHolder holder = holder(key);

    if (null == holder)
      put(key, new IntHolder(value));
    else
      holder.value = value;
  }

  /**
   * <p>Associates the key with the specified holder.</p>
   */
  public void put(Object key, IntHolder holder) {
    assert null != holder;
    map.put(key, holder);
  }

  /**
   * <p>The value associated with the key - throws an assertion exception
   * if the key is not associated with a value.</p>
   */
  public int get(Object key) {
    return ref(key).value;
  }

  /**
   * <p>The value associated with the key <i>or</i> the specified
   * alternative value if the key is not associated with any value.</p>
   */
  public int getOr(Object key, int alternative) {
    IntHolder result = holder(key);
    return null != result ? result.value : alternative;
  }

  /**
   * <p>The holder associated with the key - throws an assertion exception
   * if the key is not associated with a value.</p>
   */
  public IntHolder ref(Object key) {
    IntHolder holder = holder(key);
    assert null != holder;
    return holder;
  }

  /**
   * <p>The holder associated with the key or a new holder that holds the
   * specified value and associated with the key.</p>
   */
  public IntHolder refOr(Object key, int value) {
    IntHolder holder = holder(key);
    if (null == holder) {
      holder = new IntHolder(value);
      map.put(key, holder);
    }
    return holder;
  }

  /**
   * <p>The holder of the value associated with the key or null if the key
   * is not associated with a value.</p>
   */
  public IntHolder holder(Object key) {
    return (IntHolder)map.get(key);
  }
}
