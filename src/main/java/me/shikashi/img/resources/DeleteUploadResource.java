package me.shikashi.img.resources;

import me.shikashi.img.model.UploadedContent;
import me.shikashi.img.model.UploadedContentFactory;
import org.restlet.data.Status;
import org.restlet.resource.Delete;

/**
 * Created by marti_000 on 17.06.2015.
 */
public class DeleteUploadResource extends AuthenticatedServerResource {
    @Delete
    public void deleteUpload() {
        final UploadedContent content = getUploadedContent();

        if (content == null) {
            setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            return;
        }

        if (content.getOwner().getId() != getUser().getId()) {
            setStatus(Status.CLIENT_ERROR_FORBIDDEN);
            return;
        }

        UploadedContentFactory.deleteImage(content);
        setStatus(Status.SUCCESS_OK);
    }

    private UploadedContent getUploadedContent() {
        final String key = ((String) getRequest().getAttributes().get("key")).split("\\.")[0];
        final UploadedContent upload = UploadedContentFactory.getImageUpload(key);

        if (upload == null) {
            setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            return null;
        } else {
            return upload;
        }
    }
}
