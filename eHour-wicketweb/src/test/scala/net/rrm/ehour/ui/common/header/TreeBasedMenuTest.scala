package net.rrm.ehour.ui.common.header

import net.rrm.ehour.ui.common.BaseSpringWebAppTester
import net.rrm.ehour.ui.common.config.PageLayoutConfig
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite, Matchers}

@RunWith(classOf[JUnitRunner])
class TreeBasedMenuTest extends FunSuite with Matchers with BeforeAndAfter {
  val springTester = new BaseSpringWebAppTester()

  before {
    springTester.setUp()
  }

  test("should render menu") {
    springTester.tester.startComponentInPage(new TreeBasedMenu("id", new PageLayoutConfig().menuDefinition))
    springTester.tester.assertNoErrorMessage()
  }
}
