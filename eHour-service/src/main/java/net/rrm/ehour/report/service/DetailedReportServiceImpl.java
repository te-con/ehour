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

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.persistence.project.dao.ProjectDao;
import net.rrm.ehour.persistence.report.dao.DetailedReportDao;
import net.rrm.ehour.persistence.user.dao.UserDao;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.report.reports.element.LockableDate;
import net.rrm.ehour.timesheet.service.TimesheetLockService;
import net.rrm.ehour.util.EhourUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Report service for detailed reports implementation
 */
@Service("detailedReportService")
public class DetailedReportServiceImpl extends AbstractReportServiceImpl<FlatReportElement> implements DetailedReportService {
    private DetailedReportDao detailedReportDAO;

    DetailedReportServiceImpl() {
    }

    @Autowired
    public DetailedReportServiceImpl(DetailedReportDao detailedReportDAO, UserDao userDao, ProjectDao projectDao, ProjectService projectService, TimesheetLockService lockService) {
        super(userDao, projectDao, projectService, lockService);
        this.detailedReportDAO = detailedReportDAO;
    }

    public ReportData getDetailedReportData(ReportCriteria reportCriteria) {
        return getReportData(reportCriteria);
    }

    @Override
    protected List<FlatReportElement> getReportElements(List<User> users,
                                                        List<Project> projects,
                                                        List<Date> lockedDates,
                                                        DateRange reportRange) {
        List<FlatReportElement> elements = getElements(users, projects, reportRange);

        for (FlatReportElement element : elements) {
            Date date = element.getDayDate();
            element.setLockableDate(new LockableDate(date, lockedDates.contains(date)));
        }

        return elements;
    }

    private List<FlatReportElement> getElements(List<User> users, List<Project> projects, DateRange reportRange) {
        List<FlatReportElement> elements;

        if (users == null && projects == null) {
            elements = detailedReportDAO.getHoursPerDay(reportRange);
        } else if (projects == null) {
            elements = detailedReportDAO.getHoursPerDayForUsers(EhourUtil.getIdsFromDomainObjects(users), reportRange);
        } else if (users == null) {
            elements = detailedReportDAO.getHoursPerDayForProjects(EhourUtil.getIdsFromDomainObjects(projects), reportRange);
        } else {
            elements = detailedReportDAO.getHoursPerDayForProjectsAndUsers(EhourUtil.getIdsFromDomainObjects(projects),
                    EhourUtil.getIdsFromDomainObjects(users),
                    reportRange);
        }
        return elements;
    }
}
