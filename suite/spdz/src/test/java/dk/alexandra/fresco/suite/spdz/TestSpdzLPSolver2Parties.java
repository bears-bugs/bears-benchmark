package dk.alexandra.fresco.suite.spdz;

import dk.alexandra.fresco.framework.sce.evaluator.EvaluationStrategy;
import dk.alexandra.fresco.lib.lp.LPSolver.PivotRule;
import dk.alexandra.fresco.suite.spdz.configuration.PreprocessingStrategy;
import org.junit.Test;

public class TestSpdzLPSolver2Parties extends AbstractSpdzTest {

  @Test
  public void test_LPSolver_2_Sequential_dummy() {
    runTest(new LPSolverTests.TestLPSolver<>(PivotRule.DANZIG), EvaluationStrategy.SEQUENTIAL,
        PreprocessingStrategy.DUMMY, 2);
  }

  @Test
  public void test_LPSolver_2_Sequential_dummy_bland() {
    runTest(new LPSolverTests.TestLPSolver<>(PivotRule.BLAND), EvaluationStrategy.SEQUENTIAL,
        PreprocessingStrategy.DUMMY, 2);
  }

  @Test
  public void test_LPSolver_2_Sequential_batched_dummy() {

    runTest(new LPSolverTests.TestLPSolver<>(PivotRule.DANZIG),
        EvaluationStrategy.SEQUENTIAL_BATCHED,
        PreprocessingStrategy.DUMMY, 2);
  }

  @Test
  public void test_LPSolver_2_Sequential_batched_dummy_smaller_mod() {
    runTest(new LPSolverTests.TestLPSolver<>(PivotRule.DANZIG),
        EvaluationStrategy.SEQUENTIAL_BATCHED,
        PreprocessingStrategy.DUMMY, 2, false, 128, 30, 8);
  }

}
