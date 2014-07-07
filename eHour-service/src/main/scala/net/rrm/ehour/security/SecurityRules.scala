package net.rrm.ehour.security

import java.util

import net.rrm.ehour.domain.{User, UserRole}

object SecurityRules {
  def isAdmin(user: User): Boolean = user.getUserRoles.contains(UserRole.ADMIN)

  def isWithAdminRole(roles: util.Collection[String]): Boolean = roles.contains(UserRole.ROLE_ADMIN)

  def isManager(user: User): Boolean = user.getUserRoles.contains(UserRole.MANAGER)

  def isWithManagerRole(roles: util.Collection[String]) = roles.contains(UserRole.MANAGER)

  def isWithPmRole(roles: util.Collection[String]) = roles.contains(UserRole.ROLE_PROJECTMANAGER)

  def isWithReportRole(roles: util.Collection[String]) = roles.contains(UserRole.ROLE_REPORT)

  def allowedToModify(modifyingUser: User, userToModify: User, splittedAdminRole: Boolean): Boolean = {
    if (splittedAdminRole) {
      // should be either manager or admin
      if (!isAdmin(modifyingUser) && !isManager(modifyingUser))
        false
      else if (isAdmin(userToModify) && !isAdmin(modifyingUser))
        false
      else
        true
    }
    else if (isAdmin(modifyingUser)) true
    else
      false
  }
}



