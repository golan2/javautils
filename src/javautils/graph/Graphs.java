package javautils.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javautils.Counter;
import javautils.ImmutablePair;
import javautils.collections.Algs;
import javautils.collections.Unfold;
import javautils.fun.Function;
import javautils.fun.ObjectToBoolean;
import javautils.fun.ObjectToObject;
import javautils.fun.ObjectToObjectToBoolean;
import javautils.fun.ObjectToVoid;
import javautils.graph.adt.AugmentedGraph;
import javautils.graph.adt.Graph;
import javautils.graph.templates.AdjacentIterTemplate;
import javautils.graph.templates.DfsTemplate;
import javautils.graph.testing.BasicEdge;
import javautils.maps.ObjectToIntMap;
import javautils.maps.ObjectToListMap;

/**
 * <p>Static utility methods for dealing with graphs.</p>
 */
public class Graphs extends Algs {

  // NOTE: Please keep the methods in this class in alphabetical order.
  // These methods are mostly unrelated. Keeping the methods in
  // alphabetical order makes it easier to locate individual methods.

  /**
   * <p>A string representation of the graph, where node ids are the nodes
   * themselves and edge ids are assigned using a counter.</p>
   */
  public static String asString(Graph graph) {
    final Counter counter = new Counter();
    final ObjectToIntMap edgeToIdMap = new ObjectToIntMap();

    forEachEdge(graph,
                new ObjectToVoid() {
                  public void with(Object edge) {
                    edgeToIdMap.put(edge, (int)counter.next());
                  }});

    return asString(graph,
                    ObjectToObject.IDENTITY,
                    new Function(edgeToIdMap, "get", Object.class));
  }

  /**
   * <p>A string representation of the graph.</p>
   */
  public static String asString(final Graph graph, Function nodeToIdFun, Function edgeToIdFun) {
    final ObjectToObject nodeToId = ObjectToObject.from(nodeToIdFun);
    final ObjectToObject edgeToId = ObjectToObject.from(edgeToIdFun);

    final StringBuffer result = new StringBuffer();

    new AdjacentIterTemplate() {
      protected void beginNode(Object node) {
        result.append(nodeToId.with(node) + (0 != graph.edgesFrom(node).size() ? ":" : ""));
      }

      protected void doEdge(Object edge) {
        result.append(" " + nodeToId.with(graph.targetOf(edge)) + (null != edgeToId.with(edge) ? "[" + edgeToId.with(edge) + "]" : ""));
      }

      protected void finishNode(Object node) {
        result.append(";\n");
      }
    }.iter(graph);

    return result.toString();
  }

  /**
   * <p>Augmented view of the given graph. If the given graph is already
   * augmented, the same graph will simply be returned.</p>
   */
  public static AugmentedGraph augmented(Graph graph) {
    return AnAugmentedGraph.from(graph);
  }

  /**
   * <p>The connected components of the given graph. The graph is
   * interpreted as undirected. Two nodes <code>a</code> and
   * <code>b</code> are in the same component if there exists a path,
   * <code>{{a,c0}, {c0,c1}, ..., {cn,b}}</code>, between them. The result
   * is a collection of component graphs.</p>
   */
  public static Collection connectedComponents(final Graph directedGraph) {
    final List result = new ArrayList();

    new DfsTemplate() {
      List currentComponent = null;

      protected void discoverRoot(Object node) {
        currentComponent = new ArrayList();
      }

      protected void finishRoot(Object node) {
        result.add(unsafeRestricted(directedGraph, currentComponent));
      }

      protected void discoverNode(Object node) {
        currentComponent.add(node);
      }
    }.search(undirected(directedGraph));

    return result;
  }

  /**
   * <p>A sequence of all edges of the graph.</p>
   */
  public static Iterator edges(final Graph graph) {
    final Iterator nodes = graph.nodes().iterator();

    return new Unfold() {
        private Iterator edges = nodes.hasNext()
          ? graph.edgesFrom(nodes.next()).iterator()
            : nodes;

        protected void    init() { advance(); }
        protected boolean more() { return edges.hasNext(); }
        protected Object value() { return edges.next(); }

        protected void advance() {
          while (!edges.hasNext() && nodes.hasNext())
            edges = graph.edgesFrom(nodes.next()).iterator();
        }
      }.unfoldAsIterator();
  }

