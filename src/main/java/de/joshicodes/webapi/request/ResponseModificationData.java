package de.joshicodes.webapi.request;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 * This class is used when the Response returned by the server should end up being modified.
 * This is used for example when the server should redirect to another page.
 * @see ResponseData
 */
public abstract class ResponseModificationData extends ResponseData {

    /**
     * Returns the location to redirect to or null if the response should not be redirected
     * Has to be a valid URL
     * @return The location to redirect to
     */
    abstract @Nullable public String getRedirectLocation();


    // Override all methods from ResponseData to return null or -1

    @Override
    public String getBody() {
        return null;
    }

    @Override
    public String getHeader(String key) {
        return null;
    }

    @Override
    public HashMap<String, String> getHeaders() {
        return null;
    }

    @Override
    public int getStatusCode() {
        return -1;
    }

    @Override
    public String getContentType() {
        return null;
    }

}
