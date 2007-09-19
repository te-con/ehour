/**
 * Created on Sep 18, 2007
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

package net.rrm.ehour.ui.panel.report.criteria;

import net.rrm.ehour.ui.border.GreySquaredRoundedBorder;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * Report criteria panel
 **/

public class ReportCriteriaPanel extends Panel
{
	private static final long serialVersionUID = -7865322191390719584L;

	/**
	 * 
	 * @param id
	 * @param model
	 */
	public ReportCriteriaPanel(String id, IModel model)
	{
		super(id, model);
		
		
		GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder("border");
		add(greyBorder);
		
		setOutputMarkupId(true);		
	}

}
