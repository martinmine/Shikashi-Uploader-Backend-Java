package me.shikashi.img.routing;

import me.shikashi.img.HeaderHelper;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Header;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.routing.Router;
import org.restlet.util.Series;

/**
 * Creates a router that allows any website to use this API by modifying the HTTP access-control-allow.* header.
 */
public class RouterFactory {
    private RouterFactory() {
    }

    /**
     * Creates a router for usage with routing resources.
     * @param context The context.
     * @return A new Router.
     */
    public static Router makeRouter(Context context) {
        return new DevRouter(context);
    }

    public static class DevRouter extends Router {
        /**
         * {@inheritDoc}
         */
        public DevRouter(Context context) {
            super(context);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doHandle(Restlet next, Request request, Response response) {
            Series<Header> responseHeaders = HeaderHelper.getResponseHeaders(response);
            responseHeaders.add("Access-Control-Allow-Origin", "*");

            if (request.getMethod() == Method.OPTIONS) {
                responseHeaders.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
                responseHeaders.add("Access-Control-Allow-Headers", "Authorization");
                response.setStatus(Status.SUCCESS_OK);
            } else {
                super.doHandle(next, request, response);
            }
        }
    }
}
