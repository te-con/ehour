package net.rrm.ehour.ui.admin.user

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.ui.admin.impersonate.ImpersonateUserPage
import net.rrm.ehour.user.service.UserService
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfter

class ImpersonateUserPageSpec extends AbstractSpringWebAppSpec  with BeforeAndAfter {
  "Impersonate User Page" should {
    val service = mockService[UserService]

//    when(service.findAll()).thenReturn(List(lock))

    before {
      reset(service)
    }

    "render" in {
      tester.startPage(classOf[ImpersonateUserPage])

      tester.assertNoErrorMessage()
      tester.assertNoInfoMessage()
    }
  }
}
