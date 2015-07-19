package me.shikashi.img.model;

import me.shikashi.img.database.DatabaseDeletion;
import me.shikashi.img.database.DatabaseQuery;
import me.shikashi.img.database.HibernateUtil;
import org.hibernate.criterion.Restrictions;

/**
 * Factory class for invite keys.
 */
public class InviteKeyFactory {

    /**
     * Finds and deletes an image key if it exists.
     * @param key The invite key.
     * @return {@code InviteKey} if exists, otherwise {@code null}.
     */
    public static InviteKey grabInviteKey(final String key) {
        final InviteKey inviteKey = getInviteKey(key);

        if (inviteKey != null) {
            try (DatabaseDeletion<InviteKey> query = HibernateUtil.getInstance().delete()) {
                query.delete(inviteKey);
            }
        }

        return inviteKey;
    }

    /**
     * Finds an invite key.
     * @param key The invite key.
     * @return {@code InviteKey} if exists, otherwise {@code null}.
     */
    private static InviteKey getInviteKey(final String key) {
        try (DatabaseQuery<InviteKey> query = HibernateUtil.getInstance().query(InviteKey.class)) {
            return query.where(Restrictions.eq("inviteKey", key)).getResult();
        }
    }
}
