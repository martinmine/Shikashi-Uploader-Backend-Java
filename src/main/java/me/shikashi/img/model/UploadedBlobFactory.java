package me.shikashi.img.model;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

import java.io.InputStream;

/**
 * Manages interaction with MongoDB where the contents of the file uploads are stored.
 */
public class UploadedBlobFactory {
    private static final UploadedBlobFactory INSTANCE = new UploadedBlobFactory();

    private final MongoClient client;
    private final DB db;
    private final GridFS gridFS;

    /**
     * @return Singleton instance.
     */
    public static UploadedBlobFactory getInstance() {
        return INSTANCE;
    }

    private UploadedBlobFactory() {
        this.client = new MongoClient("localhost", 27017);
        this.db = client.getDB("shikashi");
        this.gridFS = new GridFS(db, "uploads");
    }

    /**
     * Gets uploaded content.
     * @param uploadId Id of upload.
     * @return Instance of {@code GridFSDBFile} if upload exists, otherwise {@code null}.
     */
    public GridFSDBFile getBlob(final int uploadId) {
        return gridFS.findOne(String.valueOf(uploadId));
    }

    /**
     * Deletes an upload.
     * @param uploadId Id of upload.
     */
    public void deleteBlob(final int uploadId) {
        final GridFSDBFile upload = getBlob(uploadId);
        if (upload != null) {
            gridFS.remove(upload);
        }
    }

    /**
     * Stores a new upload.
     * @param is Input stream of the upload.
     * @param uploadId Id of the upload.
     * @param contentType Content-type of the upload.
     * @return Total amount of bytes that was stored (size of the uploaded content).
     */
    public long storeBlob(final InputStream is, final int uploadId, final String contentType) {
        final GridFSInputFile storedFile = gridFS.createFile(is);
        storedFile.setFilename(String.valueOf(uploadId));
        storedFile.setContentType(contentType);
        storedFile.save();

        return storedFile.getLength();
    }
}
