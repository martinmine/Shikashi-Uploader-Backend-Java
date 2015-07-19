package me.shikashi.img.resources;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import me.shikashi.img.database.DatabaseUpdate;
import me.shikashi.img.database.HibernateUtil;
import me.shikashi.img.model.UploadedBlobFactory;
import me.shikashi.img.model.UploadedContentFactory;
import me.shikashi.img.model.UploadedContent;
import org.apache.commons.io.IOUtils;
import org.restlet.data.Disposition;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.OutputRepresentation;
import org.restlet.representation.Representation;
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

        final Disposition disposition = new Disposition(Disposition.TYPE_INLINE);
        disposition.setFilename(upload.getFileName());
        representation.setDisposition(disposition);

        return representation;
    }

    private boolean forceDownload(final MediaType mediaType) {
        if (mediaType.getMainType().equals(MediaType.IMAGE_ALL.getMainType())) {
            return false;
        } else if (mediaType.getMainType().equals(MediaType.VIDEO_ALL.getMainType())) {
            return false;
        } else if (mediaType.getMainType().equals(MediaType.AUDIO_ALL.getMainType())) {
            return false;
        }

        return true;
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
