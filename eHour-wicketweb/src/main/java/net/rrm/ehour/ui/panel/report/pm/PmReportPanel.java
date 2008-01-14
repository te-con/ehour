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

package net.rrm.ehour.ui.panel.report.pm;

import java.util.ArrayList;

import net.rrm.ehour.report.reports.ProjectManagerReport;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.ui.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.border.GreyRoundedBorder;
import net.rrm.ehour.ui.model.DateModel;
import net.rrm.ehour.ui.model.FloatModel;
import net.rrm.ehour.ui.panel.report.AbstractReportPanel;
import net.rrm.ehour.ui.util.CommonWebUtil;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.markup.html.resources.StyleSheetReference;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;

/**
 * PM Report panel
 **/

public class PmReportPanel extends AbstractReportPanel
{
	private static final long serialVersionUID = -1735419536027937563L;
	
	/**
	 * 
	 * @param id
	 */
	public PmReportPanel(String id, ProjectManagerReport report)
	{
		super(id);

		setOutputMarkupId(true);
		
		// Report model
		StringResourceModel reportTitle = new StringResourceModel("pmReport.header", 
																this, null, 
																new Object[]{report.getProject().getFullName(),
																			 new DateModel(report.getReportRange().getDateStart(), config),
																			 new DateModel(report.getReportRange().getDateEnd(), config)});
		
		GreyRoundedBorder greyBorder = new GreyRoundedBorder("reportFrame", reportTitle, null, null);
		add(greyBorder);
		GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("blueFrame");
		greyBorder.add(blueBorder);
		
		blueBorder.add(new ListView("report", new ArrayList<AssignmentAggregateReportElement>(report.getAggregates()))
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item)
			{
				AssignmentAggregateReportElement aggregate = (AssignmentAggregateReportElement)item.getModelObject();
				
				item.add(new Label("user", aggregate.getProjectAssignment().getUser().getFullName()));
				item.add(new Label("role", aggregate.getProjectAssignment().getRole()));
				item.add(new Label("type", new ResourceModel(CommonWebUtil.getResourceKeyForProjectAssignmentType(aggregate.getProjectAssignment().getAssignmentType()))));
				item.add(new Label("booked", new FloatModel(aggregate.getHours(), config)));
				item.add(new Label("allotted", new FloatModel(aggregate.getProjectAssignment().getAllottedHours(), config)));
				item.add(new Label("overrun", new FloatModel(aggregate.getProjectAssignment().getAllowedOverrun(), config)));
				item.add(new Label("available", new FloatModel(aggregate.getAvailableHours(), config)));
				item.add(new Label("percentageUsed", new FloatModel(aggregate.getProgressPercentage(), config)));
				
			}
		});
		
		// borrow css from the general reports.
		add(new StyleSheetReference("reportStyle", new CompressedResourceReference(AbstractReportPanel.class, "style/reportStyle.css")));
	}
}
