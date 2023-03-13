package de.joshicodes.webapi.router;

import de.joshicodes.webapi.router.route.Route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Router {

    private final HashMap<String, Route> routes;

    public Router() {
        this.routes = new HashMap<>();
    }

    public void addRoute(String path, Route route) {
        this.routes.put(path, route);
    }

    public void addRoutes(HashMap<String, Route> routes) {
        this.routes.putAll(routes);
    }

    public HashMap<String, Route> getRoutes() {
        return routes;
    }

    public boolean hasRoute(String requestPath) {
        for (String path : routes.keySet()) {
            if (path.equalsIgnoreCase(requestPath)) {
                return true;
            }
        }
        return false;
    }

    public Route getRoute(String path) {
        return routes.get(path);
    }

}
