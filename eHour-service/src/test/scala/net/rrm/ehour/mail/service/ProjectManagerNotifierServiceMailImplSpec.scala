package net.rrm.ehour.mail.service

import java.util.Date

import net.rrm.ehour.AbstractSpec
import net.rrm.ehour.config.EhourConfigStub
import net.rrm.ehour.domain.{ProjectAssignmentObjectMother, User}
import net.rrm.ehour.persistence.mail.dao.{MailLogAssignmentDao, MailLogDao}
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement
import org.mockito.ArgumentCaptor
import org.mockito.Matchers._
import org.mockito.Mockito._

class ProjectManagerNotifierServiceMailImplSpec extends AbstractSpec {
  val mailMan = mock[MailMan]
  val dao = mock[MailLogAssignmentDao]
  val mailLogDao = mock[MailLogDao]

  val subject = new ProjectManagerNotifierServiceMailImpl(mailMan, dao, new EhourConfigStub, mailLogDao)

  val assignment = ProjectAssignmentObjectMother.createProjectAssignment(1)
  assignment.setAllottedHours(5)

  val aggregate = new AssignmentAggregateReportElement(assignment, 5)

  val user = new User()
  user.setEmail("thies@te-con.nl")

  override protected def beforeEach() = reset(mailMan, dao, mailLogDao)

  "Project Manager Notifier Service" should {

    "mail the PM when the allotted fixed hours are reached" in {
      subject.mailPMFixedAllottedReached(aggregate, new Date(), user)

      val mailCaptor = ArgumentCaptor.forClass(classOf[Mail])
      verify(mailMan).deliver(mailCaptor.capture(), any(), any())

      val mail = mailCaptor.getValue
      mail.to equals user
      mail.subject startsWith "eHour: All allotted hours used for project"
    }

    "mail the PM when the allotted flex hours are reached" in {
      assignment.setAllowedOverrun(10)
      subject.mailPMFlexAllottedReached(aggregate, new Date(), user)

      val mailCaptor = ArgumentCaptor.forClass(classOf[Mail])
      verify(mailMan).deliver(mailCaptor.capture(), any(), any())

      val mail = mailCaptor.getValue
      mail.to equals user
      mail.subject startsWith "eHour: All allotted hours used for project"
      mail.body contains "can still book 10.0 hours"
    }

    "mail the PM when the allotted flex hours and overrun are reached" in {
      assignment.setAllowedOverrun(10)
      subject.mailPMFlexOverrunReached(aggregate, new Date(), user)

      val mailCaptor = ArgumentCaptor.forClass(classOf[Mail])
      verify(mailMan).deliver(mailCaptor.capture(), any(), any())

      val mail = mailCaptor.getValue
      mail.to equals user
      mail.subject startsWith "eHour: All allotted hours used for project"
      mail.body contains "as well the allotted hours as the extra overrun"
    }

  }
}
