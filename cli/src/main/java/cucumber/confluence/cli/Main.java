package cucumber.confluence.cli;

import cucumber.confluence.formatter.MarkupFormatter;
import gherkin.parser.Parser;
import gherkin.util.FixJava;
import org.apache.commons.cli.*;

import java.io.*;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, ParseException {
        CliOptions options = new CliOptions(args);
        FeatureFinder finder = new FeatureFinder(options.fileToParse());
        List<File> features = finder.findFeatures();
        MarkupFormatter.Options formatterOptions = new MarkupFormatter.Options(options.renderTags());
        parse(features,formatterOptions,options.outputDir());
    }

    public static void parse(List<File> features, MarkupFormatter.Options formatterOptions, File outputDir) throws IOException {

        for(File feature : features){
            File outputFile = new File(outputDir, feature.getName().replace(".feature", ".markup"));
            MarkupFormatter markupFormatter = new MarkupFormatter(new FileWriter(outputFile), formatterOptions);
            new Parser(markupFormatter).parse(FixJava.readReader(new FileReader(feature)),"",0);
        }
    }
}
