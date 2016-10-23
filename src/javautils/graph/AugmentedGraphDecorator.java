package javautils.graph;

import java.util.List;
import javautils.graph.adt.AugmentedGraph;

/**
 * <p>A basic forwarding Decorator, see [<a
 * href="{@docRoot}/overview-summary.html#[Gamma1995]">Gamma1995</a>], for
 * the {@link AugmentedGraph}-interface. By default, the decorator simply
 * forwards all operations to the original graph given at construction
 * time. A class derived from the decorator can then override operations
 * to alter the behavior of the graph.</p>
 *
 * <p>Since a derived decorator is allowed to alter the behavior of the
 * graph in any way, like adding nodes or removing edges, it is generally
 * not safe to make any assumptions about the relationship between the
 * original and the decorated graph.</p>
 *
 * @see GraphDecorator
 */
public class AugmentedGraphDecorator extends GraphDecorator implements AugmentedGraph  {
  public AugmentedGraphDecorator(AugmentedGraph graph) {
    super(graph);
  }

  public boolean isNode(Object obj) {
    return augmented().isNode(obj);
  }

  public List edgesTo(Object node) {
    return augmented().edgesTo(node);
  }

  /**
   * <p>The original graph.</p>
   */
  protected final AugmentedGraph augmented() {
    return (AugmentedGraph)graph();
  }
}
