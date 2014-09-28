package org.plafuro.cucumber.confluence.formatter;

import org.plafuro.cucumber.confluence.exceptions.FormatNotFoundException;
import gherkin.formatter.Format;
import gherkin.formatter.Formats;

import java.util.HashMap;
import java.util.Map;

public class Macros implements Formats {

    public static enum Formats {
        INFO, COLOR_DARK_GREY, COLOR_RED, PANEL, JIRA
    }

    private final Map<Formats, Format> formats;

    public Macros(){
        this.formats = new HashMap<Formats, Format>() {{
            put(Formats.PANEL, new CurlyBracesEnclosedFormat("panel"));
            put(Formats.INFO, new CurlyBracesEnclosedFormat("info"));
            put(Formats.COLOR_RED, new Color("red"));
            put(Formats.COLOR_DARK_GREY, new Color("#666666"));
        }};
    }

    public Macros(final String server){
        this();
        if (server == null){
            throw new IllegalStateException("A server is needed for the Jira Issue Macro to work");
        }
        formats.put(Formats.JIRA, new JiraIssueMacro(server));
    }

    public static class CurlyBracesEnclosedFormat implements Format {
        private final String openingTagContent;
        private String closingTagContent;

        public CurlyBracesEnclosedFormat(String openingTagContent, String closingTagContent) {
            this.openingTagContent = openingTagContent;
            this.closingTagContent = closingTagContent;
        }

        public CurlyBracesEnclosedFormat(String openingTagContent) {
            this.openingTagContent = openingTagContent;
            this.closingTagContent = openingTagContent;
        }

        public String text(String text) {
            return "{" + openingTagContent + "}" + text + "{" + closingTagContent + "}";
        }
    }

    public static class JiraIssueMacro implements Format {
        private final String server;

        public JiraIssueMacro(String server) {
            this.server = server;
        }

        public String text(String jiraId) {
            return "<ac:macro ac:name=\"jira\">" +
                   "<ac:parameter ac:name=\"server\">" + server + "</ac:parameter}"+
                   "<ac:parameter ac:name=\"key\">" + jiraId + "</ac:parameter}"+
                   "</ac:macro>";
        }
    }

    public Format get(String key) {
        Format format = formats.get(Formats.valueOf(key));
        if (format == null) throw new FormatNotFoundException(key);
        return format;
    }

    public static class Color extends CurlyBracesEnclosedFormat {

        public Color(String color) {
            super("color:" + color, "color");
        }
    }

    public Format get(Formats key) {
        return formats.get(key);
    }

    public String up(int n) {
        return "";
    }
}


