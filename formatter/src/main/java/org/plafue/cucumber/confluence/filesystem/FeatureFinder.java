package org.plafue.cucumber.confluence.filesystem;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FeatureFinder {

    private final File file;
    private List<File> featuresFound = new ArrayList<File>();


    public FeatureFinder(File file){
        this.file = file;
    }

    public List<File> findFeatures() throws IOException {
        walk(file);
        return featuresFound;
    }

    private void walk(File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles(featureFilter)) {
                walk(child);
            }
        } else {
            featuresFound.add(file);
        }
    }

    private FileFilter featureFilter = new FileFilter() {
        public boolean accept(File file) {
            return file.isDirectory() || file.getName().endsWith(".feature");
        }
    };
}
