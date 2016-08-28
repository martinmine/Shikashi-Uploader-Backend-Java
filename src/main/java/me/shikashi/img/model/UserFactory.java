package me.shikashi.img.model;

import me.shikashi.img.SystemConfiguration;
import me.shikashi.img.database.DatabaseInsertion;
import me.shikashi.img.database.DatabaseQuery;
import me.shikashi.img.database.DatabaseUpdate;
import me.shikashi.img.database.HibernateUtil;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Factory for creating users.
 */
public class UserFactory {
    private static final String PASSWORD_PEPPER = SystemConfiguration.getInstance().getProperty("PASSWORD_HASH");

    /**
     * Creates a new entry for a user.
     * @param email Email of the user.
     * @param password User password.
     */
    public static void registerUser(final String email, final String password) {
        final User user = new User(email);
        setPassword(password, user);

        try (DatabaseInsertion<User> query = HibernateUtil.getInstance().insert()) {
            query.insert(user);
        }
    }

    /**
     * Sets the current password for a user.
     * @param password New password.
     * @param user User to set the password for.
     */
    public static void setPassword(String password, User user) {
        user.setPassword(BCrypt.hashpw(password + PASSWORD_PEPPER, BCrypt.gensalt(5)));
    }

    /**
     * Saves changes that has been made to a {@code User} object.
     * @param user User to save changes for.
     */
    public static void saveUserChanges(final User user) {
        try (DatabaseUpdate<User> query = HibernateUtil.getInstance().update()) {
            query.update(user);
        }
    }

    /**
     * Gets a user from the database.
     * @param email The email address that the user has registered on.
     * @param password Password for the user.
     * @return {@code null} if no {@code User} was found, otherwise the {@code User} instance.
     */
    public static User getUser(final String email, final String password) {
        User user;
        try (DatabaseQuery<User> query = HibernateUtil.getInstance().query(User.class)) {
            user = query.whereEq("email", email)
                    .getResult();
        }

        if (user == null) {
            return null;
        }

        if (validPassword(user, password)) {
            return user;
        } else {
            return null;
        }
    }

    /**
     * Checks if a password is valid for a user.
     * @param user User to validate password for.
     * @param password Cleartext password.
     * @return True if password was valid, otherwise false.
     */
    public static boolean validPassword(final User user, final String password) {
        return BCrypt.checkpw(password + PASSWORD_PEPPER, user.getPassword());
    }

    /**
     * Finds a user by email address.
     * @param email Email address of the user.
     * @return {@code null} if no {@code User} was found, otherwise the {@code User} instance.
     */
    public static User getUserByEmail(final String email) {
        try (DatabaseQuery<User> query = HibernateUtil.getInstance().query(User.class)) {
            return query.whereEq("email", email).getResult();
        }
    }
}
