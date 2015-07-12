package me.shikashi.img;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.FileRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Resource;
import org.restlet.service.StatusService;

import java.io.File;

/**
 * Created by Martin on 12.07.2015.
 */
public class ShikashiStatusService extends StatusService {
    @Override
    public Representation toRepresentation(Status status, Resource resource) {
        return new FileRepresentation(new File("/404.html"), MediaType.TEXT_HTML);
    }
}
