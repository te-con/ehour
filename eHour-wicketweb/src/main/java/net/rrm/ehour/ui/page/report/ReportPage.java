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

package net.rrm.ehour.ui.page.report;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.panel.contexthelp.ContextualHelpPanel;
import net.rrm.ehour.ui.panel.report.criteria.ReportCriteriaPanel;
import net.rrm.ehour.ui.panel.report.criteria.ReportTabbedPanel;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

/**
 * Reporting 
 **/

@AuthorizeInstantiation("ROLE_REPORT")
public class ReportPage extends BaseReportPage
{
	private static final long serialVersionUID = 6614404841734599622L;

	/**
	 * 
	 */
	public ReportPage()
	{
		super(new ResourceModel("report.title"), null);
		
		final ReportCriteria reportCriteria = getReportCriteria();
		
		// contextual help
		add(new ContextualHelpPanel("contextHelp"));
		
		List<AbstractTab> tabList = new ArrayList<AbstractTab>();
		
		tabList.add(new AbstractTab(new ResourceModel("criteria.title"))
		{
			@Override
			public Panel getPanel(String panelId)
			{
				return new ReportCriteriaPanel(panelId, reportCriteria);
			}
		});
		
		ReportTabbedPanel tabPanel = new ReportTabbedPanel("reportContainer", tabList);
		add(tabPanel);
		
		add(new Label("sidePanel", "dummy"));
	}
}
