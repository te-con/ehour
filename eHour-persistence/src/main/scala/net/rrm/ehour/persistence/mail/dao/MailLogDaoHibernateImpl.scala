package net.rrm.ehour.persistence.mail.dao

import net.rrm.ehour.domain.MailLog
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateScalaImpl
import org.springframework.stereotype.Repository

@Repository("mailLogDao")
class MailLogDaoHibernateImpl extends AbstractGenericDaoHibernateScalaImpl[Integer, MailLog](classOf[MailLog]) with MailLogDao