package me.shikashi.img.uploading;

import me.shikashi.img.HeaderHelper;
import org.apache.commons.fileupload.RequestContext;
import org.restlet.Request;
import org.restlet.representation.Representation;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by marti_000 on 19.07.2015.
 */
public class RestletRequestContextAdapter implements RequestContext {
    private Representation representation;

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
        //return Integer.valueOf(HeaderHelper.getHeaderValue("Content-Length", request));
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return representation.getStream();
    }
}
