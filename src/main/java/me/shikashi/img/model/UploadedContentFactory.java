package me.shikashi.img.model;

import me.shikashi.img.SystemConfiguration;
import me.shikashi.img.database.DatabaseDeletion;
import me.shikashi.img.database.DatabaseInsertion;
import me.shikashi.img.database.DatabaseQuery;
import me.shikashi.img.database.HibernateUtil;
import org.apache.commons.io.FilenameUtils;
import org.hashids.Hashids;
import org.hibernate.criterion.Restrictions;

/**
 * Factory for user uploads.
 */
public class UploadedContentFactory {
    private static final int PADDING = Integer.valueOf(SystemConfiguration.getInstance().getProperty("ID_PADDING"));

    /**
     * Finds an upload for a hash key.
     * @param key Key for the upload.
     * @return Persistent instance of {@code UploadedContent}, otherwise {@code false}.
     */
    public static UploadedContent getUpload(final String key) {
        if (key == null || key.length() == 0) {
            return null;
        }

        final long[] fs = Hashids.getInstance().decode(key);
        if (fs.length == 0) {
            return null;
        }

        final int id = (int)fs[0] - PADDING;
        return getUpload(id);
    }

    public static UploadedContent getUpload(final int id) {
        try (DatabaseQuery<UploadedContent> query = HibernateUtil.getInstance().query(UploadedContent.class)) {
            return query.where(Restrictions.eq("id", id)).getResult();
        }
    }

    /**
     * Deletes an upload.
     * @param content The upload to delete.
     */
    public static void deleteUpload(final UploadedContent content) {
        try (DatabaseDeletion<UploadedContent> query = HibernateUtil.getInstance().delete()) {
            query.delete(content);
        }
        final String extension = FilenameUtils.getExtension(content.getFileName());

        UploadedBlobFactory.getInstance().deleteBlob(content.getIdHash());
        UploadedBlobFactory.getInstance().deleteBlob(content.getIdHash() + "." + extension);
    }

    /**
     * Creates and stores metadata about an upload in a {@code UploadedContent}.
     * @param type Content-type.
     * @param ip IP address of the uploader.
     * @param fileName Name of file that was uploaded.
     * @param owner Owner of the upload.
     * @return A new instance of {@code UploadedContent}.
     */
    public static UploadedContent persistUploadMetadata(final String type, final String ip, final String fileName, final User owner, final long fileSize) {
        try (DatabaseInsertion<UploadedContent> query = HibernateUtil.getInstance().insert()) {
            UploadedContent upload = new UploadedContent(type, ip, fileName, owner);
            upload.setFileSize(fileSize);
            query.insert(upload);

            return upload;
        }
    }
}
