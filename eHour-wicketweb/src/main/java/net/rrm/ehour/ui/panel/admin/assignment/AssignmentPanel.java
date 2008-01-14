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

import java.util.List;

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.project.domain.ProjectAssignmentType;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.ui.component.AddEditTabbedPanel;
import net.rrm.ehour.ui.model.AdminBackingBean;
import net.rrm.ehour.ui.panel.admin.AbstractAjaxAwareAdminPanel;
import net.rrm.ehour.ui.panel.admin.assignment.dto.AssignmentAdminBackingBean;
import net.rrm.ehour.ui.util.CommonWebUtil;
import net.rrm.ehour.user.domain.User;

import org.apache.log4j.Logger;
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
public class AssignmentPanel extends AbstractAjaxAwareAdminPanel
{
	private static final long serialVersionUID = -3721224427697057895L;
	private	final static Logger	logger = Logger.getLogger(AssignmentPanel.class);
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
		
//		tabbedPanel.setVisible(user.getUserId() != null)
		
		add(tabbedPanel);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.AjaxAwareContainer#ajaxRequestReceived(org.apache.wicket.ajax.AjaxRequestTarget, int, java.lang.Object)
	 */
	public void ajaxRequestReceived(AjaxRequestTarget target, int type, Object params)
	{
		ProjectAssignment	assignment;
		
		switch (type)
		{
			case CommonWebUtil.AJAX_LIST_CHANGE:
			{
				assignment = (ProjectAssignment)params;
				
				try
				{
					tabbedPanel.setEditBackingBean(
									new AssignmentAdminBackingBean(assignmentService.getProjectAssignment(assignment.getAssignmentId())));
					tabbedPanel.switchTabOnAjaxTarget(target, 1);
				} catch (ObjectNotFoundException e)
				{
					logger.error("While getting assignment", e);
				}
				break;
			}
			case CommonWebUtil.AJAX_FORM_SUBMIT:
			case CommonWebUtil.AJAX_DELETE:
			{
				AssignmentAdminBackingBean	backingBean = (AssignmentAdminBackingBean)((((IWrapModel) params)).getWrappedModel()).getObject();
				assignment = backingBean.getProjectAssignmentForSave();

				try
				{
					if (type == CommonWebUtil.AJAX_DELETE)
					{
							assignmentService.deleteProjectAssignment(assignment.getAssignmentId());
					}
					else if (type == CommonWebUtil.AJAX_FORM_SUBMIT)
					{
						assignmentService.assignUserToProject(assignment);
					}
					
					listPanel.updateList(target, assignment.getUser());
					
					tabbedPanel.succesfulSave(target);
				} catch (Exception e)
				{
					logger.error("While saving/deleting assignment", e);
					tabbedPanel.failedSave(backingBean, target);
				}
				
				break;
			}
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
