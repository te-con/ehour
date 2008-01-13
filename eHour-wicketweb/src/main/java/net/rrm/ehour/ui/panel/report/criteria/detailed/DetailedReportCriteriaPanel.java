/**
 * Created on Dec 29, 2007
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

package net.rrm.ehour.ui.panel.report.criteria.detailed;

import net.rrm.ehour.ui.panel.report.criteria.BaseReportCriteriaPanel;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

/**
 * Report criteria for detailed reports
 **/

public class DetailedReportCriteriaPanel extends BaseReportCriteriaPanel
{
	private static final long serialVersionUID = 860033134082780427L;

	public DetailedReportCriteriaPanel(String id, IModel model)
	{
		super(id, model, false);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.panel.report.criteria.BaseReportCriteriaPanel#fillCriteriaForm(org.apache.wicket.markup.html.form.Form)
	 */
	@Override
	protected void fillCriteriaForm(Form form)
	{
	}
}
