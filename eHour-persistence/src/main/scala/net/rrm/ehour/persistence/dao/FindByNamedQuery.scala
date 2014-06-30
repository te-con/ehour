package net.rrm.ehour.persistence.dao

import java.util

import net.rrm.ehour.persistence.retry.ExponentialBackoffRetryPolicy
import org.hibernate.Session
import org.springframework.transaction.annotation.Transactional

sealed trait FindByNamed {
  def getSession: Session

  import scala.collection.JavaConversions._
  def exec[A](queryName: String, paramNames: List[String], paramValues: List[AnyRef], cachingRegion: Option[String]): util.List[A] = {
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

    ExponentialBackoffRetryPolicy retry query.list.asInstanceOf[util.List[A]]
  }
}

@Transactional(readOnly = true)
trait FindByNamedQuery extends FindByNamed {

  def findByNamedQuery[A](queryName: String): util.List[A] = findByNamedQuery(queryName, None)

  def findByNamedQuery[A](queryName: String, cachingRegion: Option[String]): util.List[A] =
    findByNamedQuery(queryName, List[String](), List[AnyRef](), cachingRegion)


  def findByNamedQuery[A](queryName: String, param: String, value: AnyRef): util.List[A] =
    findByNamedQuery[A](queryName, param, value, None)

  def findByNamedQuery[A](queryName: String, param: String, value: AnyRef, cachingRegion: Option[String]): util.List[A] =
    findByNamedQuery[A](queryName, List(param), List(value), cachingRegion)


  def findByNamedQuery[A](queryName: String, paramNames: List[String], paramValues: List[AnyRef]): util.List[A] =
    findByNamedQuery(queryName, paramNames, paramValues, None)

  def findByNamedQuery[A](queryName: String, paramNames: List[String], paramValues: List[AnyRef], cachingRegion: Option[String]): util.List[A] =
    exec[A](queryName, paramNames, paramValues, cachingRegion)
}

@Transactional(readOnly = true)
trait SingleTypedFindByNamedQuery[T] extends FindByNamed {
  def findByNamedQuery(queryName: String, cachingRegion: Option[String]): util.List[T] =
    findByNamedQuery(queryName, List[String](), List[AnyRef](), cachingRegion)


  def findByNamedQuery(queryName: String, param: String, value: AnyRef): util.List[T] =
    findByNamedQuery(queryName, param, value, None)

  def findByNamedQuery(queryName: String, param: String, value: AnyRef, cachingRegion: Option[String]): util.List[T] =
    findByNamedQuery(queryName, List(param), List(value), cachingRegion)


  def findByNamedQuery(queryName: String, paramNames: List[String], paramValues: List[AnyRef]): util.List[T] =
    findByNamedQuery(queryName, paramNames, paramValues, None)

  def findByNamedQuery(queryName: String, paramNames: List[String], paramValues: List[AnyRef],  cachingRegion: Option[String]): util.List[T] =
    exec[T](queryName, paramNames, paramValues, cachingRegion)
}