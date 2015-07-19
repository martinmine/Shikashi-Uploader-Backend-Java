package me.shikashi.img.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * An invite key which the user has to provide when registering on the service.
 */
@Entity
public class InviteKey {
    @Id
    private String inviteKey;

    public InviteKey() {
    }

    public String getInviteKey() {
        return inviteKey;
    }
}
