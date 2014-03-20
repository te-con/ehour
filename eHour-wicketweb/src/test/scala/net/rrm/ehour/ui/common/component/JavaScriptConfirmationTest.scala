package net.rrm.ehour.ui.common.component

import org.scalatest.{WordSpec, Matchers}
import org.apache.wicket.model.Model

class JavaScriptConfirmationTest extends WordSpec with Matchers {
  "JavaScript confirmation" should {
    "escape French apostrophes" in {

      val subject = new JavaScriptConfirmation(new Model[String]("je m'appelle Thies"))

      val precondition: CharSequence = subject.getPrecondition(null).toString

      precondition should be ("return confirm('je m\\\'appelle Thies');")
    }
  }
}