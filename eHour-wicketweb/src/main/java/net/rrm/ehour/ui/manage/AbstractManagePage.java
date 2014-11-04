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

package net.rrm.ehour.ui.manage;

import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.ui.common.page.AbstractBasePage;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

/**
 * Base page for admin adding admin nav and contextual help
 */
@AuthorizeInstantiation(value = {UserRole.ROLE_ADMIN, UserRole.ROLE_MANAGER})
public abstract class AbstractManagePage<T> extends AbstractBasePage<T> {
    private static final long serialVersionUID = -1388562551962543722L;

    public AbstractManagePage(ResourceModel pageTitle) {
        this(pageTitle, null);
    }

    public AbstractManagePage(ResourceModel pageTitle, IModel<T> model) {
        super(pageTitle, model);
    }

    protected Boolean isHideInactive() {
        return EhourWebSession.getSession().getHideInactiveSelections();
    }

}
