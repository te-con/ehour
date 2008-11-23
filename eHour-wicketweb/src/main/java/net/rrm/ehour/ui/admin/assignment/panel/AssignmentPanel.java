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

package net.rrm.ehour.ui.admin.assignment.panel;

import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.ui.admin.assignment.dto.AssignmentAdminBackingBeanImpl;
import net.rrm.ehour.ui.common.ajax.AjaxEvent;
import net.rrm.ehour.ui.common.ajax.AjaxEventType;
import net.rrm.ehour.ui.common.ajax.PayloadAjaxEvent;
import net.rrm.ehour.ui.common.component.AddEditTabbedPanel;
import net.rrm.ehour.ui.common.model.AdminBackingBean;
import net.rrm.ehour.ui.panel.AbstractFormSubmittingPanel;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Assignment panel displaying the list and the tabbed form for adding/editing
 **/

@SuppressWarnings("serial")
public class AssignmentPanel extends AbstractFormSubmittingPanel
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
							final User user)
	{
		super(id);
		
		setOutputMarkupId(true);

		listPanel = new AssignmentListPanel("assignmentList", user);
		add(listPanel);
		
		tabbedPanel = new AddEditTabbedPanel("assignmentTabs",
												new ResourceModel("admin.assignment.newAssignment"),
												new ResourceModel("admin.assignment.editAssignment"),
												new ResourceModel("admin.assignment.noEditEntrySelected"))
		{

			@Override
			protected Panel getAddPanel(String panelId)
			{
				return new AssignmentFormPanel(panelId,
												new CompoundPropertyModel(getAddBackingBean()));
			}

			@Override
			protected Panel getEditPanel(String panelId)
			{
				return new AssignmentFormPanel(panelId,
												new CompoundPropertyModel(getEditBackingBean()));
			}

			@Override
			protected AdminBackingBean getNewAddBackingBean()
			{
				return AssignmentAdminBackingBeanImpl.createAssignmentAdminBackingBean(user);
			}

			@Override
			protected AdminBackingBean getNewEditBackingBean()
			{
				return AssignmentAdminBackingBeanImpl.createAssignmentAdminBackingBean(user);
			}
		};
		
//		tabbedPanel.setVisible(user.getUserId() != null)
		
		add(tabbedPanel);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.common.panel.noentry.AbstractAjaxAwareAdminPanel#ajaxEventReceived(net.rrm.ehour.ui.common.ajax.AjaxEvent)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean ajaxEventReceived(AjaxEvent ajaxEvent)
	{
		AjaxEventType type = ajaxEvent.getEventType();
		
		if (type == AssignmentAjaxEventType.ASSIGNMENT_LIST_CHANGE)
		{
			try
			{
				ProjectAssignment assignment = ((PayloadAjaxEvent<ProjectAssignment>)ajaxEvent).getPayload();
				assignment = assignmentService.getProjectAssignment(assignment.getAssignmentId());
				
				tabbedPanel.setEditBackingBean(
								new AssignmentAdminBackingBeanImpl(assignmentService.getProjectAssignment(assignment.getAssignmentId())));
				tabbedPanel.switchTabOnAjaxTarget(ajaxEvent.getTarget(), 1);
			} catch (ObjectNotFoundException e)
			{
				logger.error("While getting assignment", e);
				return false;
			}
		}
		
		if (type == AssignmentAjaxEventType.ASSIGNMENT_DELETED
				|| type == AssignmentAjaxEventType.ASSIGNMENT_UPDATED)
		{
			AssignmentAdminBackingBeanImpl	backingBean = (AssignmentAdminBackingBeanImpl)((PayloadAjaxEvent<AdminBackingBean>)ajaxEvent).getPayload();
			ProjectAssignment assignment = backingBean.getProjectAssignmentForSave();

			listPanel.updateList(ajaxEvent.getTarget(), assignment.getUser());
				
			tabbedPanel.succesfulSave(ajaxEvent.getTarget());
		}
		
		return true;
	}
}
