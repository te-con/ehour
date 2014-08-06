package net.rrm.ehour.reminder

import net.rrm.ehour.AbstractSpec
import net.rrm.ehour.config.EhourConfigStub
import net.rrm.ehour.domain.UserObjectMother
import net.rrm.ehour.mail.service.MailMan
import org.mockito.Matchers._
import org.mockito.Mockito._

class ReminderServiceSpec extends AbstractSpec {
  val mailMan = mock[MailMan]
  val userFinder = mock[IFindUsersWithoutSufficientHours]

  val config = new EhourConfigStub
  config.setReminderEnabled(true)
  config.setReminderMinimalHours(32)
  
  val subject = new ReminderService(config, userFinder, mailMan)
  
  override protected def beforeEach() = reset(mailMan, userFinder)
  
  "Reminder Service" should {
    "mail users to remind" in {
      when(userFinder.findUsersWithoutSufficientHours(32)).thenReturn(List(UserObjectMother.createUser()))

      subject.sendReminderMail()

      verify(mailMan).deliver(any(), any(), any())
      
    }
  }  
}
