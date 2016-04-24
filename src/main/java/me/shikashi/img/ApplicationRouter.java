package me.shikashi.img;

import me.shikashi.img.resources.*;
import me.shikashi.img.routing.RouterFactory;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contains main entry and the routing of resources.
 */
public class ApplicationRouter extends Application {
    private static final Logger LOGGER = Logger.getLogger(ApplicationRouter.class.getSimpleName());

    public ApplicationRouter() {
        super();
        setStatusService(new ShikashiStatusService());
    }

    @Override
    public Restlet createInboundRoot() {
        Logger.getLogger("org.restlet.Component.LogService").setLevel(Level.SEVERE);
        final Router router = RouterFactory.makeRouter(getContext());
        router.attach("/app", AppResource.class);
        router.attach("/upload", UploadResource.class);
        router.attach("/account/uploads", UserUploadsResource.class);
        router.attach("/account/password", UserPasswordResource.class);
        router.attach("/login", LoginResource.class);
        router.attach("/register", RegistrationResource.class);
        router.attach("/{key}/delete", DeleteUploadResource.class);

        return router;
    }
}