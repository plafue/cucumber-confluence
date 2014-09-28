package org.plafuro.cucumber.confluence.cli;

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

    private final File outputDir;
    private final File fileToParse;
    private boolean dontRenderTags;

    public CliOptions(String[] args) {
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

    private CommandLine parseCommandLine(String[] args) {
        Options options = new Options();
        options.addOption(NO_TAGS_SHORT, NO_TAGS_LONG, false, "Wheter tags should be suppressed from the output. Tags are processed by default");
        options.addOption(INPUT_FILE_SHORT, INPUT_FILE_LONG, true, "File to read or path to scan for '.feature' files. Default is working directory");
        options.addOption(OUTPUT_DIR_SHORT, OUTPUT_DIR_LONG, true, "Path to save markup files to. Default is working directory");
        try {
            CommandLineParser commandLineParser = new BasicParser();
            return commandLineParser.parse(options, args);
        } catch (ParseException e) {
            new HelpFormatter().printHelp("[-f FILEORDIR] [-nt] [-o DIR]", options);
            System.exit(1);
            return null;
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
