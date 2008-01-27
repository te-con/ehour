/**
 * Created on Aug 23, 2007
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

package net.rrm.ehour.ui.panel.admin.assignment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.ui.ajax.AjaxAwareContainer;
import net.rrm.ehour.ui.border.GreyRoundedBorder;
import net.rrm.ehour.ui.model.DateModel;
import net.rrm.ehour.ui.model.FloatModel;
import net.rrm.ehour.ui.session.EhourWebSession;
import net.rrm.ehour.ui.sort.ProjectAssignmentComparator;
import net.rrm.ehour.ui.util.CommonWebUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
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
	private	Border			greyBorder;
	private ListView 		assignmentListView;
	
	/**
	 * 
	 * @param id
	 */
	public AssignmentListPanel(String id, User user)
	{
		super(id);
		
		config = ((EhourWebSession)getSession()).getEhourConfig();
	
		setOutputMarkupId(true);
	
		// TODO	fixed size
		greyBorder = new GreyRoundedBorder("border",
				 							new StringResourceModel("admin.assignment.assignmentsFor", 
				 											this, null, new Object[]{new Model(user.getFullName())}),
											450);
		add(greyBorder);
		greyBorder.add(getProjectAssignmentLists(user));
	}
	
	/**
	 * Update the list
	 * @param user
	 */
	public void updateList(AjaxRequestTarget target, User user)
	{
		assignmentListView.setList(getProjectAssignments(user));
		
		target.addComponent(this);
	}

	
	/**
	 * Get listview for project assignments
	 * @param user
	 * @return
	 */
	private ListView getProjectAssignmentLists(User user)
	{
		assignmentListView = new ListView("assignments", getProjectAssignments(user))
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
						((AjaxAwareContainer)(AssignmentListPanel.this.getParent()))
								.ajaxRequestReceived(target, CommonWebUtil.AJAX_LIST_CHANGE, assignment);
					}
				};

				AjaxLink	imgLink = new AjaxLink("imgLink")
				{
					@Override
					public void onClick(AjaxRequestTarget target)
					{
						((AjaxAwareContainer)(AssignmentListPanel.this.getParent()))
								.ajaxRequestReceived(target, CommonWebUtil.AJAX_LIST_CHANGE, assignment);
					}
				};

				item.add(imgLink);
				item.add(link);
				link.add(new Label("project", assignment.getProject().getFullName()));				
				item.add(new Label("customer", assignment.getProject().getCustomer().getFullName()));
				
				Label dateStart = new Label("dateStart", new DateModel(assignment.getDateStart(), config, DateModel.DATESTYLE_FULL_SHORT));
				dateStart.setEscapeModelStrings(false);
				item.add(dateStart);
				
				Label dateEnd = new Label("dateEnd", new DateModel(assignment.getDateEnd(), config, DateModel.DATESTYLE_FULL_SHORT));
				dateEnd.setEscapeModelStrings(false);
				item.add(dateEnd);
				
				item.add(new Label("assignmentType", 
							new ResourceModel(CommonWebUtil.getResourceKeyForProjectAssignmentType(assignment.getAssignmentType()))));
				
				item.add(new Label("role",
									(StringUtils.isBlank(assignment.getRole()))
										? "--"
										: assignment.getRole()));
				
				item.add(new Label("currency",  Currency.getInstance(config.getCurrency()).getSymbol(config.getCurrency())));
				item.add(new Label("rate", new FloatModel(assignment.getHourlyRate(), config)));

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
		List<ProjectAssignment> assignments = null;
		
		if (user.getUserId() != null)
		{
			assignments = projectService.getAllProjectsForUser(user);		
		}
		
		
		if (assignments != null)
		{
			Collections.sort(assignments, new ProjectAssignmentComparator(ProjectAssignmentComparator.ASSIGNMENT_COMPARE_CUSTDATEPRJ));
			setVisible(true);
		}
		else
		{
			assignments = new ArrayList<ProjectAssignment>();
		}
		
		return assignments;
	}
}
