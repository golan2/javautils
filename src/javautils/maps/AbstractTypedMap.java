package javautils.maps;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javautils.Exceptions;
import javautils.Objects;
import javautils.collections.Algs;
import javautils.fun.ObjectToBoolean;
import javautils.fun.ObjectToObjectToObject;

/**
 * <p>Abstract base for typed maps.</p>
 */
public abstract class AbstractTypedMap implements Cloneable, Serializable {

  /**
   * <p>A new map based on {@link java.util.HashMap}.</p>
   */
  public AbstractTypedMap() {
    this.map = new HashMap();
  }

  /**
   * <p>A new map based on the given map.</p>
   */
  public AbstractTypedMap(Map map) {
    this.map = map;
    assert Algs.forAll(map.values(),
                       new ObjectToBoolean() {
                         public boolean with(Object value) {
                           return valuePredicate(value);
                         }});
  }

  /**
   * <p>Removes all mappings from this map.</p>
   */
  public void clear() {
    map.clear();
  }

  /**
   * <p>A new <em>deepish</em> clone of the map (values are cloned, but
   * keys, which should never be modified anyway, aren't).</p>
   */
  public Object clone() {
    try {
      Map clonedMap = (Map)Objects.clone(map);
      Algs.mapTransform(clonedMap,
                        new ObjectToObjectToObject() {
                          public Object with(Object key, Object value) {
                            return Objects.clone(value);
                          }});
      return getClass().getConstructor(new Class[]{Map.class}).newInstance(new Object[]{clonedMap});
    } catch (Exception e) {
      throw Exceptions.toThrowUnchecked(e);
    }
  }

  /**
   * <p>True if and only if the key is associated with a value.</p>
   */
  public boolean containsKey(Object key) {
    return map.containsKey(key);
  }

  /**
   * <p>True iff the size of this map is <code>0</code>.</p>
   */
  public boolean isEmpty() {
    return map.isEmpty();
  }

  /**
   * <p>An iterator over all keys of this map.</p>
   */
  public Iterator keyIterator() {
    return map.keySet().iterator();
  }

  /**
   * <p>A mutable set of keys of this map.</p>
   */
  public Set keySet() {
    return map.keySet();
  }

  /**
   * <p>Removes the specified key from this map.</p>
   */
  public void remove(Object key) {
    map.remove(key);
  }

  /**
   * <p>The size of this map.</p>
   */
  public int size() {
    return map.size();
  }

  /**
   * <p>A textual representation of this map.</p>
   */
  public String toString() {
    return map.toString();
  }

  /**
   * <p>An unmodifiable view to the internal map.</p>
   */
  public Map unmodifiableMap() {
    return Collections.unmodifiableMap(map);
  }

  /**
   * <p>An iterator over all values of this map.</p>
   */
  public Iterator valueIterator() {
    return map.values().iterator();
  }

  /**
   * <p>True iff the value satisfies the typing constraints.</p>
   */
  abstract protected boolean valuePredicate(Object value);

  protected final Map map;
}
