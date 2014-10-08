package org.plafue.cucumber.confluence.formatter;

import org.plafue.cucumber.confluence.exceptions.FormatNotFoundException;
import gherkin.formatter.Format;
import gherkin.formatter.Formats;

import java.util.HashMap;
import java.util.Map;

public class Macros extends ConfluenceStorageFormat {

    public static enum Formats {
        INFO, PANEL, JIRA
    }

    private final Map<Formats, Format> formats;

    public Macros() {
        this.formats = new HashMap<Formats, Format>() {{
            put(Formats.PANEL, new Macro("panel"));
            put(Formats.INFO, new Macro("info"));
        }};
    }

    public Macros(final String server) {
        this();
        if (server == null) {
            throw new IllegalStateException("A server is needed for the Jira Issue Macro to work");
        }
        formats.put(Formats.JIRA, new JiraIssueMacro(server));
    }

    public static class Macro implements Format {
        private String macroName;

        public Macro(String macroName) {
            this.macroName = macroName;
        }

        public String text(String text) {
            return new EnclosingFormat("ac:macro", "ac:name=\"" + macroName + "\"").text(text);
        }
    }

    public static class JiraIssueMacro implements Format {
        private final String server;

        public JiraIssueMacro(String server) {
            this.server = server;
        }

        public String text(String jiraId) {
            return new EnclosingFormat("ac:macro", "ac:name=\"jira\"").text(
                    new EnclosingFormat("ac:parameter", "ac:name=\"server\"").text(server) +
                    new EnclosingFormat("ac:parameter", "ac:name=\"key\"").text(jiraId));
        }
    }

    public Format get(String key) {
        Format format = formats.get(Formats.valueOf(key));
        if (format == null) throw new FormatNotFoundException(key);
        return format;
    }

    public Format get(Formats key) {
        return formats.get(key);
    }

    public String up(int n) {
        return "";
    }
}


