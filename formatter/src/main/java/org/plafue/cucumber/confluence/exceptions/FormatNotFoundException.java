package org.plafue.cucumber.confluence.exceptions;

/**
 * Exception thrown when a certain format is requested but not found
 */
public class FormatNotFoundException extends RuntimeException {
    public FormatNotFoundException(String formatKey) {
        super("No format for key " + formatKey);
    }
}
