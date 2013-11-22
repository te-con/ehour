package net.rrm.ehour.report.service

import net.rrm.ehour.report.criteria.ReportCriteria
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import net.rrm.ehour.persistence.user.dao.UserDepartmentDao
import net.rrm.ehour.persistence.project.dao.ProjectAssignmentDao
import net.rrm.ehour.persistence.report.dao.ReportAggregatedDao
import net.rrm.ehour.util._


@Service
class IndividualUserCriteriaSync @Autowired()(userDepartmentDAO: UserDepartmentDao, projectAssignmentDAO: ProjectAssignmentDao, reportAggregatedDAO: ReportAggregatedDao) {
  def syncCriteriaForIndividualUser(reportCriteria: ReportCriteria) {
    val availCriteria = reportCriteria.getAvailableCriteria
    val user = reportCriteria.getUserSelectedCriteria.getUsers.get(0)
    val assignments = toScala(projectAssignmentDAO.findProjectAssignmentsForUser(user.getUserId, reportCriteria.getUserSelectedCriteria.getReportRange))

    val customers = assignments.map(_.getProject.getCustomer).toSet
    val projects = assignments.map(_.getProject).toSet

    availCriteria.setCustomers(customers)
    availCriteria.setProjects(projects)
    availCriteria.setReportRange(reportAggregatedDAO.getMinMaxDateTimesheetEntry(user))
  }
}
