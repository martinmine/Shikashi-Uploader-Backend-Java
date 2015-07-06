package me.shikashi.img.resources;

import me.shikashi.img.SystemConfiguration;
import me.shikashi.img.model.UploadedContentFactory;
import me.shikashi.img.model.UploadedContent;
import me.shikashi.img.representations.RepresentationFactory;
import me.shikashi.img.representations.annotations.ImageUploadRepresentation;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.restlet.Request;
import org.restlet.data.Header;
import org.restlet.data.Status;
import org.restlet.ext.fileupload.RestletFileUpload;
import org.restlet.ext.gson.GsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
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
        final FileItem item = new RestletFileUpload(new DiskFileItemFactory())
                .parseRepresentation(representation)
                .stream()
                .filter(p -> !p.isFormField())
                .findFirst()
                .get();

        if (item == null) {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return null;
        }

        final InputStream inputStream = item.getInputStream();
        final int fileSize = inputStream.available();

        if (fileSize > MAX_FILE_SIZE) {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return null;
        }

        final UploadedContent upload = UploadedContentFactory.storeImage(inputStream, fileSize, item.getContentType(), getIpAddress(), item.getName(), getUser());
        return RepresentationFactory.makeRepresentation(upload, ImageUploadRepresentation.class);
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
