/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.report.panel.pm;

import java.util.ArrayList;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.report.reports.ProjectManagerReport;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.CommonWebUtil;
import net.rrm.ehour.ui.common.util.WebGeo;
import net.rrm.ehour.ui.report.panel.AbstractReportPanel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.markup.html.resources.StyleSheetReference;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;

/**
 * PM Report panel
 **/

public class PmReportPanel extends AbstractReportPanel
{
	private static final long serialVersionUID = -1735419536027937563L;
	
	public PmReportPanel(String id, ProjectManagerReport report)
	{
		super(id, WebGeo.NOT_DEFINED, WebGeo.W_CONTENT_MEDIUM);

		setOutputMarkupId(true);
		
		final EhourConfig config = EhourWebSession.getSession().getEhourConfig();
		
		// Report model
		StringResourceModel reportTitle = new StringResourceModel("pmReport.header", 
																this, null, 
																new Object[]{report.getProject().getFullName(),
																			 new DateModel(report.getReportRange().getDateStart(), config),
																			 new DateModel(report.getReportRange().getDateEnd(), config)});
		
		GreyRoundedBorder greyBorder = new GreyRoundedBorder("reportFrame", reportTitle, true, null, null, WebGeo.W_CONTENT_MEDIUM);
		add(greyBorder);
		GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("blueFrame");
		greyBorder.add(blueBorder);
		
		blueBorder.add(new ListView<AssignmentAggregateReportElement>("report", new ArrayList<AssignmentAggregateReportElement>(report.getAggregates()))
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<AssignmentAggregateReportElement> item)
			{
				AssignmentAggregateReportElement aggregate = item.getModelObject();
				
				item.add(new Label("user", aggregate.getProjectAssignment().getUser().getFullName()));
				item.add(new Label("role", aggregate.getProjectAssignment().getRole()));
				item.add(new Label("type", new ResourceModel(CommonWebUtil.getResourceKeyForProjectAssignmentType(aggregate.getProjectAssignment().getAssignmentType()))));
				item.add(new Label("booked", new Model<Float>(aggregate.getHours().floatValue())));
				item.add(new Label("allotted", new Model<Float>(aggregate.getProjectAssignment().getAllottedHours())));
				item.add(new Label("overrun", new Model<Float>(aggregate.getProjectAssignment().getAllowedOverrun())));
				item.add(new Label("available", new Model<Float>(aggregate.getAvailableHours())));
				item.add(new Label("percentageUsed", new Model<Float>(aggregate.getProgressPercentage())));
				
			}
		});
		
		// borrow css from the general reports.
		add(new StyleSheetReference("reportStyle", new CompressedResourceReference(AbstractReportPanel.class, "style/reportStyle.css")));
	}
}
