package net.rrm.ehour.persistence.mail.dao

import net.rrm.ehour.domain.MailLog
import net.rrm.ehour.persistence.dao.{AbstractGenericDaoHibernateImpl, GenericDao}
import org.springframework.stereotype.Repository
trait MailLogDao extends GenericDao[Integer, MailLog] {
  def findOnEvent(event: String): List[MailLog]
}

@Repository
class MailLogDaoHibernateImpl extends AbstractGenericDaoHibernateImpl[Integer, MailLog](classOf[MailLog]) with MailLogDao {
  import scala.collection.JavaConversions._
  override def findOnEvent(event: String):List[MailLog] = findByNamedQuery("MailLog.findOnEvent", "event", event).toList
}