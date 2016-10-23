package javautils.graph.templates;

import javautils.graph.adt.Graph;
import java.util.Iterator;

/**
 * <p>An abstract Template Method [<a
 * href="{@docRoot}/overview-summary.html#[Gamma1995]">Gamma1995</a>] for
 * iterating a graph based on the adjacency structure of the graph.
 * Iteration based on the adjacency structure has some highly useful
 * properties that make it worth implementing once as a template
 * method.</p>
 */
public abstract class AdjacentIterTemplate {
  /**
   * <p>Called once for each node before calling {@link #doEdge doEdge}
   * for each edge from the node.</p>
   */
  protected abstract void beginNode(Object node);

  /**
   * <p>Called once for each edge from a node.</p>
   */
  protected abstract void doEdge(Object edge);

  /**
   * <p>Called once for each node after all edges from the node have been
   * done.</p>
   */
  protected abstract void finishNode(Object node);

  /**
   * <p>Iterates over the graph and calls the event point methods.</p>
   */
  public final void iter(Graph graph) {
    for (Iterator ite = graph.nodes().iterator(); ite.hasNext();)
      doNode(ite.next(), graph);
  }

  private final void doNode(Object node, Graph graph) {
    beginNode(node);
    for (Iterator ite = graph.edgesFrom(node).iterator(); ite.hasNext();)
      doEdge(ite.next());
    finishNode(node);
  }
}
