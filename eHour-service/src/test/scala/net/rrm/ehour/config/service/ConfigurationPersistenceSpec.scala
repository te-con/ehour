package net.rrm.ehour.config.service

import com.google.common.collect.{Sets, Lists}
import net.rrm.ehour.AbstractSpec
import net.rrm.ehour.config.EhourConfigStub
import net.rrm.ehour.domain.{UserObjectMother, UserRole}
import net.rrm.ehour.user.service.UserService
import org.mockito.Mockito._

class ConfigurationPersistenceSpec extends AbstractSpec {

  val configService = mock[ConfigurationService]
  val userService = mock[UserService]
  val subject = new ConfigurationPersistence(configService, userService)

  override protected def beforeEach() = reset(configService, userService)

  "Configuration Persistence" should {
    "update users when manager role is disabled" in {
      val config = new EhourConfigStub
      config.setSplitAdminRole(false)

      val user = UserObjectMother.createUser()
      user.setUserRoles(Sets.newHashSet(UserRole.MANAGER))
      when(userService.getUsers(UserRole.MANAGER)).thenReturn(Lists.newArrayList(user))

      subject.persistAndCleanUp(config, UserRole.USER)

      user.getUserRoles.iterator().next() should be (UserRole.USER)

      verify(userService).persistEditedUser(user)
    }
  }
}
