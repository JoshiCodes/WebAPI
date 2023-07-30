package de.joshicodes.webapi.auth.handler;

import com.sun.net.httpserver.HttpExchange;

public abstract class AuthenticationHandler {

    public static final AuthenticationHandler NO_AUTH = new AuthenticationHandler() {
        @Override
        public boolean handle(String type, String provided, HttpExchange exchange) {
            return true;
        }
    };

    private final String value;
    private final String[] allowedTypes = new String[]{"Bearer"};

    public AuthenticationHandler(String value, String... allowedTypes) {
        this.value = value;
        if (allowedTypes.length > 0) {
            System.arraycopy(allowedTypes, 0, this.allowedTypes, 0, allowedTypes.length);
        }
    }

    public AuthenticationHandler(String... allowedTypes) {
        this.value = null;
        if (allowedTypes.length > 0) {
            System.arraycopy(allowedTypes, 0, this.allowedTypes, 0, allowedTypes.length);
        }
    }

    public AuthenticationHandler(String value, String allowedType) {
        this.value = value;
        this.allowedTypes[0] = allowedType;
    }

    /**
     * Replaces the old handle method and is used to handle the authentication process.
     * It is not recommended to send any response to the client in this method.
     * @since 1.3.3
     * @param type The type of the authentication header - should be a value set in the super constructor
     * @param provided The provided authentication header value
     * @param exchange The {@link HttpExchange} object of the request to process other kinds of authentication if needed
     * @return Whether the authentication was successful or not - if false is returned, the request will be rejected
     */
    abstract public boolean handle(String type, String provided, HttpExchange exchange);

    public String getValue() {
        return value;
    }

    public String[] getAllowedTypes() {
        return allowedTypes;
    }

}
