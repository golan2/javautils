package javautils.graph;

import java.util.Collection;
import java.util.HashSet;
import javautils.collections.Algs;
import javautils.fun.ObjectToBoolean;
import javautils.fun.ObjectToObject;
import javautils.fun.ObjectToVoid;
import javautils.graph.adt.Graph;
import javautils.graph.testing.TestGraphConstants;
import junit.framework.TestCase;

/**
 * <p>A [<a href="{@docRoot}/overview-summary.html#[JUnit]">JUnit</a>]
 * test for {@link Graphs}.</p>
 */
public class GraphsTest extends TestCase implements TestGraphConstants {

  /**
   * <p>Tests that the {@link Graphs#asString}-method gives correct
   * results on the {@link TestGraphConstants#CLOTHING_GRAPH}.</p>
   */
  public void testAsString() {
    assertEquals("belt: jacket[1];\n" +
                 "jacket;\n" +
                 "pants: shoes[2] belt[3];\n" +
                 "shirt: tie[4] belt[5];\n" +
                 "shoes;\n" +
                 "socks: shoes[6];\n" +
                 "tie: jacket[7];\n" +
                 "undershorts: pants[8] shoes[9];\n" +
                 "watch;\n",
                 Graphs.asString(CLOTHING_GRAPH));
  }

  /**
   * <p>Tests that the {@link Graphs#transposed}-method returns a graph
   * with specified properties on the {@link
   * TestGraphConstants#UVWXYZ_GRAPH}-graph.</p>
   */
  public void testTransposedOnUVWXYZ() {
    Graph uvwxyzGraphTrn = Graphs.transposed(UVWXYZ_GRAPH);
    assertTrue(Graphs.invariant(uvwxyzGraphTrn));
    assertEquals("u;\n" +
                 "v: u[1] x[2];\n" +
                 "w;\n" +
                 "x: u[3] y[4];\n" +
                 "y: v[5] w[6];\n" +
                 "z: w[7] z[8];\n",
                 Graphs.asString(uvwxyzGraphTrn));
    assertTrue(Graphs.sameNodesAndEdges(UVWXYZ_GRAPH, uvwxyzGraphTrn));
    assertTrue(sameTransposedEdges(UVWXYZ_GRAPH, uvwxyzGraphTrn));
    assertTrue(sameTransposedEdges(uvwxyzGraphTrn, UVWXYZ_GRAPH));
  }

  /**
   * <p>Tests that the {@link Graphs#undirected}-method returns correct
   * results on the {@link TestGraphConstants#UVWXYZ_GRAPH}-graph.</p>
   */
  public void testUndirectedUVWXYZ() {
    Graph undirected = Graphs.undirected(UVWXYZ_GRAPH);
    assertTrue(Graphs.invariant(undirected));
    assertEquals("u: v[1] x[2];\n" +
                 "v: y[3] u[4] x[5];\n" +
                 "w: y[6] z[7];\n" +
                 "x: v[8] u[9] y[10];\n" +
                 "y: x[11] v[12] w[13];\n" +
                 "z: z[14] w[15];\n",
                 Graphs.asString(undirected));
  }

  /**
   * <p>Tests that the {@link Graphs#connectedComponents}-method returns
   * the correct result on the {@link
   * TestGraphConstants#UVWXYZ_GRAPH}-graph, which is a connected graph.</p>
   */
  public void testConnectedComponentsOnUVWXYZ() {
    Collection components = Graphs.connectedComponents(UVWXYZ_GRAPH);
    assertEquals(1, components.size());
    Algs.forEach(components,
                 new ObjectToVoid() {
                   public void with(Object graph) {
                     assertTrue(Graphs.nodeSet((Graph)graph).equals(Graphs.nodeSet(UVWXYZ_GRAPH)));
                   }});
  }

  /**
   * <p>Tests that the {@link Graphs#connectedComponents}-method appears
   * to return the correct result on the {@link
   * TestGraphConstants#CLOTHING_GRAPH}-graph, which consists of two
   * components.</p>
   */
  public void testConnectedComponentsOnCLOTHING() {
    Collection components = Graphs.connectedComponents(CLOTHING_GRAPH);
    assertEquals(2, components.size());
    assertTrue(Algs.exists(components,
                           new ObjectToBoolean() {
                             public boolean with(Object graph) {
                               return Graphs.nodeSet((Graph)graph)
                                 .equals(new HashSet(Graphs.asUnmodifiableList(new Object[]{"watch"})));
                             }}));
  }

