package javautils.graph;

/**
 * <p>A Builder for Graphs (see [<a
 * href="{@docRoot}/overview-summary.html#[Gamma1995]">Gamma1995</a>]).</p>
 */
public interface GraphBuilder {

  /**
   * <p>Adds the node identified by <code>nodeID</code>, but only if there
   * is no node identified by an equal <code>Object</code> already.</p>
   */
  void addNode(Object nodeID);

  /**
   * <p>Adds the edge identified by <code>edgeID</code>, along with
   * identifiers for edge source and target.
   *
   * <p>If there is already an edge for which <code>edgeID</code> is
   * equal, no new edge is inserted.</p>
   */
  void addEdge(Object sourceNodeID, Object targetNodeID, Object edgeID);

  /**
   * <p>The object identified by <code>id</code>.</p>
   */
  Object object(Object id);
}
