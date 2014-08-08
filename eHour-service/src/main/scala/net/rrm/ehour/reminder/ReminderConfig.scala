package net.rrm.ehour.reminder

import javax.annotation.PostConstruct

import com.google.common.collect.Lists
import net.rrm.ehour.config.EhourConfig
import org.apache.commons.lang.StringUtils
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.scheduling.config.{CronTask, ScheduledTaskRegistrar}
import org.springframework.scheduling.support.CronTrigger
import org.springframework.stereotype.Service

@Configuration
class ReminderConfig {
  val s = new ThreadPoolTaskScheduler()

  @Bean
  def taskScheduler(): ThreadPoolTaskScheduler = s

  @Bean
  def configureScheduler(): ScheduledTaskRegistrar = {
    val taskRegistrar: ScheduledTaskRegistrar = new ScheduledTaskRegistrar

    taskRegistrar.setScheduler(taskScheduler())
    taskRegistrar
  }
}

@Service
class IScheduleReminders @Autowired()(taskRegistrar: ScheduledTaskRegistrar, config: EhourConfig, reminderService: ReminderService) {
  private final val Log = Logger.getLogger(classOf[IScheduleReminders])

  @PostConstruct
  def scheduleReminders() {
    rescheduleReminders(config)
  }
  
  def rescheduleReminders(reminderConfig: EhourConfig) {
    if (reminderConfig.isReminderEnabled) {
      // destroy any scheduled futures
      taskRegistrar.destroy()

      // remove previous crontasks
      taskRegistrar.setCronTasksList(Lists.newArrayList())

      val reminderTime = reminderConfig.getReminderTime

      if (StringUtils.isBlank(reminderTime)) {
        Log.warn("Reminder mails are enabled but reminder time is not configured.")
      } else {
        val zone = config.getTzAsTimeZone

        Log.info(s"Reminder mails are enabled, running at $reminderTime (${zone.getDisplayName}).")

        val cronTrigger = new CronTrigger(reminderTime, zone)

        val task = new CronTask(new Runnable() {
          override def run() {
            reminderService.sendReminderMail()
          }
        }, cronTrigger)

        taskRegistrar.addCronTask(task)
        taskRegistrar.afterPropertiesSet()
      }
    } else {
      Log.info("Per configuration, reminder mails are disabled.")
    }
  }
}
