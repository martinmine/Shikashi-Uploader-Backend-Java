package me.shikashi.img.resources;

import me.shikashi.img.model.UploadedContent;
import me.shikashi.img.representations.RepresentationFactory;
import me.shikashi.img.representations.annotations.ImageUploadRepresentation;
import org.restlet.ext.gson.GsonRepresentation;
import org.restlet.resource.Get;

import java.util.List;

/**
 * Resource for getting the uploads a user has made.
 */
public class UserUploadsResource extends AuthenticatedServerResource {
    @Get
    public GsonRepresentation<List<UploadedContent>> getUploads() {
        return RepresentationFactory.makeRepresentation(getUser().getUploads(), ImageUploadRepresentation.class);
    }
}
