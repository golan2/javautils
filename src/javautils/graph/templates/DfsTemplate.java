package javautils.graph.templates;

import java.util.Collection;
import java.util.Iterator;
import javautils.graph.adt.Graph;
import javautils.collections.Algs;
import javautils.dispensers.Stack;
import javautils.holders.IntHolder;

/**
 * <p>An abstract Template Method [<a
 * href="{@docRoot}/overview-summary.html#[Gamma1995]">Gamma1995</a>] for
 * depth-first search [<a
 * href="{@docRoot}/overview-summary.html#[Cormen2001]">Cormen2001</a>].</p>
 *
 * <p><b>Note</b>:</p>
 * <ul>
 * <li>Be careful to override the <i>correct</i> event-point methods.</li>
 * <li>Remember to <i>explicitly</i> call the desired
 * <code>search</code>-method.</li>
 * </ul>
 *
 * <h3>Example: DFS-tree</h3>
 *
 * <p>The following method computes the depth-first search tree.
 *
 * <pre>
 *  public static Graph dfsTree(<b>final</b> {@link Graph} graph) {
 *   // Here we define the map that will store the edges of the
 *   // depth-first search tree.
 *   <b>final</b> {@link javautils.maps.ObjectToListMap} nodeToEdgesMap = new {@link javautils.maps.ObjectToListMap#ObjectToListMap() ObjectToListMap}();
 *
 *   // Here we specialize an algorithm from the DFS-template method<b>...</b>
 *   <b>new</b> DfsTemplate() {
 *     // For each tree edge, we add the edge to the list of the
 *     // corresponding source node.
 *     <b>protected</b> void {@link #treeEdge treeEdge}(Object edge) {
 *       nodeToEdgesMap.{@link javautils.maps.ObjectToListMap#ensuredGet(Object) ensuredGet}(graph.{@link Graph#sourceOf sourceOf}(edge)).add(edge);
 *     }
 *   }.{@link #search(Graph) search}(graph);
 *   // <b>...</b>and above we call it on the graph.
 *
 *   // Here we make the lists in the map unmodifiable in order to make
 *   // the returned graph unmodifiable.
 *   nodeToEdgesMap.{@link javautils.maps.ObjectToListMap#transformListsToUnmodifiableLists() transformListsToUnmodifiableLists}();
 *
 *   // Here we define a new graph that represents the computed
 *   // depth-first search tree.
 *   return new {@link javautils.graph.GraphDecorator}(graph) {
 *       public List {@link Graph#edgesFrom edgesFrom}(Object node) {
 *         return nodeToEdgesMap.{@link javautils.maps.ObjectToListMap#getOrEmptyUnmodifiableList(Object) getOrEmptyUnmodifiableList}(node);
 *       }};
 * }
 * </pre>
 */
public abstract class DfsTemplate {

  /**
   * <p>Called once for each node before the search.</p>
   */
  protected void initNode(Object node) {}

  /**
   * <p>Called once for each node that is first encountered from the
   * search root list.</p>
   */
  protected void discoverRoot(Object node) {}

  /**
   * <p>Called once for each discovered search root after the complete
   * search tree starting at the node has been finished.</p>
   */
  protected void finishRoot(Object node) {}

  /**
   * <p>Called once for each node after the node is first discovered as a
   * root or immediately after a tree edge is found whose target is the
   * node.</p>
   */
  protected void discoverNode(Object node) {}

  /**
   * <p>Called once for each node after the complete search tree starting
   * at the node has been finished.</p>
   */
  protected void finishNode(Object node) {}

  /**
   * <p>Called once for each tree edge as it is examined by the search.</p>
   */
  protected void treeEdge(Object edge) {}

  /**
   * <p>Called once for each forward edge as it is examined by the search.</p>
   */
  protected void forwardEdge(Object edge) {}

  /**
   * <p>Called once for each cross edge as it is examined by the search.</p>
   */
  protected void crossEdge(Object edge) {}

  /**
   * <p>Called once for each back edge as it is examined by the search.</p>
   */
  protected void backEdge(Object edge) {}

  /**
   * <p>Performs depth-first search on the graph and calls the event point
   * methods.</p>
   */
  public final void search(Graph graph) {
    search(graph, graph.nodes());
  }

  /**
   * <p>Performs depth-first search on the graph, examines root nodes in
   * the given sequence, and calls the event point methods.</p>
   */
  public final void search(Graph graph, Iterator rootNodes) {
    new TimedSearchTemplate(graph, rootNodes, new Stack()) {
      int counter = 0;

      protected void prepareNode(Object node) {
        initNode(node);
      }

      protected void beginRoot(Object node, IntHolder time) {
        time.value = Integer.MIN_VALUE;
        discoverRoot(node);
      }

      protected void endRoot(Object node, IntHolder time) {
        finishRoot(node);
      }

      protected void handleNode(Object node, IntHolder time) {
        if (Integer.MIN_VALUE == time.value) {
          time.value = ++counter;
          discoverNode(node);
          eventDispenser.push(node);
          eventDispenser.pushAllRight(graph.edgesFrom(node));
        } else {
          time.value = - ++counter;
          finishNode(node);
        }
      }

      protected void handleEdge(Object edge, Object target, IntHolder targetTime) {
        if (0 == targetTime.value) {
          targetTime.value = Integer.MIN_VALUE;
          treeEdge(edge);
          eventDispenser.push(target);
        } else if (Integer.MIN_VALUE == targetTime.value) {
          forwardEdge(edge);
        } else if (0 < targetTime.value) {
          backEdge(edge);
        } else if (-targetTime.value < timeOf(graph.sourceOf(edge)).value) {
          crossEdge(edge);
        } else {
          forwardEdge(edge);
        }
      }
    }.search();
  }

  public final void search(Graph graph, Collection rootNodes) { search(graph, rootNodes.iterator()); }
  public final void search(Graph graph, Object      rootNode) { search(graph, Algs.singletonIterator(rootNode)); }
}
