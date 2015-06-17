package me.shikashi.img.database;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import java.io.Serializable;

/**
 * Wrapper around Hibernate for inserting objects to the database.
 *
 * @param <T>
 *            The type of the object that is being inserted.
 */
public class DatabaseInsertion<T> extends AbstractDatabaseAction {
    /**
     * Prepares a new database insertion action
     * 
     * @param sessionFactory
     *            Factory which makes sessions/connections to the database.
     */
    public DatabaseInsertion(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * Inserts an object to the database.
     * 
     * @param object
     *            Object being inserted to the database.
     * @return The identifier for the insertion, may for example be the value of
     *         PK in the inserted row.
     * @throws HibernateException
     *             Unable to insert the entry to the database.
     */
    public Serializable insert(T object) {
        try {
            return getSession().save(object);
        } catch (HibernateException e) {
            doRollback();
            throw e;
        }
    }
}
