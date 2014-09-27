package cucumber.confluence.exceptions;

public class FormatNotFoundException extends RuntimeException {
    public FormatNotFoundException(String formatKey) {
        super("No format for key " + formatKey);
    }
}
