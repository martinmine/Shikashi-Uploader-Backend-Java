package me.shikashi.img;

import me.shikashi.img.database.DatabaseQuery;
import me.shikashi.img.database.HibernateUtil;
import me.shikashi.img.model.UploadedBlobFactory;
import me.shikashi.img.model.UploadedContent;

import java.util.List;

/**
 * Created by marti_000 on 30.07.2015.
 */
public class AliasGenerator {
    public static void createAliases() {
        final List<UploadedContent> uploads = getAllUploads();

        for (UploadedContent upload : uploads) {
            UploadedBlobFactory.getInstance().createBlobAlias(upload.getIdHash(), upload.getMimeType(), upload.getFileName());
            System.out.println("Alias created for upload " + upload.getIdHash());
        }
    }

    private static List<UploadedContent> getAllUploads() {
        try (DatabaseQuery<UploadedContent> query = HibernateUtil.getInstance().query(UploadedContent.class)) {
            return query.getResults();
        }
    }
}
