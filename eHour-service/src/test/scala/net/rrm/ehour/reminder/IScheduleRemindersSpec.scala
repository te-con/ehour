package net.rrm.ehour.reminder

import net.rrm.ehour.AbstractSpec
import net.rrm.ehour.config.EhourConfigStub
import org.mockito.ArgumentCaptor
import org.mockito.Mockito._
import org.springframework.scheduling.config.{CronTask, ScheduledTaskRegistrar}
import org.springframework.scheduling.support.CronTrigger

class IScheduleRemindersSpec extends AbstractSpec {
  "I Schedule Reminders" should {
    "schedule a reminder" in {
      val taskRegistrar = mock[ScheduledTaskRegistrar]
      val reminderService = mock[ReminderService]
      val config = new EhourConfigStub
      config.setReminderEnabled(true)
      config.setReminderTime("*/10 10 20 * * *")

      val subject = new IScheduleReminders(taskRegistrar, config, reminderService)

      subject.scheduleReminders()

      val cronTaskCaptor = ArgumentCaptor.forClass(classOf[CronTask])
      verify(taskRegistrar).addCronTask(cronTaskCaptor.capture())

      val cronTask = cronTaskCaptor.getValue
      val cronTrigger = cronTask.getTrigger.asInstanceOf[CronTrigger]

      cronTrigger.getExpression should equal("*/10 10 20 * * *")

      verify(taskRegistrar).afterPropertiesSet()
    }
  }
}
