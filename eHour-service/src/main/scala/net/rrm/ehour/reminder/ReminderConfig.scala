package net.rrm.ehour.reminder

import com.google.common.collect.Lists
import net.rrm.ehour.config.EhourConfig
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.scheduling.annotation.{EnableScheduling, SchedulingConfigurer}
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.scheduling.config.{CronTask, ScheduledTaskRegistrar}
import org.springframework.scheduling.support.CronTrigger

object ReminderConfig {
  private final val Log = Logger.getLogger(ReminderConfig.getClass)
}

@Configuration
@EnableScheduling
class ReminderConfig extends SchedulingConfigurer {

  @Autowired
  var config: EhourConfig = _

  @Autowired
  var reminderService: ReminderService = _

  @Bean
  def taskScheduler(): ThreadPoolTaskScheduler = new ThreadPoolTaskScheduler()

  override def configureTasks(taskRegistrar: ScheduledTaskRegistrar) {
    taskRegistrar.setTaskScheduler(taskScheduler())

    if (config.isReminderEnabled) {
      taskRegistrar.setCronTasksList(Lists.newArrayList())

      ReminderConfig.Log.info("Reminder mails are enabled.")

      val reminderTime = config.getReminderTime

      val cronTrigger = new CronTrigger(reminderTime)

      val task = new CronTask(new Runnable() {
        override def run() {
          reminderService.sendMail()
        }
      }, cronTrigger)

      taskRegistrar.addCronTask(task)
    } else {
      ReminderConfig.Log.info("Reminder mails are disabled.")
    }
  }
}
