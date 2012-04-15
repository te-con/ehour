package net.rrm.ehour.ui.common.menu

import net.rrm.ehour.ui.common.AbstractSpringWebAppTester
import org.scalatest.matchers.ShouldMatchers
import org.apache.wicket.util.tester.ITestPanelSource
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

@RunWith(classOf[JUnitRunner])
class TreeBasedMenuTest extends AbstractSpringWebAppTester with FunSuite with ShouldMatchers with BeforeAndAfter {

  before {
    super.setUp()
  }

  test("should render menu") {
    tester.startPanel(new ITestPanelSource {
      override def getTestPanel(panelId: String) = new TreeBasedMenu(panelId, MenuDefinition.createMenuDefinition)
    })

    tester.assertNoErrorMessage()
  }

}
