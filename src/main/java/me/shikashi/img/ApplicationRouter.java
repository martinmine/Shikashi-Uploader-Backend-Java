package me.shikashi.img;

import me.shikashi.img.resources.*;
import me.shikashi.img.routing.RouterFactory;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.restlet.ext.servlet.ServerServlet;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by marti_000 on 07.06.2015.
 */
public class ApplicationRouter extends Application {
    public ApplicationRouter() {
        super();
        setStatusService(new ShikashiStatusService());
    }

    @Override
    public Restlet createInboundRoot() {
        Logger.getLogger("org.restlet.Component.LogService").setLevel(Level.SEVERE);
        final Router router = RouterFactory.makeRouter(getContext());
        router.attach("/upload", UploadResource.class);
        router.attach("/account/uploads", UserUploadsResource.class);
        router.attach("/account/password", UserPasswordResource.class);
        router.attach("/login", LoginResource.class);
        router.attach("/register", RegistrationResource.class);
        router.attach("/{key}", UploadedContentResource.class);
        router.attach("/{key}/delete", DeleteUploadResource.class);

        return router;
    }

    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        File base = new File(System.getProperty("java.io.tmpdir"));
        Context rootCtx = tomcat.addContext("", base.getAbsolutePath());
        rootCtx.addParameter("org.restlet.application", ApplicationRouter.class.getCanonicalName());
        Tomcat.addServlet(rootCtx, "api", new ServerServlet());

        rootCtx.addServletMapping("/*", "api");
        tomcat.start();
        tomcat.getServer().await();
    }
}