package net.rrm.ehour.persistence.mail.dao

import net.rrm.ehour.domain.MailLog
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateImpl
import org.springframework.stereotype.Repository

@Repository("mailLogDao")
class MailLogDaoHibernateImpl extends AbstractGenericDaoHibernateImpl[Integer, MailLog](classOf[MailLog]) with MailLogDao