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

package net.rrm.ehour.ui.admin.assignment.panel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.service.project.service.ProjectAssignmentService;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.event.EventPublisher;
import net.rrm.ehour.ui.common.event.PayloadAjaxEvent;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.sort.ProjectAssignmentComparator;
import net.rrm.ehour.ui.common.util.CommonWebUtil;
import net.rrm.ehour.ui.common.util.WebGeo;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * List of existing assignments 
 **/

@SuppressWarnings("serial")
public class AssignmentListPanel extends AbstractBasePanel<Void>
{
	private static final long serialVersionUID = -8798859357268916546L;

	@SpringBean
	private ProjectAssignmentService projectAssignmentService;
	private	EhourConfig		config;
	private	Border			greyBorder;
	private ListView<ProjectAssignment> assignmentListView;
	private User			user;
	
	/**
	 * 
	 * @param id
	 */
	public AssignmentListPanel(String id, User user)
	{
		super(id);
		
		config = EhourWebSession.getSession().getEhourConfig();
	
		setOutputMarkupId(true);
	
		greyBorder = new GreyRoundedBorder("border",
				 							new StringResourceModel("admin.assignment.assignmentsFor", 
				 											this, null, new Object[]{new Model<String>(user.getFullName())}),
											WebGeo.W_CONTENT_SMALL);
		add(greyBorder);
		greyBorder.add(getProjectAssignmentLists(user));
		
		greyBorder.add(getActivateCheckbox());
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
	 * 
	 * @return
	 */
	private AjaxCheckBox getActivateCheckbox()
	{
		final AjaxCheckBox	deactivateBox = new AjaxCheckBox("filterToggle", new Model<Boolean>(getEhourWebSession().getHideInactiveSelections()))
		{
			private static final long serialVersionUID = 2585047163449150793L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
            	getEhourWebSession().setHideInactiveSelections(Boolean.valueOf(getValue()));
            	updateList(target, user);
			}
		};
		
		return deactivateBox;
	}
	
	/**
	 * Get listview for project assignments
	 * @param user
	 * @return
	 */
	private ListView<ProjectAssignment> getProjectAssignmentLists(User user)
	{
		this.user = user;
		
		assignmentListView = new ListView<ProjectAssignment>("assignments", getProjectAssignments(user))
		{
			@Override
			protected void populateItem(ListItem<ProjectAssignment> item)
			{
				final ProjectAssignment assignment = item.getModelObject();
				
				AjaxLink<Void> link = new AjaxLink<Void>("itemLink")
				{
					@Override
					public void onClick(AjaxRequestTarget target)
					{
						EventPublisher.publishAjaxEvent(AssignmentListPanel.this, new PayloadAjaxEvent<ProjectAssignment>(AssignmentAjaxEventType.ASSIGNMENT_LIST_CHANGE,
																													assignment));
					}
				};

				AjaxLink<Void> imgLink = new AjaxLink<Void>("imgLink")
				{
					@Override
					public void onClick(AjaxRequestTarget target)
					{
						EventPublisher.publishAjaxEvent(AssignmentListPanel.this, new PayloadAjaxEvent<ProjectAssignment>(AssignmentAjaxEventType.ASSIGNMENT_LIST_CHANGE,
																													assignment));
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
				item.add(new Label("rate", new Model<Float>(assignment.getHourlyRate())));

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
			assignments = projectAssignmentService.getProjectAssignmentsForUser(user, getEhourWebSession().getHideInactiveSelections().booleanValue());		
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
