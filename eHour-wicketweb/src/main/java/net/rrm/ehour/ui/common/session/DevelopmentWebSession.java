package net.rrm.ehour.ui.common.session;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.ui.common.authorization.AuthUser;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;

import java.util.HashSet;
import java.util.Set;

// used only for development purposes
public class DevelopmentWebSession extends EhourWebSession {
    private Roles authorizedRoles;
    private User authenticatedUser;

    public DevelopmentWebSession(Request req) {
        this(req, null);
    }

    public DevelopmentWebSession(Request req, Roles authorizedRoles) {
        super(req);
        this.authorizedRoles = (authorizedRoles == null) ? createDefaultAuthorizedRoles() : authorizedRoles;
    }

    public AuthUser getAuthUser() {
        User user = createAuthenticatedUser();

        return new AuthUser(user);
    }


    public Roles getRoles() {
        return authorizedRoles;
    }

    @Override
    public boolean authenticate(String username, String password) {
        return true;
    }

    private Roles createDefaultAuthorizedRoles() {
        authorizedRoles = new Roles();

        for (UserRole userRole : UserRole.ROLES.values()) {
            authorizedRoles.add(userRole.getRole());
        }

        return authorizedRoles;
    }

    protected User createAuthenticatedUser() {
        if (authenticatedUser == null) {
            User user = new User(4);
            user.setUsername("thies");
            user.setPassword("secret");

            Set<UserRole> userRoles = new HashSet<>();

            if (authorizedRoles != null) {
                for (String authorizedRole : authorizedRoles) {
                    UserRole userRole = UserRole.ROLES.get(authorizedRole);
                    userRoles.add(userRole);
                }
            } else {
                userRoles.addAll(UserRole.ROLES.values());
            }

            user.setUserRoles(userRoles);

            authenticatedUser = user;
        }

        return authenticatedUser;
    }
}
