package net.rrm.ehour.persistence.activity.dao

import java.util

import net.rrm.ehour.data.DateRange
import net.rrm.ehour.domain.{Activity, Project, User}
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateImpl
import org.springframework.stereotype.Repository

@Repository("activityDao")
class ActivityDaoHibernateImpl extends AbstractGenericDaoHibernateImpl[Integer, Activity](classOf[Activity]) with ActivityDao {
  final val CacheRegion = Some("query.Activity")

  override def findAllActivitiesOfUser(assignedUser: User): util.List[Activity] = findByNamedQuery("Activity.findByUser", "assignedUser", assignedUser)

  override def findAllActivitiesOfProject(project: Project): util.List[Activity] = findByNamedQuery("Activity.findByProject", "project", project)

  override def findActivitiesOfProject(project: Project, dateRange: DateRange) = {
    val keys  = List("project", "dateStart", "dateEnd")
    val params  = List(project, dateRange.getDateStart, dateRange.getDateEnd)

    findByNamedQuery("Activity.findByProjectAndDateRange", keys, params, CacheRegion)

  }

  override def findActivitiesForUser(userId: Integer, dateRange: DateRange): util.List[Activity] = {
    val keys  = List("dateStart", "dateEnd", "userId")
    val params  = List(dateRange.getDateStart, dateRange.getDateEnd, userId)

    findByNamedQuery("Activity.findActivitiessForUserInRange", keys, params, CacheRegion)
  }
}
