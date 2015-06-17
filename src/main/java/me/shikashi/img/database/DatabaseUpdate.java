package me.shikashi.img.database;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

/**
 * Performs updates in the database of objects.
 * @param <T> Type of the object being updated.
 */
public class DatabaseUpdate<T> extends AbstractDatabaseAction {

    public DatabaseUpdate(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    /**
     * Update an object in the database.
     * @param object Object being updated
     * @throws HibernateException Unable to update the object.
     */
    public void update(T object) {
        try {
            getSession().update(object);
        } catch (HibernateException e) {
            doRollback();
            throw e;
        }
    }
}
