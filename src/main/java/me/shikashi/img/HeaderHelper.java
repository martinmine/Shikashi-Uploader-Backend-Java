package me.shikashi.img;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Header;
import org.restlet.util.Series;

/**
 * Helper class for getting headers of a response or a request.
 */
public class HeaderHelper {

    private static final String HEADER_ATTRIBUTE = "org.restlet.http.headers";
    private HeaderHelper() {
    }

    /**
     * Gets headers from a response.
     * @param response Response to get headers to.
     * @return Series of the headers. Added entries will be added to the response.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Series<Header> getResponseHeaders(Response response) {
        /**
         * Thanks to http://blog.arc90.com/2008/09/15/custom-http-response-headers-with-restlet/ for this code snippet
         */
        Series<Header> responseHeaders = (Series<Header>) response.getAttributes().get(HEADER_ATTRIBUTE);
        if (responseHeaders == null) {
            responseHeaders = new Series(Header.class);
            response.getAttributes().put(HEADER_ATTRIBUTE, responseHeaders);
        }

        return responseHeaders;
    }

    /**
     * Gets the request headers for an incoming request.
     * @param request The incoming request.
     * @return A Series of all the headers.
     */
    @SuppressWarnings("unchecked")
    public static Series<Header> getRequestHeaders(Request request) {
        return (Series<Header>)request.getAttributes().get(HEADER_ATTRIBUTE);
    }

    public static String getHeaderValue(final String fieldName, final Request request) {
        return getRequestHeaders(request).getValues(fieldName);
    }
}
