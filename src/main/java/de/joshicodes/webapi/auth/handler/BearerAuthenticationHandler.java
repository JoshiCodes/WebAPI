package de.joshicodes.webapi.auth.handler;

import com.sun.net.httpserver.HttpExchange;

public class BearerAuthenticationHandler extends AuthenticationHandler {

    public BearerAuthenticationHandler(String value) {
        super(value, "Bearer");
    }

    @Override
    public boolean handle(String type, String provided, HttpExchange exchange) {
        if (!type.equals("Bearer")) {
            return false;
        }
        return getValue().equals(provided);
    }

}
