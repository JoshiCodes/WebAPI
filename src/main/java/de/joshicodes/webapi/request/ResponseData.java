package de.joshicodes.webapi.request;

import java.util.HashMap;

public interface ResponseData {

    String getBody();
    int getStatusCode();
    String getContentType();
    String getHeader(String key);

    public static Builder from(int code, String body) {
        return new Builder().setStatusCode(code).setBody(body);
    }

    /**
     * Creates a new ResponseData object with the given body
     * If you want to alter the ResponseData object, use {@link #from(int, String)} instead
     * @param code The status code of the response
     * @param body The body of the response
     * @return The ResponseData object
     */
    public static ResponseData buildFrom(int code, String body) {
        return from(code, body).build();
    }

    public static class Builder {

        private String body;
        private int statusCode = 200;  // 200 = OK
        private String contentType = "text/plain";
        private final HashMap<String, String> headers;

        public Builder() {
            this.headers = new HashMap<>();
        }

        public Builder setBody(String body) {
            this.body = body;
            return this;
        }

        public Builder setStatusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder setContentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder setHeader(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        public ResponseData build() {
            return new ResponseData() {
                @Override
                public String getBody() {
                    return body;
                }

                @Override
                public int getStatusCode() {
                    return statusCode;
                }

                @Override
                public String getContentType() {
                    return contentType;
                }

                @Override
                public String getHeader(String key) {
                    return headers.get(key);
                }
            };
        }

    }

}
