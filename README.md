# WebAPI

![Github Release](https://img.shields.io/github/v/release/JoshiCodes/WebAPI?include_prereleases)
<a href="https://repo.joshicodes.de/javadoc/releases/de/joshicodes/webapi/1.2/raw/index.html"><img src="https://repo.joshicodes.de/api/badge/latest/releases/de/joshicodes/webapi?color=4D7A97&name=Javadoc&prefix=v"></a>

A simple-to-use and small web api for your projects.
_Mainly used for my own projects._

## Installation

You can install the WebAPI using maven.
You can also download the newest version from the [releases](https://github.com/JoshiCodes/WebAPI/releases).

```xml
<dependency>
    <groupId>de.joshicodes</groupId>
    <artifactId>webapi</artifactId>
    <version>1.3.3</version>
</dependency>
```

If you want to use my maven repository, you can add the following repo to your `pom.xml`:

```xml
<repository>
    <id>joshicodes-de-releases</id>
    <name>JoshiCodes.de Repository</name>
    <url>https://repo.joshizockt.de/releases</url>
</repository>
```

## Usage
To create a new WebAPI instance, you can use the following code:

```java
        WebserverBuilder builder = new WebserverBuilder();
        builder.setPort(8080);
        builder.build();
```
You need to set a port for the webserver to run on. You can also set a custom path for the webserver to run on. The default path is `/`.
To set a custom path, you can use `WebserverBuilder#setPath("/custom/path");`.
You can also set a custom host for the webserver to run on. The default host is `0.0.0.0`. To set a custom host, you can use `WebserverBuilder#setHost(String)`

To add a new route, create a new class that extends the `Route` class and add it to the builder using `WebserverBuilder#addRoute(Route)`.<br>
The `Route` class has a constructor that takes a path as a parameter. The path is the path that the route will be available on.

```java
        // This will create a new route that is available on /testRoute
        builder.addRoute(
                "/testRoute",
                new Route() {
                    @Override
                    public ResponseData handle(RequestData request) {
                        // Do something and return a ResponseData object
                    }
                }
        );
```
You can also add multiple routes at once using `builder.addRoutes(Route...)`.

To handle multiple routes with the same base path, you can use a `Router`.
To register a new Router, you can use `WebserverBuilder#addRouter(Router)`.
The `Router` class has a constructor that takes a path as a parameter. The path is the base path that the router will be available on.

```java
        // This will create a new router without any path, that will be set by registering it to the builder
        // This router will be available on /testRouter
        Router router = new Router();

        // This will create a new route that is available on /testRouter
        // If there is no Route for "/" in the Router, "/testRouter" will return a 404 
        router.addRoute(
                "/",
                new Route() {
                    @Override
                    public ResponseData handle(RequestData request) {
                        // Do something and return a ResponseData object
                    }
                }
        );
        
        // This will create a new route that is available on /testRouter/testRoute
        router.addRoute(
                "/testRoute",
                new Route() {
                    @Override
                    public ResponseData handle(RequestData request) {
                        // Do something and return a ResponseData object
                    }
                }
        );
        
        // This will register the router to the builder
        builder.addRouter("/testRouter", router);
```

The `Route` class has a method called `handle(RequestData request)`. This method is called when a request is made to the route. The `RequestData` object contains information about the request. The `handle` Method should return a ResponseData object.
You can create a new ResponseData object using the `ResponseData.Builder` class.

```java
        return new ResponseData.Builder()
        .setStatusCode(200)
        .setBody("Hello World!")
        .setContentType("text/plain");
        .build();
```
You can also use `ResponseData#from(int code, String body)` to create a new Builder object.

To redirect the client, return a `ResponseModificationData` Object, which you can create by using `ResponseData#redirect(String)`. This will redirect the client to the specified path.

<br><br>

## Error Handling
You can add error handlers to your routes using `WebserverBuilder#addErrorHandler(int, Route)`. The int is the code that should be handled by the specified Route. You can find common Error Codes in the `HttpErrorCode` class.
For this Route, Method and Authentication annotations will not be checked.

## Methods
You can specify the method that a route should be available on using the `@HttpMethod` annotation.

```java
        @HttpMethod(HttpMethodType.GET)
        public ResponseData handle(RequestData request) {
            // Do something and return a ResponseData object
        }
```
You can specify multiple methods by using `@HttpMethods({METHOD TYPES HERE})`.

<br><br>

## Parameters
At the moment you can only read parameters from the request URI (GET).
To get a Parameter from the request URI, use `RequestData#getParameter(String name)`. You can also provide a fallback value, which is used in case the parameter is not present in the request URI.

To read parameters from a POST request, you can use `RequestData#getPostParameters()`. This will return a `HashMap<String, String>` containing the data of the request.
If this method throws an `UnknownContentTypeException`, your Content-Type header is not supported.
For now, only `application/x-www-form-urlencoded` and `application/json` are supported.
You can also read the pure body of the request using `RequestData#getBody()`. This will return a `String` containing the body of the request which you can then parse.

<br><br>

## Authentication
You can add authentication to your routes using the `@Authentication` annotation.
The `@Authentication` annotation takes an AuthenticationHandler and a String as parameters. The String is the required value that the request should contain.
At the moment, there are two AuthenticationHandlers available. The `BasicAuthenticationHandler` and the `BearerAuthenticationHandler`.
The `BasicAuthenticationHandler` requires a Basic Authentication header with the value `username:password`.
The `BearerAuthenticationHandler` requires a Bearer token as value.
```java
        // This Request requires a Bearer token with the value "testToken"
        @Authentication(handler = BearerAuthenticationHandler.class, value="testToken")
        public ResponseData handle(RequestData request) {
            // Do something and return a ResponseData object
        }

        // This Request requires a Basic Authentication header with the value "username:password"
        @Authentication(handler = BasicAuthenticationHandler.class, value="username:password")
        public ResponseData handle(RequestData request) {
            // Do something and return a ResponseData object
        }
```
As this is not the best way to do authentication, you can also create your own AuthenticationHandler. To do this, you need to create a new class that extends the `AuthenticationHandler` class.
For more information, see the [AuthenticationHandler](https://github.com/JoshiCodes/WebAPI/blob/master/src/main/java/de/joshicodes/webapi/auth/handler/AuthenticationHandler.java) class or have a look at one of the [existing AuthenticationHandlers](https://github.com/JoshiCodes/WebAPI/tree/master/src/main/java/de/joshicodes/webapi/auth/handler).

```java
        public class MyAuthenticationHandler extends AuthenticationHandler {
    
            public MyAuthenticationHandler() {
                super(null, "Bearer"); // This is the type of the authentication header. You can allow multiple types ("Bearer", "Basic") but your handler should be able to handle all of them.
            }
    
            @Override
            public boolean handle(String type, String value, HttpExchange exchange) {
                // type is the type of the authentication header
                // value is the read value from the request
                // For the example above, value would be "testToken" and type would be "Bearer"
                // Do something and return true if the request is authenticated
            }
        }
```

## Examples
You can find examples in the [examples folder](https://github.com/JoshiCodes/WebAPI/tree/master/examples).

## License
This project is licensed under the [MIT License](https://github.com/JoshiCodes/WebAPI/blob/master/LICENSE)
