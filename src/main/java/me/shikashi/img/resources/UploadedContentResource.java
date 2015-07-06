package me.shikashi.img.resources;

import me.shikashi.img.database.DatabaseDeletion;
import me.shikashi.img.database.HibernateUtil;
import me.shikashi.img.model.UploadedContentFactory;
import me.shikashi.img.model.UploadedContent;
import org.apache.commons.io.IOUtils;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.OutputRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by marti_000 on 07.06.2015.
 */
public class UploadedContentResource extends ServerResource {
    private static final Logger LOGGER = Logger.getLogger(UploadedContentResource.class.getSimpleName());

    @Get
    public Representation getImage() {
        final UploadedContent upload = getUploadedContent();

        if (upload == null) {
            return null;
        }

        final Blob blob = upload.getContent();
        final String contentType = upload.getMimeType();

        return new OutputRepresentation(MediaType.valueOf(contentType)) {
            public void write(OutputStream os) {
                try {
                    IOUtils.copy(blob.getBinaryStream(), os);
                } catch (IOException | SQLException ex) {
                    LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
        };
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
