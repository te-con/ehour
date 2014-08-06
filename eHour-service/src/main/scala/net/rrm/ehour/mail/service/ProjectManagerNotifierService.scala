package net.rrm.ehour.mail.service

import java.text.SimpleDateFormat
import java.util.Date

import net.rrm.ehour.audit.annot.NonAuditable
import net.rrm.ehour.config.EhourConfig
import net.rrm.ehour.domain.{MailLog, ProjectAssignment, User}
import net.rrm.ehour.persistence.mail.dao.MailLogDao
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement
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
  def mailPMFixedAllottedReached(assignment: AssignmentAggregateReportElement, bookDate: Date, mailToUser: User)

  /**
   * Send project assignment overrun e-mail for flex assignments, overrun reached
   */
  def mailPMFlexOverrunReached(assignment: AssignmentAggregateReportElement, bookDate: Date, mailToUser: User)

  /**
   * Send project assignment overrun e-mail for flex assignments, allotted reached
   */
  def mailPMFlexAllottedReached(assignment: AssignmentAggregateReportElement, bookDate: Date, mailToUser: User)
}

@NonAuditable
@Service("mailService")
class ProjectManagerNotifierServiceMailImpl @Autowired()(mailMan: MailMan, config: EhourConfig, mailLogDao: MailLogDao) extends ProjectManagerNotifierService {
  override def mailPMFixedAllottedReached(aggregate: AssignmentAggregateReportElement, bookDate: Date, mailToUser: User) {
    val dateFormat = new SimpleDateFormat("dd MMM yyyy")

    val assignment = aggregate.getProjectAssignment
    val project = assignment.getProject

    val assignedUser = assignment.getUser
    val fullName = s"${assignedUser.getFirstName} ${assignedUser.getLastName}"

    val subject = s"eHour: All allotted hours used for project ${project.getFullName} by $fullName"

    val body = s"Project warning for project ${project.getFullName}  (${project.getCustomer.getName})\r\n\r\n" +
      s"${assignment.getAllottedHours.floatValue} hours were allotted for this project to $fullName.\r\n" +
      s"On ${dateFormat.format(bookDate)}, a total of ${aggregate.getHours} hours were booked reaching the allotted hours mark.\r\n\r\n" +
      s"Take note, $fullName can't book anymore hours on the project. Add more hours in the project assignment if needed."

    val mail = Mail(to = mailToUser, subject = subject, body = body)
    val mailEvent = constructMailEventForAssignment(assignment, ProjectManagerNotifierService.FixedAllottedReached)

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
   * Send project assignment overrun e-mail for flex assignments, overrun reached
   */
  override def mailPMFlexOverrunReached(aggregate: AssignmentAggregateReportElement, bookDate: Date, mailToUser: User) {
    val dateFormat = new SimpleDateFormat("dd MMM yyyy")

    val assignment = aggregate.getProjectAssignment
    val project = assignment.getProject

    val assignedUser = assignment.getUser
    val fullName = s"${assignedUser.getFirstName} ${assignedUser.getLastName}"

    val subject = s"eHour: All allotted and overrun hours used for project ${project.getFullName} by $fullName"

    val body = s"Project warning for project ${project.getFullName} (${project.getCustomer.getName})\r\n\r\n" +
      s"${assignment.getAllottedHours.floatValue} hours were allotted for this project to $fullName.\r\n\r\n" +
      s"Additionally an extra overrun of ${assignment.getAllowedOverrun} hours was created for this employee.\r\n\r\n" +
      s"On ${dateFormat.format(bookDate)}, a total of ${aggregate.getHours.floatValue()} hours were booked consuming as well the allotted hours as the extra overrun.\r\n\r\n" +
      s"Take note, $fullName can't book anymore hours on the project. Add more hours in the project assignment if needed."

    val mail = Mail(to = mailToUser, subject = subject, body = body)
    val mailEvent = constructMailEventForAssignment(assignment, ProjectManagerNotifierService.FlexOverunReached)
    mailPMAggregateMessage(mail, mailEvent, bookDate)
  }

  /**
   * Send project assignment overrun e-mail for flex assignments, allotted reached
   */
  override def mailPMFlexAllottedReached(aggregate: AssignmentAggregateReportElement, bookDate: Date, mailToUser: User) {
    val dateFormat = new SimpleDateFormat("dd MMM yyyy")

    val assignment = aggregate.getProjectAssignment
    val project = assignment.getProject

    val assignedUser = assignment.getUser
    val fullName = s"${assignedUser.getFirstName} ${assignedUser.getLastName}"


    val subject = s"eHour: All allotted hours used for project ${project.getFullName} by $fullName"

    val body = s"Project warning for project ${project.getFullName} (${project.getCustomer.getName})\r\n\r\n" +
      s"${assignment.getAllottedHours} hours were allotted for this project to $fullName.\r\n\r\n" +
      s"On ${dateFormat.format(bookDate)}, a total of ${aggregate.getHours} hours were booked reaching the allotted hours mark..\r\n\r\n" +
      s"Take note, $fullName can still book ${assignment.getAllowedOverrun} hours on the project as part of the allowed overrun."

    val mail = Mail(to = mailToUser, subject = subject, body = body)
    val mailEvent = constructMailEventForAssignment(assignment, ProjectManagerNotifierService.FlexAllottedReached)
    mailPMAggregateMessage(mail, mailEvent, bookDate)
  }

  private def isAssignmentMailAlreadySent(to: String, mailEvent: String) = mailLogDao.find(to, mailEvent).nonEmpty

  private def constructMailEventForAssignment(assignment: ProjectAssignment, eventType: String) = s"${assignment.getAssignmentId}-$eventType"
}