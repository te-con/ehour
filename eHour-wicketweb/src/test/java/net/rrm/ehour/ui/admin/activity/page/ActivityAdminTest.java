package net.rrm.ehour.ui.admin.activity.page;

import com.google.common.collect.Lists;
import net.rrm.ehour.activity.service.ActivityService;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.UserObjectMother;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import org.junit.Test;

import java.util.ArrayList;

import static org.easymock.EasyMock.*;

public class ActivityAdminTest extends BaseSpringWebAppTester {

    private ActivityService activityService;

    private ProjectService projectService;

    @Test
    public void testActivityAdminRender() {
        activityService = createMock(ActivityService.class);
        projectService = createMock(ProjectService.class);

        getMockContext().putBean("activityService", activityService);
        getMockContext().putBean("projectService", projectService);

        expect(activityService.getActivities()).andReturn(new ArrayList<Activity>());
        expect(projectService.getActiveProjects()).andReturn(new ArrayList<Project>());
        expect(userService.getUsers()).andReturn(Lists.newArrayList(UserObjectMother.createUser()));

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
