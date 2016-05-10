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
@Entity(name = "`UploadedContent`")
public class UploadedContent {
    private static final int PADDING = Integer.valueOf(SystemConfiguration.getInstance().getProperty("ID_PADDING"));

    @Column(name = "`Id`")
    @Id
    @SequenceGenerator(name = "UploadedContentAI", sequenceName = "`UploadedContent_Id_seq`")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UploadedContentAI")
    private int id;

    @Column(name = "`MimeType`")
    @FileUploadRepresentation
    private String mimeType;

    @Column(name = "`UploaderIP`")
    private String uploaderIp;

    @Column(name = "`FileName`")
    @FileUploadRepresentation
    private String fileName;

    @Column(name = "`Uploaded`")
    @FileUploadRepresentation
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploaded;

    @JoinColumn(name = "`OwnerId`")
    @ManyToOne
    private User owner;

    @Column(name = "`ViewCount`")
    @FileUploadRepresentation
    private Long viewCount;

    @Column(name = "`FileSize`")
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
        this.viewCount = Long.valueOf(0L);
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
