package net.rrm.ehour.mail.service

import java.text.SimpleDateFormat
import java.util.Date

import net.rrm.ehour.audit.annot.NonAuditable
import net.rrm.ehour.config.EhourConfig
import net.rrm.ehour.domain.{Activity, MailLog, User}
import net.rrm.ehour.persistence.mail.dao.MailLogDao
import net.rrm.ehour.report.reports.element.ActivityAggregateReportElement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

object ProjectManagerNotifierService {
  // this should be typed
  final val FixedAllottedReached = "FixedReached"
  final val FlexAllottedReached = "FlexAllotReached"
  final val FlexOverunReached = "FlexOverrunReached"
}

trait ProjectManagerNotifierService {
  /**
   * Send project assignment overrun e-mail for fixed assignments, allotted reached
   */
  def mailPMFixedAllottedReached(assignment: ActivityAggregateReportElement, bookDate: Date, mailToUser: User)

  /**
   * Send project assignment overrun e-mail for flex assignments, allotted reached
   */
  def mailPMFlexAllottedReached(assignment: ActivityAggregateReportElement, bookDate: Date, mailToUser: User)
}

@NonAuditable
@Service("mailService")
class ProjectManagerNotifierServiceMailImpl @Autowired()(mailMan: MailMan, config: EhourConfig, mailLogDao: MailLogDao) extends ProjectManagerNotifierService {
  override def mailPMFixedAllottedReached(aggregate: ActivityAggregateReportElement, bookDate: Date, mailToUser: User) {
    val dateFormat = new SimpleDateFormat("dd MMM yyyy")

    val activity = aggregate.getActivity
    val project = activity.getProject

    val assignedUser = activity.getAssignedUser
    val fullName = s"${assignedUser.getFirstName} ${assignedUser.getLastName}"

    val subject = s"eHour: All allotted hours used for project ${project.getFullName} by $fullName"

    val body = s"Project warning for project ${project.getFullName}  (${project.getCustomer.getName})\r\n\r\n" +
      s"${activity.getAllottedHours.floatValue} hours were allotted for this project to $fullName.\r\n" +
      s"On ${dateFormat.format(bookDate)}, a total of ${aggregate.getHours} hours were booked reaching the allotted hours mark.\r\n\r\n" +
      s"Take note, $fullName can't book anymore hours on the project. Add more hours in the project assignment if needed."

    val mail = Mail(to = mailToUser, subject = subject, body = body)
    val mailEvent = constructMailEventForAssignment(activity, ProjectManagerNotifierService.FixedAllottedReached)

    mailPMAggregateMessage(mail, mailEvent, bookDate)
  }

  private def mailPMAggregateMessage(mail: Mail, mailEvent: String, bookDate: Date) {
    if (!isAssignmentMailAlreadySent(mail.to.getEmail, mailEvent)) {
      val callback: CallBack = (mail, success) => {

        val mailLog = new MailLog
        mailLog.setTimestamp(new Date)
        mailLog.setSuccess(success)
        mailLog.setMailTo(mail.to.getEmail)
        mailLog.setMailEvent(mailEvent)
        mailLogDao.persist(mailLog)
      }

      mailMan.deliver(mail, callback)
    }
  }


  /**
   * Send project assignment overrun e-mail for flex assignments, allotted reached
   */
  override def mailPMFlexAllottedReached(aggregate: ActivityAggregateReportElement, bookDate: Date, mailToUser: User) {
    val dateFormat = new SimpleDateFormat("dd MMM yyyy")

    val assignment = aggregate.getActivity
    val project = assignment.getProject

    val assignedUser = assignment.getAssignedUser
    val fullName = s"${assignedUser.getFirstName} ${assignedUser.getLastName}"


    val subject = s"eHour: All allotted hours used for project ${project.getFullName} by $fullName"

    val body = s"Project warning for project ${project.getFullName} (${project.getCustomer.getName})\r\n\r\n" +
      s"${assignment.getAllottedHours} hours were allotted for this project to $fullName.\r\n\r\n" +
      s"On ${dateFormat.format(bookDate)}, a total of ${aggregate.getHours} hours were booked reaching the allotted hours mark..\r\n\r\n"

    val mail = Mail(to = mailToUser, subject = subject, body = body)
    val mailEvent = constructMailEventForAssignment(assignment, ProjectManagerNotifierService.FlexAllottedReached)
    mailPMAggregateMessage(mail, mailEvent, bookDate)
  }

  private def isAssignmentMailAlreadySent(to: String, mailEvent: String) = mailLogDao.find(to, mailEvent).nonEmpty

  private def constructMailEventForAssignment(assignment: Activity, eventType: String) = s"${assignment.getId}-$eventType"
}