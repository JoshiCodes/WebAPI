package de.joshicodes.webapi;

import de.joshicodes.webapi.router.Router;
import de.joshicodes.webapi.router.route.ErrorRoute;
import de.joshicodes.webapi.router.route.Route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class WebserverBuilder {

    private final HashMap<String, Router> routers;
    private final HashMap<Integer, ErrorRoute> errorHandlers;
    private String host;
    private int port = -1;

    private String path;

    private Router defaultRouter;

    public WebserverBuilder() {
        this.routers = new HashMap<>();
        this.errorHandlers = new HashMap<>();
        this.host = "0.0.0.0";
        path = "/";
    }

    public WebserverBuilder addRouter(String path, Router router) {
        if(!path.startsWith("/"))
            path = "/" + path;
        this.routers.put(path, router);
        return this;
    }

    public WebserverBuilder addRoute(String path, Route route) {
        if(!path.startsWith("/"))
            path = "/" + path;
        if(defaultRouter == null)
            defaultRouter = new Router();
        defaultRouter.addRoute(path, route);
        routers.put(this.path, defaultRouter);
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

    public HashMap<String, Router> getRouters() {
        return routers;
    }

    public HashMap<Integer, ErrorRoute> getErrorHandlers() {
        return errorHandlers;
    }

    public String getPath() {
        return path;
    }
}
