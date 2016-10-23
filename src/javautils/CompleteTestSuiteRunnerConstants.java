package javautils;

/**
 * <p>Contains an array of test classes (matching the pattern '*Test.java').</p>
 */
public interface CompleteTestSuiteRunnerConstants {

  // WARNING: This class is automatically generated by the script:
  //
  //   javautils-create-junit-test-runner-constants
  //
  // You should probably edit the script rather than this class.

  /**
   * <p>An array of the following classes:</p>
   * <ul>
   * <li>{@link javautils.ClassesTest}</li>
   * <li>{@link javautils.collections.AlgsTest}</li>
   * <li>{@link javautils.graph.GraphsTest}</li>
   * <li>{@link javautils.graph.templates.BfsTemplateTest}</li>
   * <li>{@link javautils.graph.templates.DfsTemplateTest}</li>
   * <li>{@link javautils.ImmutablePairTest}</li>
   * <li>{@link javautils.ObjectsTest}</li>
   * </ul>
   */
  static final Class[] TESTS = {
    javautils.ClassesTest.class,
    javautils.collections.AlgsTest.class,
    javautils.graph.GraphsTest.class,
    javautils.graph.templates.BfsTemplateTest.class,
    javautils.graph.templates.DfsTemplateTest.class,
    javautils.ImmutablePairTest.class,
    javautils.ObjectsTest.class,
  };
}
