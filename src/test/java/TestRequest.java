
import de.joshicodes.webapi.auth.Authentication;
import de.joshicodes.webapi.auth.handler.BasicAuthenticationHandler;
import de.joshicodes.webapi.auth.handler.BearerAuthenticationHandler;
import de.joshicodes.webapi.request.*;

public class TestRequest extends Route {

    public TestRequest() {
        super("/test");
    }

    @Override
    @HttpMethod(HttpMethodType.GET)
    @Authentication(handler = BasicAuthenticationHandler.class, value="test:pass")
    public ResponseData handle(RequestData exchange) {
        ResponseData.Builder builder = new ResponseData.Builder();
        builder.setBody("Hello World!!!");
        builder.setStatusCode(200);
        builder.setContentType("text/plain");
        return builder.build();
    }

}
