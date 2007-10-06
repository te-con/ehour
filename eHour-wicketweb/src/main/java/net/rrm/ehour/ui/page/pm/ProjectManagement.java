/**
 * Created on Oct 6, 2007
 * Created by Thies Edeling
 * Created by Thies Edeling
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
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

package net.rrm.ehour.ui.page.pm;

import net.rrm.ehour.ui.page.BasePage;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.ResourceModel;

/**
 * Project management base station :)
 **/
@AuthorizeInstantiation("ROLE_PROJECTMANAGER")
public class ProjectManagement extends BasePage
{
	private static final long serialVersionUID = 898442184509251553L;

	/**
	 * Default constructor 
	 * @param pageTitle
	 * @param model
	 */
	public ProjectManagement()
	{
		super(new ResourceModel("pmReport.title"), null);
	}

}
