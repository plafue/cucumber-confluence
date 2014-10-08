package org.plafue.cucumber.confluence.formatter;

import gherkin.formatter.Format;
import gherkin.formatter.Formatter;
import gherkin.formatter.NiceAppendable;
import gherkin.formatter.model.*;
import gherkin.util.Mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.plafue.cucumber.confluence.formatter.ConfluenceStorageFormat.Formats.*;
import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;
import static org.plafue.cucumber.confluence.formatter.Macros.Formats.*;
import static gherkin.util.FixJava.join;
import static gherkin.util.FixJava.map;

/**
 * This class pretty prints feature files in Confluence Markup (tested with v4.1.22).
 * This class prints "Feature", "Background", and "scenarios" with their tags if any.
 */
public class ConfluenceStorageFormatter implements Formatter {


    private static final String NEWLINE = "\\r\\n|\\r|\\n";
    public static final String JIRA_ISSUE_ID_FORMAT = "@[A-Z][A-Z]+-[0-9]{1,9}";
    private final NiceAppendable out;
    private final Options options;
    private final Macros macros;
    private final ConfluenceStorageFormat formats;

    private List<Step> steps = new ArrayList<Step>();
    private DescribedStatement statement;
    private Mapper<Tag, String> tagNameMapper = new Mapper<Tag, String>() {
        @Override
        public String map(Tag tag) {
            return getFormat(BOLD).text(getFormat(ITALICS).text(
                    tag.getName().replace("@", "")));
        }
    };

    public ConfluenceStorageFormatter(Appendable out, Options options) {
        this.out = new NiceAppendable(out);
        this.options = options;
        this.formats = new ConfluenceStorageFormat();
        if (options.isJiraTicketParsingInTags()) {
            this.macros = new Macros(options.getJiraServer());
        } else {
            this.macros = new Macros();
        }
    }

    @Override
    public void uri(String uri) {
    }

    @Override
    public void feature(Feature feature) {
        out.println(getFormat(HEADER1).text(feature.getName()));
        printTags(feature.getTags());
        String description = feature.getDescription().replaceAll(NEWLINE, " ");
        if (!description.isEmpty()) {
            out.println(description);
        }

    }

    @Override
    public void background(Background background) {
        replay();
        statement = background;
    }

    @Override
    public void scenario(Scenario scenario) {
        replay();
        statement = scenario;
    }

    @Override
    public void scenarioOutline(ScenarioOutline scenarioOutline) {
        replay();
        statement = scenarioOutline;
    }

    @Override
    public void startOfScenarioLifeCycle(Scenario scenario) {
        // NoOp
    }

    @Override
    public void endOfScenarioLifeCycle(Scenario scenario) {
        // NoOp
    }

    @Override
    public void examples(Examples examples) {
        replay();
        out.println();
        printComments(examples.getComments(), " ");
        printTags(examples.getTags());
        out.println(getFormat(TABLE).text(
                getFormat(TABLE_ROW).text(getFormat(TABLE_HEAD_CELL).text(examples.getKeyword() + ": " + examples.getName())) +
                        getFormat(TABLE_ROW).text(getFormat(CELL).text(getMacro(PANEL).text(renderTable(examples.getRows()))))));
    }

    @Override
    public void step(Step step) {
        steps.add(step);
    }

    @Override
    public void syntaxError(String state, String event, List<String> legalEvents, String uri, Integer line) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void done() {
    }

    @Override
    public void close() {
        out.close();
    }

    public void eof() {
        replay();
    }

    private void replay() {
        printSectionTitle();
        printSteps();
    }

    private void printSteps() {
        if (steps.isEmpty()) return;


        final StringBuilder tableContents = new StringBuilder();
        while (!steps.isEmpty()) {
            printStep(tableContents);
        }
        out.println(getFormat(TABLE).text(tableContents.toString()));
    }

    private void printSectionTitle() {
        if (statement == null) return;

        out.println(
                formats.get(HEADER2).text(
                        statement.getName().isEmpty() ?
                                getFormat(RED_FOREGROUND).text(getFormat(ITALICS).text("Undefined section")) :
                                statement.getName()
                ));

        if (statement instanceof TagStatement) {
            printTags(((TagStatement) statement).getTags());
        }
        out.println(statement.getDescription());
        statement = null;
    }

