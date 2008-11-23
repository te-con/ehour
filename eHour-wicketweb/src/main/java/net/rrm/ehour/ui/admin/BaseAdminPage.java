/**
 * Created on Aug 6, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.admin;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import net.rrm.ehour.ui.common.page.BasePage;
import net.rrm.ehour.ui.common.panel.contexthelp.ContextualHelpPanel;
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

	public BaseAdminPage(ResourceModel pageTitle, IModel model, String headerResourceId, String bodyResourceId)
	{
		super(pageTitle, model);
		
		add(new AdminNavPanel("adminNav"));
		
		// contextual help
		add(new ContextualHelpPanel("contextHelp", headerResourceId, bodyResourceId));

	}

}
