package javautils.collections;

import java.util.List;
import java.util.Map;
import javautils.ImmutablePair;
import javautils.collections.Algs;
import javautils.fun.Function;
import javautils.fun.ObjectToBoolean;
import junit.framework.TestCase;

/**
 * <p>A [<a href="{@docRoot}/overview-summary.html#[JUnit]">JUnit</a>]
 * test for {@link Algs}.</p>
 */
public class AlgsTest extends TestCase {

  private static final Integer[] PI = {new Integer(3),
                                       new Integer(1),
                                       new Integer(4),
                                       new Integer(1),
                                       new Integer(5),
                                       new Integer(9),
                                       new Integer(2),
                                       new Integer(6)};

  private static final List PI_LIST = Algs.asUnmodifiableList(PI);

  private static final int[] PI_ARRAY = Algs.asArray(PI, new int[0]);

  public void testConcat() {
    assertEquals("[3, 1, 4, 1, 5, 9, 2, 6, 3, 1, 4, 1, 5, 9, 2, 6]",
                 Algs.collect(Algs.concat(PI, PI)).toString());

    assertEquals("[3, 1, 4, 1, 5, 9, 2, 6]",
                 Algs.collect(Algs.concat(Algs.iterator(PI_ARRAY), Algs.EMPTY_LIST.iterator())).toString());

    assertEquals("[x]",
                 Algs.collect(Algs.concat(Algs.singletonIterator("x"), Algs.EMPTY_LIST.iterator())).toString());

    assertEquals("[x, y]",
                 Algs.collect(Algs.concat(Algs.singletonIterator("x"), Algs.singletonIterator("y"))).toString());
  }

  public void testCopyOf() {
    int[] copy = Algs.copyOf(PI_ARRAY);

    assertEquals(PI_ARRAY.length, copy.length);
    for (int i=0; i<PI_ARRAY.length; ++i)
      assertEquals(PI_ARRAY[i], copy[i]);
  }

  public void testDrop() {
    assertEquals(Algs.collect(Algs.drop(Algs.iterator(new Object[]{"a","b","c","d"}),
                                        2)).toString(),
                 "[c, d]");

    assertEquals(Algs.collect(Algs.drop(Algs.iterator(new Object[]{"a","b","c","d"}),
                                        0)).toString(),
                 "[a, b, c, d]");

    assertEquals(Algs.collect(Algs.drop(Algs.iterator(new Object[]{}),
                                        5)).toString(),
                 "[]");

    assertEquals(Algs.collect(Algs.drop(Algs.iterator(new Object[]{"a","b","c","d"}),
                                        5)).toString(),
                 "[]");
  }

  public void testExists() {
    assertTrue(Algs.exists(PI, equalsTo(new Integer(3))));
  }

  public void testFilter() {
    assertEquals(Algs.asUnmodifiableList(new Integer[]{new Integer(4),
                                                       new Integer(5),
                                                       new Integer(6)}),
                 Algs.collect(Algs.filter(Algs.filter(PI_LIST,
                                                      new Function() {
                                                        public boolean with(int x) {
                                                          return 3 < x;
                                                        }}),
                                          new Function() {
                                            public boolean with(int x) {
                                              return x < 9;
                                            }})));
  }

  public void testFilterEmptyResult() {
    assertEquals(Algs.asUnmodifiableList(new Integer[]{}),
                 Algs.collect(Algs.filter(PI_LIST,
                                          new Function() {
                                            public boolean with(int x) {
                                              return 10 < x;
                                            }})));
  }

  public void testFilterEmptySource() {
    assertEquals(Algs.asUnmodifiableList(new Integer[0]),
                 Algs.collect(Algs.filter(new Integer[0],
                                          new Function() {
                                            public boolean with(int x) {
                                              return true;
                                            }})));
  }

  public void testFind() {
    assertEquals(new Integer(5), Algs.find(PI, equalsTo(new Integer(5))));
  }

  public void testFindNotFound() {
    assertEquals(null, Algs.find(PI, equalsTo(new Integer(10))));
  }

  public void testFold() {
    assertEquals(new Integer(0),
                 Algs.fold(new Integer(-(3 + 1 + 4 + 1 + 5 + 9 + 2 + 6)),
                           PI_LIST,
                           new Function() {
                             public int with(int x, int y) {
                               return x + y;
                             }}));
  }

  public void testFoldRight() {
    assertEquals(new Integer((3 - (1 - (4 - (1 - (5 - (9 - (2 - (6 - 0))))))))),
                 Algs.foldRight(PI,
                                new Integer(0),
                                new Function() {
                                  public int with(int x, int y) {
                                    return x - y;
                                  }}));
  }

  public void testForAll() {
    assertFalse(Algs.forAll(PI,
                            new Function() {
                              public boolean with(int x) {
                                return x < 9;
                              }}));
  }

  public void testForEach() {
    final StringBuffer result = new StringBuffer();
    Algs.forEach(PI_LIST,
                 new Function() {
                   public void with(int x) {
                     result.append(x);
                   }});
    assertEquals("31415926", result.toString());
  }

