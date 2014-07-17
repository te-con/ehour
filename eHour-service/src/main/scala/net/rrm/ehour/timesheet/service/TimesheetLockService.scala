package net.rrm.ehour.timesheet.service

import java.util.Date
import java.{util => ju}

import com.github.nscala_time.time.Imports._
import com.github.nscala_time.time.TypeImports.{DateTime, Interval}
import com.google.common.collect.Lists
import net.rrm.ehour.data.DateRange
import net.rrm.ehour.domain.{Project, TimesheetEntry, TimesheetLock, User}
import net.rrm.ehour.persistence.timesheet.dao.TimesheetDao
import net.rrm.ehour.persistence.timesheetlock.dao.TimesheetLockDao
import net.rrm.ehour.util._
import org.joda.time.Days
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

trait TimesheetLockService {
  def createNew(name: Option[String] = None, startDate: Date, endDate: Date, excludedUsers: ju.List[User]): TimesheetLock

  def updateExisting(id: Int, startDate: Date, endDate: Date, name: String, excludedUsers: ju.List[User]): TimesheetLock

  def deleteLock(id: Int)

  def findAll(): List[TimesheetLock]

  def find(id: Int): Option[TimesheetLock]

  def findLockedDatesInRange(startDate: Date, endDate: Date): Seq[Interval]

  def findLockedDatesInRange(startDate: Date, endDate: Date, user: User): Seq[Interval]

  def findAffectedUsers(startDate: Date, endDate: Date, excludedUsers: Seq[User]): Seq[AffectedUser]
}

object TimesheetLockService {
  def timesheetLockToLockedTimesheetList(xs: ju.List[TimesheetLock]): List[TimesheetLock] = toScala(xs)

  def intervalToJavaList(xs: Seq[Interval]): ju.List[Date] = {
    def inc(d: DateTime, i: Interval, l: List[Date]): List[Date] =
      if (d.isBefore(i.getEnd) || d.isEqual(i.getEnd))
        inc(d.plusDays(1), i, d.toDate :: l)
      else
        l.reverse

    toJava(xs.map(i => inc(i.start, i, Nil)).flatten)
  }
}

@Service("timesheetLockService")
class TimesheetLockServiceSpringImpl @Autowired()(lockDao: TimesheetLockDao, timesheetDao: TimesheetDao) extends TimesheetLockService {

  @Transactional
  override def createNew(optionalName: Option[String] = None, startDate: Date, endDate: Date, excludedUsers: ju.List[User] = Lists.newArrayList()): TimesheetLock = {
    val lock = optionalName match {
      case Some(name) => new TimesheetLock(startDate, endDate, name, excludedUsers)
      case None => new TimesheetLock(startDate, endDate, excludedUsers)
    }

    lockDao.persist(lock)

    lock
  }

  @Transactional
  override def updateExisting(id: Int, startDate: Date, endDate: Date, name: String, excludedUsers: ju.List[User]): TimesheetLock = {
    val lock = new TimesheetLock(id, startDate, endDate, name, excludedUsers)
    lockDao.persist(lock)
  }

  @Transactional
  def deleteLock(id: Int) {
    lockDao.deleteOnId(id)
  }

  private[service] def determineName(startDate: Date, endDate: Date): String = {
    val start = new DateTime(startDate)
    val end = new DateTime(endDate)

    val days = Days.daysBetween(start, end).getDays
    days / 7 match {
      case 1 => "Week %s" format DateTimeFormat.forPattern("w, yyyy").print(start)
      case 4 | 5 => DateTimeFormat.forPattern("MMMM, yyyy").print(start)
      case 11 | 12 | 13 => "Quarter %d, %d" format((start.getMonthOfYear / 3) + 1, start.getYear)
      case _ => "%s - %s" format(DateTimeFormat.forPattern("dd MM yyyy").print(start), DateTimeFormat.forPattern("dd MM yyyy").print(end))
    }
  }

