package net.rrm.ehour.persistence.activity.dao

import net.rrm.ehour.data.DateRange
import net.rrm.ehour.domain.{Customer, Project, Activity, User}
import java.util

import net.rrm.ehour.persistence.dao.GenericDao

trait ActivityDao extends GenericDao[Integer, Activity] {
  def findAllActivitiesOfUser(assignedUser: User): util.List[Activity]

  def findAllActivitiesOfProject(project: Project): util.List[Activity]

  def findActivitiesOfProject(project: Project, dateRange: DateRange): util.List[Activity]

  def findActivitiesForUser(userId: Integer, dateRange: DateRange): util.List[Activity]

  def findActivitiesForCustomers(customers: util.List[Customer]): util.List[Activity]
}