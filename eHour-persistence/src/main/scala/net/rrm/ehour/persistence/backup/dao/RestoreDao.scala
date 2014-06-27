package net.rrm.ehour.persistence.backup.dao

import java.io
import java.io.Serializable

import net.rrm.ehour.domain.DomainObject

trait RestoreDao {
  def persist[T <: DomainObject[_, _]](obj: T): io.Serializable

  def find[T, PK <: Serializable](primaryKey: PK, obj: Class[T]): T

  def flush()

  def delete[T](obj: Class[T])
}