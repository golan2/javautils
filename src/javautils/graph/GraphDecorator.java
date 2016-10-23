package javautils.graph;

import javautils.graph.adt.Graph;
import java.util.List;

/**
 * <p>A basic forwarding Decorator, see [<a
 * href="{@docRoot}/overview-summary.html#[Gamma1995]">Gamma1995</a>], for
 * the {@link Graph}-interface. By default, the decorator simply forwards
 * all operations to the original graph given at construction time. A
 * class derived from the decorator can then override operations to alter
 * the behavior of the graph.</p>
 *
 * <p>Since a derived decorator is allowed to alter the behavior of the
 * graph in any way, like adding nodes or removing edges, it is generally
 * not safe to make any assumptions about the relationship between the
 * original and the decorated graph.</p>
 *
 * @see AugmentedGraphDecorator
 */
public class GraphDecorator implements Graph {
  public GraphDecorator(Graph graph) {
    this.original = graph;
  }

  public List nodes() {
    return original.nodes();
  }

  public List edgesFrom(Object node) {
    return original.edgesFrom(node);
  }

  public Object targetOf(Object edge) {
    return original.targetOf(edge);
  }

  public Object sourceOf(Object edge) {
    return original.sourceOf(edge);
  }

  /**
   * <p>The original graph.</p>
   */
  protected final Graph graph() {
    return original;
  }

  private final Graph original;
}
