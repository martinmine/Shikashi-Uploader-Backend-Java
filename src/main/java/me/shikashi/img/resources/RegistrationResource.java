package me.shikashi.img.resources;

import me.shikashi.img.model.InviteKeyFactory;
import me.shikashi.img.model.UserFactory;
import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

/**
 * Resources providing registration of users.
 */
public class RegistrationResource extends ServerResource {

    /**
     * Performs the registration of a user.
     * @param form Form data.
     */
    @Post
    public void registerUser(Form form) {
        final String email = form.getFirstValue("email");
        final String password = form.getFirstValue("password");
        final String inviteKey = form.getFirstValue("inviteKey");

        if (email == null || "".equals(email) || password == null || "".equals(password)) {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return;
        }

        if (password.length() < 5) {
            setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
            return;
        }

        if (UserFactory.getUserByEmail(email) != null) {
            setStatus(Status.CLIENT_ERROR_CONFLICT);
            return;
        }

        if (InviteKeyFactory.grabInviteKey(inviteKey) == null) {
            setStatus(Status.CLIENT_ERROR_FORBIDDEN);
            return;
        }

        UserFactory.registerUser(email, password);
        setStatus(Status.SUCCESS_CREATED);
    }
}
