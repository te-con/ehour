/**
 * Created on May 22, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.panel.overview.projectoverview;

import java.util.ArrayList;
import java.util.Collection;

import net.rrm.ehour.timesheet.dto.UserProjectStatus;
import net.rrm.ehour.ui.border.GreyRoundedBorder;
import net.rrm.ehour.ui.model.CurrencyModel;
import net.rrm.ehour.ui.model.DateModel;
import net.rrm.ehour.ui.model.FloatModel;
import net.rrm.ehour.ui.session.EhourWebSession;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.markup.html.resources.StyleSheetReference;

/**
 * Panel showing overview
 */

public class ProjectOverviewPanel extends Panel
{
	private static final long serialVersionUID = -5935376941518756941L;

	/**
	 * 
	 * @param id
	 * @param projectStatus
	 */
	public ProjectOverviewPanel(String id, Collection<UserProjectStatus> projectStatusSet)
	{
		super(id);
		
		Label	label;
		EhourWebSession session = (EhourWebSession)getSession();

		// TODO i18n
		GreyRoundedBorder greyBorder = new GreyRoundedBorder("greyBorder", "Aggregated per month");
		
		greyBorder.add(new Label("projectLabel", "Project"));
		greyBorder.add(new Label("projectCodeLabel", "Project code"));
		greyBorder.add(new Label("customerLabel", "Customer"));
		
		label = new Label("rateLabel", "Rate");
		label.setVisible(session.getEhourConfig().isShowTurnover());
		greyBorder.add(label);
		
		greyBorder.add(new Label("bookedHoursLabel", "Booked hours"));

		label = new Label("turnoverLabel", "Turnover");
		label.setVisible(session.getEhourConfig().isShowTurnover());
		greyBorder.add(label);
		
		ListView view = new ListView("projectStatus", new ArrayList<UserProjectStatus>(projectStatusSet))
		{
			public void populateItem(ListItem item)
			{
				EhourWebSession session = (EhourWebSession)getSession();
				
				UserProjectStatus projectStatus = (UserProjectStatus) item.getModelObject();
				item.add(new Label("projectName", projectStatus.getProjectAssignment().getProject().getName()));
				item.add(new Label("projectCode", projectStatus.getProjectAssignment().getProject().getProjectCode()));
				
				Label label = new Label("customerName", projectStatus.getProjectAssignment().getProject().getCustomer().getName());
				item.add(label);
				
				label = new Label("rate", new CurrencyModel(projectStatus.getProjectAssignment().getHourlyRate(), session.getEhourConfig()));
				label.setVisible(session.getEhourConfig().isShowTurnover());
				item.add(label);

				label = new Label("totalHours", new FloatModel(projectStatus.getHours(), session.getEhourConfig()));
				item.add(label);

				label = new Label("turnover", new FloatModel(projectStatus.getTurnOver(), session.getEhourConfig()));
				label.setVisible(session.getEhourConfig().isShowTurnover());
				item.add(label);
				
				label = new Label("validStart", new DateModel(projectStatus.getProjectAssignment().getDateStart(),
						session.getEhourConfig()));
				label.setEscapeModelStrings(false);
				item.add(label);

				label = new Label("validEnd", new DateModel(projectStatus.getProjectAssignment().getDateEnd(),
						session.getEhourConfig()));
				label.setEscapeModelStrings(false);
				item.add(label);

				label = new Label("totalHours", new FloatModel(projectStatus.getTotalBookedHours(), session.getEhourConfig()));
				label.setVersioned(projectStatus.getProjectAssignment().getAssignmentType().isAllottedType());
				item.add(label);
				
				label = new Label("remainingHours", new FloatModel(projectStatus.getHoursRemaining(), session.getEhourConfig()));
				label.setVersioned(projectStatus.getProjectAssignment().getAssignmentType().isAllottedType());
				item.add(label);
			}
		};
		
		greyBorder.add(view);
		add(greyBorder);
		
		add(new StyleSheetReference("aggregateStyle", new CompressedResourceReference(ProjectOverviewPanel.class, "style/aggregate.css")));
	}
}
