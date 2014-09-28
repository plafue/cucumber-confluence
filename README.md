[![Build Status](https://travis-ci.org/plafuro/cucumber-confluence.svg?branch=master)](http://travis-ci.org/plafuro/cucumber-confluence)
# cucumber-confluence

The aim of this project is to provide a set of tools to automate tasks that involve gherkin/cucumber Feature files as an output and Confluence as a consumer of this output.

At the moment there is the posibility to transform feature files to confluence markup through a command line interface or a maven plugin.

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

```xml
<plugins>
  <plugin>
    <groupId>org.plafuro.cucumber-confluence</groupId>
    <artifactId>cucumber-confluence-maven-plugin</artifactId>
    <version>1.0-SNAPSHOT</version>
    <executions>
      <execution>
        <id>parse</id>
        <goals>
          <goal>parse</goal> 
          <!-- Currently only the goal "parse" is provided. 
          The plugin is not hooked to any lifecycle phase. Use as you see fit. -->
        </goals>
      </execution>
      <configuration>  <!-- All settings are optional -->
        <outputDirectory>
           <!-- Output directory.  All output files match the name of the source, 
                but with the suffix .markup instead of .feature.
                Default: ${project.build.directory}/cucumber-confluence" 
                (aka "target/cucumber-confluence") -->
         </outputDirectory>
        <inputFile> 
          <!-- If a directory, all "*.feature" files within it will be parsed. 
               Default: ${basedir} (aka location of your pom.xml) -->
        </inputFile>
        <ignoreTags>
          <!-- Whether Tags are ignored. Default: false -->
        </ignoreTags>
      </configuration>
    </executions>
  </plugin>
</plugins>
```
