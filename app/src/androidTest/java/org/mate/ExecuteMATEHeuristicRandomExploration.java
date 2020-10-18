package org.mate;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mate.exploration.heuristical.HeuristicExploration;
import org.mate.utils.Coverage;

@RunWith(AndroidJUnit4.class)
public class ExecuteMATEHeuristicRandomExploration {


    @Test
    public void useAppContext() throws Exception {

        MATE.log_acc("Starting Heuristic Random Exploration...");

        MATE mate = new MATE();

        MATE.log_acc("Activities");
        for (String s : Registry.getEnvironmentManager().getActivityNames()) {
            MATE.log_acc("\t" + s);
        }

        final HeuristicExploration heuristicExploration =
                new HeuristicExploration(Properties.MAX_NUM_EVENTS());

        mate.testApp(heuristicExploration);
    }
}
