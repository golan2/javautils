package javautils.graph.templates;

import javautils.graph.adt.Graph;
import javautils.graph.testing.TestGraphConstants;
import junit.framework.TestCase;

/**
 * <p>A [<a href="{@docRoot}/overview-summary.html#[JUnit]">JUnit</a>]
 * test for {@link DfsTemplate}.</p>
 */
public class DfsTemplateTest extends TestCase implements TestGraphConstants {

  public static final String UVWXYZ_GRAPH_DFS =
    "initNode(u)\n" +
    "initNode(v)\n" +
    "initNode(w)\n" +
    "initNode(x)\n" +
    "initNode(y)\n" +
    "initNode(z)\n" +
    "discoverRoot(u) {\n" +
    " discoverNode(u,1) {\n" +
    "  treeEdge(u,v)\n" +
    "  discoverNode(v,2) {\n" +
    "   treeEdge(v,y)\n" +
    "   discoverNode(y,3) {\n" +
    "    treeEdge(y,x)\n" +
    "    discoverNode(x,4) {\n" +
    "     backEdge(x,v)\n" +
    "    } finishNode(x,5)\n" +
    "   } finishNode(y,6)\n" +
    "  } finishNode(v,7)\n" +
    "  forwardEdge(u,x)\n" +
    " } finishNode(u,8)\n" +
    "} finishRoot(u)\n" +
    "discoverRoot(w) {\n" +
    " discoverNode(w,9) {\n" +
    "  crossEdge(w,y)\n" +
    "  treeEdge(w,z)\n" +
    "  discoverNode(z,10) {\n" +
    "   backEdge(z,z)\n" +
    "  } finishNode(z,11)\n" +
    " } finishNode(w,12)\n" +
    "} finishRoot(w)\n";

  /**
   * <p>Tests that the {@link DfsTemplate}-method gives the expected
   * transcript on the {@link TestGraphConstants#UVWXYZ_GRAPH}-graph.</p>
   */
  public void testDfsOnUVWXYZ() {
    assertEquals(UVWXYZ_GRAPH_DFS, dfsTranscript(UVWXYZ_GRAPH));
  }

  public static final String STUVWXYZ_GRAPH_DFS =
    "initNode(s)\n" +
    "initNode(t)\n" +
    "initNode(u)\n" +
    "initNode(v)\n" +
    "initNode(w)\n" +
    "initNode(x)\n" +
    "initNode(y)\n" +
    "initNode(z)\n" +
    "discoverRoot(s) {\n" +
    " discoverNode(s,1) {\n" +
    "  treeEdge(s,z)\n" +
    "  discoverNode(z,2) {\n" +
    "   treeEdge(z,y)\n" +
    "   discoverNode(y,3) {\n" +
    "    treeEdge(y,x)\n" +
    "    discoverNode(x,4) {\n" +
    "     backEdge(x,z)\n" +
    "    } finishNode(x,5)\n" +
    "   } finishNode(y,6)\n" +
    "   treeEdge(z,w)\n" +
    "   discoverNode(w,7) {\n" +
    "    crossEdge(w,x)\n" +
    "   } finishNode(w,8)\n" +
    "  } finishNode(z,9)\n" +
    "  forwardEdge(s,w)\n" +
    " } finishNode(s,10)\n" +
    "} finishRoot(s)\n" +
    "discoverRoot(t) {\n" +
    " discoverNode(t,11) {\n" +
    "  treeEdge(t,v)\n" +
    "  discoverNode(v,12) {\n" +
    "   crossEdge(v,s)\n" +
    "   crossEdge(v,w)\n" +
    "  } finishNode(v,13)\n" +
    "  treeEdge(t,u)\n" +
    "  discoverNode(u,14) {\n" +
    "   crossEdge(u,v)\n" +
    "   backEdge(u,t)\n" +
    "  } finishNode(u,15)\n" +
    " } finishNode(t,16)\n" +
    "} finishRoot(t)\n";

  /**
   * <p>Tests that the {@link DfsTemplate}-method gives the expected
   * transcript on the {@link
   * TestGraphConstants#STUVWXYZ_GRAPH}-graph.</p>
   */
  public void testDfsOnSTUVWXYZ() {
    assertEquals(STUVWXYZ_GRAPH_DFS, dfsTranscript(STUVWXYZ_GRAPH));
  }

  /**
   * <p>Generates a transcript of the events generated by the {@link
   * DfsTemplate}-method on the given graph.</p>
   */
  public static String dfsTranscript(final Graph g) {
    final StringBuffer result = new StringBuffer();

    new DfsTemplate() {
      StringBuffer indent = new StringBuffer(" ");
      int time = 0;

      protected void initNode(Object node) {
        result.append("initNode(" + node + ")\n");
      }

      protected void discoverRoot(Object node) {
        result.append("discoverRoot(" + node + ") {\n");
      }

      protected void discoverNode(Object node) {
        result.append(indent + "discoverNode(" + node + "," + ++time + ") {\n");
        indent.append(' ');
      }

      protected void treeEdge(Object edge) {edge("treeEdge", edge);}
      protected void forwardEdge(Object edge) {edge("forwardEdge", edge);}
      protected void crossEdge(Object edge) {edge("crossEdge", edge);}
      protected void backEdge(Object edge) {edge("backEdge", edge);}

      protected void edge(String title, Object edge) {
        result.append(indent + title + "(" + g.sourceOf(edge) + "," + g.targetOf(edge) + ")\n");
      }

      protected void finishNode(Object node) {
        indent.setLength(indent.length()-1);
        result.append(indent + "} finishNode(" + node + "," + ++time + ")\n");
      }

      protected void finishRoot(Object node) {
        result.append("} finishRoot(" + node + ")\n");
      }
    }.search(g);

    return result.toString();
  }
}