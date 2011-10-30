/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.common;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.ui.EhourWebApplication;
import net.rrm.ehour.ui.common.authorization.AuthUser;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.login.page.SessionExpiredPage;
import org.apache.wicket.*;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;
import org.apache.wicket.authorization.strategies.role.RoleAuthorizationStrategy;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.protocol.http.HttpSessionStore;
import org.apache.wicket.protocol.http.WebRequestCycleProcessor;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.request.IRequestCycleProcessor;
import org.apache.wicket.session.ISessionStore;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.HashSet;

public class TestEhourWebApplication extends EhourWebApplication implements Serializable {
    private static final long serialVersionUID = -7336200909844170964L;
    private EhourWebSession session;
    private Roles authorizedRoles;
    private User authenticatedUser;

    /**
     * When not authorized, just let it pass
     */
    @Override
    protected void setupSecurity() {
        getApplicationSettings().setPageExpiredErrorPage(SessionExpiredPage.class);

        getSecuritySettings().setAuthorizationStrategy(new RoleAuthorizationStrategy(this));

        getSecuritySettings().setUnauthorizedComponentInstantiationListener(new IUnauthorizedComponentInstantiationListener() {
            public void onUnauthorizedInstantiation(final Component component) {
            }
        });
    }

    /*
      * (non-Javadoc)
      *
      * @see net.rrm.ehour.persistence.persistence.ui.EhourWebApplication#newRequestCycleProcessor()
      */
    @Override
    protected IRequestCycleProcessor newRequestCycleProcessor() {
        return new WebRequestCycleProcessor();
    }

    /*
      * (non-Javadoc)
      *
      * @see
      * org.apache.wicket.authentication.AuthenticatedWebApplication#newSession
      * (org.apache.wicket.Request, org.apache.wicket.Response)
      */
    @SuppressWarnings("serial")
    @Override
    public Session newSession(final Request request, final Response response) {
        session = new EhourWebSession(request) {
            public AuthUser getUser() {
                User user = createAuthenticatedUser();

                return new AuthUser(user);
            }


            public Roles getRoles() {
                return createAuthorizedRoles();
            }

            @Override
            public boolean authenticate(String username, String password) {
                return true;
            }
        };

        return session;
    }

    protected Roles createAuthorizedRoles() {
        if (authorizedRoles == null) {
            authorizedRoles = new Roles();
            authorizedRoles.add(UserRole.ROLE_PROJECTMANAGER);
            authorizedRoles.add(UserRole.ROLE_CONSULTANT);
            authorizedRoles.add(UserRole.ROLE_ADMIN);
            authorizedRoles.add(UserRole.ROLE_REPORT);
        }

        return authorizedRoles;
    }

    protected User createAuthenticatedUser() {
        if (authenticatedUser == null) {
            User user = new User(1);
            user.setUsername("thies");
            user.setPassword("secret");

            HashSet<UserRole> userRoles = new HashSet<UserRole>();
            userRoles.addAll(UserRole.ROLES.values());

            user.setUserRoles(userRoles);

            authenticatedUser = user;
        }


        return authenticatedUser;
    }

    public void setAuthenticatedUser(User authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    public void setAuthorizedRoles(Roles authorizedRoles) {
        this.authorizedRoles = authorizedRoles;
    }

    public EhourWebSession getSession() {
        return session;
    }

    protected ISessionStore newSessionStore() {
        return new HttpSessionStore(this) {
            @Override
            public Session lookup(Request request) {
                return session;
            }
        };
    }

    protected WebResponse newWebResponse(final HttpServletResponse servletResponse) {
        return new WebResponse(servletResponse);
    }

    @Override
    protected void outputDevelopmentModeWarning() {
    }

    @Override
    public String getConfigurationType() {
        return Application.DEPLOYMENT;
    }

}
