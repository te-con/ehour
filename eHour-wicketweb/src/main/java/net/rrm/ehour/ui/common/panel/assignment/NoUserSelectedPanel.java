/**
 * Created on Oct 23, 2007
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

package net.rrm.ehour.ui.common.panel.assignment;

import net.rrm.ehour.ui.common.border.GreyRoundedBorder;

import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

/**
 * Same as NoEntrySelected just with a hidden dojo date picker. Bug in DOjo prevents
 * to load the dojo js through ajax, needs to be there on initial load
 **/

public class NoUserSelectedPanel extends Panel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoUserSelectedPanel(String id, String resourceId)
	{
		super(id);
		
		this.setOutputMarkupId(true);
		Border greyBorder = new GreyRoundedBorder("border", 450);
		add(greyBorder);

		greyBorder.add(new Label("noEntry", new ResourceModel(resourceId)));

		// this is what we call a hack sir
        final DateTextField dateStart = new DateTextField("dummyDate", new Model(), new StyleDateConverter("S-", true));
        dateStart.add(new DatePicker());		
		greyBorder.add(dateStart);
		
	}
}
