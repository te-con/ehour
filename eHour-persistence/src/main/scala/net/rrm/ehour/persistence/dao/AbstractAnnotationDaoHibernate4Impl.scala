package net.rrm.ehour.persistence.dao

import net.rrm.ehour.persistence.retry.ExponentialBackoffRetryPolicy
import org.hibernate.{Session, SessionFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class AbstractAnnotationDaoHibernate4Impl {
  @Autowired
  var sessionFactory: SessionFactory = _

  implicit def retryOperation[A](operation: () => A): A = ExponentialBackoffRetryPolicy retry operation

  final protected def getSession: Session = sessionFactory.getCurrentSession

}
