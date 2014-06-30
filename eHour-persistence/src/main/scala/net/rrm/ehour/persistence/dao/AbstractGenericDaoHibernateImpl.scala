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
  extends AbstractAnnotationDaoHibernate4Impl with GenericDao[PK, T] {

  @Transactional(readOnly = true)
  protected def findByNamedQuery(queryName: String, cachingRegion: Option[String]): util.List[T] = {
    findByNamedQueryAndNamedParametersOrCache(queryName, List[String](), List[AnyRef](), cachingRegion)
  }

  @Transactional(readOnly = true)
  protected def findByNamedQueryAndNamedParam(queryName: String, param: String, value: AnyRef, cachingRegion: Option[String]): util.List[T] = {
    findByNamedQueryAndNamedParametersOrCache(queryName, List(param), List(value), cachingRegion)
  }

  @Transactional(readOnly = true)
  protected def findByNamedQueryAndNamedParameters(queryName: String, paramNames: List[String], values: List[AnyRef]): util.List[T] = {
    findByNamedQueryAndNamedParametersOrCache(queryName, paramNames, values, None)
  }

  import scala.collection.JavaConversions._
  @Transactional(readOnly = true)
  protected def findByNamedQueryAndNamedParametersOrCache[C](queryName: String, paramNames: List[String], paramValues: List[AnyRef], cachingRegion: Option[String]): util.List[C] = {
    val query = getSession.getNamedQuery(queryName)

    paramNames.zip(paramValues).foreach { case (name, value) =>
      value match {
        case _: util.Collection[_] => query.setParameterList(name, value.asInstanceOf[util.Collection[_]])
        case _: Seq[_] => query.setParameterList(name, value.asInstanceOf[Seq[_]])
        case _ => query.setParameter(name, value)
      }
    }

    cachingRegion match {
      case Some(region) =>
        query.setCacheable(true)
        query.setCacheRegion(region)
      case None =>
    }

    () => query.list.asInstanceOf[util.List[C]]
  }

  @Transactional(readOnly = true)
  override def findAll(): util.List[T] = {
    val criteria: Criteria = getSession.createCriteria(entityType)
    () => criteria.list.asInstanceOf[util.List[T]]
  }

  @Transactional
  override def delete(domObj: T) {
    ExponentialBackoffRetryPolicy retry (() => getSession.delete(domObj))
  }

  @Transactional
  override def deleteOnId(id: PK) {
    val dom = findById(id)
    delete(dom)
  }

  @Transactional
  override def persist(domObj: T): T = {
    ExponentialBackoffRetryPolicy retry (() => getSession.saveOrUpdate(domObj))
    domObj
  }

  @Transactional(readOnly = true)
  override def findById(id: PK): T = () => getSession.get(entityType, id).asInstanceOf[T]

  @Transactional
  override def merge(domobj: T): T = () => getSession.merge(domobj).asInstanceOf[T]
}