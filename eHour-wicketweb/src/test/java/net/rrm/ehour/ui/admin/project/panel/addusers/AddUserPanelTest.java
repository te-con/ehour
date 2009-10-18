package net.rrm.ehour.ui.admin.project.panel.addusers;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;
import java.util.List;

import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.ui.DummyUIDataGenerator;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;
import net.rrm.ehour.user.service.UserService;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.TestPanelSource;
import org.junit.Before;
import org.junit.Test;


public class AddUserPanelTest extends AbstractSpringWebAppTester
{
	private UserService userService;
	private Project project;

	@Before
	public void before()
	{
		userService = createMock(UserService.class);
		mockContext.putBean("userService", userService);
		
		project = new Project();
	}
	
	@Test
	public void shouldRender()
	{
		User user = DummyUIDataGenerator.getUser();
		List<User> users = Collections.singletonList(user);
		
		expect(userService.getUsers(UserRole.CONSULTANT))
			.andReturn(users);
		
		replay(userService);
	
		startPanel(project);
		
		verify(userService);
		
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
