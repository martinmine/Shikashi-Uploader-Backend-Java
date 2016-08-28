package me.shikashi.img;

import me.shikashi.img.database.HibernateUtil;
import me.shikashi.img.model.User;
import me.shikashi.img.model.UserFactory;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.restlet.ext.servlet.ServerServlet;

import java.io.File;
import java.util.logging.Logger;

/**
 * Main entry for starting the API as an embedded Tomcat-container.
 */
public class TomcatMainEntry {
    private static final Logger LOGGER = Logger.getLogger(TomcatMainEntry.class.getSimpleName());

    private TomcatMainEntry() {
    }

    /**
     * Main application entry.
     * @param args Command line arguments.
     * @throws LifecycleException
     */
    public static void main(String[] args) throws LifecycleException {
        LOGGER.info("Starting up API");

        LOGGER.info("Connecting to database");
        HibernateUtil.getInstance();

        User user = UserFactory.getUserByEmail("martin_mine@hotmail.com");
        if (user == null) {
            throw new RuntimeException("Db is broken");
        }

        Tomcat tomcat = new Tomcat();

        tomcat.setPort(8080);

        File base = new File(System.getProperty("java.io.tmpdir"));
        Context rootCtx = tomcat.addContext("", base.getAbsolutePath());
        rootCtx.addParameter("org.restlet.application", ApplicationRouter.class.getCanonicalName());
        Tomcat.addServlet(rootCtx, "apiServlet", new ServerServlet());

        rootCtx.addServletMapping("/*", "apiServlet");
        tomcat.start();
        tomcat.getServer().await();
    }
}