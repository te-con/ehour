package net.rrm.ehour.reminder

import net.rrm.ehour.config.EhourConfig
import net.rrm.ehour.data.DateRange
import net.rrm.ehour.domain.{TimesheetEntry, User}
import net.rrm.ehour.mail.service.{Mail, MailMan}
import net.rrm.ehour.persistence.mail.dao.MailLogDao
import net.rrm.ehour.persistence.timesheet.dao.TimesheetDao
import net.rrm.ehour.persistence.user.dao.UserDao
import org.apache.commons.lang.StringUtils
import org.apache.log4j.Logger
import org.joda.time.LocalDate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.mutable

@Service
class ReminderService @Autowired()(config: EhourConfig, userFinder: IFindUsersWithoutSufficientHours, mailMan: MailMan, mailLogDao: MailLogDao) {
  final val LOGGER = Logger.getLogger(classOf[ReminderService])

  def sendReminderMail() {
    if (config.isReminderEnabled) {

      val usersToRemind = userFinder.findUsersWithoutSufficientHours(config.getReminderMinimalHours)

      for (user <- usersToRemind) {
        if (StringUtils.isBlank(user.getEmail)) {
          LOGGER.warn(s"Trying to send reminder mail to ${user.getFullName} but no email address is entered")
        } else {
          val mail = Mail(user, config.getReminderCC, config.getReminderSubject, config.getReminderBody)
          mailMan.deliver(mail = mail)
        }
      }
    }
  }
}

@Service
class IFindUsersWithoutSufficientHours @Autowired()(userDao: UserDao, timesheetDao: TimesheetDao) {
  @Transactional
  def findUsersWithoutSufficientHours(minimalHours: Int): List[User] = {
    val activeUsers = userDao.findActiveUsers()

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
