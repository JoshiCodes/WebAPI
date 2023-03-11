import com.sun.net.httpserver.HttpExchange;
import de.joshicodes.webapi.HttpErrorCode;
import de.joshicodes.webapi.Webserver;
import de.joshicodes.webapi.request.ErrorRoute;
import de.joshicodes.webapi.request.RequestData;
import de.joshicodes.webapi.request.ResponseData;
import de.joshicodes.webapi.request.Route;

public class Test {

    public static void main(String[] args) {
        Webserver.Builder builder = new Webserver.Builder().setPort(1234);

        builder.addRoute(new TestRequest());

        builder.addRoute(new Route("/testRoute") {
            @Override
            public ResponseData handle(RequestData request) {
                ResponseData.Builder builder = new ResponseData.Builder();
                builder.setBody("Hello World!!!");
                builder.setStatusCode(200);
                builder.setContentType("text/plain");
                return builder.build();
            }
        });

        builder.addErrorHandler(HttpErrorCode.NOT_FOUND, new ErrorRoute() {
            @Override
            public ResponseData handle(RequestData exchange) {
                ResponseData.Builder builder = new ResponseData.Builder();
                builder.setBody("Not found!!!");
                builder.setStatusCode(404);
                builder.setContentType("text/plain");
                return builder.build();
            }
        });

        Webserver webserver = builder.build();

        webserver.start();
    }

}