  public void testGenForEach() {
    final StringBuffer result = new StringBuffer();
    Algs.genForEach(new List[]{PI_LIST,
                               Algs.asUnmodifiableList(new String[]{"{1,2}",
                                                                    "{3}",
                                                                    "{4,5,6}",
                                                                    "{7}",
                                                                    "{}",
                                                                    "{8}"}),
                               Algs.asUnmodifiableList(new String[]{"a", "b", "c", "d", "e", "f", "g"})},
                    new Function() {
                      public void with(int x, Object y, String z) {
                        result.append(z + y + x);
                      }});
    assertEquals("a{1,2}3b{3}1c{4,5,6}4d{7}1e{}5f{8}9", result.toString());
  }

  public void testForEachInProduct() {
    final StringBuffer result = new StringBuffer();
    Algs.forEachInProduct(PI_LIST,PI_LIST,
                          new Function() {
                            public void with(int x, int y) {
                              result.append("(" + x + "," + y + ")");
                            }});
    assertEquals("(3,3)(3,1)(3,4)(3,1)(3,5)(3,9)(3,2)(3,6)" +
                 "(1,3)(1,1)(1,4)(1,1)(1,5)(1,9)(1,2)(1,6)" +
                 "(4,3)(4,1)(4,4)(4,1)(4,5)(4,9)(4,2)(4,6)" +
                 "(1,3)(1,1)(1,4)(1,1)(1,5)(1,9)(1,2)(1,6)" +
                 "(5,3)(5,1)(5,4)(5,1)(5,5)(5,9)(5,2)(5,6)" +
                 "(9,3)(9,1)(9,4)(9,1)(9,5)(9,9)(9,2)(9,6)" +
                 "(2,3)(2,1)(2,4)(2,1)(2,5)(2,9)(2,2)(2,6)" +
                 "(6,3)(6,1)(6,4)(6,1)(6,5)(6,9)(6,2)(6,6)",
                 result.toString());
  }

  public void testIntegersInRange() {
    assertEquals("[]", Algs.collect(Algs.integersInRange(65,65)).toString());
    assertEquals("[5]", Algs.collect(Algs.integersInRange(5,4)).toString());
    assertEquals("[-1, 0, 1, 2, 3]", Algs.collect(Algs.integersInRange(-1,4)).toString());
  }

  public void testMap() {
    assertEquals("[7, 9, 6, 9, 5, 1, 8, 4]",
                 Algs.collect(Algs.map(PI,
                                       new Function() {
                                         public int with(int x) {
                                           return 10-x;
                                         }})).toString());
  }

  public void testMapMorphism() {
    Map integerToStringMap = Algs.newMap(new Object[][]{{new Integer(0), "0"},
                                                        {new Integer(1), "1"},
                                                        {new Integer(2), "2"}});
    Map stringToIntegerMap = Algs.newMap(new Object[][]{{"0", new Integer(0)},
                                                        {"1", new Integer(1)},
                                                        {"2", new Integer(2)}});

    assertEquals(stringToIntegerMap,
                 Algs.mapMorphism(integerToStringMap,
                                  new Function() {
                                    public ImmutablePair with(Integer key, String value) {
                                      return new ImmutablePair(value, key);
                                    }}));

    assertEquals(integerToStringMap,
                 Algs.mapMorphism(stringToIntegerMap,
                                  new Function() {
                                    public ImmutablePair with(String key, Integer value) {
                                      return new ImmutablePair(value, key);
                                    }}));
  }

  public void testNewShapedArray() {
    int[][] array = (int[][])Algs.newShapedArray(PI_ARRAY, int.class);

    assertEquals(PI_ARRAY.length, array.length);
    for (int i=0; i<PI_ARRAY.length; ++i)
      assertEquals(PI_ARRAY[i], array[i].length);
  }

  public void testSelect() {
    assertEquals(new Integer(9),
                 Algs.select(new Integer(0),
                             PI,
                             new Function() {
                               public boolean with(int max, int x) {
                                 return max < x;
                               }}));
    assertEquals(new Integer(1),
                 Algs.select(new Integer(10),
                             PI_LIST,
                             new Function() {
                               public boolean with(int min, int x) {
                                 return min > x;
                               }}));
  }

  public void testTake() {
    assertEquals(Algs.collect(Algs.take(Algs.iterator(new Object[]{"a","b","c","d"}),
                                        2)).toString(),
                 "[a, b]");

    assertEquals(Algs.collect(Algs.take(Algs.iterator(new Object[]{"a","b","c","d"}),
                                        0)).toString(),
                 "[]");

    assertEquals(Algs.collect(Algs.take(Algs.iterator(new Object[]{}),
                                        5)).toString(),
                 "[]");

    assertEquals(Algs.collect(Algs.take(Algs.iterator(new Object[]{"a","b","c","d"}),
                                        5)).toString(),
                 "[a, b, c, d]");
  }

  public void testTransform() {
    String[] array = new String[]{"Higher","Order","Programming","Is","Fun"};
    Algs.transform(array,
                   new Function() {
                     public String with(String x) {
                       return x + ".";
                     }});
    assertEquals("They.Say.That.Higher.Order.Programming.Is.Fun.",
                 Algs.fold("They.Say.That.",
                           array,
                           new Function() {
                             public String with(String r, String x) {
                               return r + x;
                             }}));
  }

  private Function equalsTo(final Object to) {
    return new ObjectToBoolean() {
        public boolean with(Object other) {
          return to.equals(other);
        }};
  }
}
