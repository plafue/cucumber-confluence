package org.plafue.cucumber.confluence.formatter;

import gherkin.formatter.Format;
import gherkin.formatter.Formats;

import org.plafue.cucumber.confluence.exceptions.FormatNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class ConfluenceStorageFormat implements Formats {

    public static enum Formats {
        BOLD, HEADER1, HEADER2, ITALICS, CELL, CELL_ALIGNED_RIGHT,
        TABLE_HEAD_CELL, TABLE, RED_FOREGROUND, COLOR_DARK_GREY, TABLE_ROW
    }

    private static final Map<Formats, Format> formats = new HashMap<Formats, Format>() {{
        put(Formats.HEADER1, new EnclosingFormat("h1"));
        put(Formats.HEADER2, new EnclosingFormat("h2"));
        put(Formats.ITALICS, new EnclosingFormat("em"));
        put(Formats.BOLD, new EnclosingFormat("strong"));
        put(Formats.TABLE_ROW, new EnclosingFormat("tr"));
        put(Formats.TABLE_HEAD_CELL, new EnclosingFormat("th"));
        put(Formats.CELL, new EnclosingFormat("td"));
        put(Formats.CELL_ALIGNED_RIGHT, new EnclosingFormat("td", "style=\"text-align:right\""));
        put(Formats.TABLE, new TableFormat());
        put(Formats.RED_FOREGROUND, new ColorFormat("red"));
        put(Formats.COLOR_DARK_GREY, new ColorFormat("#666666"));
    }};

    public static class EnclosingFormat implements Format {
        private final String enclosure;
        private final String extraOpeningTagAttributes;

        public EnclosingFormat(String enclosure) {
            this.enclosure = enclosure;
            extraOpeningTagAttributes = "";
        }

        public EnclosingFormat(String enclosure, String extraOpeningTagAttributes) {
            this.enclosure = enclosure;
            this.extraOpeningTagAttributes = " "+extraOpeningTagAttributes;
        }

        public String text(String text) {
            return "<" + enclosure + extraOpeningTagAttributes +">" +
                    text +
                    "</" + enclosure + ">";
        }
    }

    public static class EnclosingWithStyleFormat {

        private final String style;
        private final String enclosure;

        public EnclosingWithStyleFormat(String enclosure, String style) {
            this.enclosure = enclosure;
            this.style = style;
        }

        public String text(String text) {
            return "<" + enclosure + " style=\""+style+"\">" + text + "</" + enclosure + ">";
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

    private static class TableFormat implements Format {
        @Override
        public String text(String text) {
            return new EnclosingFormat("table").text(new EnclosingFormat("tbody").text(text));
        }
    }

    private static class ColorFormat implements Format {

        private final String color;

        public ColorFormat(String color) {
            this.color = color;
        }

        @Override
        public String text(String text) {
            return new EnclosingWithStyleFormat("span", "color: "+color).text(text);
        }
    }
}


