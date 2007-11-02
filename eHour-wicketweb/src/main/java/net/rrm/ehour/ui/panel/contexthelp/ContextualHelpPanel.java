/**
 * Created on Jul 10, 2007
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

package net.rrm.ehour.ui.panel.contexthelp;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

/**
 * Contextual help panel, the one that appears most of the time in the left bottom
 **/

public class ContextualHelpPanel extends Panel
{
	private static final long serialVersionUID = 8054029544833333835L;

	/**
	 * 
	 * @param id
	 */
	
	public ContextualHelpPanel(String id)
	{
		this(id, "help.header", "help.body");
	}	
	
	/**
	 * 
	 * @param id
	 */
	public ContextualHelpPanel(String id, String headerResourceId, String bodyResourceId)
	{
		super(id);
		
		add(new Label("header", new ResourceModel(headerResourceId)));
		Label body = new Label("body", new ResourceModel(bodyResourceId));
		body.setEscapeModelStrings(false);
		add(body);
	}

}
