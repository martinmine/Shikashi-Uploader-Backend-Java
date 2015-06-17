package me.shikashi.img.model;

import me.shikashi.img.SystemConfiguration;
import me.shikashi.img.representations.annotations.ExposedMethod;
import me.shikashi.img.representations.annotations.ImageUploadRepresentation;
import org.hashids.Hashids;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Blob;
import java.util.Date;
import java.util.Random;

/**
 * image of gibby
 */
@Entity
public class UploadedContent {
    private static final int PADDING = Integer.valueOf(SystemConfiguration.getInstance().getProperty("PADDING"));

    @Id
    @GeneratedValue
    private int id;

    @ImageUploadRepresentation
    private String mimeType;

    private String uploaderIp;

    @ImageUploadRepresentation
    private String fileName;

    @Lob
    private Blob content;

    @ImageUploadRepresentation
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploaded;

    @ImageUploadRepresentation
    private String deleteKey;

    @ManyToOne
    private User owner;

    private static final Random RND = new Random();

    public UploadedContent() {
    }

    public UploadedContent(String mimeType, Blob content, String uploaderIp, String fileName) {
        this.uploaderIp = uploaderIp;
        this.mimeType = mimeType;
        this.content = content;
        this.uploaded = new Date();
        this.deleteKey = new BigInteger(130, RND).toString(32);
        this.fileName = fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Blob getContent() {
        return content;
    }

    public User getOwner() {
        return owner;
    }

    @ImageUploadRepresentation
    @ExposedMethod("key")
    public String getIdHash() {
        return Hashids.getInstance().encode(id + PADDING);
    }

    public String getDeleteKey() {
        return deleteKey;
    }
}
