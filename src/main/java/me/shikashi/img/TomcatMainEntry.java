package me.shikashi.img;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.restlet.ext.servlet.ServerServlet;

import java.io.File;

/**
 * Main entry for starting the API as an embedded Tomcat-container.
 */
public class TomcatMainEntry {
    private TomcatMainEntry() {
    }

    /**
     * Main application entry.
     * @param args Command line arguments.
     * @throws LifecycleException
     */
    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();

        tomcat.setPort(8080);

        File base = new File(System.getProperty("java.io.tmpdir"));
        Context rootCtx = tomcat.addContext("", base.getAbsolutePath());
        rootCtx.addParameter("org.restlet.application", ApplicationRouter.class.getCanonicalName());
        Tomcat.addServlet(rootCtx, "dateServlet", new ServerServlet());

        rootCtx.addServletMapping("/*", "dateServlet");
        tomcat.start();
        tomcat.getServer().await();
    }
}