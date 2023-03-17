import de.joshicodes.webapi.HttpErrorCode;
import de.joshicodes.webapi.WebserverBuilder;
import de.joshicodes.webapi.request.ResponseData;

/**
 * This example shows how to use the error handlers
 * @author JoshiCodes
 */
public class ErrorExample {

    public static void main(String[] args) {

        // Create a new WebserverBuilder
        // For more information about the WebserverBuilder, see the Example.java file
        WebserverBuilder builder = new WebserverBuilder();
        builder.setPort(8080);

        // Register a route to "/hello"
        builder.addRoute("/hello", request -> ResponseData.from(200, "Hello World!").build());

        // Register Error Handlers
        // The error handlers are called when an error occurs
        // The 404 error is only called, when no "/" route is found. If a "/" route is found, the "/" route is called instead.
        builder.addErrorHandler(404, request -> ResponseData.from(404, "Not Found!").build());
        // You can also use the HttpErrorCode class to get some common error codes
        // HttpErrorCode.INTERNAL_SERVER_ERROR is the same as 500
        builder.addErrorHandler(HttpErrorCode.INTERNAL_SERVER_ERROR, request -> ResponseData.from(500, "Internal Server Error!").build());

        // Start the webserver
        builder.build().start();

        // If you now open http://localhost:8080/hello in your browser, you should see "Hello World!"
        // For every other route, you should see "Not Found!"

    }

}
