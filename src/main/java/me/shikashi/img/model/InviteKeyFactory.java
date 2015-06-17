package me.shikashi.img.model;

import me.shikashi.img.database.DatabaseDeletion;
import me.shikashi.img.database.DatabaseQuery;
import me.shikashi.img.database.HibernateUtil;
import org.hibernate.criterion.Restrictions;

/**
 * Created by marti_000 on 16.06.2015.
 */
public class InviteKeyFactory {
    public static InviteKey grabInviteKey(final String key) {
        final InviteKey inviteKey = getInviteKey(key);

        if (inviteKey != null) {
            try (DatabaseDeletion<InviteKey> query = HibernateUtil.getInstance().delete()) {
                query.delete(inviteKey);
            }
        }

        return inviteKey;
    }

    private static InviteKey getInviteKey(final String key) {
        try (DatabaseQuery<InviteKey> query = HibernateUtil.getInstance().query(InviteKey.class)) {
            return query.where(Restrictions.eq("inviteKey", key)).getResult();
        }
    }
}
