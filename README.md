# WebAPI

![Github Release](https://img.shields.io/github/v/release/JoshiCodes/WebAPI?include_prereleases)

A simple-to-use and small web api for your projects.
_Mainly used for my own projects._

## Installation

You can install the WebAPI using maven.
You can also download the newest version from the [releases](https://github.com/JoshiCodes/WebAPI/releases).

```xml
<dependency>
    <groupId>de.joshicodes</groupId>
    <artifactId>webapi</artifactId>
    <version>1.0-alpha.3</version>
</dependency>
```

## Usage
To create a new WebAPI instance, you can use the following code:

```java
        Webserver.Builder builder = new Webserver.Builder();
        builder.setPort(8080);
        builder.build();
```
You need to set a port for the webserver to run on. You can also set a custom path for the webserver to run on. The default path is `/`.
To set a custom path, you can use `builder.setPath("/custom/path");`.
You can also set a custom host for the webserver to run on. The default host is `0.0.0.0`. To set a custom host, you can use `builder.setHost(String)`

To add a new route, create a new class that extends the `Route` class and add it to the builder using `builder.addRoute(Route)`.<br>
The `Route` class has a constructor that takes a path as a parameter. The path is the path that the route will be available on.

```java
        builder.addRoute(
                // You can also create a new Route object and add it here
        
                // This will create a new route that is available on /testRoute
                new Route("/testRoute") {
                    @Override
                    public ResponseData handle(RequestData request) {
                        // Do something and return a ResponseData object
                    }
                }
                
        );
```
You can also add multiple routes at once using `builder.addRoutes(Route...)`.

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

<br><br>

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

You cannot read parameters from the request body yet. This will be added in a future update.

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
As this is not the best way to do authentication, you can also create your own AuthenticationHandler. To do this, you need to create a new class that implements the `AuthenticationHandler` interface.

```java
        public class MyAuthenticationHandler implements AuthenticationHandler {
    
            public MyAuthenticationHandler() {
                super("Bearer"); // This is the type of the authentication header. You can allow multiple types ("Bearer", "Basic") but your handler should be able to handle all of them.
            }
    
            @Override
            public boolean handle(String type, String value) {
                // type is the type of the authentication header
                // value is the read value from the request
                // For the example above, value would be "testToken" and type would be "Bearer"
                // Do something and return true if the request is authenticated
            }
        }
```
