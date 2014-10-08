package org.plafue.cucumber.confluence.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.plafue.cucumber.confluence.filesystem.FeatureFinder;
import org.plafue.cucumber.confluence.formatter.ConfluenceStorageFormatter;
import org.plafue.cucumber.confluence.parser.BatchParser;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Mojo(name = "parse", defaultPhase = LifecyclePhase.NONE)
public class ParserMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project.build.directory}/cucumber-confluence",
            property = "outputDir", required = false)
    private File outputDirectory;

    @Parameter(defaultValue = "${basedir}", property = "inputPath", required = false)
    private File inputFile;

    @Parameter(property = "ignoreTags", required = false)
    private boolean ignoreTags;

    @Parameter(property = "jiraServer", required = false)
    private String jiraServer;

    private BatchParser parser = new BatchParser();

    public void execute() throws MojoExecutionException {
        createOutputDirIfNeeded();
        FeatureFinder finder = new FeatureFinder(inputFile);
        List<File> features = findFeatures(finder);
        ConfluenceStorageFormatter.Options formatterOptions = buildOptionsObject();
        run(features, formatterOptions);
    }

    private ConfluenceStorageFormatter.Options buildOptionsObject() {
        if(ignoreTags && jiraServer != null) {
            throw new IllegalStateException("The options ignoreTags and jiraServer are mutually exclusive");
        } else if (jiraServer != null){
            return new ConfluenceStorageFormatter.Options(jiraServer);
        } else {
            return new ConfluenceStorageFormatter.Options(!ignoreTags);
        }
    }

    private void run(List<File> features, ConfluenceStorageFormatter.Options formatterOptions) throws MojoExecutionException {
        try {
            parser.parse(features, formatterOptions, outputDirectory);
        } catch (IOException e) {
            throw new MojoExecutionException("A problem occurred while parsing feature files", e);
        }
    }

    private List<File> findFeatures(FeatureFinder finder) throws MojoExecutionException {
        List<File> features;
        try {
            features = finder.findFeatures();
        } catch (IOException e) {
            throw new MojoExecutionException("A Problem occurred while looking for features to parse", e);
        }
        return features;
    }

    private void createOutputDirIfNeeded() {
        outputDirectory.mkdirs();
    }
}
