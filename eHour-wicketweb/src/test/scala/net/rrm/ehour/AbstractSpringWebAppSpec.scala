package net.rrm.ehour

import net.rrm.ehour.ui.common.BaseSpringWebAppTester
import scala.reflect.Manifest
import org.apache.wicket.event.IEvent
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfter

abstract class AbstractSpringWebAppSpec extends AbstractSpec with BeforeAndAfter {
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

  def mockEvent[T](event: T)(implicit manifest: Manifest[T]):IEvent[T] = {
    val mockedEvent = mock[IEvent[T]]
    when(mockedEvent.getPayload).thenReturn(event)

    mockedEvent
  }
}
