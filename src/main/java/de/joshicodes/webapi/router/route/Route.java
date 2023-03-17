package de.joshicodes.webapi.router.route;

import de.joshicodes.webapi.request.RequestData;
import de.joshicodes.webapi.request.ResponseData;

public interface Route {

   ResponseData handle(RequestData request);

}
