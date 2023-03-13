package de.joshicodes.webapi;

import de.joshicodes.webapi.router.Router;
import de.joshicodes.webapi.router.route.ErrorRoute;
import de.joshicodes.webapi.router.route.Route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class WebserverBuilder {

    private final List<Router> routers;
    private final List<Route> routes;
    private final HashMap<Integer, ErrorRoute> errorHandlers;
    private String host;
    private int port = -1;

    private String path;

    public WebserverBuilder() {
        this.routers = new ArrayList<>();
        this.routes = new ArrayList<>();
        this.errorHandlers = new HashMap<>();
        this.host = "0.0.0.0";
        path = null;
    }

    public WebserverBuilder addRouter(Router router) {
        this.routers.add(router);
        return this;
    }

    public WebserverBuilder addRoute(Route route) {
        this.routes.add(route);
        return this;
    }

    public WebserverBuilder addRoutes(Route... routes) {
        this.routes.addAll(Arrays.asList(routes));
        return this;
    }

    public WebserverBuilder addErrorHandler(int code, ErrorRoute request) {
        this.errorHandlers.put(code, request);
        return this;
    }

    public WebserverBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public WebserverBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Webserver build() {
        if (port == -1) {
            throw new IllegalArgumentException("You have to provide a port for the webserver");
        }
        return new Webserver(this);
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public List<Router> getRouters() {
        return routers;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public HashMap<Integer, ErrorRoute> getErrorHandlers() {
        return errorHandlers;
    }

    public String getPath() {
        return path;
    }
}
