package de.joshicodes.webapi.auth;

import de.joshicodes.webapi.auth.handler.AuthenticationHandler;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Authentication {

    /**
     * The authentication handler to use for authentication<br>
     * Can be a custom handler or one of the default handlers<br>
     * To create a custom handler, implement the {@link AuthenticationHandler} class<br>
     *
     * @see de.joshicodes.webapi.auth.handler.BearerAuthenticationHandler
     * @see de.joshicodes.webapi.auth.handler.BasicAuthenticationHandler
     * @see de.joshicodes.webapi.auth.handler.AuthenticationHandler
     * 
     * @return The authentication handler to use
     */
    Class<? extends AuthenticationHandler> handler();

    /**
     * The value to use for authentication<br>
     * If the authentication handler does not require a value, this can be left empty or null<br>
     * 
     * @see de.joshicodes.webapi.auth.handler.AuthenticationHandler#handle(String, String, com.sun.net.httpserver.HttpExchange)
     * 
     * @return The value to use for authentication
     */
    @Nullable
    String value();

}
