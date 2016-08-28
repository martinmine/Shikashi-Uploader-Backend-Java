package me.shikashi.img.database;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Helper/wrapper for Hibernate which is used for retrieving objects from the
 * database.
 *
 * @param <T>
 *            Type of the object being retrieved from the database.
 */
public class DatabaseQuery<T> extends AbstractDatabaseAction {
    private CriteriaQuery<T> criteriaQuery;
    private Root<T> root;
    private CriteriaBuilder criteriaBuilder;

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

        this.criteriaBuilder = getSession().getCriteriaBuilder();
        this.criteriaQuery = criteriaBuilder.createQuery(returnType);
        this.root = criteriaQuery.from(returnType);

        criteriaQuery.select(root);
    }

    public CriteriaQuery<T> getCriteriaQuery() {
        return criteriaQuery;
    }

    public Root<T> getRoot() {
        return root;
    }

    public CriteriaBuilder getCriteriaBuilder() {
        return criteriaBuilder;
    }

    public DatabaseQuery<T> whereEq(String property, Object value) {
        criteriaQuery.where(criteriaBuilder.equal(root.get(property), value));
        return this;
    }

    /**
     * Adds a criteria to the query, this is in many ways like adding an extra
     * WHERE clause in a normal SQL query.
     *
     * @param predicates
     *            Criteria for the where clause.
     * @return Reference to self for chainability. (cascade)
     */
    public DatabaseQuery<T> where(Predicate... predicates) {
        criteriaQuery.where(predicates);
        return this;
    }

    /**
     * Gets the object being queried for in the database.
     *
     * @return The object being looked for, null if not found.
     */
    @SuppressWarnings("unchecked")
    public T getResult() {
        final List<T> results = getResults();
        return !results.isEmpty() ? results.get(0) : null;
    }

    /**
     * Gets all the results the query found.
     * @return The results.
     */
    @SuppressWarnings("unchecked")
    public List<T> getResults() {
        final Query<T> query = getSession().createQuery(criteriaQuery);
        return query.list();
    }
}