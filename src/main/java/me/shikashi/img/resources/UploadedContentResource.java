package me.shikashi.img.resources;

import com.mongodb.gridfs.GridFSDBFile;
import me.shikashi.img.database.DatabaseUpdate;
import me.shikashi.img.database.HibernateUtil;
import me.shikashi.img.model.UploadedBlobFactory;
import me.shikashi.img.model.UploadedContentFactory;
import me.shikashi.img.model.UploadedContent;
import org.restlet.data.Disposition;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.OutputRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Resource for accessing the uploaded content.
 */
public class UploadedContentResource extends ServerResource {
    private static final Logger LOGGER = Logger.getLogger(UploadedContentResource.class.getSimpleName());

    /**
     * Gets a file from the database.
     * @return A representation that streams the content from the data store.
     */
    @Get
    public Representation getFile() {
        final UploadedContent upload = getUploadedContent();

        if (upload == null) {
            return null;
        }

        final String contentType = upload.getMimeType();

        upload.incrementViewCount();
        try (DatabaseUpdate<UploadedContent> query = HibernateUtil.getInstance().update()) {
            query.update(upload);
        }

        OutputRepresentation representation =  new OutputRepresentation(MediaType.valueOf(contentType)) {
            public void write(OutputStream os) {
                final GridFSDBFile file = UploadedBlobFactory.getInstance().getBlob(upload.getId());

                if (file == null) {
                    return;
                }
                try {
                    file.writeTo(os);
                } catch (IOException ex) {
                    LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
        };

        representation.setSize(upload.getFileSize());

        if (upload.getFileName().endsWith(".swf") || "application/x-shockwave-flash".equals(upload.getMimeType())) {
            final Disposition disposition = new Disposition(Disposition.TYPE_ATTACHMENT);
            disposition.setFilename(upload.getFileName());
            representation.setDisposition(disposition);
        } else {
            final Disposition disposition = new Disposition(Disposition.TYPE_INLINE);
            disposition.setFilename(upload.getFileName());
            representation.setDisposition(disposition);
        }


        return representation;
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
