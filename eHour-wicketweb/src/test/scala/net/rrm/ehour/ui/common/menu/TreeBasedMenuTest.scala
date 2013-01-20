package net.rrm.ehour.ui.common.menu

import net.rrm.ehour.ui.common.AbstractSpringWebAppTester
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

@RunWith(classOf[JUnitRunner])
class TreeBasedMenuTest extends AbstractSpringWebAppTester with FunSuite with ShouldMatchers with BeforeAndAfter {

  before {
    super.setUp()
  }

  test("should render menu") {
    tester.startComponentInPage(new TreeBasedMenu("id", MenuDefinition.createMenuDefinition))
    tester.assertNoErrorMessage()
  }
}
