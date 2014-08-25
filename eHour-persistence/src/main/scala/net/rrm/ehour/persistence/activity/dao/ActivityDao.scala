package net.rrm.ehour.persistence.activity.dao

import net.rrm.ehour.domain.{Project, Activity, User}
import java.util

import net.rrm.ehour.persistence.dao.GenericDao

trait ActivityDao extends GenericDao[Integer, Activity] {
  def findAllActivitiesOfUser(assignedUser: User): util.List[Activity]

  def findAllActivitiesOfProject(project: Project): util.List[Activity]
}