  override def findAll(): List[TimesheetLock] = toScala(lockDao.findAll())

  override def find(id: Int): Option[TimesheetLock] = lockDao.findById(id) match {
    case timesheetLock: TimesheetLock => Some(timesheetLock)
    case null => None
  }

  override def findLockedDatesInRange(startDate: Date, endDate: Date): Seq[Interval] =
    mergeDatesToInterval(startDate, endDate, lockDao.findMatchingLock(startDate, endDate))

  import scala.collection.JavaConversions._

  override def findLockedDatesInRange(startDate: Date, endDate: Date, user: User): Seq[Interval] = {
    val locks = lockDao.findMatchingLock(startDate, endDate)

    val filteredLocks = locks.filterNot(_.getExcludedUsers.contains(user))

    mergeDatesToInterval(startDate, endDate, filteredLocks)
  }


  import scala.collection.JavaConversions._

  private def mergeDatesToInterval(startDate: Date, endDate: Date, dates: ju.List[TimesheetLock]): Seq[Interval] = {
    implicit def dateToDateTime(d: Date): DateTime = LocalDate.fromDateFields(d).toDateTimeAtStartOfDay

    def overlapsAtEdges(i: Interval, r: Interval) = {
      def isOverlappingAtStart = r.start equals i.end
      def overlapsAtStart = if (isOverlappingAtStart) Some(new Interval(r.start, r.start)) else None

      def isOverlappingAtEnd = r.end equals i.start
      def overlapsAtEnd = if (isOverlappingAtEnd) Some(new Interval(r.end, r.end)) else None

      overlapsAtStart orElse overlapsAtEnd
    }

    val lockIntervals = dates.map(l => new Interval(l.getDateStart, l.getDateEnd)).toList

    val requestedInterval = new Interval(startDate, endDate)
    def intervalsThatOverlap = lockIntervals.map(i => requestedInterval overlap i match {
      case o: Interval => Some(o)
      case null => overlapsAtEdges(i, requestedInterval)
    }).flatten

    val overlaps = intervalsThatOverlap

    def mergeIntervals(current: Interval, stack: Seq[Interval], merged: Seq[Interval]): Seq[Interval] = {
      stack match {
        case (x :: xs) if x.overlaps(current) || x.abuts(current) => mergeIntervals(new Interval(current.start, x.end), xs, merged)
        case (x :: xs) => mergeIntervals(x, xs, current +: merged)
        case Nil => current +: merged
      }
    }

    (overlaps.sortWith(_.start isBefore _.start) match {
      case (x :: xs) => mergeIntervals(x, xs, Seq())
      case (xs) => xs
    }).reverse
  }

  def findAffectedUsers(startDate: Date, endDate: Date, excludedUsers: Seq[User]): Seq[AffectedUser] = {
    val xs = toScala(timesheetDao.getTimesheetEntriesInRange(new DateRange(startDate, endDate)))

    val entriesPerUser: Map[User, List[TimesheetEntry]] = xs.groupBy(_.getEntryId.getProjectAssignment.getUser)

    val filteredUsers = entriesPerUser.filterNot(p => excludedUsers.contains(p._1))

    (for ((k, v) <- filteredUsers) yield {

      val byProject: Map[Project, List[TimesheetEntry]] = v.groupBy(_.getPK.getProjectAssignment.getProject)

      val aggregatedProjects: Map[Project, Float] = for ((p, xs) <- byProject) yield {
        (p, xs.foldLeft(0f)(_ + _.getHours))
      }

      AffectedUser(k, aggregatedProjects)
    }).toSeq.sortWith((a, b) => a.user.compareTo(b.user) < 0)
  }
}

case class AffectedUser(user: User = null, projects: Map[Project, Float] = Map()) {
  def getJavaProjects = toJava(projects.toList)

  def getUser = user

  def hoursBooked = projects.foldLeft(0f)(_ + _._2)
}

