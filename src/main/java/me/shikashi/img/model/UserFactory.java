package me.shikashi.img.model;

import me.shikashi.img.SystemConfiguration;
import me.shikashi.img.database.DatabaseInsertion;
import me.shikashi.img.database.DatabaseQuery;
import me.shikashi.img.database.DatabaseUpdate;
import me.shikashi.img.database.HibernateUtil;
import org.hibernate.criterion.Restrictions;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Created by LeoBR on 09.06.2015.
 */
public class UserFactory {
    private static final String PASSWORD_PEPPER = SystemConfiguration.getInstance().getProperty("PASSWORD_HASH");

    public static void registerUser(final String email, final String password) {
        final User user = new User(email);
        setPassword(password, user);

        try (DatabaseInsertion<User> query = HibernateUtil.getInstance().insert()) {
            query.insert(user);
        }
    }

    public static void setPassword(String password, User user) {
        final String salt = BCrypt.gensalt(5);
        final String hashedPw = BCrypt.hashpw(password + PASSWORD_PEPPER, salt);

        user.setPassword(hashedPw, salt);
    }

    public static void saveUserChanges(final User user) {
        try (DatabaseUpdate<User> query = HibernateUtil.getInstance().update()) {
            query.update(user);
        }
    }

    public static User getUser(final String email, final String password) {
        User user;
        try (DatabaseQuery<User> query = HibernateUtil.getInstance().query(User.class)) {
            user = query.where(Restrictions.eq("email", email))
                    .getResult();
        }

        if (user == null) {
            return null;
        }

        final String hashedPw = getPasswordHash(user, password);
        if (hashedPw.equals(user.getPassword())) {
            return user;
        } else {
            return null;
        }
    }

    public static String getPasswordHash(User user, String password) {
        return BCrypt.hashpw(password + PASSWORD_PEPPER, user.getPasswordSalt());
    }

    public static User getUserByEmail(final String email) {
        try (DatabaseQuery<User> query = HibernateUtil.getInstance().query(User.class)) {
            return query.where(Restrictions.eq("email", email)).getResult();
        }
    }
}
