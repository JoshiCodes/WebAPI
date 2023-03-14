package de.joshicodes.webapi.request;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

public abstract class ResponseData {

    abstract public String getBody();
    abstract public int getStatusCode();
    abstract public String getContentType();
    abstract public String getHeader(String key);


    /**
     * Creates a new ResponseData object with the given body<br>
     * If you want a built ResponseData object, use {@link #buildFrom(int, String)} instead
     * @param code The status code of the response
     * @param body The body of the response
     * @return The ResponseData object
     */
    public static Builder from(int code, String body) {
        return new Builder().setStatusCode(code).setBody(body);
    }

    /**
     * Creates a new ResponseData object with the given body<br>
     * If you want to alter the ResponseData object, use {@link #from(int, String)} instead
     * @param code The status code of the response
     * @param body The body of the response
     * @return The ResponseData object
     */
    public static ResponseData buildFrom(int code, String body) {
        return from(code, body).build();
    }

    /**
     * Creates a new ResponseData object from a file<br>
     * This does not set the content type, you have to do that yourself using {@link Builder#setContentType(String)}
     * @param file The file to read the body from
     * @return The ResponseData object
     *
     * @see Builder#setContentType(String)
     *
     */
    public static Builder fromFile(File file) throws FileNotFoundException {
        if(file == null) throw new NullPointerException("File cannot be null");
        if(!file.exists()) throw new FileNotFoundException("File does not exist");
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            StringBuilder builder = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                builder.append((char) c);
            }
            return from(200, builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return from(500, "Internal Server Error: " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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
