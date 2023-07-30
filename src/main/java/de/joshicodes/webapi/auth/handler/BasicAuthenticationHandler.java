package de.joshicodes.webapi.auth.handler;

import com.sun.net.httpserver.HttpExchange;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class BasicAuthenticationHandler extends AuthenticationHandler {

    public BasicAuthenticationHandler(String value) {
        super(value, "Basic");
    }

    @Override
    public boolean handle(String type, String provided, HttpExchange exchange) {
        if(!type.equals("Basic")) {
            return false;
        }
        byte[] bytes = Base64.getDecoder().decode(provided);
        String encoded = new String(bytes, StandardCharsets.UTF_8);
        return getValue().equals(encoded);
    }

}
