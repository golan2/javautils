package javautils.maps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javautils.Classes;
import javautils.collections.Algs;
import javautils.fun.ObjectToObjectToObject;
import javautils.fun.ObjectToVoid;

/**
 * <p>Map from objects to non-null {@link List}-objects. This class is
 * designed mainly for convenience.</p>
 */
public final class ObjectToListMap extends AbstractTypedMap {

  /**
   * <p>A new map based on {@link HashMap}.
   */
  public ObjectToListMap() {}

  /**
   * <p>A new map that maps the given keys to new {@link
   * ArrayList}-instances.</p>
   */
  public ObjectToListMap(Collection keys) {
    this(new HashMap(), keys, ArrayList.class);
  }

  /**
   * <p>A new <code>Object->List</code> map using the given map, with the
   * specifified keys mapped to new instances of the specified list type.
   * The given map must not contain any null values not values that are
   * not instances of {@link List}. The given map should also <b>never</b>
   * be modified except by using the services of this class.</p>
   */
  public ObjectToListMap(Map aMap, Collection keys, final Class listType) {
    super(aMap);
    Algs.forEach(keys,
                 new ObjectToVoid() {
                   public void with(Object key) {
                     map.put(key, (List)Classes.newInstance(listType));
                   }});
  }

  /**
   * <p>True iff the value is a non <code>null</code> {@link
   * java.util.List}-object.</p>
   */
  protected boolean valuePredicate(Object value) {
    return null != value && value instanceof List;
  }

  /**
   * <p>A new <code>Object->List</code> map using the given map. The given
   * map must not contain any null values not values that are not
   * instances of {@link List}. The given map should also <b>never</b> be
   * modified except by using the services of this class.</p>
   */
  public ObjectToListMap(Map map) {
    super(map);
  }

  /**
   * <p>Associates the key with the specified value.</p>
   */
  public void put(Object key, List value) {
    assert null != value;
    map.put(key, value);
  }

  /**
   * <p>The value associated with the specified key or null.</p>
   */
  public List get(Object key) {
    return (List)map.get(key);
  }

  /**
   * <pre>
   * return ensuredGet(key, ArrayList.class);
   * </pre>
   */
  public List ensuredGet(Object key) {
    return ensuredGet(key, ArrayList.class);
  }

  /**
   * <p>Returns the value associated with the specified key or associates
   * a new instance of the specified {@link List}-class with the key and
   * returns the new instance.</p>
   */
  public List ensuredGet(Object key, Class listType) {
    List result = get(key);
    if (null == result) {
      result = (List)Classes.newInstance(listType);
      map.put(key, result);
    }
    return result;
  }

  /**
   * <p>The value associated with the key or an empty, unmodifiable list
   * if no value is associated with the key.</p>
   */
  public List getOrEmptyUnmodifiableList(Object key) {
    List result = (List)map.get(key);
    return null != result
      ? result
      : Algs.EMPTY_LIST;
  }

  /**
   * <p>Makes the lists in the map unmodifiable.</p>
   */
  public void transformListsToUnmodifiableLists() {
    Algs.mapTransform(map,
                      new ObjectToObjectToObject() {
                        public Object with(Object key, Object list) {
                          return Algs.newUnmodifiableList((List)list);
                        }});
  }
}
