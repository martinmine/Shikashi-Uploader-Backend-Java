package me.shikashi.img;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import me.shikashi.img.model.UploadedContent;
import me.shikashi.img.model.UploadedContentFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by marti_000 on 28.07.2015.
 */
public class S3ArchitecturalUpgrade {
    private final AmazonS3 s3;
    private final String targetBucket;
    private final String sourceBucket;

    public S3ArchitecturalUpgrade() {
        final BasicAWSCredentials awsCredentials = new BasicAWSCredentials(
                SystemConfiguration.getInstance().getProperty("AWS_ACCESS_KEY"),
                SystemConfiguration.getInstance().getProperty("AWS_SECRET_KEY"));

        this.s3 = new AmazonS3Client(awsCredentials);
        this.targetBucket = "i.shikashi.me"; // The bucket where the uploads will be put
        this.sourceBucket = "shikashi-uploads";

        final Region usWest2 = Region.getRegion(Regions.fromName(SystemConfiguration.getInstance().getProperty("AWS_S3_REGION")));
        s3.setRegion(usWest2);
    }

    public void upgrade() {
        ObjectListing listing = s3.listObjects(sourceBucket);
        List<S3ObjectSummary> summaries = listing.getObjectSummaries();

        while (listing.isTruncated()) {
            listing = s3.listNextBatchOfObjects (listing);
            summaries.addAll (listing.getObjectSummaries());
        }

        boolean skip = true;
        for (S3ObjectSummary summary : summaries) {
            // Get file id
            String name = summary.getKey();
            int id = Integer.valueOf(name);

            if (id == 445) {
                skip = false;
                continue;
            }

            if (skip) {
                continue;
            }

            final UploadedContent upload = UploadedContentFactory.getUpload(id);

            if (upload == null) {
                continue;
            }

            try (InputStream is = getBlob(id)) {
                storeBlob(is, upload.getIdHash(), upload.getMimeType(), upload.getFileSize(), upload.getFileName());
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Transferred " + upload.getFileName() + " (" + id + " / " + upload.getIdHash() + ")");
        }
    }

    public S3ObjectInputStream getBlob(final int uploadId) {
        final S3Object object = s3.getObject(new GetObjectRequest(sourceBucket, String.valueOf(uploadId)));

        if (object != null) {
            return object.getObjectContent();
        } else {
            return null;
        }
    }

    public long storeBlob(final InputStream is, final String uploadId, final String contentType, final long length, final String name) {
        final ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        objectMetadata.setContentLength(length);
        objectMetadata.setContentDisposition(String.format("inline; filename=\"%s\"", name));
        s3.putObject(new PutObjectRequest(this.targetBucket, uploadId, is, objectMetadata));

        return objectMetadata.getContentLength();
    }
}
