package me.shikashi.img.database;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;

import java.util.List;

/**
 * Helper/wrapper for Hibernate which is used for retrieving objects from the
 * database.
 *
 * @param <T>
 *            Type of the object being retrieved from the database.
 */
public class DatabaseQuery<T> extends AbstractDatabaseAction {
    private Criteria criteria;
    private Class<T> genericType;

    /**
     * Prepares a new database operation.
     * 
     * @param returnType
     *            Type of the object being retrieved from the database.
     * @param sessionFactory
     *            Factory which generates connections to the database.
     */
    public DatabaseQuery(Class<T> returnType, SessionFactory sessionFactory) {
        super(sessionFactory);
        this.genericType = returnType;
        this.criteria = getSession().createCriteria(returnType);
    }

    /**
     * Adds a criteria to the query, this is in many ways like adding an extra
     * WHERE clause in a normal SQL query.
     * 
     * @param criteria
     *            Criteria for the where clause.
     * @return Reference to self for chainability. (cascade)
     */
    public DatabaseQuery<T> where(Criterion criteria) {
        this.criteria.add(criteria);
        return this;
    }

    /**
     * Gets the object being queried for in the database.
     * 
     * @return The object being looked for, null if not found.
     */
    @SuppressWarnings("unchecked")
    public T getResult() {
        Object result = criteria.uniqueResult();
        if (result != null && this.genericType.isAssignableFrom(result.getClass())) {
            return (T) result;
        }
        return null;
    }
    
    /**
     * Gets all the results the query found.
     * @return The results.
     */
    @SuppressWarnings("unchecked")
    public List<T> getResults() {
        return criteria.list();
    }
}
