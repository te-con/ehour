package net.rrm.ehour.ui.financial.lock

import net.rrm.ehour.ui.common.AbstractSpringWebAppTester
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ClassicMatchers
import org.scalatest.mock.MockitoSugar
import org.scalatest.{WordSpec, BeforeAndAfterAll, BeforeAndAfterEach}

@RunWith(classOf[JUnitRunner])
class LockAdminPageTest extends AbstractSpringWebAppTester with ClassicMatchers with MockitoSugar with WordSpec with BeforeAndAfterEach with BeforeAndAfterAll {
  override def beforeEach() {
    super.setUp()
  }

  "Lock Admin Page" should {
    "render" in {
      tester.startPage(classOf[LockAdminPage])
    }
  }
}
