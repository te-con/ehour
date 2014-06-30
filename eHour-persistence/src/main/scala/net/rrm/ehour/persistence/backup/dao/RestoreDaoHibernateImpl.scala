package net.rrm.ehour.persistence.backup.dao

import java.io
import java.io.Serializable

import net.rrm.ehour.domain.{DomainObject, User}
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoHibernate4Impl
import net.rrm.ehour.persistence.retry.ExponentialBackoffRetryPolicy
import org.hibernate.Session
import org.springframework.stereotype.Repository

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 19, 2010 - 12:09:19 AM
 *         Converted on: Jun 27, 2014
 */
@Repository("importDao")
class RestoreDaoHibernateImpl extends AbstractAnnotationDaoHibernate4Impl with RestoreDao {
  override def persist[T <: DomainObject[_, _]](obj: T): io.Serializable = {
    ExponentialBackoffRetryPolicy.retry(getSession.persist(obj))
    obj.getPK.asInstanceOf[io.Serializable]
  }

  override def find[T, PK <: Serializable](primaryKey: PK, obj: Class[T]): T = getSession.get(obj, primaryKey).asInstanceOf[T]

  override def flush() {
    val session: Session = getSession
    session.flush()
    session.clear()
  }

  override def delete[T](obj: Class[T]) {
    if (obj eq classOf[User]) {
      ExponentialBackoffRetryPolicy.retry(getSession.createSQLQuery("DELETE FROM USER_TO_USERROLE").executeUpdate)
    }

    ExponentialBackoffRetryPolicy.retry(getSession.createQuery("DELETE FROM " + obj.getName).executeUpdate)
  }
}
