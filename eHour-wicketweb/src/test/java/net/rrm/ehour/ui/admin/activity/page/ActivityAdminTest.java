package net.rrm.ehour.ui.admin.activity.page;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;

import net.rrm.ehour.activity.service.ActivityService;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;
import net.rrm.ehour.user.service.UserService;

import org.junit.Before;
import org.junit.Test;

public class ActivityAdminTest extends AbstractSpringWebAppTester {

	private ActivityService activityService;
	
	private UserService userService;
	
	private ProjectService projectService;
	

	@Test
	public void testActivityAdminRender() {

		activityService = createMock(ActivityService.class);
		userService = createMock(UserService.class);
		projectService = createMock(ProjectService.class);

		getMockContext().putBean("activityService", activityService);
		getMockContext().putBean("userService", userService);
		getMockContext().putBean("projectService", projectService);
		
		expect(activityService.getActivities()).andReturn(new ArrayList<Activity>());		
		expect(projectService.getAllProjects(true)).andReturn(new ArrayList<Project>());
		expect(userService.getUsers()).andReturn(new ArrayList<User>());
		
		replay(activityService);
		replay(userService);
		replay(projectService);
		
		getTester().startPage(ActivityAdmin.class);
		getTester().assertRenderedPage(ActivityAdmin.class);
		getTester().assertNoErrorMessage();		
		
		verify(activityService);
		
		verify(userService);
		
		verify(projectService);
	}
}
