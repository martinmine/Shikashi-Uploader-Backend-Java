package me.shikashi.img.database;

import me.shikashi.img.SystemConfiguration;
import me.shikashi.img.database.lazyloading.GenericLazyLoader;
import me.shikashi.img.database.lazyloading.LazyLoader;
import me.shikashi.img.database.lazyloading.NoLoader;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

/**
 * Helper class for interaction with Hibernate, manages connections and settings.
 */
public class HibernateUtil {
    private static final String HIBERNATE_CONNECTION_URL = "hibernate.connection.url";
    private static final String HIKARI_CONNECTION_URL = "hibernate.hikari.dataSource.url";
    private static final Logger LOGGER = Logger.getLogger(HibernateUtil.class);
    private static HibernateUtil instance = new HibernateUtil();
    
    private SessionFactory sessionFactory;

    private HibernateUtil() {
        LOGGER.info("Loading SQL driver");
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception ex) {
            LOGGER.log(Level.FATAL, ex.getMessage(), ex);
            LOGGER.fatal("Unable to start the main environment, no SQL driver found");
            return;
        }

        LOGGER.info("Reading configuration");
        Configuration configuration = new Configuration().configure();
        configuration.configure();
        String configString = configuration.getProperty("hibernate.hikari.dataSource.url");

        configString = configString.replace("${env.DB_HOST}", SystemConfiguration.getInstance().getProperty("RDS_HOSTNAME"));
        configString = configString.replace("${env.DB_PORT}", SystemConfiguration.getInstance().getProperty("RDS_PORT"));
        configString = configString.replace("${env.DB_NAME}", SystemConfiguration.getInstance().getProperty("RDS_DB_NAME"));
        configuration.setProperty(HIBERNATE_CONNECTION_URL, configString);
        configuration.setProperty(HIKARI_CONNECTION_URL, configString);

        configuration.setProperty("hibernate.connection.username", SystemConfiguration.getInstance().getProperty("RDS_USERNAME"));
        configuration.setProperty("hibernate.hikari.dataSource.user", SystemConfiguration.getInstance().getProperty("RDS_USERNAME"));
        configuration.setProperty("hibernate.connection.password", SystemConfiguration.getInstance().getProperty("RDS_PASSWORD"));
        configuration.setProperty("hibernate.hikari.dataSource.password", SystemConfiguration.getInstance().getProperty("RDS_PASSWORD"));

        LOGGER.info("Initializing service registry");
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
                configuration.getProperties()).build();

        LOGGER.info("Building session factory");
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        LOGGER.info("Database initialized");
    }
    
    /**
     * Gets instance to the utility class.
     * @return The utility class.
     */
    public static HibernateUtil getInstance() {
        return instance;
    }
    
    /**
     * Prepares a new database query for retrieving data from the database.
     * @param type Type of the object that shall be retrieved from the server.
     * @return A database query helper object that is ready for use.
     */
    public <T> DatabaseQuery<T> query(Class<T> type) {
        return new DatabaseQuery<>(type, sessionFactory);
    }
    
    /**
     * Prepares a deletion helper for deleting objects from the database.
     * @return A ready to use deletion instance.
     */
    public <T> DatabaseDeletion<T> delete() {
        return new DatabaseDeletion<>(sessionFactory);
    }
    
    /**
     * Prepares an update operation towards the database.
     * @return An update operation instance ready for use.
     */
    public <T> DatabaseUpdate<T> update() {
        return new DatabaseUpdate<>(sessionFactory);
    }
    
    /**
     * Prepares a new database insertion operation for inserting objects to the database.
     * @return A new database insertion helper object that is ready for use.
     */
    public <T> DatabaseInsertion<T> insert() {
        return new DatabaseInsertion<>(sessionFactory);
    }

    /**
     * Prepares a new operation for initializing a member of a class through lazy loading.
     * Will determine whether a database transaction is required or not and act accordingly.
     * @param owner The class with the member that has lazy loading.
     * @param member The member that has lazy loading.
     * @param <T> Type of the lazy loading member.
     * @return A helper class for loading the member.
     */
    public <T> GenericLazyLoader<T> using(Object owner, T member) {
        if (Hibernate.isInitialized(member)) {
            return new NoLoader<>();
        } else {
            return new LazyLoader<>(owner, sessionFactory);
        }
    }
}
