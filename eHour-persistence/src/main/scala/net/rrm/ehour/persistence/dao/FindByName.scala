package net.rrm.ehour.persistence.dao

import java.util

import net.rrm.ehour.persistence.retry.ExponentialBackoffRetryPolicy
import org.hibernate.Session
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
trait FindByName {
  def getSession: Session

  def findByNamedQuery[A](queryName: String, cachingRegion: Option[String] = None): util.List[A] =
    findByNamedQueryAndNamedParams(queryName, List[String](), List[AnyRef](), cachingRegion)

  def findByNamedQueryAndNamedParam[A](queryName: String, param: String, value: AnyRef, cachingRegion: Option[String] = None): util.List[A] =
    findByNamedQueryAndNamedParams[A](queryName, List(param), List(value), cachingRegion)

  import scala.collection.JavaConversions._
  def findByNamedQueryAndNamedParams[A](queryName: String, paramNames: List[String], paramValues: List[AnyRef], cachingRegion: Option[String] = None): util.List[A] = {
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

    val operation = () => query.list.asInstanceOf[util.List[A]]

    ExponentialBackoffRetryPolicy retry operation
  }
}
