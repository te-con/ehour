package net.rrm.ehour.persistence.dao

import org.hibernate.{Session, SessionFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class AbstractAnnotationDaoHibernate4Impl {
  @Autowired
  var sessionFactory: SessionFactory = _

  final def getSession: Session = sessionFactory.getCurrentSession
}
