package net.rrm.ehour.ui.admin.project.panel.addusers;

import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import net.rrm.ehour.user.service.UserService;

import org.apache.wicket.spring.injection.annot.SpringBean;

public class AddUserPanel extends AbstractBasePanel
{
	private static final long serialVersionUID = 3355347499532412241L;

	@SpringBean(name = "userService")
	private UserService userService;
	
	public AddUserPanel(String id, Project project)
	{
		super(id);
		
		userService.getUsers(UserRole.CONSULTANT);
	}

}
