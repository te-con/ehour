package net.rrm.ehour.ui.admin.project.panel.addusers;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.ui.admin.assignment.dto.AssignmentAdminBackingBeanImpl;
import net.rrm.ehour.ui.admin.assignment.panel.form.AssignmentFormComponentContainerPanel;
import net.rrm.ehour.ui.admin.assignment.panel.form.AssignmentFormComponentContainerPanel.DisplayOption;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;

public class AddUserPanel extends AbstractBasePanel
{
	private static final long serialVersionUID = 3355347499532412241L;

	
	@SuppressWarnings("serial")
	public AddUserPanel(String id, Project project)
	{
		super(id);

		Form form = new Form("form");
		add(form);
		
		
		final List<User> users = new ArrayList<User>();
		ListUsersPanel listUsersPanel = new ListUsersPanel("users", users);
		form.add(listUsersPanel);
		
		ProjectAssignment assignment = new ProjectAssignment();
		AssignmentAdminBackingBeanImpl bBean = new AssignmentAdminBackingBeanImpl(assignment);
		
		form.add(new AssignmentFormComponentContainerPanel("assignmentInfo", form, new CompoundPropertyModel(bBean), DisplayOption.HIDE_DELETE_BUTTON, DisplayOption.HIDE_PROJECT_SELECTION));
		
		AjaxButton button = new AjaxButton("submit", form)
		{
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form)
			{
				System.out.println(users);
			}
		};
		
		add(button);
		
	}
}