  /**
   * <p>A set of all edges of the graph.</p>
   */
  public static Set edgeSet(Graph graph) {
    Set result = new HashSet();
    addAll(edges(graph), result);
    return result;
  }

  /**
   * <p>Calls the given procedure for each edge of the given graph.</p>
   */
  public static void forEachEdge(Graph graph, Function proc) {
    forEach(edges(graph), proc);
  }

  /**
   * <p>Calls the given procedure for each node of the given graph.</p>
   */
  public static void forEachNode(Graph graph, Function proc) {
    forEach(graph.nodes(), proc);
  }

  /**
   * <p>A graph induced by the specified edges and additionally containing
   * the specified nodes.</p>
   */
  public static Graph inducedByEdgesAndContainingNodes(final Graph graph,
                                                       Iterator edges,
                                                       Iterator includedNodes) {
    final ObjectToListMap nodeToEdgesFromMap = new ObjectToListMap();

    forEach(includedNodes,
            new ObjectToVoid() {
              public void with(Object node) {
                nodeToEdgesFromMap.ensuredGet(node);
              }});

    forEach(edges,
            new ObjectToVoid() {
              public void with(Object edge) {
                nodeToEdgesFromMap.ensuredGet(graph.sourceOf(edge)).add(edge);
                nodeToEdgesFromMap.ensuredGet(graph.targetOf(edge));
              }});

    final List nodes = newUnmodifiableList(nodeToEdgesFromMap.keySet());

    nodeToEdgesFromMap.transformListsToUnmodifiableLists();

    return new GraphDecorator(graph) {
        public List nodes() { return nodes; }
        public List edgesFrom(Object node) { return nodeToEdgesFromMap.get(node); }
      };
  }

  /**
   * <p>True if and only if the invariant of the graph holds. This method
   * handles both graphs and augmented graphs.</p>
   */
  public static boolean invariant(final Graph graph) {
    return new Object() {
        Set nodeSet = nodeSet(graph);

        boolean with() {
          return fromEdgeInvariant(graph) &&
            !(graph instanceof AugmentedGraph) ||
            fromEdgeInvariant(transposed(graph)) &&
            forAll(graph.nodes(),
                   new Function(graph, "isNode", Object.class)) &&
            !exists(edges(graph),
                    new Function(graph, "isNode", Object.class));
        }

        boolean fromEdgeInvariant(final Graph graph) {
          return
            forAll(graph.nodes(),
                   new ObjectToBoolean() {
                     public boolean with(final Object node) {
                       return
                         forAll(graph.edgesFrom(node),
                                new ObjectToBoolean() {
                                  public boolean with(Object edge) {
                                    return node.equals(graph.sourceOf(edge)) &&
                                      nodeSet.contains(graph.targetOf(edge));
                                  }});
                     }});
        }
      }.with();
  }

  /**
   * <p>True iff the given graph is acyclic. This method is not meaningful
   * on undirected graphs.</p>
   */
  public static boolean isAcyclic(Graph graph) {
    try {
      new DfsTemplate() {
        protected void backEdge(Object edge) {
          throw new BackEdgeException();
        }
      }.search(graph);

      return true;
    } catch (BackEdgeException e) {
      return false;
    }
  }

  private static class BackEdgeException extends RuntimeException {}

  /**
   * <p>True iff the given node is the target of the given edge.</p>
   */
  public static boolean isIncoming(Graph graph, Object edge, Object node) {
    return graph.targetOf(edge).equals(node);
  }

  /**
   * <p>True iff the given edge is a self edge.</p>
   */
  public static boolean isSelf(Graph graph, Object edge) {
    return graph.sourceOf(edge).equals(graph.targetOf(edge));
  }

  /**
   * <p>A list of all nodes of the given graph in increasing order of
   * indegree.</p>
   */
  public static List nodesByIncreasingIndegree(Graph graph) {
    return nodesByIncreasingOutdegree(transposed(graph));
  }

