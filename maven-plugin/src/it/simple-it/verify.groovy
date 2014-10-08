File expectedGeneratedFile = new File(basedir, "target/cucumber-confluence/completeFeatureDescription.xhtml");
List<String> expectedContent = new File(basedir, "completeFeatureDescription.xhtml").readLines();


assert expectedGeneratedFile.isFile()

expectedGeneratedFile.eachLine("UTF-8", { line, lineNr -> assert expectedContent.get(lineNr - 1).contentEquals(line) })
