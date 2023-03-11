package de.joshicodes.webapi.request;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface HttpMethod {

    HttpMethodType[] value();

}
