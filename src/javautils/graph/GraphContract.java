package javautils.graph;

import java.util.List;
import java.util.Set;
import javautils.graph.adt.AugmentedGraph;

/**
 * <p>A Design-by-Contract [<a
 * href="{@docRoot}/overview-summary.html#[Meyer1997]">Meyer1997</a>]
 * decorator for the {@link AugmentedGraph}-interface. This decorator
 * checks the graph invariant only once upon construction and then checks
 * only the preconditions each time a method is called.</p>
 */
public final class GraphContract extends AugmentedGraphDecorator {

  /**
   * <p>A contract checking version of the given graph. If the graph is
   * already a contract checking graph, then the given graph is simply
   * returned.</p>
   */
  public static GraphContract from(AugmentedGraph graph) {
    return graph instanceof GraphContract
      ? (GraphContract)graph
      : new GraphContract(graph);
  }

  private GraphContract(AugmentedGraph graph) {
    super(graph);
    this.edges = Graphs.edgeSet(graph);
    Graphs.invariant(this);
  }

  public List edgesFrom(Object node) {
    assertNode(node);
    return super.edgesFrom(node);
  }

  public List edgesTo(Object node) {
    assertNode(node);
    return super.edgesTo(node);
  }

  public Object sourceOf(Object edge) {
    assertEdge(edge);
    return super.sourceOf(edge);
  }

  public Object targetOf(Object edge) {
    assertEdge(edge);
    return super.targetOf(edge);
  }

  /**
   * <p>True if and only if the object is an edge of this graph.</p>
   */
  public boolean isEdge(Object obj) {
    return edges.contains(obj);
  }

  private void assertNode(Object node) {
    if (!isNode(node))
      throw new AssertionError("Object '" + node + "' is not a node of this this graph.");
  }

  private void assertEdge(Object edge) {
    if (!isEdge(edge))
      throw new AssertionError("Object '" + edge + "' is not an edge of this this graph.");
  }

  private Set edges;
}
