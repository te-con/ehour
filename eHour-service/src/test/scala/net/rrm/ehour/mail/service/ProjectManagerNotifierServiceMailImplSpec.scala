package net.rrm.ehour.mail.service

import java.util.Date

import net.rrm.ehour.AbstractSpec
import net.rrm.ehour.config.EhourConfigStub
import net.rrm.ehour.domain.{ActivityMother, MailLog, User}
import net.rrm.ehour.persistence.mail.dao.MailLogDao
import net.rrm.ehour.report.reports.element.ActivityAggregateReportElement
import org.mockito.ArgumentCaptor
import org.mockito.Matchers._
import org.mockito.Mockito._

class ProjectManagerNotifierServiceMailImplSpec extends AbstractSpec {
  val mailMan = mock[MailMan]
  val mailLogDao = mock[MailLogDao]

  val subject = new ProjectManagerNotifierServiceMailImpl(mailMan, new EhourConfigStub, mailLogDao)

  val assignment = ActivityMother.createActivity(1)
  assignment.setAllottedHours(5f)

  val aggregate = new ActivityAggregateReportElement(assignment, 5)

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
