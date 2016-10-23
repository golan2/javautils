package javautils.graph.templates;

import java.util.Collection;
import java.util.Iterator;
import javautils.graph.adt.Graph;
import javautils.collections.Algs;
import javautils.dispensers.Queue;
import javautils.holders.IntHolder;

/**
 * <p>An abstract Template Method [<a
 * href="{@docRoot}/overview-summary.html#[Gamma1995]">Gamma1995</a>] for
 * breadth-first search [<a
 * href="{@docRoot}/overview-summary.html#[Cormen2001]">Cormen2001</a>].</p>
 *
 * <p><b>Note</b>:</p>
 * <ul>
 * <li>Be careful to override the <i>correct</i> event-point methods.</li>
 * <li>Remember to <i>explicitly</i> call the desired
 * <code>search</code>-method.</li>
 * </ul>
 *
 * <h3>Example: Shortest path in an unweighted graph</h3>
 *
 * <p>This example shows how the BFS template could be used to compute a
 * shortest path between two nodes. The following algorithm is
 * asymptotically optimal in the worst case.</p>
 *
 * <pre>
 * // First we need a method to compute an inverted BFS-tree representation.
 * public static Map mapOfInvertedBfsTree(<b>final</b> {@link Graph} graph, List roots) {
 *   // Here we define a map that holds the inverted BFS-tree.
 *   <b>final</b> Map nodeToParentMap = new HashMap();
 *
 *   // Here we specialize an algorithm from the BFS template<b>...</b>
 *   <b>new</b> BfsTemplate() {
 *     // For each tree edge, we update the inverted BFS-tree.
 *     <b>protected</b> void {@link #treeEdge treeEdge}(Object edge) {
 *       nodeToParentMap.put(graph.{@link Graph#targetOf targetOf}(edge), graph.{@link Graph#sourceOf sourceOf}(edge));
 *     }
 *   }.{@link #search(Graph,Collection) search}(graph, roots);
 *   // <b>...</b>and above we invoke the specialized template, searching
 *   // only the specified roots.
 *
 *   return nodeToParentMap;
 * }
 *
 * // The actual shortest path finding method.
 * public static List shortestUnweightedPath({@link Graph} graph,
 *                                           Object source,
 *                                           <b>final</b> Object target) {
 *   <b>final</b> Map nodeToParentMap =
 *     mapOfInvertedBfsTree(graph, Collections.singletonList(source));
 *
 *   // Is there a path?
 *   return nodeToParentMap.containsKey(target)
 *     ? <b>new</b> {@link javautils.collections.Unfold}() {
 *         // Here we traverse the inverted BFS-tree starting at the
 *         // target node towards the source node and construct the path.
 *
 *         Object work = target;
 *
 *         <b>protected</b> boolean {@link javautils.collections.Unfold#more more}() {<b>return</b> null != work;}
 *         <b>protected</b> Object {@link javautils.collections.Unfold#value value}() {<b>return</b> work;}
 *         <b>protected</b> void {@link javautils.collections.Unfold#advance advance}() {work = nodeToParentMap.get(work);}
 *       }.{@link javautils.collections.Unfold#unfoldRight unfoldRight}()
 *     : null;
 * }
 * </pre>
 */
public abstract class BfsTemplate {

  /**
   * <p>Called once for each node before the search.</p>
   */
  protected void initNode(Object node) {}

  /**
   * <p>Called once for each node that is first encountered from the root
   * list.</p>
   */
  protected void discoverRoot(Object node) {}

  /**
   * <p>Called once for each discovered root node after the entire search
   * tree rooted at the node has been examined.</p>
   */
  protected void finishRoot(Object node) {}

  /**
   * <p>Called once for each discovered node either after the node has
   * been discovered as a root or after all edges in the previous level of
   * the search tree have been examined, but before any edges in the
   * current level are examined.</p>
   */
  protected void discoverNode(Object node) {}

  /**
   * <p>Called once for each node in the searh tree after all edges from
   * the node have been examined but before any nodes on the next level of
   * the search tree are examined.</p>
   */
  protected void finishNode(Object node) {}

  /**
   * <p>Called for each search tree edge after discovering all the nodes
   * in the current level of the search tree, but before any nodes in the
   * next level are discovered.</p>
   */
  protected void treeEdge(Object edge) {}

  /**
   * <p>Called for each edge that is not part of the search tree after
   * discovering all the nodes in the current level of the search tree,
   * but before any nodes in the next level are discovered.</p>
   */
  protected void nonTreeEdge(Object edge) {}

  /**
   * <p>Performs breadth-first search on the given graph and calls the
   * event point methods.</p>
   */
  public final void search(Graph graph) {
    search(graph, graph.nodes());
  }

  /**
   * <p>Performs breadth-first search on the given graph, examines root
   * nodes in the given sequence, and calls the event point methods.</p>
   */
  public final void search(Graph graph, Iterator roots) {
    new TimedSearchTemplate(graph, roots, new Queue()) {
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
          time.value = 1;
          discoverNode(node);
          eventDispenser.pushAll(graph.edgesFrom(node));
          eventDispenser.push(node);
        } else {
          time.value = -1;
          finishNode(node);
        }
      }

      protected void handleEdge(Object edge, Object target, IntHolder targetTime) {
        if (0 == targetTime.value) {
          targetTime.value = Integer.MIN_VALUE;
          treeEdge(edge);
          eventDispenser.push(target);
        } else {
          nonTreeEdge(edge);
        }
      }
    }.search();
  }

  public final void search(Graph graph, Collection rootOrder) { search(graph, rootOrder.iterator()); }
  public final void search(Graph graph, Object      rootNode) { search(graph, Algs.singletonIterator(rootNode)); }
}
