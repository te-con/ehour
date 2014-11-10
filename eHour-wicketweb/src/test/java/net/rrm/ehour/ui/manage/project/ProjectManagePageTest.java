/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.manage.project;

import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.ProjectObjectMother;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.project.service.ProjectAssignmentManagementService;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import net.rrm.ehour.user.service.UserService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProjectManagePageTest extends BaseSpringWebAppTester {
    private ProjectService projectService;
    private UserService userService;
    private CustomerService customerService;

    @Before
    public void before() throws Exception {
        projectService = mock(ProjectService.class);
        getMockContext().putBean("projectService", projectService);

        userService = mock(UserService.class);
        getMockContext().putBean("userService", userService);

        customerService = mock(CustomerService.class);
        getMockContext().putBean("customerService", customerService);

        ProjectAssignmentService projectAssignmentService = mock(ProjectAssignmentService.class);
        getMockContext().putBean(projectAssignmentService);

        ProjectAssignmentManagementService assignmentManagementService = mock(ProjectAssignmentManagementService.class);
        getMockContext().putBean(assignmentManagementService);

    }

    @Test
    public void shouldRender() {
        when(customerService.getActiveCustomers())
                .thenReturn(new ArrayList<Customer>());

        when(userService.getUsersWithEmailSet())
                .thenReturn(new ArrayList<User>());

        when(projectService.getActiveProjects())
                .thenReturn(Arrays.asList(ProjectObjectMother.createProject(1)));

        tester.startPage(ProjectManagePage.class);
        tester.assertRenderedPage(ProjectManagePage.class);
        tester.assertNoErrorMessage();
    }
}
