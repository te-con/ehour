package net.rrm.ehour.report.service

import net.rrm.ehour.domain.ProjectAssignment
import net.rrm.ehour.persistence.project.dao.ProjectAssignmentDao
import net.rrm.ehour.persistence.report.dao.ReportAggregatedDao
import net.rrm.ehour.persistence.user.dao.UserDepartmentDao
import net.rrm.ehour.report.criteria.ReportCriteria
import net.rrm.ehour.util._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class IndividualUserCriteriaSync @Autowired()(userDepartmentDAO: UserDepartmentDao, projectAssignmentDAO: ProjectAssignmentDao, reportAggregatedDAO: ReportAggregatedDao) {
  def syncCriteriaForIndividualUser(reportCriteria: ReportCriteria) {
    def filterCustomers(xs: List[ProjectAssignment]) = if (reportCriteria.getUserSelectedCriteria.isOnlyActiveCustomers) xs.filter(p => p.getProject.getCustomer.isActive) else xs

    def filterProjects(xs: List[ProjectAssignment]) = if (reportCriteria.getUserSelectedCriteria.isOnlyActiveProjects) xs.filter(p => p.getProject.isActive) else xs

    def filterBillable(xs: List[ProjectAssignment]) = if (reportCriteria.getUserSelectedCriteria.isOnlyBillableProjects) xs.filter(p => p.getProject.isBillable) else xs

    val availCriteria = reportCriteria.getAvailableCriteria
    val user = reportCriteria.getUserSelectedCriteria.getUsers.get(0)
    val assignments: List[ProjectAssignment] = toScala(projectAssignmentDAO.findAllProjectAssignmentsForUser(user.getUserId, reportCriteria.getUserSelectedCriteria.getReportRange))

    val filteredAssignments = filterBillable(filterProjects(filterCustomers(assignments)))

    val customers = filteredAssignments.map(_.getProject.getCustomer).toSet
    val projects = filteredAssignments.map(_.getProject).toSet

    availCriteria.setCustomers(customers)
    availCriteria.setProjects(projects)
    availCriteria.setReportRange(reportAggregatedDAO.getMinMaxDateTimesheetEntry(user))
  }
}