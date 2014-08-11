package net.rrm.ehour.ui.admin.config.panel

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.config.EhourConfigStub
import net.rrm.ehour.ui.admin.config.MainConfigBackingBean
import org.apache.wicket.model.CompoundPropertyModel

class ReminderConfigPanelSpec extends AbstractSpringWebAppSpec {
  val formPath = "id:reminderForm"

  "Reminder Config Panel" should {
    "hide reminder time dropdowns when reminders are not enabled" in {
      val config = new EhourConfigStub
      config.setReminderEnabled(false)
      config.setReminderTime("0 10 16 * * FRI")

      val bean = new MainConfigBackingBean(config)

      tester.startComponentInPage(new ReminderConfigPanel("id", new CompoundPropertyModel[MainConfigBackingBean](bean)))

      tester.assertInvisible(s"$formPath:reminderTimeContainer")
      tester.assertInvisible(s"$formPath:reminderBodyContainer")

      val miscFormTester = tester.newFormTester(formPath)
      miscFormTester.setValue("config.reminderEnabled", true)

      tester.executeAjaxEvent(s"$formPath:config.reminderEnabled", "click")
      tester.assertVisible(s"$formPath:reminderTimeContainer")
      tester.assertVisible(s"$formPath:reminderBodyContainer")

      tester.assertNoErrorMessage()
    }
  }

}
