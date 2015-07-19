package me.shikashi.img;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.FileRepresentation;
import org.restlet.representation.Representation;
import org.restlet.service.StatusService;

import java.io.*;

/**
 * Handles the sending of error pages.
 */
public class ShikashiStatusService extends StatusService {

    /**
     * Overrides the default 404 page and sends the contents of 404.html instead.
     * @param status Current status.
     * @param request Current request.
     * @param response Current response.
     * @return A representation of 404.html if the status code is 404. Otherwise, return default representation.
     */
    @Override
    public Representation toRepresentation(Status status, Request request, Response response) {
        if (status.getCode() == 404) {
            return new FileRepresentation(new File("404.html"), MediaType.TEXT_HTML);
        }

        return super.toRepresentation(status, request, response);
    }
}
