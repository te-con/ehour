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

import net.rrm.ehour.domain.User;
import net.rrm.ehour.persistence.report.dao.ReportAggregatedDao;
import net.rrm.ehour.persistence.user.dao.UserDepartmentDao;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.ReportCriteriaUpdateType;
import net.rrm.ehour.report.criteria.UserSelectedCriteria;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ReportCriteriaServiceImplTest {
    private ReportCriteriaServiceImpl reportCriteriaService;

    @Mock
    private ReportAggregatedDao reportAggregatedDAO;

    @Mock
    private UserDepartmentDao userDepartmentDAO;

    @Mock
    private CustomerCriteriaFilter customerCriteriaFilter;

    @Mock
    private ProjectCriteriaFilter projectCriteriaFilter;

    @Mock
    private UserCriteriaFilter userCriteriaFilter;

    @Mock
    private IndividualUserCriteriaSync individualUserCriteriaSync;

    @Before
    public void setup() {
        reportCriteriaService = new ReportCriteriaServiceImpl(userDepartmentDAO, reportAggregatedDAO, customerCriteriaFilter, projectCriteriaFilter, userCriteriaFilter, individualUserCriteriaSync);
    }

    @Test
    public void should_sync_criteria_for_single_user() {
        UserSelectedCriteria userSelectedCriteria = new UserSelectedCriteria();
        userSelectedCriteria.addReportType(UserSelectedCriteria.ReportType.INDIVIDUAL_USER);
        userSelectedCriteria.setUsers(Arrays.asList(new User(1)));
        ReportCriteria reportCriteria = new ReportCriteria(userSelectedCriteria);

        reportCriteriaService.syncUserReportCriteria(reportCriteria, ReportCriteriaUpdateType.UPDATE_ALL);

        verify(individualUserCriteriaSync).syncCriteriaForIndividualUser(reportCriteria);
    }

    @Test
    public void should_sync_criteria_for_global_for_all_users() {
        UserSelectedCriteria userSelectedCriteria = new UserSelectedCriteria();
        userSelectedCriteria.addReportType(UserSelectedCriteria.ReportType.REPORT);
        userSelectedCriteria.setUsers(Arrays.asList(new User(1)));
        ReportCriteria reportCriteria = new ReportCriteria(userSelectedCriteria);

        reportCriteriaService.syncUserReportCriteria(reportCriteria, ReportCriteriaUpdateType.UPDATE_USERS_AND_DEPTS);

        verify(userCriteriaFilter).getAvailableUsers(userSelectedCriteria);
    }

    @Test
    public void should_sync_criteria_for_global_for_all_customers() {
        UserSelectedCriteria userSelectedCriteria = new UserSelectedCriteria();
        userSelectedCriteria.addReportType(UserSelectedCriteria.ReportType.REPORT);
        userSelectedCriteria.setUsers(Arrays.asList(new User(1)));
        ReportCriteria reportCriteria = new ReportCriteria(userSelectedCriteria);

        reportCriteriaService.syncUserReportCriteria(reportCriteria, ReportCriteriaUpdateType.UPDATE_CUSTOMERS_AND_PROJECTS);

        verify(customerCriteriaFilter).getAvailableCustomers(userSelectedCriteria);
        verify(projectCriteriaFilter).getAvailableProjects(userSelectedCriteria);

    }

    @Test
    public void should_sync_criteria_for_global_for_all_departments() {
        UserSelectedCriteria userSelectedCriteria = new UserSelectedCriteria();
        userSelectedCriteria.addReportType(UserSelectedCriteria.ReportType.REPORT);
        userSelectedCriteria.setUsers(Arrays.asList(new User(1)));
        ReportCriteria reportCriteria = new ReportCriteria(userSelectedCriteria);

        reportCriteriaService.syncUserReportCriteria(reportCriteria, ReportCriteriaUpdateType.UPDATE_ALL);

        verify(userDepartmentDAO).findAll();
        verify(reportAggregatedDAO).getMinMaxDateTimesheetEntry();
    }
}
