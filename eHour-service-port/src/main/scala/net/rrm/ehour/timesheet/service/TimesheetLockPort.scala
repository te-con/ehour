package net.rrm.ehour.timesheet.service

import net.rrm.ehour.timesheet.dto.TimesheetLock
import org.joda.time.Interval

trait TimesheetLockPort {
  def findMatchingLock(between: Interval): List[TimesheetLock]

  def deleteOnId(id: Int)

  def findAll(): List[TimesheetLock]

  def findById(id: Int): Option[TimesheetLock]

  def persist(lock: TimesheetLock): TimesheetLock

  def delete(id: Int)
}
