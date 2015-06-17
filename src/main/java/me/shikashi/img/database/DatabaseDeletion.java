package me.shikashi.img.database;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

/**
 * Performs a database deletion operation.
 * @param <T> Type of the object to delete.
 */
public class DatabaseDeletion<T> extends AbstractDatabaseAction {
    
    /**
     * Prepares a new database deletion action
     * @param sessionFactory Factory which makes sessions/connections to the database. 
     */
    public DatabaseDeletion(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * Deletes an object from the database.
     * @param object Object being deleted from the database.
     * @throws HibernateException Unable to insert the entry to the database.
     */
    public void delete(Object object) {
        try {
            getSession().delete(object);
        } catch (HibernateException e) {
            doRollback();
            throw e;
        }
    }
}
