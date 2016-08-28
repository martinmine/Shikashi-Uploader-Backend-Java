package me.shikashi.img.resources;

import me.shikashi.img.model.UploadedContentFactory;
import me.shikashi.img.model.UploadedContent;
import org.restlet.data.Status;
import org.restlet.resource.ServerResource;

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
   /* @Get
    public Representation getFile() {
        /*final UploadedContent upload = getUploadedContent();

        if (upload == null) {
            return null;
        }

        final String contentType = upload.getMimeType();

        upload.incrementViewCount();
        try (DatabaseUpdate<UploadedContent> query = HibernateUtil.getInstance().update()) {
            query.update(upload);
        }

        getResponse().getCacheDirectives().add(CacheDirective.publicInfo());
        getResponse().getCacheDirectives().add(CacheDirective.maxAge(31536000));
        getResponse().setAge(0);

      //  HeaderHelper.getResponseHeaders(getResponse()).add("ETag", upload.getIdHash());

        OutputRepresentation representation =  new OutputRepresentation(MediaType.valueOf(contentType)) {
            public void write(OutputStream os) {
                final S3ObjectInputStream file = UploadedBlobFactory.getInstance().getBlob(upload.getIdHash());

                if (file == null) {
                    return;
                }

                try {
                    IOUtils.copy(file, os);
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
        return null;
    }*/

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
