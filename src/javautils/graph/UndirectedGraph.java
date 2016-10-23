package javautils.graph;

import java.util.ArrayList;
import java.util.List;
import javautils.graph.adt.Graph;
import javautils.graph.templates.NodesAndEdgesIterTemplate;
import javautils.maps.ObjectToListMap;

/**
 * <p>An undirected graph. Undirected graphs are created from unaugmented
 * graphs, because the augmented information does not have much use in an
 * undirected graph. Note that you can always augment an undirected graph
 * afterwards.</p>
 */
public final class UndirectedGraph extends GraphDecorator implements Graph {
  /**
   * <p>An undirected version of the given graph. If the graph is already
   * undirected, the same graph will be returned.</p>
   */
  public static UndirectedGraph from(Graph graph) {
    if (graph instanceof UndirectedGraph)
      return (UndirectedGraph)graph;
    else if (graph instanceof AnAugmentedGraph)
      return from(((AnAugmentedGraph)graph).original());
    else
      return new UndirectedGraph(graph);
  }

  private static class Inverted {
    public Inverted(Object edge) {
      this.edge = edge;
    }

    public final Object edge;
  }

  private UndirectedGraph(final Graph graph) {
    super(graph);

    new NodesAndEdgesIterTemplate() {
      protected void doNode(Object node) {
        nodeToEdgesMap.put(node, new ArrayList(graph.edgesFrom(node)));
      }

      protected void doEdge(Object edge) {
        if (!graph.targetOf(edge).equals(graph.sourceOf(edge)))
          nodeToEdgesMap.get(graph.targetOf(edge)).add(new Inverted(edge));
      }
    }.iter(graph);

    nodeToEdgesMap.transformListsToUnmodifiableLists();
  }

  /**
   * <p>True if and only if the object is a node of this graph.</p>
   */
  public boolean isNode(Object obj) {
    return nodeToEdgesMap.containsKey(obj);
  }

  /**
   * <p>The underlying uninverted edge of the specified edge.</p>
   */
  public Object uninvertedOf(Object edge) {
    return edge instanceof Inverted
      ? ((Inverted)edge).edge
      : edge;
  }

  public List edgesFrom(Object node) {
    return nodeToEdgesMap.get(node);
  }

  public Object targetOf(Object edge) {
    return edge instanceof Inverted
      ? super.sourceOf(((Inverted)edge).edge)
      : super.targetOf(edge);
  }

  public Object sourceOf(Object edge) {
    return edge instanceof Inverted
      ? super.targetOf(((Inverted)edge).edge)
      : super.sourceOf(edge);
  }

  private final ObjectToListMap nodeToEdgesMap = new ObjectToListMap();
}
