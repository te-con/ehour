/**
 * Created on Aug 23, 2007
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

package net.rrm.ehour.ui.panel.admin.assignment;

import java.util.Collections;
import java.util.List;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.ui.border.GreyRoundedBorder;
import net.rrm.ehour.ui.model.DateModel;
import net.rrm.ehour.ui.session.EhourWebSession;
import net.rrm.ehour.ui.sort.ProjectAssignmentComparator;
import net.rrm.ehour.user.domain.User;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * List of existing assignments 
 **/

@SuppressWarnings("serial")
public class AssignmentListPanel extends Panel
{
	private static final long serialVersionUID = -8798859357268916546L;

	@SpringBean
	private ProjectService	projectService;
	private	EhourConfig		config;
	/**
	 * 
	 * @param id
	 */
	public AssignmentListPanel(String id, User user)
	{
		super(id);
		
		config = ((EhourWebSession)getSession()).getEhourConfig();
	
		GreyRoundedBorder greyBorder = new GreyRoundedBorder("border");
		add(greyBorder);
		greyBorder.add(getProjectAssignmentLists(user));
	}

	/**
	 * Get listview for project assignments
	 * @param user
	 * @return
	 */
	private ListView getProjectAssignmentLists(User user)
	{
		ListView assignmentListView = new ListView("assignments", getProjectAssignments(user))
		{
			@Override
			protected void populateItem(ListItem item)
			{
				final ProjectAssignment assignment = (ProjectAssignment)item.getModelObject();
				
				AjaxLink	link = new AjaxLink("itemLink")
				{
					@Override
					public void onClick(AjaxRequestTarget target)
					{
//						replaceAssignmentPanel(target, user);
					}
				};
				
				item.add(link);
				link.add(new Label("project", assignment.getProject().getName()));				
				item.add(new Label("code", assignment.getProject().getProjectCode()));
				item.add(new Label("customer", assignment.getProject().getCustomer().getFullName()));
				
				Label dateStart = new Label("dateStart", new DateModel(assignment.getDateStart(), config));
				dateStart.setEscapeModelStrings(false);
				item.add(dateStart);
				
				Label dateEnd = new Label("dateEnd", new DateModel(assignment.getDateEnd(), config));
				dateEnd.setEscapeModelStrings(false);
				item.add(dateEnd);

			}
		};
		
		return assignmentListView;
	}	
	
	/**
	 * Get project assignments for user
	 * @param user
	 * @return
	 */
	private List<ProjectAssignment> getProjectAssignments(User user)
	{
		List<ProjectAssignment> assignments = projectService.getAllProjectsForUser(user.getUserId());
		
		if (assignments != null && assignments.size() > 0)
		{
			Collections.sort(assignments, new ProjectAssignmentComparator(ProjectAssignmentComparator.ASSIGNMENT_COMPARE_CUSTDATEPRJ));
		}
		
		return assignments;
	}
}
