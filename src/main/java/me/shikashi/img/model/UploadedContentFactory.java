package me.shikashi.img.model;

import me.shikashi.img.SystemConfiguration;
import me.shikashi.img.database.DatabaseDeletion;
import me.shikashi.img.database.DatabaseInsertion;
import me.shikashi.img.database.DatabaseQuery;
import me.shikashi.img.database.HibernateUtil;
import org.hashids.Hashids;
import org.hibernate.LobHelper;
import org.hibernate.criterion.Restrictions;

import java.io.InputStream;
import java.sql.Blob;

/**
 * Created by marti_000 on 07.06.2015.
 */
public class UploadedContentFactory {
    private static final int PADDING = Integer.valueOf(SystemConfiguration.getInstance().getProperty("PADDING"));

    public static UploadedContent getImageUpload(final String key) {
        if (key == null || key.length() == 0) {
            return null;
        }

        final long[] fs = Hashids.getInstance().decode(key);
        final int id = (int)fs[0] - PADDING;
        try (DatabaseQuery<UploadedContent> query = HibernateUtil.getInstance().query(UploadedContent.class)) {
            return query.where(Restrictions.eq("id", id)).getResult();
        }
    }

    public static void deleteImage(final UploadedContent content) {
        try (DatabaseDeletion<UploadedContent> query = HibernateUtil.getInstance().delete()) {
            query.delete(content);
        }
    }

    public static UploadedContent storeImage(final InputStream inputStream, final int fileSize, final String type, final String ip, final String fileName) {
        try (DatabaseInsertion<UploadedContent> query = HibernateUtil.getInstance().insert()) {
            final LobHelper lobHelper = query.getSession().getLobHelper();
            final Blob attachmentData = lobHelper.createBlob(inputStream, fileSize);

            UploadedContent upload = new UploadedContent(type, attachmentData, ip, fileName);
            query.insert(upload);

            return upload;
        }
    }
}
