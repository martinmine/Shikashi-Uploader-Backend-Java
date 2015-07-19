package me.shikashi.img.model;

import me.shikashi.img.representations.annotations.APIKeyRepresentation;
import me.shikashi.img.representations.annotations.ExposedMethod;

import javax.persistence.*;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Contains data about an API key.
 */
@Entity
public class APIKey {
    private static final SecureRandom RND = new SecureRandom();

    @Id
    @GeneratedValue
    private int id;

    private String identifier;

    @APIKeyRepresentation
    private long expirationTime;

    @ManyToOne
    private User user;

    public APIKey(User user) {
        this.user = user;
        this.identifier = new BigInteger(130, RND).toString(100);
        this.expirationTime = (System.currentTimeMillis() / 1000) + 60*60*24;
    }

    public APIKey() {
    }

    @APIKeyRepresentation
    @ExposedMethod("key")
    public String getApiKey() {
        return String.format("%s-%s", identifier, id);
    }

    public User getUser() {
        return user;
    }
}
