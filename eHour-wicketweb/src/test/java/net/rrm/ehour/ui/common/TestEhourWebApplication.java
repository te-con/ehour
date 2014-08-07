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

import net.rrm.ehour.ui.EhourWebApplication;
import net.rrm.ehour.ui.common.session.DevelopmentWebSession;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.login.page.SessionExpiredPage;
import org.apache.wicket.Component;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;
import org.apache.wicket.authroles.authorization.strategies.role.RoleAuthorizationStrategy;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;

import java.io.Serializable;

public class TestEhourWebApplication extends EhourWebApplication implements Serializable {
    private static final long serialVersionUID = -7336200909844170964L;
    private EhourWebSession session;
    private Roles authorizedRoles = null;

    private Boolean enableBookWholeWeek = true;

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


    @SuppressWarnings("serial")
    @Override
    public Session newSession(final Request request, final Response response) {
        session = new DevelopmentWebSession(request, authorizedRoles);

        return session;
    }

    public void setAuthorizedRoles(Roles authorizedRoles) {
        this.authorizedRoles = authorizedRoles;
    }

    public EhourWebSession getSession() {
        return session;
    }

    @Override
    public RuntimeConfigurationType getConfigurationType() {
        return RuntimeConfigurationType.DEPLOYMENT;
    }

    @Override
    protected void registerEhourHomeResourceLoader() {
    }

    @Override
    public Boolean isBookWholeWeekEnabled() {
        return enableBookWholeWeek;
    }

    public void setEnableBookWholeWeek(Boolean enableBookWholeWeek) {
        this.enableBookWholeWeek = enableBookWholeWeek;
    }
}
