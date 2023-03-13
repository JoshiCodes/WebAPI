package de.joshicodes.webapi.router.route;

import de.joshicodes.webapi.request.RequestData;
import de.joshicodes.webapi.request.ResponseData;

public abstract class Route {

    public Route() {}

    abstract public ResponseData handle(RequestData request);

}
