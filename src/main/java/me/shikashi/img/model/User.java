package me.shikashi.img.model;

import me.shikashi.img.database.HibernateUtil;

import javax.persistence.*;
import java.util.List;

/**
 * Representing information about a registered user.
 */
@Entity
public class User {
    @Id
    @GeneratedValue
    private int id;

    @Column(unique=true)
    private String email;
    private String password;
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

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password, String passwordSalt) {
        this.password = password;
        this.passwordSalt = passwordSalt;
    }

    public List<UploadedContent> getUploads() {
        return HibernateUtil.getInstance().using(this, this.uploads).assertLoaded(this.uploads);
    }
}
