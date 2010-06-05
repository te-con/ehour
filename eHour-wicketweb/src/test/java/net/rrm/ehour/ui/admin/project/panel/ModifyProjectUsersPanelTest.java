package net.rrm.ehour.ui.admin.project.panel;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.project.service.ProjectAssignmentManagementService;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.ui.DummyUIDataGenerator;
import net.rrm.ehour.user.service.UserService;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.TestPanelSource;
import org.junit.Before;
import org.junit.Test;


public class ModifyProjectUsersPanelTest extends AbstractProjectPanelTest
{
	private ProjectAssignmentManagementService managementService;
	private ProjectAssignmentService projectAssignmentService;
	private UserService userService;
	
	@Before
	public void init()
	{
		projectAssignmentService = createMock(ProjectAssignmentService.class);
		mockContext.putBean("projectAssignmentService", projectAssignmentService);
		
		managementService = createMock(ProjectAssignmentManagementService.class);
		mockContext.putBean("projectAssignmentManagementService", managementService);
		
		userService = createMock(UserService.class);
		mockContext.putBean("userService", userService);
	}

	@Test
	public void shouldRender()
	{
		final Project project = new Project();

		defineExpectations(project);
		
		replay(managementService, projectAssignmentService, userService);
		
		startPanel(project);
		
		verify(managementService, projectAssignmentService, userService);
	}

	@Test
	public void shouldSubmit()
	{
		managementService.updateProjectAssignment(isA(ProjectAssignment.class));

		final Project project = new Project();

		defineExpectations(project);
		
		replay(managementService, projectAssignmentService, userService);

		startPanel(project);
		
		tester.clickLink("panel:border:submit", true);
		
		verify(managementService, projectAssignmentService, userService);
	}

	
	@SuppressWarnings("serial")
	private void startPanel(final Project project)
	{
		tester.startPanel(new TestPanelSource()
		{
			public Panel getTestPanel(String panelId)
			{
				return new ModifyProjectUsersPanel(panelId, project);
			}
		});
	}
	
	private void defineExpectations(Project project)
	{
		expect(projectAssignmentService.getProjectAssignments(project, true))
			.andReturn(assignments);
	
		expect(projectAssignmentService.getProjectAssignmentTypes())
			.andReturn(DummyUIDataGenerator.getProjectAssignmentTypes());
	
		expect(userService.getUsers(UserRole.CONSULTANT))
			.andReturn(DummyUIDataGenerator.getUserList());
	}
}
