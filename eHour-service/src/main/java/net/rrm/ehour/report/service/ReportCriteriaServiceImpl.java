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
import net.rrm.ehour.audit.annot.NonAuditable;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.persistence.report.dao.ReportAggregatedDao;
import net.rrm.ehour.report.criteria.AvailableCriteria;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.ReportCriteriaUpdateType;
import net.rrm.ehour.report.criteria.UserSelectedCriteria;
import net.rrm.ehour.timesheet.service.TimesheetLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.Tuple2;
import scala.collection.convert.WrapAsJava$;

import java.util.List;

/**
 * Report Criteria services
 */
@NonAuditable
@Service("reportCriteriaService")
public class ReportCriteriaServiceImpl implements ReportCriteriaService {
    private ReportAggregatedDao reportAggregatedDAO;

    private CustomerAndProjectCriteriaFilter customerAndProjectCriteriaFilter;

    private UserAndDepartmentCriteriaFilter userAndDepartmentCriteriaFilter;

    private IndividualUserCriteriaSync individualUserCriteriaSync;
    private TimesheetLockService lockService;

    protected ReportCriteriaServiceImpl() {
    }

    @Autowired
    public ReportCriteriaServiceImpl(ReportAggregatedDao reportAggregatedDAO,
                                     CustomerAndProjectCriteriaFilter customerAndProjectCriteriaFilter,
                                     UserAndDepartmentCriteriaFilter userAndDepartmentCriteriaFilter,
                                     IndividualUserCriteriaSync individualUserCriteriaSync,
                                     TimesheetLockService lockService) {
        this.reportAggregatedDAO = reportAggregatedDAO;
        this.customerAndProjectCriteriaFilter = customerAndProjectCriteriaFilter;
        this.userAndDepartmentCriteriaFilter = userAndDepartmentCriteriaFilter;
        this.individualUserCriteriaSync = individualUserCriteriaSync;
        this.lockService = lockService;
    }

    /**
     * Update available report criteria
     */
    public ReportCriteria syncUserReportCriteria(ReportCriteria reportCriteria, ReportCriteriaUpdateType updateType) {
        UserSelectedCriteria userSelectedCriteria = reportCriteria.getUserSelectedCriteria();
        AvailableCriteria availCriteria = reportCriteria.getAvailableCriteria();

        List<TimesheetLock> timesheetLocks = Lists.newArrayList(WrapAsJava$.MODULE$.asJavaCollection(lockService.findAll()));
        availCriteria.setTimesheetLocks(timesheetLocks);

        if (userSelectedCriteria.isForGlobalReport() || userSelectedCriteria.isForPm()) {
            if (updateType == ReportCriteriaUpdateType.UPDATE_CUSTOMERS_AND_PROJECTS ||
                    updateType == ReportCriteriaUpdateType.UPDATE_ALL) {
                Tuple2<List<Customer>, List<Project>> available = customerAndProjectCriteriaFilter.getAvailableCustomers(userSelectedCriteria);
                List<Customer> updatedAvailableCustomers = available._1();
                availCriteria.setCustomers(updatedAvailableCustomers);
                List<Project> updatedAvailableProjects = available._2();
                availCriteria.setProjects(updatedAvailableProjects);

                List<Customer> updatedSelectedCustomers = Lists.newArrayList();

                if (userSelectedCriteria.getCustomers() != null) {
                    for (Customer updatedAvailableCustomer : updatedAvailableCustomers) {
                        if (userSelectedCriteria.getCustomers().contains(updatedAvailableCustomer)) {
                            updatedSelectedCustomers.add(updatedAvailableCustomer);
                        }
                    }
                }


                userSelectedCriteria.setCustomers(updatedSelectedCustomers);

                List<Project> updatedSelectedProjects = Lists.newArrayList();

                if (userSelectedCriteria.getProjects() != null) {
                    for (Project updatedAvailableProject : updatedAvailableProjects) {
                        if (userSelectedCriteria.getProjects().contains(updatedAvailableProject)) {
                            updatedSelectedProjects.add(updatedAvailableProject);
                        }
                    }
                }

                userSelectedCriteria.setProjects(updatedSelectedProjects);

            }

            if (updateType == ReportCriteriaUpdateType.UPDATE_USERS_AND_DEPTS ||
                    updateType == ReportCriteriaUpdateType.UPDATE_ALL) {

                Tuple2<List<UserDepartment>, List<User>> avail = userAndDepartmentCriteriaFilter.getAvailableUsers(userSelectedCriteria);

                availCriteria.setUserDepartments(avail._1());
                availCriteria.setUsers(avail._2());
            }

            if (updateType == ReportCriteriaUpdateType.UPDATE_ALL) {
                availCriteria.setReportRange(reportAggregatedDAO.getMinMaxDateTimesheetEntry());
            }
        } else {
            individualUserCriteriaSync.syncCriteriaForIndividualUser(reportCriteria);
        }

        return reportCriteria;
    }
}
