package org.plafue.cucumber.confluence.parser;

import gherkin.util.FixJava;
import org.plafue.cucumber.confluence.formatter.ConfluenceStorageFormatter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class BatchParser {

    public void parse(List<File> features, ConfluenceStorageFormatter.Options formatterOptions, File outputDir) throws IOException {

        for(File feature : features){
            File outputFile = new File(outputDir, feature.getName().replace(".feature", ".xhtml"));
            ConfluenceStorageFormatter confluenceStorageFormatter = new ConfluenceStorageFormatter(new FileWriter(outputFile), formatterOptions);
            new gherkin.parser.Parser(confluenceStorageFormatter).parse(FixJava.readReader(new FileReader(feature)),"",0);
        }
    }

}
