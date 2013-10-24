package net.rrm.ehour

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester
import org.scalatest.matchers.ClassicMatchers
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, WordSpec}

@RunWith(classOf[JUnitRunner])
abstract class AbstractSpringWebAppSpec extends AbstractSpringWebAppTester with ClassicMatchers with MockitoSugar with WordSpec with BeforeAndAfterEach with BeforeAndAfterAll {
  override def beforeEach() {
    super.setUp()
  }
}
