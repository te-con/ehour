package net.rrm.ehour.ui.manage.lock

import java.util.Locale

import org.joda.time.DateTime
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class LockAdminBackingBeanSpec extends WordSpec with Matchers {
  "Lock Admin Backing Bean" should {
    "set week number as name when there are less than 7 days between start and end" in {
      val start = new DateTime()
      val end = start.plusDays(5)

      LockAdminBackingBean.determineName(start.toDate, end.toDate, Locale.US) should startWith("Week")
    }

    "set start and end date as name when there are less than 7 days between start and end" in {
      val start = new DateTime(2014, 2, 1, 0, 0)
      val end = start.plusDays(14)

      LockAdminBackingBean.determineName(start.toDate, end.toDate, Locale.US) should be("2/1/14 - 2/15/14")
    }

    "set month name when there's approx. a month between start and end date" in {
      val start = new DateTime(2014, 1, 1, 0, 0)
      val end = start.plusDays(30)

      LockAdminBackingBean.determineName(start.toDate, end.toDate, Locale.US) should be("January, 2014")
    }

    "set quarter name when there's 12 or 13 weeks between start and end date" in {
      val start = new DateTime(2014, 4, 1, 0, 0)
      val end = start.plusWeeks(13)

      LockAdminBackingBean.determineName(start.toDate, end.toDate, Locale.US) should be("Q2, 2014")
    }
  }
}
