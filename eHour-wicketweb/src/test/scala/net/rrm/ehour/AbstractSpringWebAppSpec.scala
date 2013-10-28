package net.rrm.ehour

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import net.rrm.ehour.ui.common.BaseSpringWebAppTester
import org.scalatest.mock.MockitoSugar
import org.scalatest.{Matchers, BeforeAndAfterAll, BeforeAndAfterEach, WordSpec}

@RunWith(classOf[JUnitRunner])
abstract class AbstractSpringWebAppSpec extends WordSpec with Matchers with MockitoSugar with BeforeAndAfterEach with BeforeAndAfterAll {
  val springTester = new BaseSpringWebAppTester
  def tester = springTester.getTester

  override def beforeEach() {
    springTester.setUp()
  }
}
