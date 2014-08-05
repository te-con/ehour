package net.rrm.ehour.reminder

import net.rrm.ehour.config.EhourConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ReminderService @Autowired() (config: EhourConfig) {
  def sendMail() {
    Console.println("WE'RE HERE")
  }
}
