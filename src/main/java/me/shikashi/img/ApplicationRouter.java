package me.shikashi.img;

import me.shikashi.img.resources.*;
import me.shikashi.img.routing.RouterFactory;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.engine.Engine;
import org.restlet.engine.connector.HttpServerHelper;
import org.restlet.routing.Router;

/**
 * Created by marti_000 on 07.06.2015.
 */
public class ApplicationRouter extends Application {
    @Override
    public Restlet createInboundRoot() {
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
        Engine.getInstance().getRegisteredServers().add(new HttpServerHelper(null));
        Component component = new Component();
        component.getServers().add(Protocol.HTTP, 8080);
        component.getDefaultHost().attach("", new ApplicationRouter());
        component.start();
    }
}
