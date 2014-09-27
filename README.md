[![Build Status](https://travis-ci.org/plafuro/cucumber-confluence.svg?branch=master)](http://travis-ci.org/plafuro/cucumber-confluence)
# cucumber-confluence

The aim of this project is to provide a set of tools to automate tasks that involve gherkin/cucumber Feature files as an output and Confluence as a consumer of this output.

At the moment there is the posibility to transform feature files to confluence markup through a command line interface.

<pre>
# java -jar cucumber-confluence-cli-1.0-SNAPSHOT.jar 

usage: [-f FILEORDIR] [-nt,--no-tags] [-o,--output-dir DIR]
 -o,--output-dir    Path to save markup files to. Default is working
                    directory
 -nt,--no-tags      Wheter tags should be suppressed from the output. Tags
                    are processed by default
 -f,--file          File to read or path to scan for '.feature' files.
                    Default is working directory
</pre>
