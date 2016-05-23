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

package net.rrm.ehour.ui.manage.assignment;

import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.ProjectAssignmentType;
import net.rrm.ehour.domain.UserObjectMother;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import net.rrm.ehour.user.service.UserService;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AssignmentManagePageTest extends BaseSpringWebAppTester {
    @Test
    public void shouldRender() {
        UserService userService = mock(UserService.class);
        getMockContext().putBean("userService", userService);

        ProjectService projectService = mock(ProjectService.class);
        getMockContext().putBean("projectService", projectService);

        CustomerService customerService = mock(CustomerService.class);
        getMockContext().putBean("customerService", customerService);

        ProjectAssignmentService assignmentService = mock(ProjectAssignmentService.class);
        getMockContext().putBean("assignmentService", assignmentService);

        when(assignmentService.getProjectAssignmentTypes()).thenReturn(new ArrayList<ProjectAssignmentType>());

        Customer cust = new Customer(22);
        List<Customer> custs = new ArrayList<>();
        custs.add(cust);

        when(customerService.getActiveCustomers()).thenReturn(custs);

        when(userService.getUsers(UserRole.USER)).thenReturn(Arrays.asList(UserObjectMother.createUser()));

        getTester().startPage(AssignmentManagePage.class);
        getTester().assertRenderedPage(AssignmentManagePage.class);
        getTester().assertNoErrorMessage();
    }
}
