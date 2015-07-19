package me.shikashi.img.model;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

import java.io.InputStream;

/**
 * Created by marti_000 on 19.07.2015.
 */
public class UploadedBlobFactory {
    private final MongoClient client;
    private final DB db;
    private final GridFS gridFS;

    private static final UploadedBlobFactory INSTANCE = new UploadedBlobFactory();

    public static UploadedBlobFactory getInstance() {
        return INSTANCE;
    }

    private UploadedBlobFactory() {
        this.client = new MongoClient("localhost", 27017);
        this.db = client.getDB("shikashi");
        this.gridFS = new GridFS(db, "uploads");
    }

    public GridFSDBFile getBlob(final int uploadId) {
        return gridFS.findOne(String.valueOf(uploadId));
    }

    public void deleteBlob(final int uploadId) {
        final GridFSDBFile upload = getBlob(uploadId);
        if (upload != null) {
            gridFS.remove(upload);
        }
    }

    public void storeBlob(final InputStream is, final int uploadId, final String contentType) {
        final GridFSInputFile storedFile =  gridFS.createFile(is);
        storedFile.setFilename(String.valueOf(uploadId));
        storedFile.setContentType(contentType);

        storedFile.save();
    }
}
