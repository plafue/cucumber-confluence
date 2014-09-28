package org.plafuro.cucumber.confluence.cli;

import org.apache.commons.cli.ParseException;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CliOptionsTest{

    public static final String[] NO_ARGS = new String[]{};
    public static final String WORKING_DIR = System.getProperty("user.dir");

    @Test
    public void testNoRenderTags() throws Exception {
        assertFalse(new CliOptions(new String[]{"-nt"}).renderTags());
        assertTrue(new CliOptions(NO_ARGS).renderTags());
    }

    @Test
    public void testFileToParseOptionMissingLeadsToWorkingDir() throws Exception {
        assertEquals(WORKING_DIR, new CliOptions(NO_ARGS).fileToParse().getAbsolutePath());

    }

    @Test
    public void testFileToParseProvidedIsTakenIntoAccount() throws Exception {
        String expectedPath = File.createTempFile("pre", "su").getParent();
        assertEquals(expectedPath,
                new CliOptions(new String[]{"-f", expectedPath}).fileToParse().getAbsolutePath());

    }

    @Test
    public void testOutputDirMissingLeadsToWorkingDir() throws Exception {
        assertEquals(WORKING_DIR, new CliOptions(NO_ARGS).outputDir().getAbsolutePath());

    }

    @Test
    public void testOutputDirProvidedIsTakenIntoAccount() throws Exception {
        String expectedPath = File.createTempFile("pre", "su").getParent();
        assertEquals(expectedPath,
                new CliOptions(new String[]{"-o", expectedPath}).outputDir().getAbsolutePath());
    }

    @Test(expected = ParseException.class)
    public void noTagsAndJiraServerAreMutuallyExclusive() throws ParseException {
        new CliOptions(new String[]{"-nt","-j","someServerName"});
    }
}