import de.joshicodes.webapi.HttpErrorCode;
import de.joshicodes.webapi.Webserver;
import de.joshicodes.webapi.WebserverBuilder;
import de.joshicodes.webapi.auth.Authentication;
import de.joshicodes.webapi.auth.handler.BearerAuthenticationHandler;
import de.joshicodes.webapi.request.HttpMethod;
import de.joshicodes.webapi.request.HttpMethodType;
import de.joshicodes.webapi.request.RequestData;
import de.joshicodes.webapi.request.ResponseData;
import de.joshicodes.webapi.router.Router;
import de.joshicodes.webapi.router.route.Route;

public class Example {

    public static void main(String[] args) {

        WebserverBuilder builder = new WebserverBuilder();  // create a new builder
        // builder.setHost("localhost");  // (optional) set the host
        builder.setPort(8080);  // set the port
        // builder.setPath("/api");  // (optional) set the path

        // Register Routes
        builder.addRoute("/", request -> ResponseData.from(200, "Hello World!").build());
        builder.addRoute("/test", request -> ResponseData.from(200, "Test").build());

        // Register a Router
        Router router = new Router();
        router.addRoute("/", request -> ResponseData.from(200, "Hello World! (Router)").build());
        router.addRoute("/test", request -> ResponseData.from(200, "Test (Router)").build());

        builder.addRouter("/testRouter", router);

        // Add a route that is only accessible via POST
        builder.addRoute("/post", new Route() {
            @Override
            @HttpMethod(HttpMethodType.POST)
            public ResponseData handle(RequestData request) {
                return ResponseData.from(200, "POST").build();
            }
        });

        // Add a route that is only accessible via GET and POST
        builder.addRoute("/getAndPost", new Route() {
            @Override
            @HttpMethod({HttpMethodType.GET, HttpMethodType.POST})
            public ResponseData handle(RequestData request) {
                return ResponseData.from(200, "GET and POST").build();
            }
        });

        // Add a Route that requires authentication
        router.addRoute("/auth", new Route() {
            @Override
            // Authentication is done by the AuthenticationHandler
            // In this case, the BearerAuthenticationHandler is used to authenticate the user with the given token
            // It is advised to create your own AuthenticationHandler, to not expose or hardcode your tokens
            @Authentication(handler = BearerAuthenticationHandler.class, value = "bearerTokenHere")
            public ResponseData handle(RequestData request) {
                return ResponseData.from(200, "Authenticated!").build();
            }
        });

        // Register Error Handlers
        // The error handlers are called when an error occurs
        // The 404 error is only called, when no "/" route is found. If a "/" route is found, the "/" route is called instead.
        builder.addErrorHandler(404, request -> ResponseData.from(404, "Not Found!").build());
        // You can also use the HttpErrorCode class to get some common error codes
        // HttpErrorCode.INTERNAL_SERVER_ERROR is the same as 500
        builder.addErrorHandler(HttpErrorCode.INTERNAL_SERVER_ERROR, request -> ResponseData.from(500, "Internal Server Error!").build());

        Webserver server = builder.build();  // build the server
        server.start();  // start the server

    }

}
