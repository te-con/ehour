package net.rrm.ehour.ui.admin.assignment.panel;

import net.rrm.ehour.service.exception.ObjectNotFoundException;
import net.rrm.ehour.service.exception.ObjectNotUniqueException;
import net.rrm.ehour.service.exception.ParentChildConstraintException;
import net.rrm.ehour.service.exception.PasswordEmptyException;
import net.rrm.ehour.service.project.service.ProjectAssignmentManagementService;
import net.rrm.ehour.ui.admin.assignment.dto.AssignmentAdminBackingBean;
import net.rrm.ehour.ui.admin.assignment.panel.form.AssignmentFormComponentContainerPanel;
import net.rrm.ehour.ui.admin.assignment.panel.form.AssignmentFormComponentContainerPanel.DisplayOption;
import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.common.component.ServerMessageLabel;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.form.FormUtil;
import net.rrm.ehour.ui.common.model.AdminBackingBean;
import net.rrm.ehour.ui.common.panel.AbstractFormSubmittingPanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.WebGeo;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class AssignmentFormPanel extends AbstractFormSubmittingPanel<AssignmentAdminBackingBean>
{
	private static final long serialVersionUID = 7704141227799017924L;

	@SpringBean
	private ProjectAssignmentManagementService projectAssignmentManagementService;
	
	public AssignmentFormPanel(String id, final IModel<AssignmentAdminBackingBean> model)
	{
		super(id, model);

		setOutputMarkupId(true);
		
		setUpPage(model);
	}

	private void setUpPage(IModel<AssignmentAdminBackingBean> model)
	{
		Border greyBorder = new GreySquaredRoundedBorder("border", WebGeo.W_CONTENT_SMALL);
		add(greyBorder);
		
		final Form<AssignmentAdminBackingBean> form = new Form<AssignmentAdminBackingBean>("assignmentForm");
		greyBorder.add(form);
		

		// add submit form
		FormUtil.setSubmitActions(form 
									,((AssignmentAdminBackingBean)getDefaultModelObject()).getProjectAssignment().isDeletable() 
									,this
									,AssignmentAjaxEventType.ASSIGNMENT_UPDATED
									,AssignmentAjaxEventType.ASSIGNMENT_DELETED
									,EhourWebSession.getSession().getEhourConfig());
		
		form.add(new AssignmentFormComponentContainerPanel("formComponents", form, model, DisplayOption.SHOW_PROJECT_SELECTION, DisplayOption.SHOW_SAVE_BUTTON, DisplayOption.SHOW_DELETE_BUTTON));
		
		form.add(new ServerMessageLabel("serverMessage", "formValidationError"));
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
}
