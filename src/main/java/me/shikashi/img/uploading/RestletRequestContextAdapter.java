package me.shikashi.img.uploading;

import org.apache.commons.fileupload.RequestContext;
import org.restlet.representation.Representation;

import java.io.IOException;
import java.io.InputStream;

/**
 * Adapter for requests for the Apache file uploading library.
 */
public class RestletRequestContextAdapter implements RequestContext {
    private Representation representation;

    /**
     * Creates a new instance of the adapter.
     * @param representation Representation of the incoming data/request.
     */
    public RestletRequestContextAdapter(Representation representation) {
        this.representation = representation;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public String getContentType() {
        return representation.getMediaType().getName();
    }

    @Override
    public int getContentLength() {
        return (int)representation.getAvailableSize();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return representation.getStream();
    }
}
