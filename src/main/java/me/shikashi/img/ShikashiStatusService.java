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
 * Created by Martin on 12.07.2015.
 */
public class ShikashiStatusService extends StatusService {
    @Override
    public Representation toRepresentation(Status status, Request request, Response response) {
        if (status.getCode() == 404) {
            return new FileRepresentation(new File("404.html"), MediaType.TEXT_HTML);
        }

        return super.toRepresentation(status, request, response);
    }
}
