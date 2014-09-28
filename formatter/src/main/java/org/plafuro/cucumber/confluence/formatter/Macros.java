package org.plafuro.cucumber.confluence.formatter;

import org.plafuro.cucumber.confluence.exceptions.FormatNotFoundException;
import gherkin.formatter.Format;
import gherkin.formatter.Formats;

import java.util.HashMap;
import java.util.Map;

public class Macros implements Formats {

    private String key;

    public static enum Formats {
        INFO, COLOR_DARK_GREY, COLOR_RED, PANEL
    }

    private static final Map<Formats, Format> formats = new HashMap<Formats, Format>() {{
        put(Formats.PANEL, new CurlyBracesEnclosedFormat("panel", "panel"));
        put(Formats.INFO, new CurlyBracesEnclosedFormat("info", "info"));
        put(Formats.COLOR_RED, new Color("red"));
        put(Formats.COLOR_DARK_GREY, new Color("#666666"));
    }};

    public static class CurlyBracesEnclosedFormat implements Format {
        private final String openingTagContent;
        private String closingTagContent;

        public CurlyBracesEnclosedFormat(String openingTagContent, String closingTagContent) {
            this.openingTagContent = openingTagContent;
            this.closingTagContent = closingTagContent;
        }

        public String text(String text) {
            return "{" + openingTagContent + "}" + text + "{" + closingTagContent + "}";
        }
    }

    public Format get(String key) {
        this.key = key;
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


