package org.mate;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mate.exploration.genetic.algorithm.StandardGeneticAlgorithm;
import org.mate.exploration.genetic.builder.GeneticAlgorithmBuilder;
import org.mate.exploration.genetic.core.IGeneticAlgorithm;
import org.mate.exploration.genetic.crossover.TestCaseMergeCrossOverFunction;
import org.mate.exploration.genetic.fitness.BranchDistanceFitnessFunction;
import org.mate.exploration.genetic.mutation.CutPointMutationFunction;
import org.mate.exploration.genetic.selection.FitnessProportionateSelectionFunction;
import org.mate.exploration.genetic.termination.NeverTerminationCondition;
import org.mate.exploration.intent.IntentChromosomeFactory;
import org.mate.model.TestCase;
import org.mate.utils.Coverage;

@RunWith(AndroidJUnit4.class)
public class ExecuteMATEStandardGeneticAlgorithm {


    @Test
    public void useAppContext() throws Exception {
        MATE.log_acc("Starting Evolutionary Search...");
        MATE.log_acc("StandardGeneticAlgorithm implementation");

        MATE mate = new MATE();

        MATE.log_acc("Activities");
        for (String s : Registry.getEnvironmentManager().getActivityNames()) {
            MATE.log_acc("\t" + s);
        }

        final IGeneticAlgorithm<TestCase> genericGA = new GeneticAlgorithmBuilder()
                .withAlgorithm(StandardGeneticAlgorithm.ALGORITHM_NAME)
                .withChromosomeFactory(IntentChromosomeFactory.CHROMOSOME_FACTORY_ID)
                .withRelativeIntentAmount(Properties.STANDARD_GA_RELATIVE_INTENT_AMOUNT())
                .withSelectionFunction(FitnessProportionateSelectionFunction.SELECTION_FUNCTION_ID)
                .withCrossoverFunction(TestCaseMergeCrossOverFunction.CROSSOVER_FUNCTION_ID)
                .withMutationFunction(CutPointMutationFunction.MUTATION_FUNCTION_ID)
                .withFitnessFunction(BranchDistanceFitnessFunction.FITNESS_FUNCTION_ID)
                .withTerminationCondition(NeverTerminationCondition.TERMINATION_CONDITION_ID)
                .withPopulationSize(Properties.EVOLUTIONARY_SEARCH_POPULATION_SIZE())
                .withBigPopulationSize(Properties.EVOLUTIONARY_SEARCH_BIG_POPULATION_SIZE())
                .withMaxNumEvents(Properties.MAX_NUM_EVENTS())
                .withPMutate(Properties.P_MUTATE())
                .withPCrossover(Properties.P_CROSSOVER())
                .build();

        mate.testApp(genericGA);

        if (Properties.COVERAGE() != Coverage.NO_COVERAGE
                // TODO: handle combined activity coverage
                && Properties.COVERAGE() != Coverage.ACTIVITY_COVERAGE) {

            // store coverage of test case interrupted by timeout
            Registry.getEnvironmentManager().storeCoverageData(Properties.COVERAGE(),
                    "lastIncompleteTestCase", null);

            // get combined coverage
            MATE.log_acc("Total coverage: "
                    + Registry.getEnvironmentManager()
                    .getCombinedCoverage(Properties.COVERAGE()));
        }
    }
}
