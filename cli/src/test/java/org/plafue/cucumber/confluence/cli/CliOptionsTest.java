package org.plafue.cucumber.confluence.cli;

import org.apache.commons.cli.ParseException;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;


public class CliOptionsTest {

    public static final String[] NO_ARGS = new String[]{};
    public static final String WORKING_DIR = System.getProperty("user.dir");

    @Test
    public void testNoRenderTags() throws Exception {
        assertThat(new CliOptions(new String[]{"-nt"}).renderTags()).isFalse();
        assertThat(new CliOptions(NO_ARGS).renderTags()).isTrue();
    }

    @Test
    public void testFileToParseOptionMissingLeadsToWorkingDir() throws Exception {
        assertThat(new CliOptions(NO_ARGS).fileToParse().getAbsolutePath()).isEqualTo(WORKING_DIR);

    }

    @Test
    public void testFileToParseProvidedIsTakenIntoAccount() throws Exception {
        String expectedPath = File.createTempFile("pre", "su").getParent();
        assertThat(new CliOptions(new String[]{"-f", expectedPath}).fileToParse().getAbsolutePath())
                .isEqualTo(expectedPath);

    }

    @Test
    public void testOutputDirMissingLeadsToWorkingDir() throws Exception {
        assertThat(new CliOptions(NO_ARGS).fileToParse().getAbsolutePath())
                .isEqualTo(WORKING_DIR);

    }

    @Test
    public void testOutputDirProvidedIsTakenIntoAccount() throws Exception {
        String expectedPath = File.createTempFile("pre", "su").getParent();
        assertThat(new CliOptions(new String[]{"-o", expectedPath}).outputDir().getAbsolutePath())
                .isEqualTo(expectedPath);
    }

    @Test(expected = ParseException.class)
    public void noTagsAndJiraServerAreMutuallyExclusive() throws ParseException {
        new CliOptions(new String[]{"-nt", "-j", "someServerName"});
    }
}