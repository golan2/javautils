package javautils.graph.testing;

import java.util.ArrayList;
import java.util.List;
import javautils.Counter;
import javautils.collections.Algs;
import javautils.fun.Function;
import javautils.fun.ObjectToObject;
import javautils.graph.adt.Graph;
import javautils.maps.ObjectToListMap;

/**
 * <p>Basic implementation of the {@link Graph}-inferface for testing.</p>
 */
public class BasicGraph extends Algs implements Graph {

  /**
   * <p>A Graph created from the given "association table"
   * representation.</p>
   */
  public BasicGraph(Object[][] graph) {
    final Counter counter = new Counter();

    forEach(graph,
            new Function() {
              public void with(final Object[] row) {
                nodes.add(row[0]);

                nodeToEdgesMap.put(row[0],
                                   collect(map(iterator(row, 1),
                                               new ObjectToObject() {
                                                 public Object with(Object target) {
                                                   return new BasicEdge(row[0], target, (int)counter.next());
                                                 }})));
              }});

    nodes = newUnmodifiableList(nodes);
    nodeToEdgesMap.transformListsToUnmodifiableLists();
  }

  public List nodes() {
    return nodes;
  }

  public List edgesFrom(Object node) {
    return nodeToEdgesMap.get(node);
  }

  public Object sourceOf(Object edge) {
    return ((BasicEdge)edge).first;
  }

  public Object targetOf(Object edge) {
    return ((BasicEdge)edge).second;
  }

  private List nodes = new ArrayList();
  private ObjectToListMap nodeToEdgesMap = new ObjectToListMap();
}
