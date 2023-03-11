package de.joshicodes.webapi.request;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public record RequestData(HttpExchange exchange) {

    public String getMethod() {
        return exchange.getRequestMethod();
    }

    public String getProtocol() {
        return exchange.getProtocol();
    }

    public String getQuery() {
        return exchange.getRequestURI().getQuery();
    }

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

    public boolean hasParameter(String key) {
        return getParameters().containsKey(key);
    }

    public String getParameter(String key) {
        return getParameters().get(key);
    }

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
