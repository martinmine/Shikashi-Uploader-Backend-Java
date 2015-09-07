package me.shikashi.img.resources;

import me.shikashi.img.database.DatabaseUpdate;
import me.shikashi.img.database.HibernateUtil;
import me.shikashi.img.model.APIKey;
import me.shikashi.img.model.APIKeyFactory;
import me.shikashi.img.model.User;
import me.shikashi.img.model.UserFactory;
import me.shikashi.img.representations.RepresentationFactory;
import me.shikashi.img.representations.annotations.APIKeyRepresentation;
import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.ext.gson.GsonRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

/**
 * Provides login for the user.
 */
public class LoginResource extends ServerResource {

    /**
     * Authenticates the user and generates an auth token.
     * @param form Form data with email and password.
     * @return A representation of an auth key, {@code null} if the credentials were invalid.
     */
    @Post
    public GsonRepresentation<APIKey> login(Form form) {
        final String email = form.getFirstValue("email");
        final String password = form.getFirstValue("password");

        if (email == null || "".equals(email) || password == null || "".equals(password)) {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return null;
        }

        final User user = UserFactory.getUser(email, password);

        if (user == null) {
            setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            return null;
        }

        final APIKey key = APIKeyFactory.createKey(user);

        // If this is a login from a client, make the key last forever
        if ("Shikashi-Win32".equals(form.getFirstValue("client"))) {
            key.setNoExpiration();

            try (DatabaseUpdate<APIKey> query = HibernateUtil.getInstance().update()) {
                query.update(key);
            }
        }

        return RepresentationFactory.makeRepresentation(key, APIKeyRepresentation.class);
    }
}
