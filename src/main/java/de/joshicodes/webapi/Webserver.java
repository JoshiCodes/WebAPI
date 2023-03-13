package de.joshicodes.webapi;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import de.joshicodes.webapi.auth.Authentication;
import de.joshicodes.webapi.auth.handler.AuthenticationHandler;
import de.joshicodes.webapi.request.*;
import de.joshicodes.webapi.router.Router;
import de.joshicodes.webapi.router.route.ErrorRoute;
import de.joshicodes.webapi.router.route.Route;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.*;

public class Webserver {

    private final String host;
    private final int port;

    private HttpServer server;

    private final List<Router> routers;
    private final List<Route> routes;
    private final HashMap<Integer, ErrorRoute> errorHandlers;

    private final String path;

    Webserver(WebserverBuilder builder) {
        this.host = builder.getHost();
        this.port = builder.getPort();
        this.routers = builder.getRouters();
        this.routes = builder.getRoutes();
        this.errorHandlers = builder.getErrorHandlers();
        this.path = builder.getPath();
        create();
    }

    private void create() {

        try {
            server = HttpServer.create(new InetSocketAddress(host, port), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        server.createContext((path != null && !path.replaceAll(" ", "").equals("") ? path : "/"), exchange -> handle(exchange.getRequestURI(), exchange));

    }

    public void start() {
        System.out.println("Starting server on " + host + ":" + port);
        server.start();
    }

    public void stop() {
        System.out.println("Stopping server on " + host + ":" + port);
        server.stop(0);
    }

    private void handle(URI uri, HttpExchange exchange) throws IOException {
        // Check if there is a router for the path
        for (Router router : routers) {
            System.out.println(uri.getPath() + " " + router.getPath());
            if(uri.getPath().startsWith(router.getPath())) {
                String path = uri.getPath().replace(router.getPath(), "");
                if(!path.endsWith("/"))
                    path = path.concat("/");
                Route request = router.getRoute(path);
                if(request == null) {
                    continue;
                }
                if(handleRoute(request, exchange)) {
                    return;
                }
                continue;
            }
        }
        // Check if there is a route for the path
        for (Route request : routes) {
            if (request.getPath().equals(uri.getPath())) {
                if(handleRoute(request, exchange)) {
                    return;
                }
            }
            continue;
        }
        if(errorHandlers.containsKey(HttpErrorCode.NOT_FOUND)) {
            ResponseData response = errorHandlers.get(HttpErrorCode.NOT_FOUND).handle(new RequestData(exchange));
            write(response, exchange);
        } else {
            String response = "{\"error\": \"404 Not found\"}";  // default 404 response
            exchange.sendResponseHeaders(HttpErrorCode.NOT_FOUND, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        }
    }

    private boolean handleRoute(Route request, HttpExchange exchange) throws IOException {
        Method m = null;
        try {
            m = request.getClass().getMethod("handle", RequestData.class);
        } catch (NoSuchMethodException ignore) {
        }
        if (m == null) {
            return false;
        }
        List<String> methods = List.of("GET");
        if (m.isAnnotationPresent(HttpMethod.class)) {
            de.joshicodes.webapi.request.HttpMethod annotation = m.getAnnotation(HttpMethod.class);
            methods = Arrays.stream(annotation.value()).map(HttpMethodType::name).toList();
        }
        if (methods.contains(exchange.getRequestMethod())) {
            if (m.isAnnotationPresent(Authentication.class)) {
                boolean auth = false;
                Authentication annotation = m.getAnnotation(Authentication.class);
                String value = annotation.value();

                List<String> headers = exchange.getRequestHeaders().get("Authorization");
                if (headers != null && !headers.isEmpty()) {
                    for (String header : headers) {
                        if (header == null) continue;
                        if (!header.contains(" ")) break;
                        String[] split = header.split(" ");
                        if (split.length != 2) break; // Invalid header, skip (should be "<type> <value>")
                        String type = split[0];
                        header = split[1];
                        Class<? extends AuthenticationHandler> clazz = annotation.handler();
                        AuthenticationHandler handler = null;
                        for (Constructor<?> constructor : clazz.getConstructors()) {
                            try {
                                if (constructor.getParameterCount() == 0) {
                                    handler = ((AuthenticationHandler) constructor.newInstance());
                                    break;
                                } else if (constructor.getParameterCount() == 1 && constructor.getParameterTypes()[0].equals(String.class)) {
                                    handler = ((AuthenticationHandler) constructor.newInstance(value));
                                    break;
                                }
                            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                        if (handler != null) {
                            if (Arrays.asList(handler.getAllowedTypes()).contains(type))
                                auth = handler.handle(type, header);
                        }
                    }
                }

                if (!auth) {
                    if (errorHandlers.containsKey(HttpErrorCode.UNAUTHORIZED)) {
                        ResponseData response = errorHandlers.get(HttpErrorCode.UNAUTHORIZED).handle(new RequestData(exchange));
                        write(response, exchange);
                    } else {
                        String response = "{\"error\": \"401 Unauthorized\"}";  // default 401 response
                        exchange.sendResponseHeaders(HttpErrorCode.UNAUTHORIZED, response.length());
                        exchange.getResponseBody().write(response.getBytes());
                        exchange.getResponseBody().close();
                    }
                    return false;
                }

            }
            ResponseData response = request.handle(new RequestData(exchange));
            write(response, exchange);
            return true;
        } else {
            if(errorHandlers.containsKey(HttpErrorCode.METHOD_NOT_ALLOWED)) {
                ResponseData response = errorHandlers.get(HttpErrorCode.METHOD_NOT_ALLOWED).handle(new RequestData(exchange));
                write(response, exchange);
            } else {
                String response = "{\"error\": \"405 Method not allowed\"}"; // default 405 response
                exchange.sendResponseHeaders(HttpErrorCode.METHOD_NOT_ALLOWED, response.length());
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().close();
            }
        }
        return false;
    }

    private void write(ResponseData response, HttpExchange exchange) throws IOException {
        String body = response.getBody();
        if(body != null) {
            exchange.sendResponseHeaders(response.getStatusCode(), body.length());
            exchange.getResponseBody().write(body.getBytes());
            exchange.getResponseBody().close();
        } else {
            exchange.sendResponseHeaders(response.getStatusCode(), 0);
            exchange.getResponseBody().close();
        }
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

}
