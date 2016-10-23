package javautils.graph.adt;

import java.util.List;

/**
 * <p>Abstract, adjacency list style, representation of a directed
 * <b>finite</b> graph. The structure allows multiple edges between the
 * same nodes and nodes to have self edges. Each node and edge must be
 * represented by a separate object.</p>
 *
 * <p>In this library, graphs are generally considered to be
 * <b>immutable</b> - even though immutability may not be strictly
 * enforced. Instead of mutating a graph, one should generally create new
 * graphs.</p>
 *
 * <p><b>Note:</b> If you need to get <i>reproducible</i> results from
 * graph algorithms, you must make sure that the hash codes of node and
 * edge objects do not vary between different runs of the same program.
 * Note that if you override the {@link java.lang.Object#hashCode}-method,
 * you generally also need to override the {@link
 * java.lang.Object#equals}-method.</p>
 *
 * <h3>Design rationale</h3>
 *
 * <p>The basic assumption underlying the design of this interface is that
 * a client of a graph algorithm library already has some representation
 * of graphs. This graph interface is an Adapter, see [<a
 * href="{@docRoot}/overview-summary.html#[Gamma1995]">Gamma1995</a>],
 * that the client implements in order to allow the graph algorithms
 * implemented by the library to examine the graphs of the client.</p>
 *
 * <p>The superclass of nodes and edges is simply {@link Object}. The
 * rationale for this is that requiring more specific interfaces would
 * require the client to actually implement the interfaces, potentially
 * requiring modifications to client code, or to implement an adapter
 * layer and actually instantiate new node and edge objects.</p>
 */
public interface Graph {

  /**
   * <p>List of <i>all</i> nodes.</p>
   *
   * <p><b>Important</b>: This method should have <code>O(1)</code> time
   * complexity. This means that you should avoid constructing the list
   * each time this method is called.</p>
   *
   * <p><b>Note</b>: The order of nodes in the returned list may have an
   * effect on the results of graph algorithms.</p>
   */
  List nodes();

  /**
   * <p>List of all edges from the specified source node.</p>
   *
   * <p><b>Important</b>: This method should have <code>O(1)</code> time
   * complexity. This means that you should avoid constructing the list
   * each time this method is called.</p>
   *
   * <p><b>Note</b>: The order of edges in the returned list may have an
   * effect on the results of graph algorithms.</p>
   */
  List edgesFrom(Object node);

  /**
   * <p>The source node of the edge.</p>
   */
  Object sourceOf(Object edge);

  /**
   * <p>The target node of the edge.</p>
   */
  Object targetOf(Object edge);
}
