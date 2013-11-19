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

import net.rrm.ehour.audit.annot.NonAuditable;
import net.rrm.ehour.persistence.report.dao.ReportAggregatedDao;
import net.rrm.ehour.persistence.user.dao.UserDepartmentDao;
import net.rrm.ehour.report.criteria.AvailableCriteria;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.ReportCriteriaUpdateType;
import net.rrm.ehour.report.criteria.UserSelectedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Report Criteria services
 */
@NonAuditable
@Service("reportCriteriaService")
public class ReportCriteriaServiceImpl implements ReportCriteriaService {
    private UserDepartmentDao userDepartmentDAO;

    private ReportAggregatedDao reportAggregatedDAO;

    private CustomerCriteriaFilter customerCriteriaFilter;

    private ProjectCriteriaFilter projectCriteriaFilter;

    private UserCriteriaFilter userCriteriaFilter;

    private IndividualUserCriteriaSync individualUserCriteriaSync;

    protected ReportCriteriaServiceImpl() {
    }

    @Autowired
    public ReportCriteriaServiceImpl(UserDepartmentDao userDepartmentDAO,
                                     ReportAggregatedDao reportAggregatedDAO,
                                     CustomerCriteriaFilter customerCriteriaFilter,
                                     ProjectCriteriaFilter projectCriteriaFilter,
                                     UserCriteriaFilter userCriteriaFilter,
                                     IndividualUserCriteriaSync individualUserCriteriaSync) {
        this.userDepartmentDAO = userDepartmentDAO;
        this.reportAggregatedDAO = reportAggregatedDAO;
        this.customerCriteriaFilter = customerCriteriaFilter;
        this.projectCriteriaFilter = projectCriteriaFilter;
        this.userCriteriaFilter = userCriteriaFilter;
        this.individualUserCriteriaSync = individualUserCriteriaSync;
    }

    /**
     * Update available report criteria
     */
    public ReportCriteria syncUserReportCriteria(ReportCriteria reportCriteria, ReportCriteriaUpdateType updateType) {
        UserSelectedCriteria userSelectedCriteria = reportCriteria.getUserSelectedCriteria();
        AvailableCriteria availCriteria = reportCriteria.getAvailableCriteria();

        if (userSelectedCriteria.isForGlobalReport() || userSelectedCriteria.isForPm()) {
            if (updateType == ReportCriteriaUpdateType.UPDATE_CUSTOMERS ||
                    updateType == ReportCriteriaUpdateType.UPDATE_ALL) {
                availCriteria.setCustomers(customerCriteriaFilter.getAvailableCustomers(userSelectedCriteria));
            }

            if (updateType == ReportCriteriaUpdateType.UPDATE_PROJECTS ||
                    updateType == ReportCriteriaUpdateType.UPDATE_ALL) {
                availCriteria.setProjects(projectCriteriaFilter.getAvailableProjects(userSelectedCriteria));
            }

            if (updateType == ReportCriteriaUpdateType.UPDATE_ALL) {
                availCriteria.setUserDepartments(userDepartmentDAO.findAll());

                availCriteria.setReportRange(reportAggregatedDAO.getMinMaxDateTimesheetEntry());
            }

            if (updateType == ReportCriteriaUpdateType.UPDATE_USERS ||
                    updateType == ReportCriteriaUpdateType.UPDATE_ALL) {
                availCriteria.setUsers(userCriteriaFilter.getAvailableUsers(userSelectedCriteria));
            }
        } else {
            individualUserCriteriaSync.syncCriteriaForIndividualUser(reportCriteria);
        }

        return reportCriteria;
    }
}
