package net.rrm.ehour.persistence.dao

import java.{io, util}

import net.rrm.ehour.domain.DomainObject
import net.rrm.ehour.persistence.retry.ExponentialBackoffRetryPolicy
import org.hibernate.Criteria
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

/**
 * GenericDAO interface for CRUD on domain objects
 */
@Repository
abstract class AbstractGenericDaoHibernateImpl[PK <: io.Serializable, T <: DomainObject[PK, _]](entityType: Class[T])
  extends AbstractAnnotationDaoHibernate4Impl with GenericDao[PK, T] with SingleTypedFindByNamedQuery[T] {
  @Transactional(readOnly = true)
  override def findAll(): util.List[T] = {
    val criteria: Criteria = getSession.createCriteria(entityType)
    ExponentialBackoffRetryPolicy retry criteria.list.asInstanceOf[util.List[T]]
  }

  @Transactional
  override def delete(domObj: T) {
    ExponentialBackoffRetryPolicy retry getSession.delete(domObj)
  }

  @Transactional
  override def deleteOnId(id: PK) {
    val dom = findById(id)
    delete(dom)
  }

  @Transactional
  override def persist(domObj: T): T = {
    ExponentialBackoffRetryPolicy retry getSession.saveOrUpdate(domObj)
    domObj
  }

  @Transactional(readOnly = true)
  override def findById(id: PK): T = ExponentialBackoffRetryPolicy retry getSession.get(entityType, id).asInstanceOf[T]

  @Transactional
  override def merge(domobj: T): T = ExponentialBackoffRetryPolicy retry getSession.merge(domobj).asInstanceOf[T]
}