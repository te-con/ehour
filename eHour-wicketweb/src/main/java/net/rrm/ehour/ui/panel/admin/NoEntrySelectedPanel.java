/**
 * Created on Aug 24, 2007
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

package net.rrm.ehour.ui.panel.admin;

import net.rrm.ehour.ui.border.GreyRoundedBorder;
import net.rrm.ehour.ui.border.GreySquaredRoundedBorder;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

/**
 * No entry selected
 **/

public class NoEntrySelectedPanel extends Panel
{
	private static final long serialVersionUID = -4318947090257979895L;

	public NoEntrySelectedPanel(String id)
	{
		this(id, false);
	}
	
	public NoEntrySelectedPanel(String id, boolean useRoundBorder)
	{
		super(id);
		
		Border greyBorder = (useRoundBorder) ? new GreyRoundedBorder("border", 500) : new GreySquaredRoundedBorder("border");
		add(greyBorder);

		greyBorder.add(new Label("noEntry", new ResourceModel("admin.noEditEntrySelected")));
	}	
}
