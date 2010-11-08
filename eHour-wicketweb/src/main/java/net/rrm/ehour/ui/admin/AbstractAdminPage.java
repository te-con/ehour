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

package net.rrm.ehour.ui.admin;

import net.rrm.ehour.ui.common.page.AbstractBasePage;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

/**
 * Base page for admin adding admin nav and contextual help
 **/

@AuthorizeInstantiation("ROLE_ADMIN")
public abstract class AbstractAdminPage<T> extends AbstractBasePage<T>
{
	private static final long serialVersionUID = -1388562551962543722L;

	public AbstractAdminPage(ResourceModel pageTitle, IModel<T> model, String headerResourceId, String bodyResourceId)
	{
		super(pageTitle, model);

//		add(new AdminNavPanel("adminNav"));

		// contextual help
//		add(new ContextualHelpPanel("contextHelp", headerResourceId, bodyResourceId));

	}

}
