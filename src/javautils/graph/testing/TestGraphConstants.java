package javautils.graph.testing;

import javautils.graph.adt.Graph;

/**
 * <p>Graphs for testing graph algorithms. Most of the graphs here are
 * from [<a
 * href="{@docRoot}/overview-summary.html#[Cormen2001]">Cormen2001</a>]
 * and [<a
 * href="{@docRoot}/overview-summary.html#[Weiss1997]">Weiss1997</a>] and
 * most of the graphs are named based on the names of their nodes.</p>
 */
public interface TestGraphConstants {

  // NOTE: Please keep the graphs in this class in alphabetical order.

  /**
   * <pre>
   * a: b[1];
   * b: e[2] f[3] c[4];
   * c: g[5] d[6];
   * d: c[7] h[8];
   * e: f[9] a[10];
   * f: g[11];
   * g: f[12] h[13];
   * h: h[14];
   * </pre>
   */
  static final Graph ABCDEFGH_GRAPH =
    new BasicGraph(new Object[][]{{"a", "b"},
                                  {"b", "e", "f", "c"},
                                  {"c", "g", "d"},
                                  {"d", "c", "h"},
                                  {"e", "f", "a"},
                                  {"f", "g"},
                                  {"g", "f", "h"},
                                  {"h", "h"}});

  /**
   * <pre>
   * a: b[1] d[2];
   * b: c[3] f[4];
   * c: a[5] d[6] e[7];
   * d: e[8];
   * e;
   * f: c[9];
   * g: f[10] h[11];
   * h: f[12] j[13];
   * i: h[14];
   * j: i[15];
   * </pre>
   */
  static final Graph ABCDEFGHIJ_GRAPH =
    new BasicGraph(new Object[][]{{"a", "b", "d"},
                                  {"b", "c", "f"},
                                  {"c", "a", "d", "e"},
                                  {"d", "e"},
                                  {"e"},
                                  {"f", "c"},
                                  {"g", "f", "h"},
                                  {"h", "f", "j"},
                                  {"i", "h"},
                                  {"j", "i"}});

  /**
   * <pre>
   * belt: jacket[1];
   * jacket;
   * pants: shoes[2] belt[3];
   * shirt: tie[4] belt[5];
   * shoes;
   * socks: shoes[6];
   * tie: jacket[7];
   * undershorts: pants[8] shoes[9];
   * watch;
   * </pre>
   */
  static final Graph CLOTHING_GRAPH =
    new BasicGraph(new Object[][]{{"belt", "jacket"},
                                  {"jacket"},
                                  {"pants", "shoes", "belt"},
                                  {"shirt", "tie", "belt"},
                                  {"shoes"},
                                  {"socks", "shoes"},
                                  {"tie", "jacket"},
                                  {"undershorts", "pants", "shoes"},
                                  {"watch"}});

  /**
   * <pre>
   * s: w[1] r[2];
   * r: s[3] v[4];
   * t: x[5] w[6] u[7];
   * u: t[8] x[9] y[10];
   * v: r[11];
   * w: t[12] x[13] s[14];
   * x: w[15] t[16] u[17] y[18];
   * y: u[19] x[20];
   * </pre>
   */
  static final Graph RSTUVWXY_GRAPH =
    new BasicGraph(new Object[][]{{"s", "w", "r"},
                                  {"r", "s", "v"},
                                  {"t", "x", "w", "u"},
                                  {"u", "t", "x", "y"},
                                  {"v", "r"},
                                  {"w", "t", "x", "s"},
                                  {"x", "w", "t", "u", "y"},
                                  {"y", "u", "x"}});

  /**
   * <pre>
   * s: z[1] w[2];
   * t: v[3] u[4];
   * u: v[5] t[6];
   * v: s[7] w[8];
   * w: x[9];
   * x: z[10];
   * y: x[11];
   * z: y[12] w[13];
   * </pre>
   */
  static final Graph STUVWXYZ_GRAPH =
    new BasicGraph(new Object[][]{{"s", "z", "w"},
                                  {"t", "v", "u"},
                                  {"u", "v", "t"},
                                  {"v", "s", "w"},
                                  {"w", "x"},
                                  {"x", "z"},
                                  {"y", "x"},
                                  {"z", "y", "w"}});

  /**
   * <pre>
   * u: v[1] x[2];
   * v: y[3];
   * w: y[4] z[5];
   * x: v[6];
   * y: x[7];
   * z: z[8];
   * </pre>
   */
  static final Graph UVWXYZ_GRAPH =
    new BasicGraph(new Object[][]{{"u", "v", "x"},
                                  {"v", "y"},
                                  {"w", "y", "z"},
                                  {"x", "v"},
                                  {"y", "x"},
                                  {"z", "z"}});
}
