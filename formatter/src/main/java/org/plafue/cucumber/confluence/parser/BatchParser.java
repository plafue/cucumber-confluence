package org.plafue.cucumber.confluence.parser;

import gherkin.util.FixJava;
import org.plafue.cucumber.confluence.formatter.MarkupFormatter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class BatchParser {

    public void parse(List<File> features, MarkupFormatter.Options formatterOptions, File outputDir) throws IOException {

        for(File feature : features){
            File outputFile = new File(outputDir, feature.getName().replace(".feature", ".markup"));
            MarkupFormatter markupFormatter = new MarkupFormatter(new FileWriter(outputFile), formatterOptions);
            new gherkin.parser.Parser(markupFormatter).parse(FixJava.readReader(new FileReader(feature)),"",0);
        }
    }

}
