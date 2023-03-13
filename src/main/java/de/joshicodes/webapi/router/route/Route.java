package de.joshicodes.webapi.router.route;

import de.joshicodes.webapi.request.RequestData;
import de.joshicodes.webapi.request.ResponseData;

public abstract class Route {

    private String path;

    public Route(String path) {
        this.path = path;
    }

    abstract public ResponseData handle(RequestData request);

    public String getPath() {
        return path;
    }

}
