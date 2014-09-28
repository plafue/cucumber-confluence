package org.plafuro.cucumber.confluence.formatter;


import gherkin.formatter.Formatter;
import gherkin.parser.Parser;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static gherkin.util.FixJava.readResource;
import static org.junit.Assert.assertEquals;

public class ConfluenceMarkupFormatterTest {

    public static final String RESOURCES_PATH = "/org/plafuro/cucumber/confluence/formatter/";

    @Test
    public void completeFeatureShouldBeFormattedAsDesiredWithTags() throws IOException {

        String basicFeatureDescription = readFeature("completeFeatureDescription");
        String[] expectedOutput = readExpectedMarkup("completeFeatureDescriptionWithTags");

        List<String> formatterOutput = doFormatter(basicFeatureDescription, new MarkupFormatter.Options(true));

        // looping through the collection to get junit to provide helpful output

        assertListEquality(expectedOutput, formatterOutput);
    }

    @Test
    public void completeFeatureShouldBeFormattedAsDesiredWithoutTags() throws IOException {

        String basicFeatureDescription = readFeature("completeFeatureDescription");
        String[] expectedOutput = readExpectedMarkup("completeFeatureDescriptionWithoutTags");

        List<String> formatterOutput = doFormatter(basicFeatureDescription, new MarkupFormatter.Options(false));

        assertListEquality(expectedOutput, formatterOutput);
    }

    @Test
    public void completeFeatureShouldBeFormattedAsDesiredWithJiraMacroActive() throws IOException {

        String basicFeatureDescription = readFeature("completeFeatureDescription");
        String[] expectedOutput = readExpectedMarkup("completeFeatureDescriptionWithJIraMacroActive");

        List<String> formatterOutput = doFormatter(basicFeatureDescription, new MarkupFormatter.Options(true,"someServer"));

        assertListEquality(expectedOutput, formatterOutput);
    }

    private void assertListEquality(String[] expectedLines, List<String> formatterOutput) {
        // looping through the collection to get junit to provide helpful output
        for (String expectedLine : expectedLines) {
            assertEquals("Mismatch on line "+(expectedLines.length-formatterOutput.size()+1),
                    expectedLine, formatterOutput.remove(0));
        }
    }

    private String readFeature(final String feature) {
        return readResource(RESOURCES_PATH + feature + ".feature");
    }

    private String[] readExpectedMarkup(final String featureName) {
        String basicFeatureDescriptionMarkup = readResource(RESOURCES_PATH + featureName + ".markup");
        return basicFeatureDescriptionMarkup.split(System.getProperty("line.separator"));
    }

    private List<String> doFormatter(String feature, MarkupFormatter.Options options) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(byteArrayOutputStream);

        Formatter formatter;
        formatter = new MarkupFormatter(out, options);
        Parser parser = new Parser(formatter);
        parser.parse(feature, "", 0);
        formatter.close();

        return extractLines(byteArrayOutputStream);
    }

    private List<String> extractLines(ByteArrayOutputStream byteArrayOutputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(
                new ByteArrayInputStream(byteArrayOutputStream.toByteArray())));

        String line;
        List<String> lines = new ArrayList<String>();
        int lineNumber = 0;

        while ((line = br.readLine()) != null) {
            System.out.println(lineNumber + ":" + line);
            lineNumber++;
            lines.add(line);
        }
        return lines;
    }
}