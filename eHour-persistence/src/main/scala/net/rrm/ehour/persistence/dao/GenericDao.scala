package net.rrm.ehour.persistence.dao

import java.{io, util}

import net.rrm.ehour.domain.DomainObject

trait GenericDao[PK <: io.Serializable, T <: DomainObject[PK, _]] {
  /**
   * Find all domain objects
   */
  def findAll(): util.List[T]

  /**
   * Delete domain object
   */
  def delete(domObj: T)

  /**
   * Delete on primary key
   */
  def deleteOnId(pk: PK)

  /**
   * Persist domain object
   */
  def persist(domObj: T): T

  /**
   * Find by primary key
   */
  def findById(id: PK): T

  /**
   * Merge the domain object
   */
  def merge(domoj: T): T
}
