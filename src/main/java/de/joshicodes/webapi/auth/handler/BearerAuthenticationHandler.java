package de.joshicodes.webapi.auth.handler;

public class BearerAuthenticationHandler extends AuthenticationHandler {

    public BearerAuthenticationHandler(String value) {
        super(value, "Bearer");
    }

    @Override
    public boolean handle(String type, String provided) {
        if (!type.equals("Bearer")) {
            return false;
        }
        return getValue().equals(provided);
    }

}
