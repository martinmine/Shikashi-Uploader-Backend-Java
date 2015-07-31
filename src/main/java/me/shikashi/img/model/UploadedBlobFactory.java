package me.shikashi.img.model;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.*;
import me.shikashi.img.SystemConfiguration;
import org.apache.commons.io.FilenameUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Manages interaction with MongoDB where the contents of the file uploads are stored.
 */
public class UploadedBlobFactory {
    private static final UploadedBlobFactory INSTANCE = new UploadedBlobFactory();

    private final AmazonS3 s3;
    private final String uploadBuckedName;

    /**
     * @return Singleton instance.
     */
    public static UploadedBlobFactory getInstance() {
        return INSTANCE;
    }

    private UploadedBlobFactory() {
        final BasicAWSCredentials awsCredentials = new BasicAWSCredentials(
                SystemConfiguration.getInstance().getProperty("AWS_ACCESS_KEY"),
                SystemConfiguration.getInstance().getProperty("AWS_SECRET_KEY"));

        this.s3 = new AmazonS3Client(awsCredentials);
        this.uploadBuckedName = SystemConfiguration.getInstance().getProperty("AWS_UPLOAD_BUCKET");

        final Region usWest2 = Region.getRegion(Regions.fromName(SystemConfiguration.getInstance().getProperty("AWS_S3_REGION")));
        s3.setRegion(usWest2);
    }

    /**
     * Gets uploaded content.
     * @param uploadId Id of upload.
     * @return Instance of {@code GridFSDBFile} if upload exists, otherwise {@code null}.
     */
    public S3ObjectInputStream getBlob(final String uploadId) {
        final S3Object object = s3.getObject(new GetObjectRequest(uploadBuckedName, String.valueOf(uploadId)));

        if (object != null) {
            return object.getObjectContent();
        } else {
            return null;
        }
    }

    public S3ObjectInputStream getBlob(final int uploadId) {
        return getBlob(String.valueOf(uploadId));
    }

    /**
     * Deletes an upload.
     * @param uploadId Id of upload.
     */
    public void deleteBlob(final String uploadId) {
        s3.deleteObject(uploadBuckedName, uploadId);
    }

    /**
     * Stores a new upload.
     * @param is Input stream of the upload.
     * @param uploadId Id of the upload.
     * @param contentType Content-type of the upload.
     * @return Total amount of bytes that was stored (size of the uploaded content).
     */
    public long storeBlob(final InputStream is, final String uploadId, final String contentType, final long length, final String name) {
        final ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        objectMetadata.setContentLength(length);
        objectMetadata.setContentDisposition(String.format("inline; filename=\"%s\"", name));
        s3.putObject(new PutObjectRequest(this.uploadBuckedName, uploadId, is, objectMetadata));

        return objectMetadata.getContentLength();
    }

    public void createBlobAlias(final String uploadId, final String contentType, final String uploadName) {
        if (!uploadName.contains(".")) {
            return;
        }

        final String extension = FilenameUtils.getExtension(uploadName);
        final ObjectMetadata objectMetadata = new ObjectMetadata();
        final InputStream x = new ByteArrayInputStream(new byte[] {});
        objectMetadata.setContentType(contentType);
        objectMetadata.setContentLength(0);
        objectMetadata.setHeader(Headers.REDIRECT_LOCATION, String.format("/%s", uploadId));
        s3.putObject(new PutObjectRequest(this.uploadBuckedName, String.format("%s.%s", uploadId, extension), x, objectMetadata));
    }
}
