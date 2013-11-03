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
import java.util.{List => JList}
import scala.collection.convert.WrapAsJava

trait TimesheetLockService {
  def createNew(startDate: LocalDate, endDate: LocalDate): LockedTimesheet

  def updateExisting(id: Int, startDate: LocalDate, endDate: LocalDate, name: String)

  def findAll(): List[LockedTimesheet]

  def find(id: Int): Option[LockedTimesheet]

  def find(startDate: Date, endDate: Date): JList[LockedTimesheet]
}

@Service("timesheetLockService")
class TimesheetLockServiceImpl @Autowired()(repository: TimesheetLockDao) extends TimesheetLockService {
  import TimesheetLockServiceImpl.localDateToDate

  @Transactional
  override def createNew(startDate: LocalDate, endDate: LocalDate): LockedTimesheet = LockedTimesheet(repository.persist(new TimesheetLock(startDate, endDate)))

  @Transactional
  override def updateExisting(id: Int, startDate: LocalDate, endDate: LocalDate, name: String) {
    val lock = new TimesheetLock(id, startDate, endDate, name)
    repository.persist(lock)
  }

  override def findAll(): List[LockedTimesheet] = TimesheetLockServiceImpl.timesheetLockToLockedTimesheetList(repository.findAll())

  override def find(id: Int): Option[LockedTimesheet] = repository.findById(id) match {
    case timesheet: TimesheetLock => Some(LockedTimesheet(timesheet))
    case null => None
  }

  override def find(startDate: Date, endDate: Date): JList[LockedTimesheet] = {
    val xs = repository.findMatchingLock(startDate, endDate)
    WrapAsJava.bufferAsJavaList(xs.map(LockedTimesheet(_)).toBuffer)
  }
}

object TimesheetLockServiceImpl {
  def timesheetLockToLockedTimesheetList(xs: JList[TimesheetLock]): List[LockedTimesheet] = xs.map(x => LockedTimesheet(x)).toList
  implicit def localDateToDate(date: LocalDate): Date = date.toDateMidnight.toDate
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
  implicit def dateToLocalDate(date: Date): LocalDate = LocalDate.fromDateFields(date)

  // weird if because of nullpointer when unboxing getLockId: Integer = null to scala.Int primitive type
  def apply(timesheetLock: TimesheetLock): LockedTimesheet = LockedTimesheet(if (timesheetLock.getLockId == null) None else Some(timesheetLock.getLockId), timesheetLock.getDateStart, timesheetLock.getDateEnd, Option(timesheetLock.getName))


}

