package net.rrm.ehour.ui.admin.project.panel.addusers;

import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignmentType;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.project.service.ProjectAssignmentManagementService;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.ui.DummyUIDataGenerator;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;
import net.rrm.ehour.user.service.UserService;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.ITestPanelSource;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.easymock.EasyMock.*;


public class AddUserPanelTest extends AbstractSpringWebAppTester
{
	private UserService userService;
	private Project project;
	private ProjectAssignmentManagementService managementService;
	private ProjectAssignmentService assignmentService;

	@Before
	public void before()
	{
		userService = createMock(UserService.class);
		mockContext.putBean("userService", userService);
		
		managementService = createMock(ProjectAssignmentManagementService.class);
		mockContext.putBean("projectAssignmentManagementService", managementService);
		
		assignmentService = createMock(ProjectAssignmentService.class);
		mockContext.putBean("projectAssignmentService", assignmentService);
		
		project = new Project();
	}
	
	@Test
	public void shouldRender()
	{
		User user = DummyUIDataGenerator.getUser();
		List<User> users = Collections.singletonList(user);
		
		expect(assignmentService.getProjectAssignmentTypes())
			.andReturn(new ArrayList<ProjectAssignmentType>());
		
		expect(userService.getUsers(UserRole.CONSULTANT))
			.andReturn(users);
		
		replay(userService, managementService, assignmentService);
	
		startPanel(project);
		
		verify(userService, managementService, assignmentService);
		
	}
	
	@SuppressWarnings("serial")
	private void startPanel(final Project project)
	{
		tester.startPanel(new ITestPanelSource()
		{
			public Panel getTestPanel(String panelId)
			{
				return new AddUserPanel(panelId, project);
			}
		});
	}
}
