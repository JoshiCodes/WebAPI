package de.joshicodes.webapi.request;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import de.joshicodes.webapi.exceptions.MissingPostDataException;
import de.joshicodes.webapi.exceptions.UnknownContentTypeException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public record RequestData(HttpExchange exchange) {

    /**
     * Returns the request method provided by the exchange
     * @return the request method as string (GET, POST, PUT, DELETE, ...)
     */
    public String getMethod() {
        return exchange.getRequestMethod();
    }

    /**
     * Returns the protocol string from the request in the form protocol/majorVersion.minorVersion. For example, "HTTP/1.1".
     * @return the protocol string
     */
    public String getProtocol() {
        return exchange.getProtocol();
    }

    /**
     * Returns the provided Query String
     * @return the query string
     */
    public String getQuery() {
        return exchange.getRequestURI().getQuery();
    }

    /**
     * Returns the first value of the provided "Content-Type" header
     * @return the content type
     */
    public String getContentType() {
        return exchange.getRequestHeaders().getFirst("Content-Type");
    }

    /**
     * Returns the post parameters of the request
     * @return the post parameters
     * @throws MissingPostDataException if the request has no post data
     * @throws NullPointerException if the content type in the request is null
     * @throws UnknownContentTypeException if the content type is not supported
     */
    public HashMap<String, String> getPostParameters() throws MissingPostDataException, UnknownContentTypeException {
        // read content type from exchange
        String contentType = getContentType();
        if (contentType == null) {
            throw new NullPointerException("The content type is null, cannot handle request");
        }
        // handle content type
        HashMap<String, String> parameters = new HashMap<>();
        String body = null;
        try {
            body = getBody();
        } catch (IOException e) {
            throw new MissingPostDataException(this);
        }
        if (body == null) {
            throw new MissingPostDataException(this);
        }

        if (contentType.contains("application/x-www-form-urlencoded")) {
            String[] params = body.split("&");
            for (String param : params) {
                if (param.contains("=")) {
                    String[] split = param.split("=");
                    if (split.length == 2)
                        parameters.put(split[0], split[1]);
                    else if (split.length == 1)
                        parameters.put(split[0], null);
                } else {
                    parameters.put(param, null);
                }
            }
        } else if (contentType.contains("application/json")) {
            JsonElement jsonElement = JsonParser.parseString(body);
            jsonElement.getAsJsonObject().entrySet().forEach(
                    entry -> parameters.put(entry.getKey(), entry.getValue().getAsString())
            );
        } else if (contentType.contains("multipart/form-data")) {
            throw new UnknownContentTypeException(contentType);
            // TODO: implement multipart parsing
        } else {
            throw new UnknownContentTypeException(contentType);
        }

        return parameters;
    }

    /**
     * Returns the GET parameters of the request query<br>
     * (e.g. ?param1=value1&param2=value2 -> param1=value1, param2=value2)
     * @return the GET parameters
     */
    public HashMap<String, String> getParameters() {
        HashMap<String, String> parameters = new HashMap<>();
        String query = getQuery();
        if (query == null) {
            return parameters;
        }
        String[] params = query.split("&");
        for (String param : params) {
            if (param.contains("=")) {
                String[] split = param.split("=");
                if (split.length == 2)
                    parameters.put(split[0], split[1]);
                else if (split.length == 1)
                    parameters.put(split[0], null);
            } else {
                parameters.put(param, null);
            }
        }
        return parameters;
    }

    /**
     * Checks if the request has a parameter with the provided key<br>
     * Works for both GET and POST requests
     * @param key the key to check
     * @return true if the request has a parameter with the provided key
     */
    public boolean hasParameter(String key) {
        if(getMethod().equals("POST")) {
            try {
                return getPostParameters().containsKey(key);
            } catch (MissingPostDataException | UnknownContentTypeException e) {
                throw new RuntimeException(e);
            }
        } else return getParameters().containsKey(key);
    }

    /**
     * Returns the value of the provided parameter key<br>
     * Works for both GET and POST requests
     * @param key the key of the parameter
     * @return the value of the parameter, always a string
     */
    public String getParameter(String key) {
        if(getMethod().equals("POST")) {
            try {
                return getPostParameters().get(key);
            } catch (MissingPostDataException | UnknownContentTypeException e) {
                throw new RuntimeException(e);
            }
        } else return getParameters().get(key);
    }

    /**
     * Returns the uri parts of the request path<br>
     * @return the uri parts
     */
    public String[] getUriParts() {
        String path = getPath();
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        return path.split("/");
    }

    /**
     * Returns the value of the provided parameter key<br>
     * Works for both GET and POST requests
     * @param key the key of the parameter
     * @param def the default value if the parameter is not present
     * @return the value of the parameter, always a string
     */
    public String getParameter(String key, String def) {
        return getParameters().getOrDefault(key, def);
    }

    public String getPath() {
        return exchange.getRequestURI().getPath();
    }

    public String getBody() throws IOException {
        return new String(exchange.getRequestBody().readAllBytes());
    }

    public List<String> getHeader(String key) {
        return exchange.getRequestHeaders().get(key);
    }

    public Headers getHeaders() {
        return exchange.getRequestHeaders();
    }

}
