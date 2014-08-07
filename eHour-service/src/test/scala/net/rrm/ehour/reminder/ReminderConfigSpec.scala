package net.rrm.ehour.reminderIS

import net.rrm.ehour.AbstractSpec
import net.rrm.ehour.reminder.ReminderConfig

class ReminderConfigSpec extends AbstractSpec {
  "Reminder Config" should {
    "configure a scheduler" in {
      val reminderConfig = new ReminderConfig()

      val scheduler = reminderConfig.configureScheduler()
      scheduler.getScheduler should not be null
    }
  }
}
