package net.rrm.ehour.mail.service

import java.util.Properties

import net.rrm.ehour.appconfig.EhourSystemConfig
import net.rrm.ehour.config.EhourConfig
import net.rrm.ehour.domain.User
import org.apache.commons.lang.StringUtils
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.task.TaskExecutor
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.mail.{MailException, MailSender, SimpleMailMessage}
import org.springframework.stereotype.Service

case class Mail(to: User, cc: String = "", subject: String, body: String)

trait MailMan {
  @Autowired
  var ehourConfig: EhourConfig = _

  @Autowired
  var taskExecutor: TaskExecutor = _

  @Autowired
  var systemConfig: EhourSystemConfig = _

  def sendTestMessage(alternativeConfig: EhourConfig)

  def deliver(mail: Mail, postDeliverCallBack: CallBack = (_, _) => {}, config: EhourConfig = ehourConfig)
}


@Service
class MailManSmtpImpl extends MailMan {

  final val LOGGER = Logger.getLogger(classOf[MailManSmtpImpl])

  override def sendTestMessage(alternativeConfig: EhourConfig) {
    val subject = "eHour test message"
    val body = "You successfully configured eHour's mail settings."

    val user = new User()
    user.setEmail(alternativeConfig.getMailFrom)

    deliver(Mail(to = user, subject = subject, body = body), (_, _) => {}, alternativeConfig)
  }

  override def deliver(mail: Mail, postDeliverCallBack: CallBack = (_, _) => {}, config: EhourConfig = ehourConfig) {
    val mailTask = new MailTask(mail, postDeliverCallBack, config)
    taskExecutor.execute(mailTask)
  }

  private class MailTask(mail: Mail, postDeliverCallBack: CallBack, config: EhourConfig) extends Runnable {

    override def run() {
      val msg = new SimpleMailMessage
      msg.setText(mail.body)
      msg.setSubject(mail.subject)
      msg.setFrom(config.getMailFrom)
      msg.setCc(mail.cc.split(","))
      msg.setTo(mail.to.getEmail)

      try {
        val mailSender = createMailSender(config)

        if (systemConfig.isEnableMail) {
          mailSender.send(msg)
          LOGGER.info(s"Mail sent to ${mail.to.getEmail}: ${mail.subject} ")

        } else {
          LOGGER.info(s"Mail is disabled, otherwise I would be sending email to ${mail.to.getEmail}: ${mail.subject} ")
        }

        postDeliverCallBack(mail, true)
      }

      catch {
        case me: MailException =>
          LOGGER.error(s"Failed to email to ${msg.getTo}: ${me.getMessage}")

          postDeliverCallBack(mail, false)
      }
    }

    private def createMailSender(config: EhourConfig): MailSender = {
      val mailSender = new JavaMailSenderImpl
      mailSender.setHost(config.getMailSmtp)

      if (StringUtils.isNotBlank(config.getSmtpPort)) {
        try {
          val port = config.getSmtpPort.toInt
          mailSender.setPort(port)
        }
        catch {
          case nfe: NumberFormatException =>
            LOGGER.error("Using default port 25, couldn't parse configured port " + config.getSmtpPort)
        }
      }

      if (StringUtils.isNotBlank(config.getSmtpUsername) && StringUtils.isNotBlank(config.getSmtpPassword)) {
        val prop = new Properties
        prop.put("mail.smtp.auth", "true")

        mailSender.setJavaMailProperties(prop)
        mailSender.setUsername(config.getSmtpUsername)
        mailSender.setPassword(config.getSmtpPassword)
      }

      mailSender
    }
  }

}


