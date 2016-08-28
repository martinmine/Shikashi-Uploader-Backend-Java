package me.shikashi.img;

import me.shikashi.img.resources.*;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import org.restlet.service.CorsService;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contains main entry and the routing of resources.
 */
public class ApplicationRouter extends Application {
    public ApplicationRouter() {
        super();
        setStatusService(new ShikashiStatusService());
        configureCORS();
    }

    private void configureCORS() {
        final CorsService corsService = new CorsService();
        corsService.setAllowingAllRequestedHeaders(true);
        corsService.setAllowedOrigins(new HashSet<>(Collections.singletonList("*")));
        corsService.setAllowedCredentials(true);
        corsService.setSkippingResourceForCorsOptions(true);
        getServices().add(corsService);
    }

    @Override
    public Restlet createInboundRoot() {
        Logger.getLogger("org.restlet.Component.LogService").setLevel(Level.SEVERE);
        final Router router = new Router();
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