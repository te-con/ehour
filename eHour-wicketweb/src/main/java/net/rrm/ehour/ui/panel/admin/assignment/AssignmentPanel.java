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

import java.util.List;

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.project.domain.ProjectAssignmentType;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.ui.ajax.AjaxAwareContainer;
import net.rrm.ehour.ui.component.AddEditTabbedPanel;
import net.rrm.ehour.ui.model.AdminBackingBean;
import net.rrm.ehour.ui.panel.admin.assignment.dto.AssignmentAdminBackingBean;
import net.rrm.ehour.ui.util.CommonUIStaticData;
import net.rrm.ehour.user.domain.User;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IWrapModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Assignment panel displaying the list and the tabbed form for adding/editing
 **/

@SuppressWarnings("serial")
public class AssignmentPanel extends Panel implements AjaxAwareContainer
{
	private static final long serialVersionUID = -3721224427697057895L;
	
	@SpringBean
	private	ProjectAssignmentService	assignmentService;
	private AddEditTabbedPanel			tabbedPanel;
	private	AssignmentListPanel			listPanel;
	
	/**
	 * 
	 * @param id
	 * @param model
	 */
	public AssignmentPanel(String id,
							final User user,
							final List<Customer> customers,
							final List<ProjectAssignmentType> types)
	{
		super(id);
		
		setOutputMarkupId(true);

		listPanel = new AssignmentListPanel("assignmentList", user);
		add(listPanel);
		
		tabbedPanel = new AddEditTabbedPanel("assignmentTabs",
											new ResourceModel("admin.assignment.newAssignment"),
													new ResourceModel("admin.assignment.editAssignment"))
		{

			@Override
			protected Panel getAddPanel(String panelId)
			{
				return new AssignmentFormPanel(panelId,
												new CompoundPropertyModel(getAddBackingBean()),
												customers,
												types);
			}

			@Override
			protected Panel getEditPanel(String panelId)
			{
				return new AssignmentFormPanel(panelId,
												new CompoundPropertyModel(getEditBackingBean()),
												customers,
												types);			
			}

			@Override
			protected AdminBackingBean getNewAddBackingBean()
			{
				ProjectAssignment			projectAssignment;
				AssignmentAdminBackingBean	assignmentBean;
				
				projectAssignment = new ProjectAssignment();
				projectAssignment.setUser(user);
				projectAssignment.setActive(true);
				
				assignmentBean = new AssignmentAdminBackingBean(projectAssignment);

				return assignmentBean;			
			}

			@Override
			protected AdminBackingBean getNewEditBackingBean()
			{
				return new AssignmentAdminBackingBean(new ProjectAssignment());
			}
		};
		
		add(tabbedPanel);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.AjaxAwareContainer#ajaxRequestReceived(org.apache.wicket.ajax.AjaxRequestTarget, int, java.lang.Object)
	 */
	public void ajaxRequestReceived(AjaxRequestTarget target, int type, Object params)
	{
		ProjectAssignment	assignment;
		
		if (type == CommonUIStaticData.AJAX_LIST_CHANGE)
		{
			assignment = (ProjectAssignment)params;
			
			tabbedPanel.setEditBackingBean(
							new AssignmentAdminBackingBean(assignmentService.getProjectAssignment(assignment.getAssignmentId())));
			tabbedPanel.switchTabOnAjaxTarget(target, 1);
		}
		else if (type == CommonUIStaticData.AJAX_FORM_SUBMIT)
		{
			AssignmentAdminBackingBean	backingBean = (AssignmentAdminBackingBean)((((IWrapModel) params)).getWrappedModel()).getObject();
			assignment = backingBean.getProjectAssignment();
			
			assignmentService.assignUserToProject(assignment);
			
			listPanel.updateList(target, assignment.getUser());
			
			tabbedPanel.succesfulSave(target);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.AjaxAwareContainer#ajaxRequestReceived(org.apache.wicket.ajax.AjaxRequestTarget, int)
	 */
	public void ajaxRequestReceived(AjaxRequestTarget target, int type)
	{
		ajaxRequestReceived(target, type, null);
	}	
}
