import de.joshicodes.webapi.HttpErrorCode;
import de.joshicodes.webapi.Webserver;
import de.joshicodes.webapi.WebserverBuilder;
import de.joshicodes.webapi.event.EventHandler;
import de.joshicodes.webapi.event.EventManager;
import de.joshicodes.webapi.event.ListenerAdapter;
import de.joshicodes.webapi.event.events.RouteRequestEvent;
import de.joshicodes.webapi.request.ResponseData;

public class EventExample {

    public static void main(String[] args) {
        // Create a new WebserverBuilder
        // For more information about the WebserverBuilder, see the Example.java file
        WebserverBuilder builder = new WebserverBuilder();
        builder.setPort(8080);

        // Register a route to "/hello"
        builder.addRoute("/hello", request -> ResponseData.from(200, "Hello World!").build());

        Webserver server = builder.build();

        // Start the webserver
        server.start();

        // Get the EventManager
        EventManager eventManager = server.getEventManager();

        // Register an event listener
        eventManager.registerListener(new ExampleListener());

    }

    static class ExampleListener implements ListenerAdapter {

        @EventHandler
        public void onRequest(RouteRequestEvent event) {
            System.out.println("Request to " + event.getRequestData().getPath() + " (" + event.getRequestData().getMethod() + ")");
        }

    }

}
