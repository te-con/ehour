package net.rrm.ehour.timesheet.service

import org.joda.time.LocalDate
import org.springframework.stereotype.Service
import net.rrm.ehour.persistence.timesheetlock.dao.TimesheetLockDao
import org.springframework.beans.factory.annotation.Autowired
import net.rrm.ehour.domain.TimesheetLock
import scala.collection.JavaConversions._
import java.util.{Locale, Date}
import org.springframework.transaction.annotation.Transactional
import net.rrm.ehour.util.DateUtil

trait TimesheetLockService {
  def createNew(startDate: LocalDate, endDate: LocalDate): LockedTimesheet

  def findAll(): List[LockedTimesheet]
}

@Service("timesheetLockService")
class TimesheetLockServiceImpl @Autowired()(repository: TimesheetLockDao) extends TimesheetLockService {
  @Transactional
  override def createNew(startDate: LocalDate, endDate: LocalDate): LockedTimesheet = LockedTimesheet(repository.persist(new TimesheetLock(startDate.toDateMidnight.toDate, endDate.toDateMidnight.toDate)))

  def findAll(): List[LockedTimesheet] = repository.findAll().map(LockedTimesheet(_)).toList
}

case class LockedTimesheet(id: Option[Int] = None, dateStart: LocalDate, dateEnd: LocalDate, name: Option[String] = None) {
  def lockName(implicit locale: Locale): String = name match {
    case Some(n) => n
    case _ => {
      val start = DateUtil.formatDate(dateStart, locale)
      val end = DateUtil.formatDate(dateEnd, locale)

      s"$start - $end"
    }
  }
}

object LockedTimesheet {
  implicit private def dateToLocalDate(date: Date): LocalDate = LocalDate.fromDateFields(date)

  // weird if because of spnullpointer when unboxing getLockId: Integer = null to scala.Int primitive type
  def apply(timesheetLock: TimesheetLock): LockedTimesheet = LockedTimesheet(if (timesheetLock.getLockId == null) None else Some(timesheetLock.getLockId), timesheetLock.getDateStart, timesheetLock.getDateEnd, Option(timesheetLock.getName))
}

