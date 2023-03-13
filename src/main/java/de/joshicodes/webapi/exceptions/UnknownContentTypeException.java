package de.joshicodes.webapi.exceptions;

public class UnknownContentTypeException extends Exception {

    public UnknownContentTypeException(String contentType) {
        super("Unknown content type: " + contentType + "! If you think this Content Type should be supported, please open an issue on GitHub.");
    }

}
