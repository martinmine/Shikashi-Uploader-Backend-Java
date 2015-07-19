package me.shikashi.img.model;

import me.shikashi.img.database.DatabaseInsertion;
import me.shikashi.img.database.DatabaseQuery;
import me.shikashi.img.database.HibernateUtil;
import org.hibernate.criterion.Restrictions;

/**
 * Factory class for API keys.
 */
public class APIKeyFactory {

    /**
     * Creates a new API key for a user.
     * @param user User to create the API key for.
     * @return The generated key.
     */
    public static APIKey createKey(User user) {
        final APIKey key = new APIKey(user);

        try (DatabaseInsertion<APIKey> query = HibernateUtil.getInstance().insert()) {
            query.insert(key);
        }
        return key;
    }

    /**
     * Gets an existing API key.
     * @param key Random token for the key.
     * @param id Unique ID for the key.
     * @return {@code APIKey} if exists, otherwise {@code null}.
     */
    public static APIKey getKey(final String key, final int id) {
        try (DatabaseQuery<APIKey> query = HibernateUtil.getInstance().query(APIKey.class)) {
            return query.where(Restrictions.eq("id", id))
                    .where(Restrictions.eq("identifier", key))
                    .getResult();
        }
    }
}
