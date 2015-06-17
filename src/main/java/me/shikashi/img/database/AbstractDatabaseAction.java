package me.shikashi.img.database;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Generic class for database operations.
 */
public abstract class AbstractDatabaseAction implements AutoCloseable {
    private Session session;
    private Transaction transaction;

    /**
     * Prepares a new database action.
     * @param sessionFactory Hibernate session factory.
     */
    public AbstractDatabaseAction(SessionFactory sessionFactory) {
        this.session = sessionFactory.getCurrentSession();
        this.transaction = this.session.beginTransaction();
    }

    /**
     * @return The current session of the database action.
     */
    public Session getSession() {
        return this.session;
    }

    /**
     * Rolls back the changes made to the database.
     */
    protected void doRollback() {
        this.transaction.rollback();
    }

    /**
     * Finishes up the database operation and commits the changes.
     */
    @Override
    public void close() {
        try {
            this.transaction.commit();
        } catch (HibernateException ex) {
            doRollback();
            throw ex;
        }
    }
}
