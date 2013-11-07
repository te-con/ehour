package net.rrm.ehour.timesheet.service

import org.springframework.stereotype.Service
import net.rrm.ehour.persistence.timesheetlock.dao.TimesheetLockDao
import org.springframework.beans.factory.annotation.Autowired
import net.rrm.ehour.domain.TimesheetLock
import scala.collection.JavaConversions._
import java.util.{Locale, Date}
import org.springframework.transaction.annotation.Transactional
import net.rrm.ehour.util.DateUtil
import java.{util => ju}
import com.github.nscala_time.time.Imports._
import com.github.nscala_time.time.TypeImports.DateTime
import com.github.nscala_time.time.TypeImports.Interval
import scala.collection.convert.WrapAsJava

trait TimesheetLockService {
  def createNew(startDate: LocalDate, endDate: LocalDate): LockedTimesheet

  def updateExisting(id: Int, startDate: LocalDate, endDate: LocalDate, name: String)

  def findAll(): List[LockedTimesheet]

  def find(id: Int): Option[LockedTimesheet]

  def findLockedDatesInRange(startDate: Date, endDate: Date): Seq[Interval]
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

  override def findLockedDatesInRange(startDate: Date, endDate: Date): Seq[Interval] = {
    implicit def dateToDateTime(d: Date): DateTime = LocalDate.fromDateFields(d).toDateTimeAtStartOfDay

    def overlapsAtEdges(i: Interval, r: Interval) = {
      def isOverlappingAtStart = r.start equals i.end
      def overlapsAtStart = if (isOverlappingAtStart) Some(new Interval(r.start, r.start)) else None

      def isOverlappingAtEnd = r.end equals i.start
      def overlapsAtEnd = if (isOverlappingAtEnd) Some(new Interval(r.end, r.end)) else None

      overlapsAtStart orElse overlapsAtEnd
    }

    val requestedInterval = new Interval(startDate, endDate)
    val lockIntervals = repository.findMatchingLock(startDate, endDate).map(l => new Interval(l.getDateStart, l.getDateEnd)).toList

    def intervalsThatOverlap = lockIntervals.map(i => requestedInterval overlap i match {
      case o: Interval => Some(o)
      case null => overlapsAtEdges(i, requestedInterval)
    }).flatten

    val overlaps = intervalsThatOverlap

    def mergeIntervals(overlaps: List[Interval]): Seq[Interval] = {
      overlaps.sortWith(_.start isAfter _.start).foldLeft(Seq[Interval]()) {
        case (Nil, e) => Seq(e)
        case (h :: t, e) if (h gap e) == null => new Interval(e.toInterval.start, h.toInterval.end) +: t
        case (a, e) => e +: a
      }
    }

    mergeIntervals(overlaps)
  }
}

object TimesheetLockServiceImpl {
  def timesheetLockToLockedTimesheetList(xs: ju.List[TimesheetLock]): List[LockedTimesheet] = xs.map(x => LockedTimesheet(x)).toList
  def intervalToJavaList(xs: Seq[Interval]): ju.Collection[Date] = {
    def inc(d: DateTime, i: Interval, l: List[Date]): List[Date] =
      if (d.isBefore(i.getEnd) || d.isEqual(i.getEnd))
        inc(d.plusDays(1), i, d.toDate :: l)
      else
        l.reverse

    WrapAsJava.asJavaCollection(xs.map(i => inc(i.start, i, Nil)).flatten)
  }

  implicit def localDateToDate(date: LocalDate): Date = date.toDate
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

