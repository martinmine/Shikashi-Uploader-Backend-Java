package me.shikashi.img.resources;

import me.shikashi.img.HeaderHelper;
import me.shikashi.img.SystemConfiguration;
import me.shikashi.img.database.DatabaseUpdate;
import me.shikashi.img.database.HibernateUtil;
import me.shikashi.img.model.UploadedBlobFactory;
import me.shikashi.img.model.UploadedContentFactory;
import me.shikashi.img.model.UploadedContent;
import me.shikashi.img.representations.RepresentationFactory;
import me.shikashi.img.representations.annotations.FileUploadRepresentation;
import me.shikashi.img.uploading.RestletRequestContextAdapter;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.restlet.data.Header;
import org.restlet.ext.gson.GsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.util.Series;

import java.io.IOException;
import java.io.InputStream;

/**
 * Resource for handling new file uploads.
 */
public class UploadResource extends AuthenticatedServerResource {
    private static final int MAX_FILE_SIZE = Integer.valueOf(SystemConfiguration.getInstance().getProperty("MAX_FILE_SIZE"));

    /**
     * Stores a new file upload for the user. First, the metadata of the file is stored. The file is then stored.
     * @param representation Representation of the current HTTP request.
     * @return A representation of the file upload. {@code null} if no upload was received.
     * @throws FileUploadException
     * @throws IOException
     */
    @Post
    public GsonRepresentation<UploadedContent> uploadImage(Representation representation) throws FileUploadException, IOException {
        final ServletFileUpload upload = new ServletFileUpload();
        upload.setSizeMax(MAX_FILE_SIZE);

        final FileItemIterator iterator = upload.getItemIterator(new RestletRequestContextAdapter(representation));

        if (!iterator.hasNext()) {
            return null;
        }

        final FileItemStream item = iterator.next();
        final InputStream stream = item.openStream();

        final UploadedContent uploadedContent = UploadedContentFactory.persistUploadMetadata(item.getContentType(), getIpAddress(), item.getName(), getUser());
        final long fileSize = UploadedBlobFactory.getInstance().storeBlob(stream, uploadedContent.getId(), item.getContentType());

        uploadedContent.setFileSize(fileSize);

        try (DatabaseUpdate<UploadedContent> query = HibernateUtil.getInstance().update()) {
            query.update(uploadedContent);
        }

        return RepresentationFactory.makeRepresentation(uploadedContent, FileUploadRepresentation.class);
    }

    private String getIpAddress() {
        final Series<Header> headers = HeaderHelper.getRequestHeaders(getRequest());

        // TODO: Figure out what LiteSpeed does with the ip
        if (headers.getValues("HTTP_CF_CONNECTING_IP") != null) {
            return headers.getValues("HTTP_CF_CONNECTING_IP");
        } else {
            return getRequest().getClientInfo().getAddress();
        }
    }
}
