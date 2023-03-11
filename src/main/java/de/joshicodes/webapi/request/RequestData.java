package de.joshicodes.webapi.request;

import com.sun.net.httpserver.HttpExchange;

public class RequestData {

    private final HttpExchange exchange;

    public RequestData(HttpExchange exchange) {
        this.exchange = exchange;
    }

    public HttpExchange getExchange() {
        return exchange;
    }

}
