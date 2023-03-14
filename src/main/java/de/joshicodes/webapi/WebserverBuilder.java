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

    /**
     * Creates a new WebserverBuilder<br>
     * Use this to build a webserver
     *
     * @see WebserverBuilder#build()
     */
    public WebserverBuilder() {
        this.routers = new HashMap<>();
        this.errorHandlers = new HashMap<>();
        this.host = "0.0.0.0";
        path = "/";
    }

    /**
     * Adds a router to the webserver
     * @param path the path of the router
     * @param router the router
     * @return this WebserverBuilder instance
     */
    public WebserverBuilder addRouter(String path, Router router) {
        if(!path.startsWith("/"))
            path = "/" + path;
        this.routers.put(path, router);
        return this;
    }

    /**
     * Adds a route to the webserver
     * @param path the path of the route
     * @param route the route
     * @return this WebserverBuilder instance
     */
    public WebserverBuilder addRoute(String path, Route route) {
        if(!path.startsWith("/"))
            path = "/" + path;
        if(defaultRouter == null)
            defaultRouter = new Router();
        defaultRouter.addRoute(path, route);
        routers.put(this.path, defaultRouter);
        return this;
    }

    /**
     * Adds an error route to the webserver
     * @param code the error code to handle
     * @param route the route
     * @return this WebserverBuilder instance
     */
    public WebserverBuilder addErrorHandler(int code, ErrorRoute route) {
        this.errorHandlers.put(code, route);
        return this;
    }

    /**
     * Sets the host of the webserver (default: 0.0.0.0)
     * @param host the host
     * @return this WebserverBuilder instance
     */
    public WebserverBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    /**
     * Sets the port of the webserver
     * @param port the port
     * @return this WebserverBuilder instance
     */
    public WebserverBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    /**
     * Sets the path of the webserver (default: /)
     * @param path the path
     * @return this WebserverBuilder instance
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Creates a new webserver instance
     * @throws IllegalArgumentException if no port is provided via {@link WebserverBuilder#setPort(int)}
     * @return the created webserver instance
     *
     * @see WebserverBuilder#setPort(int)
     */
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
