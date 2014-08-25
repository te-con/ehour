package net.rrm.ehour.persistence.activity.dao

import net.rrm.ehour.domain.{Activity, Project, User}
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateImpl
import org.springframework.stereotype.Repository

@Repository("activityDao")
class ActivtiyDaoHibernateImpl extends AbstractGenericDaoHibernateImpl[Number, Activity](classOf[Activity]) with ActivityDao {
  def findAllActivitiesOfUser(assignedUser: User): List[Activity] =
    getHibernateTemplate.findByNamedQueryAndNamedParam("Activity.findByUser", "assignedUser", assignedUser)

  def findAllActivitiesOfProject(project: Project): List[Activity] =
    getHibernateTemplate.findByNamedQueryAndNamedParam("Activity.findByProject", "project", project)
}
