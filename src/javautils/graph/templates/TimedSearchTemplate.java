package javautils.graph.templates;

import javautils.graph.adt.Graph;
import javautils.maps.ObjectToIntMap;
import javautils.dispensers.Dispenser;
import javautils.holders.IntHolder;
import java.util.Iterator;

/**
 * <p>An abstract Template Method [<a
 * href="{@docRoot}/overview-summary.html#[Gamma1995]">Gamma1995</a>] for
 * searching directed graphs in a timed fashion.</p>
 */
public abstract class TimedSearchTemplate {

  /**
   * <p>A new timed search template.</p>
   */
  protected TimedSearchTemplate(Graph graph,
                                Iterator roots,
                                Dispenser eventDispenser) {
    assert 0 != graph.nodes().size();
    assert roots.hasNext();
    assert eventDispenser.isEmpty();
    this.graph = graph;
    this.roots = roots;
    this.eventDispenser = eventDispenser;
  }

  /**
   * <p>Called once for each node before the search.</p>
   */
  protected void prepareNode(Object node) {}

  /**
   * <p>Called once for each root drawn from the root dispenser that has
   * not already been done.</p>
   */
  protected void beginRoot(Object node, IntHolder time) {}

  /**
   * <p>Called once for root that was begun after the search has finished
   * handling all events generated from the root.</p>
   */
  protected void endRoot(Object node, IntHolder time) {}

  /**
   * <p>Called once for each node event drawn from the event dispenser.</p>
   */
  protected abstract void handleNode(Object node, IntHolder time);

  /**
   * <p>Called once for each edge event drawn from the event dispenser.</p>
   */
  protected abstract void handleEdge(Object edge, Object target, IntHolder timeOfTarget);

  /**
   * <p>Performs the search calling the event point methods.</p>
   */
  public final void search() {
    prepareSearch();
    handleRoots();
  }

  private void prepareSearch() {
    for (Iterator ite = graph.nodes().iterator(); ite.hasNext();) {
      Object node = ite.next();
      nodeToTimeMap.put(node, newInitialTime());
      prepareNode(node);
    }
  }

  /**
   * <p>Called to create a new {@link IntHolder}-instance for each edge
   * before the search. By default the initial time is 0.</p>
   *
   * @see #notDone
   */
  protected IntHolder newInitialTime() {
    return new IntHolder(0);
  }

  private void handleRoots() {
    while (roots.hasNext())
      handleRoot(roots.next());
  }

  private void handleRoot(Object root) {
    IntHolder time = timeOf(root);

    if (notDone(time)) {
      beginRoot(root, time);
      handleNode(root, time);
      handleEvents();
      endRoot(root, time);
    }
  }

  private void handleEvents() {
    while (eventDispenser.notEmpty())
      handleEvent(eventDispenser.pop());
  }

  private void handleEvent(Object event) {
    IntHolder time = timeOf(event);

    if (null != time) {
      handleNode(event, timeOf(event));
    } else {
      Object target = graph.targetOf(event);
      handleEdge(event, target, timeOf(target));
    }
  }

  /**
   * <p>True if and only if the time indicates that associated node hasn't
   * already been done or discovered. By default a node is considered done
   * if the time is not 0.</p>
   *
   * @see #newInitialTime
   */
  protected boolean notDone(IntHolder time) {
    return time.value == 0;
  }

  /**
   * <p>The time associated with the node.</p>
   */
  protected final IntHolder timeOf(Object node) {
    return nodeToTimeMap.holder(node);
  }

  /**
   * <p>The graph being searched.</p>
   */
  protected final Graph graph;

  /**
   * <p>The derived class should directly push any events into this event
   * dispenser. The timed search template then pops events from this
   * dispenser and calls the appropriate event handling method.</p>
   */
  protected final Dispenser eventDispenser;

  private Iterator roots;
  private ObjectToIntMap nodeToTimeMap = new ObjectToIntMap();
}
