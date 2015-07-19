package me.shikashi.img.resources;

import me.shikashi.img.SystemConfiguration;
import me.shikashi.img.database.DatabaseUpdate;
import me.shikashi.img.database.HibernateUtil;
import me.shikashi.img.model.UploadedBlobFactory;
import me.shikashi.img.model.UploadedContentFactory;
import me.shikashi.img.model.UploadedContent;
import me.shikashi.img.representations.RepresentationFactory;
import me.shikashi.img.representations.annotations.ImageUploadRepresentation;
import me.shikashi.img.uploading.RestletRequestContextAdapter;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.restlet.Request;
import org.restlet.data.Header;
import org.restlet.data.Status;
import org.restlet.ext.fileupload.RestletFileUpload;
import org.restlet.ext.gson.GsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.util.Series;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by marti_000 on 07.06.2015.
 */
public class UploadResource extends AuthenticatedServerResource {
    private static final int MAX_FILE_SIZE = Integer.valueOf(SystemConfiguration.getInstance().getProperty("MAX_FILE_SIZE"));

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

        final UploadedContent uploadedContent = UploadedContentFactory.storeImage(item.getContentType(), getIpAddress(), item.getName(), getUser());
        final long fileSize = UploadedBlobFactory.getInstance().storeBlob(stream, uploadedContent.getId(), item.getContentType());

        uploadedContent.setFileSize(fileSize);

        try (DatabaseUpdate<UploadedContent> query = HibernateUtil.getInstance().update()) {
            query.update(uploadedContent);
        }

        return RepresentationFactory.makeRepresentation(uploadedContent, ImageUploadRepresentation.class);
    }

    private String getIpAddress() {
        final Series<Header> headers = getRequestHeaders(getRequest());

        if (headers.getValues("HTTP_CF_CONNECTING_IP") != null) {
            return headers.getValues("HTTP_CF_CONNECTING_IP");
        } else {
            return getRequest().getClientInfo().getAddress();
        }
    }

    private static final String HEADER_ATTRIBUTE = "org.restlet.http.headers";

    /**
     * Gets the request headers for an incoming request.
     * @param request The incoming request.
     * @return A Series of all the headers.
     */
    @SuppressWarnings("unchecked")
    public static Series<Header> getRequestHeaders(Request request) {
        return (Series<Header>)request.getAttributes().get(HEADER_ATTRIBUTE);
    }
}
