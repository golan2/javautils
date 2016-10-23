package javautils;

/**
 * <p>An utility program for running all the unit tests of the
 * project.</p>
 */
public class CompleteTestSuiteRunner implements CompleteTestSuiteRunnerConstants {
  public static void main(String argv[]) {
    junit.framework.TestSuite suite = new junit.framework.TestSuite();

    for (int i=0; i<TESTS.length; ++i)
      suite.addTestSuite(TESTS[i]);

    junit.textui.TestRunner.run(suite);
  }
}
