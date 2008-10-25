/**
 * Created on Oct 24, 2008
 * Author: Thies
 *
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

package net.rrm.ehour.ui.page.user.prefs;

import net.rrm.ehour.ui.page.BasePage;
import net.rrm.ehour.ui.panel.user.form.prefs.UserPreferenceFormPanel;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.ResourceModel;

/**
 * User preference page 
 **/
@AuthorizeInstantiation("ROLE_CONSULTANT")
public class UserPreferencePage extends BasePage
{

	public UserPreferencePage()
	{
		super(new ResourceModel("userprefs.title"));
		
		add(new UserPreferenceFormPanel("preferenceForm", getEhourWebSession().getUser().getUser()));
	}
}
