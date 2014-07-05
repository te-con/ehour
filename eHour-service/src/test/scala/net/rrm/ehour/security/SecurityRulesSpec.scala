package net.rrm.ehour.security

import net.rrm.ehour.AbstractSpec
import net.rrm.ehour.domain.{UserObjectMother, UserRole}

import scala.collection.convert.WrapAsJava

class SecurityRulesSpec extends AbstractSpec {
  "Security Service" should {
    val manager = UserObjectMother.createUser()
    manager.getUserRoles.clear()
    manager.addUserRole(UserRole.MANAGER)

    val admin = UserObjectMother.createUser()
    admin.getUserRoles.clear()
    admin.addUserRole(UserRole.ADMIN)

    val user = UserObjectMother.createUser()
    user.getUserRoles.clear()
    user.addUserRole(UserRole.USER)

    "user is admin" in {
      val user = UserObjectMother.createUser()
      user.addUserRole(UserRole.ADMIN)

      SecurityRules.isAdmin(user)
    }

    "find admin" in {
      SecurityRules.isWithAdminRole(WrapAsJava.asJavaCollection(List(UserRole.ROLE_ADMIN, UserRole.ROLE_MANAGER))) should be(true)
    }

    "don't find admin" in {
      SecurityRules.isWithAdminRole(WrapAsJava.asJavaCollection(List(UserRole.ROLE_USER, UserRole.ROLE_MANAGER))) should be(false)
    }

    "find manager" in {
      SecurityRules.isWithManagerRole(WrapAsJava.asJavaCollection(List(UserRole.ROLE_USER, UserRole.ROLE_MANAGER))) should be(true)
    }

    "not find manager" in {
      SecurityRules.isWithManagerRole(WrapAsJava.asJavaCollection(List(UserRole.ROLE_ADMIN, UserRole.ROLE_USER))) should be(false)
    }

    "when role split is enabled, manager cannot modifyadmin" in {
        SecurityRules.allowedToModify(manager, admin, splittedAdminRole = true) should be (false)
    }

    "when role split is enabled, admin can modify manager" in {
      SecurityRules.allowedToModify(admin, manager, splittedAdminRole = true) should be (true)
    }

    "when role split is disabled, admin can modify" in {
      SecurityRules.allowedToModify(manager, admin, splittedAdminRole = false) should be (false)
    }

    "when role split is disabled, non-admin cannot to modify" in {
      SecurityRules.allowedToModify(manager, user, splittedAdminRole = false) should be (false)
    }


  }
}
