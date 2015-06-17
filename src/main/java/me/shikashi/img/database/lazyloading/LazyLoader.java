package me.shikashi.img.database.lazyloading;

import me.shikashi.img.database.AbstractDatabaseAction;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;

/**
 * Utility class for initializing members of a class that uses lazy loading.
 */
public class LazyLoader<T> extends AbstractDatabaseAction implements GenericLazyLoader<T>{
    /**
     * Prepares a new lazy loader by initializing the current session and refreshing the context object.
     * @param owner (context) The owner of the member that is being initialized.
     * @param sessionFactory Hibernate session factory.
     */
    public LazyLoader(Object owner, SessionFactory sessionFactory) {
        super(sessionFactory);

        try {
            getSession().update(owner);
        } catch (Exception e) {
            super.close();
            throw e;
        }
    }

    /**
     * Loads the member from the database.
     * @param proxy Data member.
     * @return The data member being loaded (chaining).
     */
    @Override
    public T assertLoaded(T proxy) {
        try {
            Hibernate.initialize(proxy);
            return proxy;
        } finally {
            super.close();
        }
    }
}
