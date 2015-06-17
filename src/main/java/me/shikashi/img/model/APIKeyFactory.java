package me.shikashi.img.model;

import me.shikashi.img.database.DatabaseInsertion;
import me.shikashi.img.database.DatabaseQuery;
import me.shikashi.img.database.HibernateUtil;
import org.hibernate.criterion.Restrictions;

/**
 * Created by marti_000 on 09.06.2015.
 */
public class APIKeyFactory {
    public static APIKey createKey(User user) {
        final APIKey key = new APIKey(user);

        try (DatabaseInsertion<APIKey> query = HibernateUtil.getInstance().insert()) {
            query.insert(key);
        }
        return key;
    }

    public static APIKey getKey(final String key, final int id) {
        try (DatabaseQuery<APIKey> query = HibernateUtil.getInstance().query(APIKey.class)) {
            return query.where(Restrictions.eq("id", id))
                    .where(Restrictions.eq("identifier", key))
                    .getResult();
        }
    }
}
