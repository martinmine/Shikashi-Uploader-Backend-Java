package me.shikashi.img.resources;

import me.shikashi.img.model.UploadedContent;
import me.shikashi.img.model.UploadedContentFactory;
import org.restlet.data.Status;
import org.restlet.resource.Delete;

/**
 * Enables deletion of a file upload. This resource is decoupled from {@code UploadResource} as the DELETE
 * requires that the user has sufficient rights towards the resource.
 */
public class DeleteUploadResource extends AuthenticatedServerResource {

    /**
     * Deletes an upload if the user is the owner of the upload.
     */
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

        UploadedContentFactory.deleteUpload(content);
        setStatus(Status.SUCCESS_OK);
    }

    private UploadedContent getUploadedContent() {
        final String key = ((String) getRequest().getAttributes().get("key")).split("\\.")[0];
        final UploadedContent upload = UploadedContentFactory.getUpload(key);

        if (upload == null) {
            setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            return null;
        } else {
            return upload;
        }
    }
}
