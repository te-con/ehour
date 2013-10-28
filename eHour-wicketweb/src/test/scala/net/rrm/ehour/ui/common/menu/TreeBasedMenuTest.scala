package net.rrm.ehour.ui.common.menu

import net.rrm.ehour.ui.common.BaseSpringWebAppTester
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, BeforeAndAfter, FunSuite}

@RunWith(classOf[JUnitRunner])
class TreeBasedMenuTest extends FunSuite with Matchers with BeforeAndAfter {
  val springTester = new BaseSpringWebAppTester()

  before {
    springTester.setUp()
  }

  test("should render menu") {
    springTester.tester.startComponentInPage(new TreeBasedMenu("id", MenuDefinition.createMenuDefinition))
    springTester.tester.assertNoErrorMessage()
  }
}
