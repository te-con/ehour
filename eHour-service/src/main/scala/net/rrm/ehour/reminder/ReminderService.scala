package net.rrm.ehour.reminder

import java.util.Date

import net.rrm.ehour.config.EhourConfig
import net.rrm.ehour.data.DateRange
import net.rrm.ehour.domain.{MailLog, TimesheetEntry, User, UserRole}
import net.rrm.ehour.mail.service._
import net.rrm.ehour.persistence.mail.dao.MailLogDao
import net.rrm.ehour.persistence.timesheet.dao.TimesheetDao
import net.rrm.ehour.user.service.UserService
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
  final val LOGGER = Logger.getLogger(classOf[ReminderService])
  final val Formatter = DateTimeFormat.forPattern("yyyyMMdd")

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
      val usersToRemind = userFinder.findUsersWithoutSufficientHours(minimumHours)

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
          val mail = Mail(user, config.getReminderCC, config.getReminderSubject, config.getReminderBody)

          mailMan.deliver(mail = mail, postDeliverCallBack = callback)
        }
      }
    }
  }
}

@Service
class IFindUsersWithoutSufficientHours @Autowired()(userService: UserService, timesheetDao: TimesheetDao) {
  @Transactional
  def findUsersWithoutSufficientHours(minimalHours: Int): List[User] = {
    val activeUsers = userService.getUsers(UserRole.USER)

    val endDate = new LocalDate()
    val startDate = endDate.minusWeeks(1).plusDays(1)
    val range = new DateRange(startDate.toDate, endDate.toDate)
    val timesheetEntriesInRange = timesheetDao.getTimesheetEntriesInRange(range)

    import scala.collection.JavaConversions._
    val entriesByUser: Map[User, mutable.Buffer[TimesheetEntry]] = timesheetEntriesInRange groupBy (_.getEntryId.getProjectAssignment.getUser)
    val entriesByActiveUsers = entriesByUser.filterKeys(activeUsers.contains)
    val hoursPerUser: Map[User, Float] = entriesByActiveUsers.map(f => (f._1, f._2.foldLeft(0f)(_ + _.getHours))).toMap

    val userNotMeetingMinimalHours: Map[User, Float] = hoursPerUser.filter(p => p._2 < minimalHours)

    val usersWithoutAnyHours: Set[User] = activeUsers.toSet.diff(entriesByUser.keySet)

    usersWithoutAnyHours.toList ++ userNotMeetingMinimalHours.keySet.toList
  }
}
