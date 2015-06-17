package me.shikashi.img.resources;

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
 * Created by marti_000 on 09.06.2015.
 */
public class LoginResource extends ServerResource {
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

        return RepresentationFactory.makeRepresentation(key, APIKeyRepresentation.class);
    }
}
