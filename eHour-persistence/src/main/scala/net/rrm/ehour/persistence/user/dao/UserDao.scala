package net.rrm.ehour.persistence.user.dao

import java.util

import net.rrm.ehour.domain.{UserRole, User, UserDepartment}
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
   * Delete users with PM role but are not PM anymore
   */
  def deletePmWithoutProject()

  /**
   * Helper method to clean up implicit role information from underlying data-store
   */
  def cleanRedundantRoleInformation(userRole: UserRole)
}


