package net.rrm.ehour.reminder

import net.rrm.ehour.AbstractSpec
import net.rrm.ehour.config.EhourConfigStub
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.springframework.scheduling.config.{CronTask, ScheduledTaskRegistrar}

class ReminderConfigSpec extends AbstractSpec {
  "Reminder Config" should {
    "configure a cron job" in {
      val reminderConfig = new ReminderConfig()

      val config = new EhourConfigStub()
      config.setReminderEnabled(true)
      config.setReminderTime("*/10 * * * * *")

      reminderConfig.config = config

      val reminderService = mock[ReminderService]
      reminderConfig.reminderService = reminderService

      val registrarMock = mock[ScheduledTaskRegistrar]
      reminderConfig.configureTasks(registrarMock)

      verify(registrarMock).addCronTask(any(classOf[CronTask]))
    }
  }
}
