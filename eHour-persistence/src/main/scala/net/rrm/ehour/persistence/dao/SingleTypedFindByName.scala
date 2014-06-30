package net.rrm.ehour.persistence.dao

import java.util

import net.rrm.ehour.persistence.retry.ExponentialBackoffRetryPolicy
import org.hibernate.Session
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
trait SingleTypedFindByName[T] {
  def getSession: Session

  def findByNamedQuery(queryName: String, cachingRegion: Option[String] = None): util.List[T] =
    findByNamedQueryAndNamedParams(queryName, List[String](), List[AnyRef](), cachingRegion)

  def findByNamedQueryAndNamedParam(queryName: String, param: String, value: AnyRef, cachingRegion: Option[String] = None): util.List[T] =
    findByNamedQueryAndNamedParams(queryName, List(param), List(value), cachingRegion)

  import scala.collection.JavaConversions._
  def findByNamedQueryAndNamedParams(queryName: String, paramNames: List[String], paramValues: List[AnyRef],  cachingRegion: Option[String] = None): util.List[T] = {
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

    val operation = () => query.list.asInstanceOf[util.List[T]]

    ExponentialBackoffRetryPolicy retry operation
  }
}
