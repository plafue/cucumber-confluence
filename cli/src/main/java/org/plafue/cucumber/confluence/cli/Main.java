package org.plafue.cucumber.confluence.cli;

import org.apache.commons.cli.ParseException;
import org.plafue.cucumber.confluence.filesystem.FeatureFinder;
import org.plafue.cucumber.confluence.formatter.ConfluenceStorageFormatter;
import org.plafue.cucumber.confluence.parser.BatchParser;

import java.io.*;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, ParseException {
        CliOptions options = new CliOptions(args);
        FeatureFinder finder = new FeatureFinder(options.fileToParse());
        BatchParser parser = new BatchParser();
        List<File> features = finder.findFeatures();
        ConfluenceStorageFormatter.Options formatterOptions = new ConfluenceStorageFormatter.Options(options.renderTags());
        parser.parse(features, formatterOptions, options.outputDir());
    }
}
