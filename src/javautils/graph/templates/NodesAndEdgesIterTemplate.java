package javautils.graph.templates;

import javautils.graph.adt.Graph;
import java.util.Iterator;

/**
 * <p>An abstract Template Method [<a
 * href="{@docRoot}/overview-summary.html#[Gamma1995]">Gamma1995</a>] for
 * iterating over all nodes and edges of a graph.</p>
 */
public abstract class NodesAndEdgesIterTemplate {

  /**
   * <p>Called once for each node.</p>
   */
  protected abstract void doNode(Object node);

  /**
   * <p>Called once between switching from nodes to edges or vice
   * versa.</p>
   */
  protected void doBetween() {}

  /**
   * <p>Called once for each edge.</p>
   */
  protected abstract void doEdge(Object edge);

  /**
   * <p>First does all nodes, then does all edges.</p>
   */
  public final void iter(Graph graph) {
    iterNodes(graph);
    doBetween();
    iterEdges(graph);
  }

  /**
   * <p>First does all edges, then does all nodes.</p>
   */
  public final void iterEdgesThenNodes(Graph graph) {
    iterEdges(graph);
    doBetween();
    iterNodes(graph);
  }

  private void iterNodes(Graph graph) {
    for (Iterator nodeIte = graph.nodes().iterator(); nodeIte.hasNext();)
      doNode(nodeIte.next());
  }

  private void iterEdges(Graph graph) {
    for (Iterator nodeIte = graph.nodes().iterator(); nodeIte.hasNext();)
      for (Iterator edgeIte = graph.edgesFrom(nodeIte.next()).iterator(); edgeIte.hasNext();)
        doEdge(edgeIte.next());
  }
}
