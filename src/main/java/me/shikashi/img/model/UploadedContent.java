package me.shikashi.img.model;

import me.shikashi.img.SystemConfiguration;
import me.shikashi.img.representations.annotations.ExposedMethod;
import me.shikashi.img.representations.annotations.FileUploadRepresentation;
import org.hashids.Hashids;

import javax.persistence.*;
import java.util.Date;

/**
 * Represents metadata about an upload.
 */
@Entity
public class UploadedContent {
    private static final int PADDING = Integer.valueOf(SystemConfiguration.getInstance().getProperty("ID_PADDING"));

    @Id
    @GeneratedValue
    private int id;

    @FileUploadRepresentation
    private String mimeType;

    private String uploaderIp;

    @FileUploadRepresentation
    private String fileName;

    @FileUploadRepresentation
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploaded;

    @ManyToOne
    private User owner;

    @FileUploadRepresentation
    private int viewCount;

    @FileUploadRepresentation
    private long fileSize;

    public UploadedContent() {
    }

    public UploadedContent(String mimeType, String uploaderIp, String fileName, User owner) {
        this.uploaderIp = uploaderIp;
        this.mimeType = mimeType;
        this.uploaded = new Date();
        this.fileName = fileName;
        this.owner = owner;
        this.viewCount = 0;
    }

    public int getId() {
        return id;
    }

    public String getMimeType() {
        return mimeType;
    }

    public User getOwner() {
        return owner;
    }

    @FileUploadRepresentation
    @ExposedMethod("key")
    public String getIdHash() {
        return Hashids.getInstance().encode(id + PADDING);
    }

    public String getFileName() {
        return fileName;
    }

    public void incrementViewCount() {
        this.viewCount++;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
