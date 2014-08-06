package net.rrm.ehour.persistence.mail.dao

import net.rrm.ehour.domain.MailLog
import net.rrm.ehour.persistence.dao.{AbstractGenericDaoHibernateImpl, GenericDao}
import org.springframework.stereotype.Repository
trait MailLogDao extends GenericDao[Integer, MailLog] {
  def find(event: String): List[MailLog]

  def find(mailTo: String, event: String): List[MailLog]
}

@Repository
class MailLogDaoHibernateImpl extends AbstractGenericDaoHibernateImpl[Integer, MailLog](classOf[MailLog]) with MailLogDao {
  import scala.collection.JavaConversions._
  override def find(event: String):List[MailLog] = findByNamedQuery("MailLog.findOnEvent", "event", event).toList

  override def find(mailTo: String, event: String):List[MailLog] = findByNamedQuery("MailLog.findOnEventAndMailTo", List("event", "mailTo"), List(event, mailTo)).toList
}