  /**
   * <p>A list of all nodes of the given graph in increasing order of
   * outdegree.</p>
   */
  public static List nodesByIncreasingOutdegree(final Graph graph) {
    return sorted(graph.nodes(),
                  new ObjectToObjectToBoolean() {
                    public boolean with(Object lhs, Object rhs) {
                      return graph.edgesFrom(lhs).size() < graph.edgesFrom(rhs).size();
                    }});
  }

  /**
   * <p>A list of all nodes of the given graph in order of decreasing
   * DFS-finishing time.</p>
   */
  public static List nodesByDecreasingDfsFinishingTime(Graph graph) {
    final LinkedList result = new LinkedList();

    new DfsTemplate() {
      protected void finishNode(Object node) {
        result.addFirst(node);
      }}.search(graph);

    return result;
  }

  /**
   * <p>Set of nodes reachable from the specified roots.</p>
   */
  public static Set nodesReachableFrom(Graph graph, Iterator roots) {
    final Set result = new HashSet();

    new DfsTemplate() {
      protected void discoverNode(Object node) {
        result.add(node);
      }
    }.search(graph, roots);

    return result;
  }

  public static Set nodesReachableFrom(Graph graph, Collection roots) { return nodesReachableFrom(graph, roots.iterator()); }
  public static Set nodesReachableFrom(Graph graph, Object      root) { return nodesReachableFrom(graph, singletonIterator(root)); }

  /**
   * <p>A set of all nodes of the graph.</p>
   */
  public static Set nodeSet(Graph graph) {
    return new HashSet(graph.nodes());
  }

  /**
   * <p>The other node of the edge.</p>
   */
  public static Object otherNode(Graph graph, Object edge, Object node) {
    assert node.equals(graph.targetOf(edge)) || node.equals(graph.sourceOf(edge));

    return graph.targetOf(edge).equals(node)
      ? graph.sourceOf(edge)
      : graph.targetOf(edge);
  }

  /**
   * <p>A new random graph with the specified number of nodes and edges.</p>
   */
  public static Graph randomGraph(final int numNodes, int numEdges) {
    final List nodes = newUnmodifiableList(new Unfold() {
        int i = 0;

        protected boolean more() { return i < numNodes; }
        protected Object value() { return new Integer(i); }
        protected void advance() { ++i; }
      }.unfold());

    final ObjectToListMap nodeToEdgesMap = new ObjectToListMap();

    while (0 < numEdges--) {
      BasicEdge edge = new BasicEdge(nodes.get((int)(Math.random() * numNodes)),
                                     nodes.get((int)(Math.random() * numNodes)),
                                     numEdges);
      nodeToEdgesMap.ensuredGet(edge.first).add(edge);
    }

    nodeToEdgesMap.transformListsToUnmodifiableLists();

    return new Graph() {
        public List nodes() { return nodes; }
        public List edgesFrom(Object node) { return nodeToEdgesMap.getOrEmptyUnmodifiableList(node); }
        public Object sourceOf(Object edge) { return ((BasicEdge)edge).first; }
        public Object targetOf(Object edge) { return ((BasicEdge)edge).second; }
      };
  }

  /**
   * <p>A graph restricted to the specified nodes and edges between the
   * specified nodes.</p>
   */
  public static Graph restrictedToNodes(final Graph graph, Iterator nodes) {
    final List nodeList = collectUnmodifiable(nodes);
    final Set nodeSet = new HashSet(nodeList);
    final ObjectToListMap nodeToEdgesMap = new ObjectToListMap();

    forEach(nodeList,
            new ObjectToVoid() {
              public void with(Object node) {
                List edges = collect(filter(graph.edgesFrom(node),
                                            new ObjectToBoolean() {
                                              public boolean with(Object edge) {
                                                return nodeSet.contains(graph.targetOf(edge));
                                              }}));

                if (0 != edges.size())
                  nodeToEdgesMap.put(node, edges);
              }});

    nodeToEdgesMap.transformListsToUnmodifiableLists();

    return new GraphDecorator(graph) {
        public List nodes() {
          return nodeList;
        }

        public List edgesFrom(Object node) {
          return nodeToEdgesMap.getOrEmptyUnmodifiableList(node);
        }};
  }

