package net.rrm.ehour.persistence.activity.dao

import net.rrm.ehour.domain.{Project, Activity, User}
trait ActivityDao {
  def findAllActivitiesOfUser(assignedUser: User): List[Activity]

  def findAllActivitiesOfProject(project: Project): List[Activity]
}