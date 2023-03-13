package de.joshicodes.webapi.exceptions;

import de.joshicodes.webapi.request.RequestData;

import java.util.Locale;

public class MissingPostDataException extends Exception {

    public MissingPostDataException(RequestData requestData) {
        super("Missing post data for request: " + requestData.getMethod().toUpperCase(Locale.ROOT) + " " + requestData.getPath() + "");
    }

}
