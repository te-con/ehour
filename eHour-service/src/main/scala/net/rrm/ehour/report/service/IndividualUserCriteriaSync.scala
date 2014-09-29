package net.rrm.ehour.report.service

import net.rrm.ehour.persistence.activity.dao.ActivityDao
import net.rrm.ehour.persistence.report.dao.ReportAggregatedDao
import net.rrm.ehour.report.criteria.ReportCriteria
import net.rrm.ehour.util._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class IndividualUserCriteriaSync @Autowired()(activityDao: ActivityDao, reportAggregatedDAO: ReportAggregatedDao) {
  def syncCriteriaForIndividualUser(reportCriteria: ReportCriteria) {
    val availCriteria = reportCriteria.getAvailableCriteria
    val user = reportCriteria.getUserSelectedCriteria.getUsers.get(0)
    val assignments = toScala(activityDao.findActivitiesForUser(user.getUserId, reportCriteria.getUserSelectedCriteria.getReportRange))

    val customers = assignments.map(_.getProject.getCustomer).toSet
    val projects = assignments.map(_.getProject).toSet

    availCriteria.setCustomers(customers)
    availCriteria.setProjects(projects)
    availCriteria.setReportRange(reportAggregatedDAO.getMinMaxDateTimesheetEntry(user))
  }
}
