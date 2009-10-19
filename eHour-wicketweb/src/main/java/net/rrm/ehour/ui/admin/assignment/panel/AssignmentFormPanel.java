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


import java.util.Arrays;
import java.util.List;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.exception.PasswordEmptyException;
import net.rrm.ehour.project.service.ProjectAssignmentManagementService;
import net.rrm.ehour.ui.admin.assignment.dto.AssignmentAdminBackingBean;
import net.rrm.ehour.ui.common.ajax.AjaxAwareContainer;
import net.rrm.ehour.ui.common.ajax.AjaxEvent;
import net.rrm.ehour.ui.common.ajax.AjaxEventType;
import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.common.component.PlaceholderPanel;
import net.rrm.ehour.ui.common.component.ServerMessageLabel;
import net.rrm.ehour.ui.common.form.FormUtil;
import net.rrm.ehour.ui.common.model.AdminBackingBean;
import net.rrm.ehour.ui.common.panel.AbstractFormSubmittingPanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.WebGeo;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Assignment form (and yes, it's a little (too) big & complex)
 **/

public class AssignmentFormPanel extends AbstractFormSubmittingPanel implements AjaxAwareContainer
{
	public enum DisplayOption
	{
		PROJECT_SELECTION,
		DELETE_BUTTON;
	}
	
	private static final long serialVersionUID = -85486044225123470L;
	
	@SpringBean
	private ProjectAssignmentManagementService projectAssignmentManagementService;
	
	private EhourConfig		config;

	private AssignmentTypeFormPartPanel typeFormPartPanel;

	public AssignmentFormPanel(String id, final IModel model)
	{
		this(id, model, DisplayOption.PROJECT_SELECTION, DisplayOption.DELETE_BUTTON);
	}
	
	public AssignmentFormPanel(String id, final IModel model, DisplayOption... displayOptions)
	{
		super(id, model);
		
		config = EhourWebSession.getSession().getEhourConfig();
		
		setOutputMarkupId(true);
		
		setUpPage(this, model, Arrays.asList(displayOptions));
	}

	@Override
	protected final void processFormSubmit(AjaxRequestTarget target, AdminBackingBean backingBean, AjaxEventType type) throws Exception
	{
		AssignmentAdminBackingBean assignmentBackingBean = (AssignmentAdminBackingBean) backingBean;
		
		if (type == AssignmentAjaxEventType.ASSIGNMENT_UPDATED)
		{
			persistAssignment(assignmentBackingBean);
		}
		else if (type == AssignmentAjaxEventType.ASSIGNMENT_DELETED)
		{
			deleteAssignment(assignmentBackingBean);
		}		
	}		
	
	/**
	 * Persist user
	 * @param userBackingBean
	 * @throws ObjectNotUniqueException 
	 * @throws PasswordEmptyException 
	 */
	private void persistAssignment(AssignmentAdminBackingBean backingBean)
	{
		projectAssignmentManagementService.assignUserToProject(backingBean.getProjectAssignmentForSave());
	}
	
	/**
	 * 
	 * @param backingBean
	 * @throws ParentChildConstraintException 
	 * @throws ObjectNotFoundException 
	 */
	private void deleteAssignment(AssignmentAdminBackingBean backingBean) throws ObjectNotFoundException, ParentChildConstraintException
	{
		projectAssignmentManagementService.deleteProjectAssignment(backingBean.getProjectAssignment().getAssignmentId());
	}
	
	
	/**
	 * Setup form
	 */
	private void setUpPage(WebMarkupContainer parent, final IModel model, List<DisplayOption> displayOptions)
	{
		Border greyBorder = new GreySquaredRoundedBorder("border", WebGeo.W_CONTENT_SMALL);
		add(greyBorder);
		
		final Form form = new Form("assignmentForm");
		greyBorder.add(form);
		
		// setup the customer & project dropdowns
		form.add(createProjectSelection("projectSelection", model, displayOptions));
		
		// Add rate & role
		addRateRole(form, model);

		// Project duration form components
		addProjectDuration(form, model);

		// active
		form.add(new CheckBox("projectAssignment.active"));
		
		// data save label
		form.add(new ServerMessageLabel("serverMessage", "formValidationError"));
		
		// add submit form
		FormUtil.setSubmitActions(form 
									,((AssignmentAdminBackingBean)model.getObject()).getProjectAssignment().isDeletable() 
									,this
									,AssignmentAjaxEventType.ASSIGNMENT_UPDATED
									,AssignmentAjaxEventType.ASSIGNMENT_DELETED
									,config);
		
		parent.add(greyBorder);
	}
	
	private WebMarkupContainer createProjectSelection(String id, IModel model, List<DisplayOption> displayOptions)
	{
		if (displayOptions.contains(DisplayOption.PROJECT_SELECTION))
		{
			return new AssignmentProjectSelectionPanel(id, model);
		}
		else
		{
			return new PlaceholderPanel(id);
		}
	}
	
	/**
	 * Add rate, role & active
	 * @param form
	 * @param model
	 */
	private void addRateRole(Form form, IModel model)
	{
		form.add(new AssignmentRateRoleFormPartPanel("rateRole", model));
	}
	
	/**
	 * Add project duration
	 * @param form
	 * @param model
	 */
	private void addProjectDuration(Form form, final IModel model)
	{
		typeFormPartPanel = new AssignmentTypeFormPartPanel("assignmentType", model, form);
		form.add(typeFormPartPanel);
	}
	
	@Override
	public boolean ajaxEventReceived(AjaxEvent ajaxEvent)
	{
		if (ajaxEvent.getEventType() == AssignmentProjectSelectionPanel.EntrySelectorAjaxEventType.PROJECT_CHANGE)
		{
			updateNotifiableComponents(ajaxEvent.getTarget());
			
			return false;
		}
		
		return super.ajaxEventReceived(ajaxEvent);
	}
	
	private void updateNotifiableComponents(AjaxRequestTarget target)
	{
		Component[] components = typeFormPartPanel.getNotifiableComponents();
		
		for (Component component : components)
		{
			target.addComponent(component);
		}
	}
}
