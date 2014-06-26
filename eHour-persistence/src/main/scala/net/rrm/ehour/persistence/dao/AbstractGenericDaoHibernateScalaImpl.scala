package net.rrm.ehour.persistence.dao

import java.{io, util}

import net.rrm.ehour.domain.DomainObject
import org.hibernate.Criteria
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

/**
 * GenericDAO interface for CRUD on domain objects
 */

@Repository
@Transactional
abstract class AbstractGenericDaoHibernateScalaImpl[PK <: io.Serializable, T <: DomainObject[PK, _]](entityType: Class[T])
  extends AbstractAnnotationDaoHibernate4Impl with GenericDao[PK, T] {

  protected def findByNamedQuery(queryName: String, cachingRegion: Option[String]): util.List[T] = {
    findByNamedQueryAndNamedParametersOrCache(queryName, List[String](), List[AnyRef](), cachingRegion)
  }

  protected def findByNamedQueryAndNamedParam(queryName: String, param: String, value: AnyRef, cachingRegion: Option[String]): util.List[T] = {
    findByNamedQueryAndNamedParametersOrCache(queryName, List(param), List(value), cachingRegion)
  }

  protected def findByNamedQueryAndNamedParameters(queryName: String, paramNames: List[String], values: List[_]): util.List[T] = {
    findByNamedQueryAndNamedParametersOrCache(queryName, paramNames, values, None)
  }

  protected def findByNamedQueryAndNamedParametersOrCache[C](queryName: String, paramNames: List[String], paramValues: List[_], cachingRegion: Option[String]): util.List[C] = {
    val query = getSession.getNamedQuery(queryName)

    paramNames.zip(paramValues).foreach { case (name, value) => if (paramValues.isInstanceOf[util.Collection[_]])
      query.setParameterList(name, value.asInstanceOf[util.Collection[_]])
    else
      query.setParameter(name, value)
    }

    cachingRegion match {
      case Some(region) =>
        query.setCacheable(true)
        query.setCacheRegion(region)
      case None =>
    }

    query.list.asInstanceOf[util.List[C]]
  }

  override def findAll: util.List[T] = {
    val criteria: Criteria = getSession.createCriteria(entityType)
    criteria.list.asInstanceOf[util.List[T]]
  }

  override def delete(domObj: T) {
    getSession.delete(domObj)
  }

  override def deleteOnId(id: PK) {
    val dom: T = findById(id)
    delete(dom)
  }

  override def persist(domObj: T): T = {
    getSession.saveOrUpdate(domObj)
    domObj
  }

  override def findById(id: PK): T = getSession.get(entityType, id).asInstanceOf[T]

  override def merge(domobj: T): T = getSession.merge(domobj).asInstanceOf[T]
}