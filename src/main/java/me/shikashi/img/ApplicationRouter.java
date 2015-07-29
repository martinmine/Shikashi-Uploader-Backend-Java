package me.shikashi.img;

import com.mongodb.gridfs.GridFSDBFile;
import me.shikashi.img.database.DatabaseQuery;
import me.shikashi.img.database.HibernateUtil;
import me.shikashi.img.model.MongoDbFacade;
import me.shikashi.img.model.UploadedBlobFactory;
import me.shikashi.img.model.UploadedContent;
import me.shikashi.img.resources.*;
import me.shikashi.img.routing.RouterFactory;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import org.restlet.ext.servlet.ServerServlet;

import java.io.File;
import java.util.List;
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
        router.attach("/upload", UploadResource.class);
        router.attach("/account/uploads", UserUploadsResource.class);
        router.attach("/account/password", UserPasswordResource.class);
        router.attach("/login", LoginResource.class);
        router.attach("/register", RegistrationResource.class);
        router.attach("/app", AppResource.class);
       // router.attach("/{key}", UploadedContentResource.class);
        router.attach("/{key}/delete", DeleteUploadResource.class);

        return router;
    }

    /**
     * Application main entry.
     * @param args Command line arguments.
     * @throws Exception Something really bad happened.
     */
    /*public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        File base = new File(System.getProperty("java.io.tmpdir"));
        Context rootCtx = tomcat.addContext("", base.getAbsolutePath());
        rootCtx.addParameter("org.restlet.application", ApplicationRouter.class.getCanonicalName());
        Tomcat.addServlet(rootCtx, "api", new ServerServlet());

        rootCtx.addServletMapping("/*", "api");
        tomcat.start();
        tomcat.getServer().await();
    }*/

    /*
    public static void main(String[] args) throws Exception {
        List<UploadedContent> uploads;

        try (DatabaseQuery<UploadedContent> query = HibernateUtil.getInstance().query(UploadedContent.class)) {
            uploads = query.getResults();
        }

        LOGGER.info("Converting " + uploads.size() + " uploads");

        final MongoDbFacade mongoDb = MongoDbFacade.getInstance();
        final UploadedBlobFactory s3 = UploadedBlobFactory.getInstance();

        for (UploadedContent upload : uploads) {
            int uploadId = upload.getId();
            final GridFSDBFile file = mongoDb.getBlob(uploadId);
            if (file == null || file.getInputStream() == null) {
                LOGGER.warning("File " + uploadId + " does not have any associated data, ignoring call");
            } else {
                LOGGER.info("Putting file " + uploadId + " into Amazon S3");
                s3.storeBlob(file.getInputStream(), uploadId, upload.getMimeType(), file.getLength());
            }
        }
    }*/
    /*public static void main(String[] args) {
        new S3ArchitecturalUpgrade().upgrade();
    }*/
}