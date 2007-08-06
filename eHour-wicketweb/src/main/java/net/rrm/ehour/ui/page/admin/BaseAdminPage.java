/**
 * Created on Aug 6, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.page.admin;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import net.rrm.ehour.ui.page.BasePage;
import net.rrm.ehour.ui.panel.contexthelp.ContextualHelpPanel;
import net.rrm.ehour.ui.panel.nav.admin.AdminNavPanel;

/**
 * Base page for admin adding admin nav and contextual help
 **/

@AuthorizeInstantiation("ROLE_ADMIN")
public class BaseAdminPage extends BasePage
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1388562551962543722L;

	public BaseAdminPage(ResourceModel pageTitle, IModel model)
	{
		super(pageTitle, model);
		
		add(new AdminNavPanel("adminNav"));
		
		// contextual help
		add(new ContextualHelpPanel("contextHelp"));

	}

}