  public static Graph restrictedToNodes(Graph graph, Collection nodes) { return restrictedToNodes(graph, iterator(nodes)); }
  public static Graph restrictedToNodes(Graph graph, Object[]   nodes) { return restrictedToNodes(graph, iterator(nodes)); }


  /**
   * <p>True if and only if the given graphs have the same nodes and
   * edges.</p>
   */
  public static boolean sameNodesAndEdges(Graph lhs, Graph rhs) {
    return
      nodeSet(lhs).equals(nodeSet(rhs)) &&
      edgeSet(lhs).equals(edgeSet(rhs));
  }

  public static ImmutablePair asSourceTargetPair(Graph graph, Object edge) {
    return new ImmutablePair(graph.sourceOf(edge), graph.targetOf(edge));
  }

  /**
   * <p>The strongly connected components of the given directed graph. The
   * result is a collection of collections of nodes of each component.</p>
   */
  public static Collection stronglyConnectedComponents(Graph graph) {
    final List result = new ArrayList();

    new DfsTemplate() {
      List currentComponent = null;

      protected void discoverRoot(Object node) {
        currentComponent = new ArrayList();
      }

      protected void finishRoot(Object node) {
        result.add(currentComponent);
      }

      protected void discoverNode(Object node) {
        currentComponent.add(node);
      }
    }.search(transposed(graph), nodesByDecreasingDfsFinishingTime(graph));

    return result;
  }

  /**
   * <p>Transitive, irreflexive closure of the given graph.</p>
   */
  public static Graph transitiveIrreflexiveClosure(final Graph graph) {
    final ObjectToListMap nodeToEdgesMap = new ObjectToListMap();

    forEach(graph.nodes(),
            new ObjectToVoid() {
              public void with(final Object sourceNode) {
                new DfsTemplate() {
                  protected void discoverNode(Object targetNode) {
                    if (!sourceNode.equals(targetNode))
                      nodeToEdgesMap.ensuredGet(sourceNode).add(new ImmutablePair(sourceNode,
                                                                                  targetNode));
                  }
                }.search(graph, sourceNode);
              }});

    nodeToEdgesMap.transformListsToUnmodifiableLists();

    return new GraphDecorator(graph) {
        public List edgesFrom(Object node) {
          return nodeToEdgesMap.getOrEmptyUnmodifiableList(node);
        }

        public Object sourceOf(Object edge) {
          return ((ImmutablePair)edge).first;
        }

        public Object targetOf(Object edge) {
          return ((ImmutablePair)edge).second;
        }
      };
  }

  /**
   * <p>Transpose view of the given finite graph. The transpose view
   * shares the same node and edge objects with the original graph.</p>
   *
   * <p><b>Note</b>: Neither the original graph nor the transpose view
   * should ever be modified.</p>
   */
  public static AugmentedGraph transposed(AugmentedGraph graph) {
    return TransposedGraph.from(graph);
  }

  /**
   * <p>Transpose view of the given finite graph. The transpose view
   * shares the same node and edge objects with the original graph.</p>
   *
   * <p><b>Note</b>: Neither the original graph nor the transpose view
   * should ever be modified.</p>
   */
  public static Graph transposed(Graph graph) {
    return graph instanceof UndirectedGraph
      ? graph
      : transposed(augmented(graph));
  }

  /**
   * <p>Undirected view of the given finite graph. The undirected view
   * shares the same node objects with the original graph.</p>
   *
   * <p><b>Note</b>:</p>
   * <ul>
   * <li>The undirected view does not share the same edge objects with the
   * original graph.</li>
   * <li>Neither the original graph nor the undirected view should ever be
   * modified.</li>
   * </ul>
   */
  public static UndirectedGraph undirected(Graph graph) {
    return UndirectedGraph.from(graph);
  }

  private static Graph unsafeRestricted(Graph graph, final List nodes) {
    return graph instanceof AugmentedGraph
      ? (Graph)new AugmentedGraphDecorator((AugmentedGraph)graph) {
          public List nodes() {
            return nodes;
          }}
      : (Graph)new GraphDecorator(graph) {
          public List nodes() {
            return nodes;
          }};
  }
}
