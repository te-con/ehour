package net.rrm.ehour

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import net.rrm.ehour.ui.common.BaseSpringWebAppTester
import org.scalatest.mock.MockitoSugar
import org.scalatest.{Matchers, BeforeAndAfterEach, WordSpec}
import scala.reflect.Manifest

@RunWith(classOf[JUnitRunner])
abstract class AbstractSpringWebAppSpec extends WordSpec with Matchers with MockitoSugar with BeforeAndAfterEach  {
  val springTester = new BaseSpringWebAppTester
  def tester = springTester.getTester

  override def beforeEach() {
    springTester.setUp()
  }

  def mockService[T <: AnyRef](implicit manifest: Manifest[T]):T  = {
    val mocked = mock[T]
    springTester.getMockContext.putBean(mocked)
    mocked
  }
}
