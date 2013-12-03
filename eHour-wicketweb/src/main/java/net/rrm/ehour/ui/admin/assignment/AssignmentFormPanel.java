package net.rrm.ehour.ui.admin.assignment;

import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.project.service.ProjectAssignmentManagementService;
import net.rrm.ehour.ui.admin.assignment.form.AssignmentFormComponentContainerPanel;
import net.rrm.ehour.ui.admin.assignment.form.AssignmentFormComponentContainerPanel.DisplayOption;
import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.common.component.ServerMessageLabel;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.form.FormConfig;
import net.rrm.ehour.ui.common.form.FormUtil;
import net.rrm.ehour.ui.common.model.AdminBackingBean;
import net.rrm.ehour.ui.common.panel.AbstractFormSubmittingPanel;
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
		Border greyBorder = new GreySquaredRoundedBorder("border", WebGeo.AUTO);
		add(greyBorder);
		
		final Form<AssignmentAdminBackingBean> form = new Form<AssignmentAdminBackingBean>("assignmentForm", model);
		greyBorder.add(form);

		// add submit form
        boolean deletable = ((AssignmentAdminBackingBean) getDefaultModelObject()).getProjectAssignment().isDeletable();
        FormConfig formConfig = FormConfig.forForm(form).withDelete(deletable).withSubmitTarget(this)
                .withDeleteEventType(AssignmentAjaxEventType.ASSIGNMENT_DELETED)
                .withSubmitEventType(AssignmentAjaxEventType.ASSIGNMENT_UPDATED);


        FormUtil.setSubmitActions(formConfig);
		
		form.add(new AssignmentFormComponentContainerPanel("formComponents", form, model, DisplayOption.SHOW_PROJECT_SELECTION, DisplayOption.SHOW_SAVE_BUTTON, DisplayOption.SHOW_DELETE_BUTTON));
		
		form.add(new ServerMessageLabel("serverMessage", "formValidationError"));
	}
	
	@Override
	protected final boolean processFormSubmit(AjaxRequestTarget target, AdminBackingBean backingBean, AjaxEventType type) throws Exception
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
        return true;
    }
	
	private void persistAssignment(AssignmentAdminBackingBean backingBean)
	{
		projectAssignmentManagementService.assignUserToProject(backingBean.getProjectAssignmentForSave());
	}
	
	private void deleteAssignment(AssignmentAdminBackingBean backingBean) throws ObjectNotFoundException, ParentChildConstraintException
	{
		projectAssignmentManagementService.deleteProjectAssignment(backingBean.getProjectAssignment());
	}	
}
