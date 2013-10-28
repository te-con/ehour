package net.rrm.ehour.timesheet.service

import org.joda.time.LocalDate
import org.springframework.stereotype.Service
import net.rrm.ehour.persistence.timesheetlock.dao.TimesheetLockDao
import org.springframework.beans.factory.annotation.Autowired
import net.rrm.ehour.domain.TimesheetLock
import scala.collection.JavaConversions._

trait TimesheetLockService {
  def createNew(startDate: LocalDate, endDate: LocalDate): LockedTimesheet

  def findAll(): List[LockedTimesheet]
}

@Service("timesheetLockService")
class TimesheetLockServiceImpl @Autowired()(repository: TimesheetLockDao) extends TimesheetLockService {
  override def createNew(startDate: LocalDate, endDate: LocalDate): LockedTimesheet = LockedTimesheet(repository.persist(new TimesheetLock(startDate.toDateMidnight.toDate, endDate.toDateMidnight.toDate)))

  def findAll(): List[LockedTimesheet] = repository.findAll().map(LockedTimesheet(_)).toList
}

case class LockedTimesheet(startDate: LocalDate, endDate: LocalDate)

object LockedTimesheet {
  def apply(timesheetLock: TimesheetLock): LockedTimesheet = LockedTimesheet(LocalDate.fromDateFields(timesheetLock.getDateStart), LocalDate.fromDateFields(timesheetLock.getDateEnd))
}

