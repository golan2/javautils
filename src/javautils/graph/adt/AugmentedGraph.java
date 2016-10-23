package javautils.graph.adt;

import java.util.List;

/**
 * <p>An augmented graph allows accessing edges from target nodes. This
 * can improve the efficiency of graph algorithms that would otherwise
 * need to effectively compute the transpose of the graph first (in
 * <code>O(N+E)</code> time).</p>
 *
 * <p><b>Note</b>: Clients of non-trivial graph libraries usually do not
 * explicitly need to implement this interface - not even for efficiency
 * reasons. A non-trivial library can easily compute the augmented graph
 * if necessary. If the client passes an already augmented graph to the
 * library, then only the initial augmentation step will be bypassed.</p>
 */
public interface AugmentedGraph extends Graph {

  /**
   * <p>True if and only if the object is a node of this graph.</p>
   */
  boolean isNode(Object obj);

  /**
   * <p>List of all edges to the specified target node.</p>
   *
   * <p><b>Important</b>: This method should have <code>O(1)</code> time
   * complexity. This means that you should avoid constructing the list
   * each time this method is called.</p>
   *
   * <p><b>Note</b>: The order of edges in the returned list may have an
   * effect on the results of graph algorithms.</p>
   */
  List edgesTo(Object node);
}
