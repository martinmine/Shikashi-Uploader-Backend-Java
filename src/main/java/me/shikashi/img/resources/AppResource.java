package me.shikashi.img.resources;

import org.restlet.ext.gson.GsonRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * Created by marti_000 on 28.07.2015.
 */
public class AppResource extends ServerResource {
    @Get
    public GsonRepresentation<String> getVersion() {
        return new GsonRepresentation<String>("Hi");
    }
}
