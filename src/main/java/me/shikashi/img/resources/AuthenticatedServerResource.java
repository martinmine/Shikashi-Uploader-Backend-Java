package me.shikashi.img.resources;

import me.shikashi.img.HeaderHelper;
import me.shikashi.img.model.APIKey;
import me.shikashi.img.model.APIKeyFactory;
import me.shikashi.img.model.User;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ServerResource;

/**
 * Performs authorization of the user. Resources that implements this class requires that the user
 * has provided a valid auth key.
 */
public abstract class AuthenticatedServerResource extends ServerResource {
    private User user;
    private APIKey key;

    /**
     * Retrieves the API key and checks that the user is authenticated.
     */
    @Override
    public Representation handle() {
        authenticate();

        if (key == null) {
            setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
            return null;
        } else if (restricted()) {
            if (getStatus() == Status.SUCCESS_OK) {
                setStatus(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
            }
            return null;
        } else {
            return super.handle();
        }
    }

    /**
     * Authenticates the user and fetches session and user data.
     */
    public void authenticate() {
        final String authString = HeaderHelper.getHeaderValue("Authorization", getRequest());
        if (authString == null || authString.length() == 0) {
            return;
        }

        final String[] authHeader = authString.split("-");

        final String key = authHeader[0];
        final String id = authHeader[1];

        final APIKey apiKey = APIKeyFactory.getKey(key, Integer.valueOf(id));

        if (apiKey != null) {
            user = apiKey.getUser();
            this.key = apiKey;
        }
    }

    /**
     * Gets the current user in the session from the API key.
     * @return Current user.
     */
    public User getUser() {
        return this.user;
    }

    public APIKey getKey() {
        return key;
    }

    /**
     * Checks if the user is allowed to see this resource, this function can be overridden to
     * add extra restrictions to a server resource.
     * @return True if the user is not allowed to see the resource, otherwise false.
     */
    public boolean restricted() {
        return false;
    }


    /**
     * Sets the current user for the session.
     * @param user User of the session.
     */
    public void setUser(User user) {
        this.user = user;
    }
}
