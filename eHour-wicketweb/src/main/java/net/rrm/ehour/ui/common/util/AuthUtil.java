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

package net.rrm.ehour.ui.common.util;

import com.google.common.base.Optional;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.ui.admin.config.page.MainConfigPage;
import net.rrm.ehour.ui.admin.customer.CustomerAdminPage;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.report.page.ReportPage;
import net.rrm.ehour.ui.timesheet.page.MonthOverviewPage;
import org.apache.wicket.Page;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Common stuff for auth
 */

public class AuthUtil {
    public static User getUser() {
        EhourWebSession session = EhourWebSession.getSession();

        return (session.getAuthUser() != null) ? session.getAuthUser().getUser() : null;
    }

    /**
     * Check if the logged in user has the specified role
     *
     * @param role
     * @return
     */
    public static boolean hasRole(String role) {
        Roles roles = getRoles();

        return (roles != null) && roles.contains(role);
    }

    /**
     * Get the roles of the logged in user
     *
     * @return
     */
    public static Roles getRoles() {
        EhourWebSession session = EhourWebSession.getSession();

        return session.getRoles();
    }

    /**
     * Is the user authorized for the page?
     *
     * @param pageClass
     * @return
     */
    public static boolean isUserAuthorizedForPage(Class<? extends WebPage> pageClass) {
        AuthorizeInstantiation authorizeAnnotation;
        Roles userRoles;
        boolean authorized = false;

        if (pageClass.isAnnotationPresent(AuthorizeInstantiation.class)) {
            userRoles = getRoles();

            if (userRoles != null) {
                authorizeAnnotation = pageClass.getAnnotation(AuthorizeInstantiation.class);

                OUTER:
                for (String role : userRoles) {
                    for (int i = 0;
                         i < authorizeAnnotation.value().length;
                         i++) {
                        if (authorizeAnnotation.value()[i].equalsIgnoreCase(role)) {
                            authorized = true;
                            break OUTER;
                        }
                    }
                }
            }
        } else {
            // no authorize annotation available
            authorized = true;
        }

        return authorized;
    }

    private static final Homepage UserHomepage;
    private static final Homepage ManagerHomepage = new Homepage(CustomerAdminPage.class, Optional.<PageParameters>absent());
    private static final Homepage AdminHomepage = new Homepage(MainConfigPage.class, Optional.<PageParameters>absent());
    private static final Homepage ReportHomepage = new Homepage(ReportPage.class, Optional.<PageParameters>absent());

    static {
        PageParameters parameters = new PageParameters();
        parameters.add(MonthOverviewPage.PARAM_OPEN, MonthOverviewPage.OpenPanel.TIMESHEET);

        UserHomepage = new Homepage(MonthOverviewPage.class, Optional.of(parameters));
    }

    public static Homepage getHomepageForRole(Roles roles) {
        if (roles.contains(UserRole.ROLE_USER)) {
            return UserHomepage;
        } else if (roles.contains(UserRole.ROLE_MANAGER)) {
            return ManagerHomepage;
        } else if (roles.contains(UserRole.ROLE_ADMIN)) {
            return AdminHomepage;
        } else if (roles.contains(UserRole.ROLE_REPORT)) {
            return ReportHomepage;
        } else {
            return UserHomepage;
        }
    }

    public static class Homepage {
        public final Class<? extends Page> homePage;
        public final PageParameters parameters;

        public Homepage(Class<? extends Page> homePage, Optional<PageParameters> parameters) {
            this.homePage = homePage;
            this.parameters = parameters.or(new PageParameters());
        }
    }
}