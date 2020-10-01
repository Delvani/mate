package org.mate.serialization;


import android.support.test.InstrumentationRegistry;

import com.thoughtworks.xstream.XStream;

import org.mate.MATE;
import org.mate.model.TestCase;
import org.mate.utils.TimeoutRun;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Provides the functionality to serialize and de-serialize a {@link org.mate.model.TestCase}.
 * This basically enables the recording and replaying of test cases.
 */
public final class TestCaseSerializer {

    // the location where test cases are stored
    private static final String TEST_CASES_DIR = "/data/data/org.mate/test-cases";

    // tracks the number of recorded test cases
    private static int recordCounter = 0;

    // tracks the number of replayed test cases
    private static int replayCounter = 0;

    /**
     * Serializes a given {@link TestCase} to XML and stores it on the
     * app-internal storage of MATE.
     *
     * @param testCase The test case to be serialized and stored.
     */
    public static void serializeTestCase(TestCase testCase) {
        MATE.log("Serializing TestCase " + recordCounter);

        // create the test-cases folder if not yet present
        File dir = new File(TEST_CASES_DIR);
        if (!dir.exists()) {
            MATE.log("Creating test-cases folder succeeded: " + dir.mkdir());
        }

        // log whether execution of test case resulted in a crash
        if (testCase.getCrashDetected()) {
            MATE.log_acc("TestCase " + recordCounter + " caused a crash!");
        }

        // the output file
        File testCaseFile = new File(dir, "TestCase" + recordCounter + ".xml");

        try (Writer fileWriter = new FileWriter(testCaseFile)) {

            // convert test case to xml
            XStream xstream = new XStream();
            xstream.registerConverter(new IntentBasedActionConverter());
            String testCaseXML = xstream.toXML(testCase);

            fileWriter.write(testCaseXML);
            fileWriter.flush();

        } catch (IOException e) {
            MATE.log_acc("Serializing TestCase " + recordCounter + " failed!");
            e.printStackTrace();
        }

        // update counter
        recordCounter++;
    }

    /**
     * Deserializes a recorded test case.
     *
     * @return Returns the deserialized test case.
     */
    public static TestCase deserializeTestCase() {

        MATE.log("Deserializing TestCase " + replayCounter);

        try {

            String testCaseName = "TestCase" + replayCounter + ".xml";

            // retrieves the file from /data/data/org.mate/files/
            FileInputStream testCaseFile = InstrumentationRegistry.getTargetContext().openFileInput(testCaseName);

            XStream xstream = new XStream();
            xstream.ignoreUnknownElements();
            xstream.registerConverter(new IntentBasedActionConverter());

            TestCase testCase = (TestCase) xstream.fromXML(testCaseFile);
            MATE.log("Number of Actions: " + testCase.getEventSequence().size());

            // update counter
            replayCounter++;

            return testCase;
        } catch (FileNotFoundException e) {
            MATE.log("TestCase file for deserialization not found!");
            return null;
        }
    }
}