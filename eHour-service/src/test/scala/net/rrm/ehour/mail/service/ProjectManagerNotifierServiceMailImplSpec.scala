package net.rrm.ehour.mail.service

import java.util.Date

import net.rrm.ehour.AbstractSpec
import net.rrm.ehour.config.EhourConfigStub
import net.rrm.ehour.domain.{MailLog, ProjectAssignmentObjectMother, User}
import net.rrm.ehour.persistence.mail.dao.MailLogDao
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement
import org.mockito.ArgumentCaptor
import org.mockito.Matchers._
import org.mockito.Mockito._

class ProjectManagerNotifierServiceMailImplSpec extends AbstractSpec {
  val mailMan = mock[MailMan]
  val mailLogDao = mock[MailLogDao]

  val subject = new ProjectManagerNotifierServiceMailImpl(mailMan, new EhourConfigStub, mailLogDao)

  val assignment = ProjectAssignmentObjectMother.createProjectAssignment(1)
  assignment.setAllottedHours(5)

  val aggregate = new AssignmentAggregateReportElement(assignment, 5)

  val user = new User()
  user.setEmail("thies@te-con.nl")

  override protected def beforeEach() = reset(mailMan, mailLogDao)

  "Project Manager Notifier Service" should {

    "mail the PM when the allotted fixed hours are reached" in {
      when(mailLogDao.find(any(), any())).thenReturn(List())

      subject.mailPMFixedAllottedReached(aggregate, new Date(), user)

      val mail: Mail = deliverMail

      mail.to should equal(user)
      mail.subject should startWith("eHour: All allotted hours used for project")

      mailLog.getMailEvent should equal("100-FixedReached")
    }

    "mail the PM when the allotted flex hours are reached" in {
      when(mailLogDao.find(any(), any())).thenReturn(List())

      assignment.setAllowedOverrun(10)
      subject.mailPMFlexAllottedReached(aggregate, new Date(), user)

      val mail: Mail = deliverMail

      mail.to should equal(user)
      mail.subject should startWith("eHour: All allotted hours used for project")
      mail.body should include("can still book 10.0 hours")

      mailLog.getMailEvent should equal("100-FlexAllotReached")
    }

    "mail the PM when the allotted flex hours and overrun are reached" in {
      when(mailLogDao.find(any(), any())).thenReturn(List())

      assignment.setAllowedOverrun(10)
      subject.mailPMFlexOverrunReached(aggregate, new Date(), user)

      val mail: Mail = deliverMail

      mail.to equals user
      mail.subject startsWith "eHour: All allotted hours used for project"
      mail.body contains "as well the allotted hours as the extra overrun"

      mailLog.getMailEvent should equal("100-FlexOverrunReached")
    }
  }

  def deliverMail: Mail = {
    val mailCaptor = ArgumentCaptor.forClass(classOf[Mail])
    val callbackCaptor = ArgumentCaptor.forClass(classOf[CallBack])
    verify(mailMan).deliver(mailCaptor.capture(), callbackCaptor.capture(), any())
    val mail = mailCaptor.getValue
    callbackCaptor.getValue.apply(mail, true)
    mail
  }

  def mailLog: MailLog = {
    val mailLogCaptor = ArgumentCaptor.forClass(classOf[MailLog])
    verify(mailLogDao).persist(mailLogCaptor.capture())

    val mailLog = mailLogCaptor.getValue
    mailLog
  }
}
