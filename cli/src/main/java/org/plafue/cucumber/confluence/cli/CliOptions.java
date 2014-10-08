package org.plafue.cucumber.confluence.cli;

import org.apache.commons.cli.*;

import java.io.File;

public class CliOptions {

    public static final String WORKING_DIR = System.getProperty("user.dir");
    public static final String NO_TAGS_SHORT = "nt";
    public static final String NO_TAGS_LONG = "no-tags";
    public static final String INPUT_FILE_SHORT = "f";
    public static final String INPUT_FILE_LONG = "file";
    public static final String OUTPUT_DIR_SHORT = "o";
    public static final String OUTPUT_DIR_LONG = "output-dir";

    public static final String JIRA_SERVER_LONG = "jira-server";
    public static final String JIRA_SERVER_SHORT = "j";

    private final File outputDir;
    private final File fileToParse;
    private boolean dontRenderTags;

    public CliOptions(String[] args) throws ParseException {
        CommandLine cmd = parseCommandLine(args);
        this.dontRenderTags = cmd.hasOption("nt");
        this.outputDir = getOutPutDirectory(cmd);
        this.fileToParse = new File(cmd.getOptionValue("f", WORKING_DIR));
    }

    public boolean renderTags() {
        return !dontRenderTags;
    }

    public File fileToParse() {
        return fileToParse;
    }

    public File outputDir() {
        return outputDir;
    }

    private CommandLine parseCommandLine(String[] args) throws ParseException {
        Options options = new Options();
        Option noTags = new Option(NO_TAGS_SHORT, NO_TAGS_LONG, false, "Wheter tags should be suppressed from the output. Tags are processed by default");
        Option jiraServer = new Option(JIRA_SERVER_SHORT, JIRA_SERVER_LONG, true, "Name of the Jira-Server as it is known to Confluence.");
        options.addOptionGroup(new OptionGroup().addOption(noTags).addOption(jiraServer));
        options.addOption(INPUT_FILE_SHORT, INPUT_FILE_LONG, true, "File to read or path to scan for '.feature' files. Default is working directory");
        options.addOption(OUTPUT_DIR_SHORT, OUTPUT_DIR_LONG, true, "Path to save xhtml files to. Default is working directory");
        try {
            CommandLineParser commandLineParser = new BasicParser();
            return commandLineParser.parse(options, args);
        } catch (ParseException e) {
            new HelpFormatter().printHelp("[-f FILEORDIR] [[-nt]|[-j SERVERNAME]] [-o DIR]", options);
            throw e;
        }
    }

    private File getOutPutDirectory(CommandLine cmd) {
        if (!cmd.hasOption("o")) {
            return new File(WORKING_DIR);
        }
        File userProvidedDir = new File(cmd.getOptionValue("o"));
        return userProvidedDir.isDirectory() ? userProvidedDir : new File(WORKING_DIR);
    }
}
