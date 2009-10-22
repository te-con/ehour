package net.rrm.ehour.ui.admin.project.panel.addusers;

import java.util.List;

import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.ui.admin.assignment.dto.AssignmentAdminBackingBeanImpl;
import net.rrm.ehour.ui.admin.assignment.panel.form.AssignmentFormComponentContainerPanel;
import net.rrm.ehour.ui.admin.assignment.panel.form.AssignmentFormComponentContainerPanel.DisplayOption;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import net.rrm.ehour.user.service.UserService;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class AddUserPanel extends AbstractBasePanel
{
	private static final long serialVersionUID = 3355347499532412241L;

	@SpringBean(name = "userService")
	private UserService userService;
	
	public AddUserPanel(String id, Project project)
	{
		super(id);

		Form form = new Form("form");
		add(form);

		List<User> users = userService.getUsers(UserRole.CONSULTANT);
		
		form.add(new ListUsersPanel("users", users));
		
		ProjectAssignment assignment = new ProjectAssignment();
		AssignmentAdminBackingBeanImpl bBean = new AssignmentAdminBackingBeanImpl(assignment);
		
		form.add(new AssignmentFormComponentContainerPanel("assignmentInfo", form, new CompoundPropertyModel(bBean), DisplayOption.HIDE_DELETE_BUTTON, DisplayOption.HIDE_PROJECT_SELECTION));
	}
}
