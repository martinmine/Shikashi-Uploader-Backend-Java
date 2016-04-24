package me.shikashi.img.resources;

        import me.shikashi.img.model.UserFactory;
        import org.restlet.data.Form;
        import org.restlet.data.Status;
        import org.restlet.resource.Post;

/**
 * Resource for resetting the password for the user.
 */
public class UserPasswordResource extends AuthenticatedServerResource {
    @Post
    public void changeUserPassword(Form form) {
        final String currentPassword = form.getFirstValue("currentPassword");
        final String newPassword = form.getFirstValue("newPassword");

        if (!UserFactory.validPassword(getUser(), currentPassword)) {
            setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
        } else {
            UserFactory.setPassword(newPassword, getUser());
            UserFactory.saveUserChanges(getUser());

            setStatus(Status.SUCCESS_ACCEPTED);
        }
    }
}
