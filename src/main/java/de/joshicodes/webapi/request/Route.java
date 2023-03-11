package de.joshicodes.webapi.request;

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