  /**
   * <p>Tests that the {@link Graphs#stronglyConnectedComponents}-method
   * returns the correct result on the {@link
   * TestGraphConstants#ABCDEFGH_GRAPH}-graph.</p>
   */
  public void testStronglyConnectedComponentsOnABCDEFGH() {
    assertEquals("[[a, e, b], [c, d], [f, g], [h]]",
                 Graphs.stronglyConnectedComponents(ABCDEFGH_GRAPH).toString());
  }

  /**
   * <p>Tests that the {@link Graphs#stronglyConnectedComponents}-method
   * returns the correct result on the {@link
   * TestGraphConstants#ABCDEFGHIJ_GRAPH}-graph.</p>
   */
  public void testStronglyConnectedComponentsOnABCDEFGHIJ() {
    assertEquals("[[g], [h, i, j], [a, c, b, f], [d], [e]]",
                 Graphs.stronglyConnectedComponents(ABCDEFGHIJ_GRAPH).toString());
  }

  /**
   * <p>Tests that the {@link Graphs#restrictedToNodes}-method returns the
   * correct result on the {@link TestGraphConstants#ABCDEFGH_GRAPH}-graph
   * when restricted to nodes <code>{a,b,c,d}</code>.</p>
   */
  public void testRestrictedToNodesOnABCDEFGH() {
    assertEquals("a: b[1];\n" +
                 "b: c[2];\n" +
                 "c: d[3];\n" +
                 "d: c[4];\n",
                 Graphs.asString(Graphs.restrictedToNodes(ABCDEFGH_GRAPH,
                                                          Graphs.asUnmodifiableList(new String[]{"a", "b", "c", "d"}))));
  }

  /**
   * <p>Tests that the {@link Graphs#transitiveIrreflexiveClosure}-method
   * returns the correct result on the {@link
   * TestGraphConstants#CLOTHING_GRAPH}-graph.</p>
   */
  public void testTransitiveIrreflexiveClosureOnCLOTHING() {
    assertEquals("belt: jacket[1];\n" +
                 "jacket;\n" +
                 "pants: shoes[2] belt[3] jacket[4];\n" +
                 "shirt: tie[5] jacket[6] belt[7];\n" +
                 "shoes;\n" +
                 "socks: shoes[8];\n" +
                 "tie: jacket[9];\n" +
                 "undershorts: pants[10] shoes[11] belt[12] jacket[13];\n" +
                 "watch;\n",
                 Graphs.asString(Graphs.transitiveIrreflexiveClosure(CLOTHING_GRAPH)));
  }

  /**
   * <p>Tests that the {@link Graphs#transitiveIrreflexiveClosure}-method
   * returns the correct result on the {@link
   * TestGraphConstants#UVWXYZ_GRAPH}-graph, which is a cyclic graph
   * containing a self-edge.</p>
   */
  public void testTransitiveIrreflexiveClosureOnUVWXYZ() {
    assertEquals("u: v[1] y[2] x[3];\n" +
                 "v: y[4] x[5];\n" +
                 "w: y[6] x[7] v[8] z[9];\n" +
                 "x: v[10] y[11];\n" +
                 "y: x[12] v[13];\n" +
                 "z;\n",
                 Graphs.asString(Graphs.transitiveIrreflexiveClosure(UVWXYZ_GRAPH)));
  }

  /**
   * <p>Tests that the {@link Graphs#edges}-method returns the correct
   * result on the {@link TestGraphConstants#ABCDEFGHIJ_GRAPH}-graph.</p>
   */
  public void testEdgesOnABCDEFGHIJ() {
    assertEquals("[(a,b), (a,d), (b,c), (b,f), (c,a), (c,d), (c,e), (d,e), (f,c), (g,f), (g,h), (h,f), (h,j), (i,h), (j,i)]",
                 Algs.collect(Algs.map(Graphs.edges(ABCDEFGHIJ_GRAPH),
                                       new ObjectToObject() {
                                         public Object with(Object edge) {
                                           return "(" + ABCDEFGHIJ_GRAPH.sourceOf(edge) + "," + ABCDEFGHIJ_GRAPH.targetOf(edge) + ")";
                                         }})).toString());
  }

  static boolean sameTransposedEdges(final Graph g, final Graph t) {
    return Algs.forAll(Graphs.edges(g),
                       new ObjectToBoolean() {
                         public boolean with(Object edge) {
                           return
                             g.sourceOf(edge) == t.targetOf(edge) &&
                             g.targetOf(edge) == t.sourceOf(edge);
                         }});
  }
}
