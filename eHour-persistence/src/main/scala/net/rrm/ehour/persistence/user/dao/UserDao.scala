package net.rrm.ehour.persistence.user.dao

import java.util

import net.rrm.ehour.data.LegacyUserDepartment
import net.rrm.ehour.domain.User
import net.rrm.ehour.persistence.dao.GenericDao

trait UserDao extends GenericDao[Integer, User] {
  /**
   * Find user by username
   */
  def findByUsername(username: String): User

  /**
   * Find users
   */
  def findUsers(onlyActive: Boolean): util.List[User]

  /**
   * Find active users
   */
  def findActiveUsers(): util.List[User]

  /**
   * Find all active users with email address set
   */
  def findAllActiveUsersWithEmailSet(): util.List[User]

  /**
   * Delete users with PM role but are not PM anymore
   */
  def deletePmWithoutProject()

  /**
   * Returns a list of user id & department id tuple for users with the one-to-many user department
   */
  def findLegacyUserDepartments(): util.List[LegacyUserDepartment]
}


