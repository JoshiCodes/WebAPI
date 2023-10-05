package de.joshicodes.webapi.event.events;

import de.joshicodes.webapi.Webserver;
import de.joshicodes.webapi.request.RequestData;
import de.joshicodes.webapi.request.ResponseData;
import de.joshicodes.webapi.router.route.Route;

public class RouteRequestEvent extends Event {

    private final Route route;
    private final RequestData requestData;

    private ResponseData cancelResponse;

    /**
     * Is called every time a route is requested and contains some data about the request.
     * Cancelling this event will prevent the route from being executed and send the cancel response to the client.
     * If no cancel response is set, the default 401 response will be sent.
     * @param server The server instance.
     * @param route The route that was requested.
     * @param requestData The request data.
     */
    public RouteRequestEvent(Webserver server, Route route, RequestData requestData) {
        super(server, true);
        this.route = route;
        this.requestData = requestData;
    }

    public void setCancelled(boolean cancelled, ResponseData cancelResponse) {
        super.setCancelled(cancelled);
        setCancelResponse(cancelResponse);
    }

    public void setCancelResponse(ResponseData cancelResponse) {
        this.cancelResponse = cancelResponse;
    }

    public Route getRoute() {
        return route;
    }

    public RequestData getRequestData() {
        return requestData;
    }

    public ResponseData getCancelResponse() {
        return cancelResponse;
    }

    public boolean hasCancelResponse() {
        return cancelResponse != null;
    }

}