    private void printStep(StringBuilder stringBuilder) {
        Step step = steps.remove(0);
        Format keyword = getFormat(CELL_ALIGNED_RIGHT);
        Format cell = getFormat(CELL);
        Format row = getFormat(TABLE_ROW);
        Format bold = getFormat(BOLD);
        stringBuilder.append(
                row.text(
                        keyword.text(getFormat(COLOR_DARK_GREY).text(bold.text(step.getKeyword().trim()))) +
                                cell.text(escapeHtml4(step.getName().trim()))));

        if (hasNestedTable(step)) {
            stringBuilder.append(renderNestedTableWithinPanelInSecondColumn(step.getRows()));
        }
    }

    private String renderNestedTableWithinPanelInSecondColumn(List<DataTableRow> rows) {
        return getFormat(TABLE_ROW).text(
                getFormat(CELL).text("") +
                        getFormat(CELL).text(getMacro(PANEL).text(renderTable(rows))));
    }

    private boolean hasNestedTable(Step step) {
        return step.getRows() != null;
    }

    private Format getFormat(ConfluenceStorageFormat.Formats key) {
        return formats.get(key);
    }

    private Format getMacro(Macros.Formats key) {
        return macros.get(key);
    }

    private String renderTable(List<? extends Row> rows) {
        if (rows.isEmpty()) return "";

        Format table = getFormat(TABLE);
        StringBuilder tableContents = new StringBuilder();

        for (int i = 0; i < rows.size(); i++) {
            Format cellFormat = isHeaderRow(i) ? getFormat(TABLE_HEAD_CELL) : getFormat(CELL);
            tableContents.append(
                    getFormat(TABLE_ROW).text(renderCells(rows.get(i), cellFormat).toString())
            );
        }

        return table.text(tableContents.toString());
    }

    private StringBuilder renderCells(Row row, Format cellFormat) {
        StringBuilder cells = new StringBuilder();
        for (String cellContents : row.getCells()) {
            cells.append(cellFormat.text(escapeHtml4(cellContents)));
        }
        return cells;
    }

    private boolean isHeaderRow(int i) {
        return (i == 0);
    }

    private void printComments(List<Comment> comments, String indent) {
        for (Comment comment : comments) {
            out.println(indent + comment.getValue());
        }
    }

    private void printTags(List<Tag> tags) {
        if (tags.isEmpty() || !options.isTagRenderingActive() ||
                (options.isJiraTicketParsingInTags() && options.jiraServer == null)) return;

        List<Tag> jiraIds = Collections.EMPTY_LIST;

        if (options.isJiraTicketParsingInTags()) {
            jiraIds = findJiraIdsAndExtractFromOriginalList(tags);
        }

        if (!tags.isEmpty()) {
            out.println(getMacro(INFO).text(
                    " This section is tagged as " +
                            join(map(tags, tagNameMapper), ", ")));
        }

        if (!jiraIds.isEmpty()) {
            printJiraMacros(jiraIds);
        }
    }

    private void printJiraMacros(List<Tag> jiraIds) {
        out.println(join(map(jiraIds, new Mapper<Tag, String>() {
            @Override
            public String map(Tag tag) {
                return getMacro(JIRA).text(tag.getName().replace("@", ""));
            }
        }), System.lineSeparator()));
    }

    private List<Tag> findJiraIdsAndExtractFromOriginalList(List<Tag> tags) {
        List<Tag> jiraIds = new ArrayList<Tag>();
        for (Tag tag : tags) {
            if (tag.getName().matches(JIRA_ISSUE_ID_FORMAT)) {
                jiraIds.add(tag);
            }
        }
        tags.removeAll(jiraIds);
        return jiraIds;
    }

    public static class Options {
        private boolean tagRenderingActive;
        private boolean jiraTicketParsingInTags;
        private String jiraServer;

        public Options(boolean tagRenderingActive) {
            this.tagRenderingActive = tagRenderingActive;
        }

        public Options(String jiraServer) {
            if (jiraServer == null) {
                throw new IllegalStateException("A Jira server must be provided");
            }
            this.tagRenderingActive = true;
            this.jiraTicketParsingInTags = true;
            this.jiraServer = jiraServer;
        }

        public boolean isTagRenderingActive() {
            return tagRenderingActive;
        }

        public String getJiraServer() {
            return jiraServer;
        }

        public void setJiraServer(String jiraServer) {
            this.jiraServer = jiraServer;
        }

        public boolean isJiraTicketParsingInTags() {
            return jiraTicketParsingInTags;
        }
    }
}