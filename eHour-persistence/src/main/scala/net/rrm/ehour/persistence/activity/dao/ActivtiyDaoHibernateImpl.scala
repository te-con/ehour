package net.rrm.ehour.persistence.activity.dao

import java.util

import net.rrm.ehour.domain.{Activity, Project, User}
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateImpl
import org.springframework.stereotype.Repository

@Repository("activityDao")
class ActivtiyDaoHibernateImpl extends AbstractGenericDaoHibernateImpl[Integer, Activity](classOf[Activity]) with ActivityDao {
  def findAllActivitiesOfUser(assignedUser: User): util.List[Activity] = findByNamedQuery("Activity.findByUser", "assignedUser", assignedUser)

  def findAllActivitiesOfProject(project: Project): util.List[Activity] = findByNamedQuery("Activity.findByProject", "project", project)
}
