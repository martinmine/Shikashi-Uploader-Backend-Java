package me.shikashi.img.model;

import me.shikashi.img.database.HibernateUtil;

import javax.persistence.*;
import java.util.List;

/**
 * Representing information about a registered user.
 */
@Entity(name = "`User`")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "`Id`")
    private int id;

    @Column(unique = true, name = "`Email`")
    private String email;

    @Column(name = "`Password`")
    private String password;

    @Column(name = "`PasswordSalt`")
    private String passwordSalt;

    @OneToMany(mappedBy = "user")
    private List<APIKey> apiKeys;

    @OneToMany(mappedBy = "owner")
    private List<UploadedContent> uploads;

    public User(String email) {
        this.email = email;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<UploadedContent> getUploads() {
        return HibernateUtil.getInstance().using(this, this.uploads).assertLoaded(this.uploads);
    }
}
