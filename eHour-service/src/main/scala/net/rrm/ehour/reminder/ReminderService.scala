package net.rrm.ehour.reminder

import net.rrm.ehour.config.EhourConfig
import net.rrm.ehour.data.DateRange
import net.rrm.ehour.domain.{TimesheetEntry, User}
import net.rrm.ehour.mail.service.MailMan
import net.rrm.ehour.persistence.timesheet.dao.TimesheetDao
import net.rrm.ehour.persistence.user.dao.UserDao
import org.joda.time.LocalDate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import scala.collection.mutable

@Service
class ReminderService @Autowired() (config: EhourConfig, userDao: UserDao, timesheetDao: TimesheetDao, mailMan: MailMan) {
  def sendMail() {
    if (config.isReminderEnabled) {




    }

    Console.println("WE'RE HERE")
  }
}

@Service
class UserDiscoveryService @Autowired() (userDao: UserDao, timesheetDao: TimesheetDao) {
  def usersWithoutSufficientHours(minimalHours: Int) {
    val activeUsers = userDao.findActiveUsers()

    val endDate = new LocalDate()
    val startDate = endDate.minusWeeks(1).plusDays(1)
    val range = new DateRange(startDate.toDate, endDate.toDate)
    val timesheetEntriesInRange = timesheetDao.getTimesheetEntriesInRange(range)

    import scala.collection.JavaConversions._
    val entriesByUser: Map[User, mutable.Buffer[TimesheetEntry]] = timesheetEntriesInRange groupBy ( _.getEntryId.getProjectAssignment.getUser)
    val hoursPerUser: Map[User, Float] = entriesByUser.map(f => (f._1, f._2.foldLeft(0f)(_ + _.getHours))).toMap

    val userNotMeetingMinimalHours: Map[User, Float] = hoursPerUser.filter(p => p._2 < minimalHours)

    val usersWithoutAnyHours: Set[User] = activeUsers.toSet.diff(entriesByUser.keySet)

    usersWithoutAnyHours :: userNotMeetingMinimalHours.keySet :: Nil
  }
}
