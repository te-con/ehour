package net.rrm.ehour.mail.service

import java.text.SimpleDateFormat
import java.util.Date

import net.rrm.ehour.audit.annot.NonAuditable
import net.rrm.ehour.config.EhourConfig
import net.rrm.ehour.domain.User
import net.rrm.ehour.persistence.mail.dao.MailLogDao
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement
import net.rrm.ehour.util.EhourConstants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

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

    mailPMAggregateMessage(mail, aggregate, EhourConstants.MAILTYPE_FIXED_ALLOTTED_REACHED, bookDate)
  }

  private def mailPMAggregateMessage(mail: Mail, assignmentAggregate: AssignmentAggregateReportElement, mailTypeId: Int, bookDate: Date) {
    if (!isAssignmentMailAlreadySent(assignmentAggregate, mailTypeId)) {
      val callback: callBack = (mail, success) => {

        // TODO
//        val mailLog = new MailLogAssignment
//        mailLog.setProjectAssignment(assignmentAggregate.getProjectAssignment)
//        mailLog.setBookDate(bookDate)
//        mailLog.setBookedHours(assignmentAggregate.getHours.floatValue)
//        mailLog.setTimestamp(new Date)
//        mailLog.setToUser(mail.to)
//        mailLog.setSuccess(success)
//        mailLogDao.persist(mailLog)
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
      s"On ${dateFormat.format(bookDate)}, a total of ${aggregate.getHours} hours were booked consuming as well the allotted hours as the extra overrun.\r\n\r\n" +
      s"Take note, $fullName can't book anymore hours on the project. Add more hours in the project assignment if needed."

    val mail = Mail(to = mailToUser, subject = subject, body = body)
    mailPMAggregateMessage(mail, aggregate, EhourConstants.MAILTYPE_FLEX_OVERRUN_REACHED, bookDate)
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
    mailPMAggregateMessage(mail, aggregate, EhourConstants.MAILTYPE_FLEX_ALLOTTED_REACHED, bookDate)
  }

  private def isAssignmentMailAlreadySent(aggregate: AssignmentAggregateReportElement, mailTypeId: Int): Boolean = {
    // TODO
    true
//    val sentMail = mailLogAssignmentDao.findMailLogOnAssignmentIds(Array[Integer](aggregate.getProjectAssignment.getAssignmentId))
//
//    sentMail.exists(m => m.getMailType.getMailTypeId == mailTypeId && m.getSuccess)
  }
}