package net.rrm.ehour.persistence.user.dao

import net.rrm.ehour.domain.UserDepartment
import net.rrm.ehour.persistence.dao.GenericDao

/**
 * CRUD on UserDepartment domain object
 **/
trait UserDepartmentDao extends GenericDao[Integer, UserDepartment] {
  def findOnNameAndCode(name: String, code: String): UserDepartment
}

