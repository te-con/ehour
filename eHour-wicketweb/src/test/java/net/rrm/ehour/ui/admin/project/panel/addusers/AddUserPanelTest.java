package net.rrm.ehour.ui.admin.project.panel.addusers;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignmentType;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.service.project.service.ProjectAssignmentManagementService;
import net.rrm.ehour.service.project.service.ProjectAssignmentService;
import net.rrm.ehour.service.user.service.UserService;
import net.rrm.ehour.ui.DummyUIDataGenerator;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.TestPanelSource;
import org.junit.Before;
import org.junit.Test;


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
		tester.startPanel(new TestPanelSource()
		{
			public Panel getTestPanel(String panelId)
			{
				return new AddUserPanel(panelId, project);
			}
		});
	}
}
