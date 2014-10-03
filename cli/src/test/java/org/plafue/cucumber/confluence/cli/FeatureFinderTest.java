package org.plafue.cucumber.confluence.cli;

import org.junit.Test;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class FeatureFinderTest {

    @Test
    public void testFindFeatures() throws Exception {
        /**
         * tmp
         *  |-findMe1.feature
         *  |-findMe2.feature
         *  |-dontFindMe1.tja
         *  |-subDir1
         *  | \-findMe3.feature
         *  | |-dontFindMe2.feature.pff
         *  |-emptySubdir
         */
        File tempDir = new File(System.getProperty("java.io.tmpdir"), Long.toString(System.nanoTime()));
        tempDir.mkdir();
        File findMe1 = File.createTempFile("findMe1", ".feature", tempDir);
        File findMe2 = File.createTempFile("findMe2", ".feature", tempDir);
        File.createTempFile("dontFindMe1", ".tja", tempDir);
        File subdir1 = new File(tempDir, "subDir1");
        subdir1.mkdir();
        File findMe3 = File.createTempFile("findMe3", ".feature", subdir1);
        File.createTempFile("dontFindMe2", ".pff", subdir1);
        File emptySubdir = new File(tempDir, "emptySubdir");
        emptySubdir.mkdir();

        FeatureFinder featureFinder = new FeatureFinder(tempDir);
        List<File> features = featureFinder.findFeatures();
        Collections.sort(features);
        assertThat(features).containsExactly(findMe1, findMe2, findMe3);

    }
}