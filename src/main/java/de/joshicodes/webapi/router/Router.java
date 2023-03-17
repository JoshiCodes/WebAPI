package de.joshicodes.webapi.router;

import de.joshicodes.webapi.router.route.Route;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Router {

    private final HashMap<String, Route> routes;

    public Router() {
        this.routes = new HashMap<>();
    }

    /**
     * Searches for a route that matches the given uri
     * @param uri The uri to search for
     * @return The route that matches the uri or null if no route was found
     */
    public Route search(String uri) {
        if(!uri.startsWith("/")) uri = "/" + uri;
        String[] args = uri.split("/");
        int i = args.length;
        while (i > 0) {
            String path = String.join("/", Arrays.copyOfRange(args, 0, i));
            i--;
            if(path.replaceAll(" ", "").equals("")) continue;  // Skip empty paths
            if(hasRoute(path)) {
                return getRoute(path);
            }
        }
        return null;
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
