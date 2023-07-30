import com.sun.net.httpserver.HttpExchange;
import de.joshicodes.webapi.WebserverBuilder;
import de.joshicodes.webapi.auth.Authentication;
import de.joshicodes.webapi.auth.handler.AuthenticationHandler;
import de.joshicodes.webapi.auth.handler.BearerAuthenticationHandler;
import de.joshicodes.webapi.request.RequestData;
import de.joshicodes.webapi.request.ResponseData;
import de.joshicodes.webapi.router.route.Route;

/**
 * This example shows how to use the AuthenticationHandler and how to create your own
 * @author JoshiCodes
 */
public class AuthenticationExample {

    public static void main(String[] args) {
        // Create a new WebserverBuilder
        // For more information about the WebserverBuilder, see the Example.java file
        WebserverBuilder builder = new WebserverBuilder();
        builder.setPort(8080);

        // Add a Route that requires authentication
        builder.addRoute("/auth", new Route() {
            @Override
            // Authentication is done by the AuthenticationHandler
            // In this case, the BearerAuthenticationHandler is used to authenticate the user with the given token
            // It is advised to create your own AuthenticationHandler, to not expose or hardcode your tokens
            @Authentication(handler = BearerAuthenticationHandler.class, value = "bearerTokenHere")
            public ResponseData handle(RequestData request) {
                return ResponseData.from(200, "Authenticated!").build();
            }
        });

        // Add a Route that requires authentication with a custom AuthenticationHandler
        builder.addRoute("/customAuth", new Route() {
            @Override
            // The CustomAuthenticationHandler is used to authenticate the user
            // The value is not used in this case, because the CustomAuthenticationHandler does not use it
            @Authentication(handler = CustomAuthenticationHandler.class, value = "")
            public ResponseData handle(RequestData request) {
                return ResponseData.from(200, "Authenticated!").build();
            }
        });

    }

    static class CustomAuthenticationHandler extends AuthenticationHandler {

        CustomAuthenticationHandler() {
            // The first parameter is the value provided in the Authentication annotation
            // It is recommended to use null and check the provided value against a database or something similar

            // The second parameter is the type of the authentication
            // In this case, the type is "Bearer"
            // You can also use multiple types
            // The Type has to be the same as the Authentication used in the request
            super(null, "Bearer");
        }

        // If you want to have the value provided in the Authentication annotation, you can use this constructor
        // This is not recommended, as it is better to check the provided value against a database or something similar
        // You can retrieve the value using the getValue() method
        // The type is still required
        CustomAuthenticationHandler(String value) {
            super(value, "Bearer");
        }

        @Override
        public boolean handle(String type, String provided, HttpExchange exchange) {

            // Double check if the type is correct
            // This is not necessary, because the AuthenticationHandler only calls this method, if the type is correct
            if (!type.equals("Bearer")) {
                return false;
            }

            // Check if the provided token is valid
            // You should use a database or something similar to store your tokens
            final String tokenFromDatabase = "bearerTokenHere";  // Replace this with your token from the database or something similar
            if (provided.equals(tokenFromDatabase)) {
                // Return true if the token is valid
                return true;
            }
            // Return false if the token is invalid
            return false;

        }

    }

}
