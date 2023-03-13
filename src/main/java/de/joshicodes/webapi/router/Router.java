package de.joshicodes.webapi.router;

import de.joshicodes.webapi.router.route.Route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Router {

    private final String path;
    private final List<Route> routes;

    public Router(String path) {
        this.path = path;
        this.routes = new ArrayList<>();
    }

    public void addRoute(Route route) {
        this.routes.add(route);
    }

    public void addRoutes(Route... routes) {
        this.routes.addAll(Arrays.asList(routes));
    }

    public String getPath() {
        return path;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public boolean hasRoute(String path) {
        for (Route route : routes) {
            if (route.getPath().equals(path)) {
                return true;
            }
        }
        return false;
    }

    public Route getRoute(String path) {
        for (Route route : routes) {
            if (route.getPath().equals(path)) {
                return route;
            }
        }
        return null;
    }

}
