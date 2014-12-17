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

package net.rrm.ehour.report.service;

import com.google.common.collect.Lists;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.persistence.project.dao.ProjectDao;
import net.rrm.ehour.persistence.report.dao.ReportAggregatedDao;
import net.rrm.ehour.persistence.user.dao.UserDao;
import net.rrm.ehour.persistence.user.dao.UserDepartmentDao;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.ReportCriteriaUpdateType;
import net.rrm.ehour.report.criteria.UserSelectedCriteria;
import net.rrm.ehour.timesheet.service.TimesheetLockService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import scala.Tuple2;
import scala.collection.immutable.List$;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReportCriteriaServiceImplTest {
    private ReportCriteriaServiceImpl reportCriteriaService;

    @Mock
    private ReportAggregatedDao reportAggregatedDAO;

    @Mock
    private UserDepartmentDao userDepartmentDAO;

    @Mock
    private CustomerAndProjectCriteriaFilter customerAndProjectCriteriaFilter;

    @Mock
    private UserAndDepartmentCriteriaFilter userAndDepartmentCriteriaFilter;

    @Mock
    private IndividualUserCriteriaSync individualUserCriteriaSync;

    @Mock
    private TimesheetLockService timesheetLockService;

    @Mock
    private UserDao userDao;

    @Mock
    private ProjectDao projectDao;

    @Before
    public void setup() {
        reportCriteriaService = new ReportCriteriaServiceImpl(reportAggregatedDAO,
                customerAndProjectCriteriaFilter, userAndDepartmentCriteriaFilter, individualUserCriteriaSync, timesheetLockService,
                userDao, projectDao);

        when(timesheetLockService.findAll()).thenReturn(List$.MODULE$.<TimesheetLock>empty());
    }

    @Test
    public void should_sync_criteria_for_single_user() {
        UserSelectedCriteria userSelectedCriteria = new UserSelectedCriteria();
        userSelectedCriteria.setSelectedReportType(UserSelectedCriteria.ReportType.INDIVIDUAL_USER);
        userSelectedCriteria.setUsers(Arrays.asList(new User(1)));
        ReportCriteria reportCriteria = new ReportCriteria(userSelectedCriteria);

        reportCriteriaService.syncUserReportCriteria(reportCriteria, ReportCriteriaUpdateType.UPDATE_ALL);

        verify(individualUserCriteriaSync).syncCriteriaForIndividualUser(reportCriteria);
    }

    @Test
    public void should_sync_criteria_for_global_for_all_users() {
        UserSelectedCriteria userSelectedCriteria = new UserSelectedCriteria();
        userSelectedCriteria.setSelectedReportType(UserSelectedCriteria.ReportType.REPORT);
        userSelectedCriteria.setUsers(Arrays.asList(new User(1)));
        ReportCriteria reportCriteria = new ReportCriteria(userSelectedCriteria);

        Tuple2<List<UserDepartment>, List<User>> apply = new Tuple2<List<UserDepartment>, List<User>>(Lists.<UserDepartment>newArrayList(), Lists.<User>newArrayList());
        when(userAndDepartmentCriteriaFilter.getAvailableUsers(userSelectedCriteria)).thenReturn(apply);

        reportCriteriaService.syncUserReportCriteria(reportCriteria, ReportCriteriaUpdateType.UPDATE_USERS_AND_DEPTS);

        verify(userAndDepartmentCriteriaFilter).getAvailableUsers(userSelectedCriteria);
    }

    @Test
    public void should_sync_criteria_for_global_for_all_customers() {
        UserSelectedCriteria userSelectedCriteria = new UserSelectedCriteria();
        userSelectedCriteria.setSelectedReportType(UserSelectedCriteria.ReportType.REPORT);
        userSelectedCriteria.setUsers(Arrays.asList(new User(1)));

        Tuple2<List<Customer>, List<Project>> apply = new Tuple2<List<Customer>, List<Project>>(Lists.<Customer>newArrayList(), Lists.<Project>newArrayList());
        when(customerAndProjectCriteriaFilter.getAvailableCustomers(userSelectedCriteria)).thenReturn(apply);

        ReportCriteria reportCriteria = new ReportCriteria(userSelectedCriteria);
        reportCriteriaService.syncUserReportCriteria(reportCriteria, ReportCriteriaUpdateType.UPDATE_CUSTOMERS_AND_PROJECTS);

        verify(customerAndProjectCriteriaFilter).getAvailableCustomers(userSelectedCriteria);
    }

    @Test
    public void should_return_empty_users_and_projects_when_no_filter_is_provided() {
        UsersAndProjects usersAndProjects = reportCriteriaService.criteriaToUsersAndProjects(new UserSelectedCriteria());

        assertTrue("Users should be empty", usersAndProjects.getUsers().isEmpty());
        assertTrue("Projects should be empty", usersAndProjects.getProjects().isEmpty());
    }

    @Test
    public void should_return_single_user_when_its_provided() {
        User user = UserObjectMother.createUser();

        UserSelectedCriteria userSelectedCriteria = new UserSelectedCriteria();
        userSelectedCriteria.setUsers(Lists.newArrayList(user));

        UsersAndProjects usersAndProjects = reportCriteriaService.criteriaToUsersAndProjects(userSelectedCriteria);

        assertThat(userSelectedCriteria.getUsers(), contains(user));
        assertTrue("Projects should be empty", usersAndProjects.getProjects().isEmpty());
    }

    @Test
    public void should_find_users_when_a_department_is_provided() {
        UserSelectedCriteria userSelectedCriteria = new UserSelectedCriteria();
        UserDepartment userDepartment = UserDepartmentObjectMother.createUserDepartment(1);
        userSelectedCriteria.setDepartments(Lists.newArrayList(userDepartment));

        User matchingUser = UserObjectMother.createUser();
        matchingUser.setUserDepartment(userDepartment);

        User nonMatchingUser = UserObjectMother.createUser();
        UserDepartment wrongDepartment = UserDepartmentObjectMother.createUserDepartment(2);
        wrongDepartment.setCode("cc");
        nonMatchingUser.setUserDepartment(wrongDepartment);

        when(userDao.findUsers(true)).thenReturn(Lists.newArrayList(matchingUser, nonMatchingUser));

        UsersAndProjects usersAndProjects = reportCriteriaService.criteriaToUsersAndProjects(userSelectedCriteria);

        assertEquals(1, usersAndProjects.getUsers().size());
        assertThat(usersAndProjects.getUsers(), contains(matchingUser));
        assertTrue("Projects should be empty", usersAndProjects.getProjects().isEmpty());
    }

    @Test
    public void should_find_projects_when_project_is_provided() {
        Project project = ProjectObjectMother.createProject(1);

        UserSelectedCriteria userSelectedCriteria = new UserSelectedCriteria();
        userSelectedCriteria.setProjects(Lists.newArrayList(project));

        UsersAndProjects usersAndProjects = reportCriteriaService.criteriaToUsersAndProjects(userSelectedCriteria);

        assertThat(userSelectedCriteria.getProjects(), contains(project));
        assertTrue("Users should be empty", usersAndProjects.getUsers().isEmpty());
    }

    @Test
    public void should_find_projects_when_customer_is_provided() {
        Customer customer = CustomerObjectMother.createCustomer();

        UserSelectedCriteria userSelectedCriteria = new UserSelectedCriteria();
        List<Customer> customers = Lists.newArrayList(customer);
        userSelectedCriteria.setCustomers(customers);

        Project project = ProjectObjectMother.createProject(1);
        when(projectDao.findProjectForCustomers(customers, true)).thenReturn(Lists.newArrayList(project));

        UsersAndProjects usersAndProjects = reportCriteriaService.criteriaToUsersAndProjects(userSelectedCriteria);

        assertThat(usersAndProjects.getProjects(), contains(project));
        assertTrue("Users should be empty", usersAndProjects.getUsers().isEmpty());
    }

    @Test
    public void should_find_only_billable_projects() {
        UserSelectedCriteria userSelectedCriteria = new UserSelectedCriteria();

        Project billableProject = ProjectObjectMother.createProject(1);
        billableProject.setBillable(true);

        Project notBillableProject = ProjectObjectMother.createProject(2);
        notBillableProject.setBillable(false);

        when(projectDao.findAllActive()).thenReturn(Lists.newArrayList(billableProject, notBillableProject));
        userSelectedCriteria.setOnlyBillableProjects(true);

        UsersAndProjects usersAndProjects = reportCriteriaService.criteriaToUsersAndProjects(userSelectedCriteria);

        assertEquals(1, usersAndProjects.getProjects().size());
        assertThat(usersAndProjects.getProjects(), contains(billableProject));
        assertTrue("Users should be empty", usersAndProjects.getUsers().isEmpty());
    }
}
