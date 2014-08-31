package net.rrm.ehour.reminder

import java.util.Date

import net.rrm.ehour.config.EhourConfig
import net.rrm.ehour.data.DateRange
import net.rrm.ehour.domain._
import net.rrm.ehour.mail.service._
import net.rrm.ehour.persistence.mail.dao.MailLogDao
import net.rrm.ehour.persistence.timesheet.dao.TimesheetDao
import net.rrm.ehour.project.service.ProjectAssignmentService
import net.rrm.ehour.timesheet.service.TimesheetLockService
import net.rrm.ehour.user.service.UserService
import net.rrm.ehour.util.JodaDateUtil
import org.apache.commons.lang.StringUtils
import org.apache.log4j.Logger
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.mutable

@Service
class ReminderService @Autowired()(config: EhourConfig, userFinder: IFindUsersWithoutSufficientHours, mailMan: MailMan, mailLogDao: MailLogDao) {
  private final val LOGGER = Logger.getLogger(classOf[ReminderService])
  private final val Formatter = DateTimeFormat.forPattern("yyyyMMdd")

  @Transactional
  def sendReminderMail() {
    val minimumHours = config.getReminderMinimalHours

    def determineMailEvent() = {
      val end = new LocalDate()
      val start = end minusDays 6
      val startFormatted = Formatter.print(start)
      val endFormatted = Formatter.print(end)

      s"Reminder for $startFormatted-$endFormatted not $minimumHours hours"
    }

    def hasMailBeenSent(mailTo: String, mailEvent: String) = mailLogDao.find(mailTo, mailEvent).size > 0

    if (config.isReminderEnabled) {
      val usersToRemind = userFinder.findUsersWithoutSufficientHours(minimumHours, config.getCompleteDayHours)

      LOGGER.info(s"Mail reminder job running, will remind ${usersToRemind.size} users.")

      for (user <- usersToRemind) {
        val mailEvent = s"${user.getUserId}:${determineMailEvent()}"

        val callback: CallBack = (mail, success) => {
          val mailLog = new MailLog
          mailLog.setTimestamp(new Date)
          mailLog.setSuccess(success)
          mailLog.setMailTo(mail.to.getEmail)
          mailLog.setMailEvent(mailEvent)
          mailLogDao.persist(mailLog)
        }

        if (StringUtils.isBlank(user.getEmail)) {
          LOGGER.warn(s"Trying to send reminder mail to ${user.getFullName} but no email address is entered.")
        } else if (hasMailBeenSent(user.getEmail, mailEvent)) {
          LOGGER.info(s"Mail to ${user.getFullName} (${user.getEmail}) about $mailEvent was already sent.")
        } else {
          val body = enrichMailBody(user)

          val mail = Mail(user, config.getReminderCC, config.getReminderSubject, body)

          mailMan.deliver(mail = mail, postDeliverCallBack = callback)
        }
      }
    }
  }

  private[reminder] def enrichMailBody(user: User): String = {
    val fullName = user.getFirstName + " " + user.getLastName
    val template = config.getReminderBody

    if (StringUtils.isNotBlank(template))
      template.replace("$name", fullName)
    else
      template
  }
}

@Service
class IFindUsersWithoutSufficientHours @Autowired()(userService: UserService,
                                                    timesheetDao: TimesheetDao,
                                                    projectAssignmentService: ProjectAssignmentService,
                                                    lockService: TimesheetLockService
                                                     ) {
  @Transactional
  def findUsersWithoutSufficientHours(minimumHours: Int, workHoursPerDay: Float): List[User] = {

    val reminderEndDate = new LocalDate()
    val reminderStartDate = reminderEndDate.minusWeeks(1).plusDays(1)

    val correctedMinimumHours = subtractLockedDaysFromMinimumHours(minimumHours, workHoursPerDay, reminderStartDate, reminderEndDate)

    val activeUsers = findActiveUsersWithAssignments(reminderStartDate, reminderEndDate)

    val timesheetEntriesInRange = timesheetDao.getTimesheetEntriesInRange(new DateRange(reminderStartDate.toDate, reminderEndDate.toDate))

    import scala.collection.JavaConversions._
    val entriesByUser: Map[User, mutable.Buffer[TimesheetEntry]] = timesheetEntriesInRange groupBy (_.getEntryId.getProjectAssignment.getUser)
    val entriesByActiveUsers = entriesByUser.filterKeys(activeUsers.contains)
    val hoursPerUser: Map[User, Float] = entriesByActiveUsers.map(f => (f._1, f._2.foldLeft(0f)(_ + _.getHours))).toMap

    val userNotMeetingMinimalHours: Map[User, Float] = hoursPerUser.filter(p => p._2 < correctedMinimumHours)

    val usersWithoutAnyHours: Set[User] = activeUsers.toSet.diff(entriesByUser.keySet)

    usersWithoutAnyHours.toList ++ userNotMeetingMinimalHours.keySet.toList
  }

  // for every locked day in the range, subtract the work hours per day from the minimum hours
  // excluded locked days that fall in the weekend
  private def subtractLockedDaysFromMinimumHours(minimumHours: Int, workHoursPerDay: Float, reminderStartDate: LocalDate, reminderEndDate: LocalDate): Int = {
    val lockedDatesInRange = lockService.findLockedDatesInRange(reminderStartDate.toDate, reminderEndDate.toDate)

    val correction = lockedDatesInRange.foldLeft(0f)((v, interval) => v + (JodaDateUtil.findWorkDays(interval).size * workHoursPerDay))

    Math.ceil(minimumHours - correction).toInt
  }

  import scala.collection.JavaConversions._
  private def findActiveUsersWithAssignments(reminderStartDate: LocalDate, reminderEndDate: LocalDate): List[User] = {
    def toWeekDays(t: (LocalDate, LocalDate)) = JodaDateUtil.enumerate(t._1, t._2).map(_.getDayOfWeek)

    val reminderWeekDays = toWeekDays(reminderStartDate, reminderEndDate)

    def coversReminderDays(ds: List[(LocalDate, LocalDate)]): Boolean = {
      val boundedDs = ds.map(t =>
        (if (t._1.isBefore(reminderStartDate)) reminderStartDate else t._1,
          if (t._2.isAfter(reminderEndDate)) reminderEndDate else t._2)
      )

      val joinedWeekDays = boundedDs.flatMap(toWeekDays).toSet
      joinedWeekDays.foldLeft(0)(_ + _) == reminderWeekDays.foldLeft(0)(_ + _)
    }

    val activeUsers = userService.getUsers(UserRole.USER).toList

    activeUsers.filter(u => {
      val assignments = projectAssignmentService.getProjectAssignmentsForUser(u.getUserId, new DateRange(reminderStartDate.toDate, reminderEndDate.toDate))

      val assignmentDates = assignments.toList.map(a => {
        val s = if (a.getDateStart == null) reminderStartDate else new LocalDate(a.getDateStart)
        val e = if (a.getDateEnd == null) reminderEndDate else new LocalDate(a.getDateEnd)
        (s,e)
      })
      coversReminderDays(assignmentDates)
    })
  }
}
