package javautils.graph;

import java.util.List;
import javautils.collections.Algs;
import javautils.fun.ObjectToVoid;
import javautils.graph.Graphs;
import javautils.graph.adt.AugmentedGraph;
import javautils.graph.adt.Graph;
import javautils.maps.ObjectToListMap;

/**
 * <p>An augmented version of an original {@link Graph}-instance. Because
 * this class is final, it is safe to access the original graph which is
 * simply the unaugmented version of the graph.</p>
 */
public final class AnAugmentedGraph extends GraphDecorator implements AugmentedGraph  {
  /**
   * <p>An augmented version of the graph. If the graph is already
   * augmented, the same graph will be returned.</p>
   */
  public static AugmentedGraph from(Graph graph) {
    return graph instanceof AugmentedGraph
      ? (AugmentedGraph)graph
      : new AnAugmentedGraph(graph);
  }

  private AnAugmentedGraph(final Graph graph) {
    super(graph);

    Graphs.forEachEdge(graph,
                       new ObjectToVoid() {
                         public void with(Object edge) {
                           targetNodeToEdgesMap.ensuredGet(graph.targetOf(edge)).add(edge);
                         }});

    targetNodeToEdgesMap.transformListsToUnmodifiableLists();

    Graphs.forEachNode(graph,
                       new ObjectToVoid() {
                         public void with(Object node) {
                           if (!targetNodeToEdgesMap.containsKey(node))
                             targetNodeToEdgesMap.put(node, Algs.EMPTY_LIST);
                         }});
  }

  public boolean isNode(Object obj) {
    return targetNodeToEdgesMap.containsKey(obj);
  }

  public List edgesTo(Object node) {
    return targetNodeToEdgesMap.get(node);
  }

  /**
   * <p>The original, unaugmented, graph.</p>
   */
  public Graph original() {
    return graph();
  }

  private ObjectToListMap targetNodeToEdgesMap = new ObjectToListMap();
}
