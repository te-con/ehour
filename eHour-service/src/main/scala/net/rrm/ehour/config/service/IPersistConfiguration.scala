package net.rrm.ehour.config.service

import net.rrm.ehour.config.EhourConfig
import net.rrm.ehour.domain.UserRole
import net.rrm.ehour.reminder.IScheduleReminders
import net.rrm.ehour.user.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.convert.WrapAsScala

trait IPersistConfiguration {
  def persistAndCleanUp(config: EhourConfig, newManagerRole: UserRole)
}

@Service("configurationPersistence")
class ConfigurationPersistence @Autowired()(configurationService: ConfigurationService,
                                            userService: UserService,
                                            reminderScheduler: IScheduleReminders) extends IPersistConfiguration {

  @Transactional
  override def persistAndCleanUp(config: EhourConfig, newManagerRole: UserRole) {
    configurationService.persistConfiguration(config)

    reminderScheduler.rescheduleReminders(config)

    val withManagerRole = config.isSplitAdminRole

    if (!withManagerRole) {
      val managers = userService.getUsers(UserRole.MANAGER)
      WrapAsScala.asScalaBuffer(managers).foreach(user => {
        user.deleteUserRole(UserRole.MANAGER).addUserRole(newManagerRole)
        userService.persistEditedUser(user)
      })
    }
  }
}
