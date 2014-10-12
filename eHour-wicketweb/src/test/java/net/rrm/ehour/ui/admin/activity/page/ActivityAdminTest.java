package net.rrm.ehour.ui.admin.activity.page;

import com.google.common.collect.Lists;
import com.richemont.jira.JiraService;
import com.richemont.windchill.WindChillUpdateService;
import net.rrm.ehour.activity.service.ActivityService;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.UserObjectMother;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import org.junit.Test;

import java.util.ArrayList;

import static org.easymock.EasyMock.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ActivityAdminTest extends BaseSpringWebAppTester {

    private ActivityService activityService;

    private ProjectService projectService;

    @Test
    public void testActivityAdminRender() {
        activityService = mock(ActivityService.class);
        projectService = mock(ProjectService.class);

        getMockContext().putBean("activityService", activityService);
        getMockContext().putBean("projectService", projectService);

        JiraService jiraService = createMock(JiraService.class);
        getMockContext().putBean(jiraService);

        WindChillUpdateService windChillUpdateService = createMock(WindChillUpdateService.class);
        getMockContext().putBean(windChillUpdateService);

        when(activityService.getActivities()).thenReturn(new ArrayList<Activity>());
        when(projectService.getActiveProjects()).thenReturn(new ArrayList<Project>());
        when(userService.getUsers()).thenReturn(Lists.newArrayList(UserObjectMother.createUser()));

        getTester().startPage(ActivityAdmin.class);
        getTester().assertRenderedPage(ActivityAdmin.class);
        getTester().assertNoErrorMessage();
    }
}
