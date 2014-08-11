package net.rrm.ehour.ui.admin.config

import net.rrm.ehour.AbstractSpec
import net.rrm.ehour.config.EhourConfigStub

class MainConfigBackingBeanSpec extends AbstractSpec {
  "Main Config Backing Bean" should {
    "parse time out of cron expression" in {
      val backingBean = createSubject

      backingBean.getReminderDay should equal("TUE")
      backingBean.getReminderHour should equal(16)
      backingBean.getReminderMinute should equal(25)
    }

    "use default reminder when invalid values provided" in {
      val config = new EhourConfigStub
      config.setReminderTime("0 70 25 * * AMS")

      val backingBean = new MainConfigBackingBean(config)
      backingBean.getReminderDay should equal("FRI")
      backingBean.getReminderHour should equal(17)
      backingBean.getReminderMinute should equal(30)
    }

    "use default reminder when the invalid values are not integer parsable" in {
      val config = new EhourConfigStub
      config.setReminderTime("* * 17 * * FRI")

      val backingBean = new MainConfigBackingBean(config)
      backingBean.getReminderDay should equal("FRI")
      backingBean.getReminderHour should equal(17)
      backingBean.getReminderMinute should equal(30)
    }

    "when time/day setted, update cron expression" in {
      val backingBean = createSubject

      backingBean.setReminderHour(12)
      backingBean.setReminderMinute(45)
      backingBean.setReminderDay("THU")
      backingBean.getConfig.getReminderTime should equal("0 45 12 * * THU")
    }
  }

  def createSubject  = {
    val config = new EhourConfigStub
    config.setReminderTime("0 25 16 * * TUE")

    new MainConfigBackingBean(config)
  }
}

