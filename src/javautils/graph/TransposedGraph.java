package javautils.graph;

import java.util.List;
import javautils.graph.adt.AugmentedGraph;

/**
 * <p>A transposed graph. The purpose of this class is to support
 * efficient graph transposition. Transposed graphs are created from
 * augmented graphs because the information needed to transpose a graph is
 * essentially the same information that an augmented graph already
 * has.</p>
 */
public final class TransposedGraph extends AugmentedGraphDecorator {
  /**
   * <p>A transposed version of the given graph. If the graph is a
   * transposed graph, the original graph is returned.</p>
   */
  public static AugmentedGraph from(AugmentedGraph graph) {
    if (graph instanceof TransposedGraph)
      return ((TransposedGraph)graph).augmented();
    else
      return new TransposedGraph(graph);
  }

  private TransposedGraph(AugmentedGraph graph) {
    super(graph);
  }

  public List edgesFrom(Object node) {
    return super.edgesTo(node);
  }

  public List edgesTo(Object node) {
    return super.edgesFrom(node);
  }

  public Object targetOf(Object edge) {
    return super.sourceOf(edge);
  }

  public Object sourceOf(Object edge) {
    return super.targetOf(edge);
  }
}
