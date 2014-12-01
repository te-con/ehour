package net.rrm.ehour.timesheet.dto

import net.rrm.ehour.domain.User
import org.joda.time.DateTime

case class TimesheetLock(id: Option[Int], name: Option[String], startDate: DateTime, endDate: DateTime, excludedUsers: List[User])

object TimesheetLock {
  def apply(startDate: DateTime, endDate: DateTime): TimesheetLock = TimesheetLock(id = None,
    name = None,
    startDate = startDate,
    endDate = endDate,
    excludedUsers = List())

  def apply(startDate: DateTime, endDate: DateTime, excluded: List[User]): TimesheetLock = TimesheetLock(id = None,
    name = None,
    startDate = startDate,
    endDate = endDate,
    excludedUsers = excluded)

}