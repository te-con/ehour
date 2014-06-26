package net.rrm.ehour.persistence.mail.dao

import net.rrm.ehour.domain.MailLog
import net.rrm.ehour.persistence.dao.GenericDao
/**
 * DAO for MailLog db operations
 */
trait MailLogDao extends GenericDao[Integer, MailLog]