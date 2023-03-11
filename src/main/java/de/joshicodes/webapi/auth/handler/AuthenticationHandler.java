package de.joshicodes.webapi.auth.handler;

public abstract class AuthenticationHandler {

    public static final AuthenticationHandler NO_AUTH = new AuthenticationHandler() {
        @Override
        public boolean handle(String type, String provided) {
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

    abstract public boolean handle(String type, String provided);

    public String getValue() {
        return value;
    }

    public String[] getAllowedTypes() {
        return allowedTypes;
    }

}
