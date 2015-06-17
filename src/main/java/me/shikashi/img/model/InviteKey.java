package me.shikashi.img.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by marti_000 on 16.06.2015.
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
