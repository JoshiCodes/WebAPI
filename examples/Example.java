import de.joshicodes.webapi.Webserver;
import de.joshicodes.webapi.WebserverBuilder;
import de.joshicodes.webapi.request.HttpMethod;
import de.joshicodes.webapi.request.HttpMethodType;
import de.joshicodes.webapi.request.RequestData;
import de.joshicodes.webapi.request.ResponseData;
import de.joshicodes.webapi.router.Router;
import de.joshicodes.webapi.router.route.Route;

/**
 * This example show the basic usage of the WebserverBuilder and the Webserver
 * @author JoshiCodes
 */
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

        Webserver server = builder.build();  // build the server
        server.start();  // start the server

    }

}
