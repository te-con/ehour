package net.rrm.ehour.reminder

import net.rrm.ehour.AbstractSpec
import net.rrm.ehour.config.EhourConfigStub
import net.rrm.ehour.domain.UserObjectMother
import net.rrm.ehour.mail.service.MailMan
import net.rrm.ehour.persistence.mail.dao.MailLogDao
import org.mockito.ArgumentCaptor
import org.mockito.Matchers._
import org.mockito.Mockito._

class ReminderServiceSpec extends AbstractSpec {
  val mailMan = mock[MailMan]
  val userFinder = mock[IFindUsersWithoutSufficientHours]
  val mailLogDao = mock[MailLogDao]

  val config = new EhourConfigStub
  config.setReminderEnabled(true)
  config.setReminderMinimalHours(32)
  
  val subject = new ReminderService(config, userFinder, mailMan, mailLogDao)
  
  override protected def beforeEach() = reset(mailMan, userFinder)
  
  "Reminder Service" should {
    "mail users to remind" in {
      val user = UserObjectMother.createUser
      when(userFinder.findUsersWithoutSufficientHours(32, 8)).thenReturn(List(user))
      when(mailLogDao.find(any(), any())).thenReturn(List())

      subject.sendReminderMail()
      verify(mailMan).deliver(any(), any(), any())

      val mailEventCaptor = ArgumentCaptor.forClass(classOf[String])
      val emailCaptor = ArgumentCaptor.forClass(classOf[String])
      verify(mailLogDao).find(emailCaptor.capture(), mailEventCaptor.capture())

      emailCaptor.getValue should equal(user.getEmail)
      mailEventCaptor.getValue should startWith("1:Reminder for ")
    }
  }  
}
