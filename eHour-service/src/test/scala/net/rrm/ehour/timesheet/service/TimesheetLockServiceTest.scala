package net.rrm.ehour.timesheet.service

import org.scalatest.{Matchers, WordSpec}
import java.{util => ju}
import org.joda.time.{LocalDate, Interval}
import scala.collection.convert.WrapAsScala

class TimesheetLockServiceTest extends WordSpec with Matchers {
  "Timesheet Lock Service" should {
    "should extract 3 dates from single interval" in {
      val x: ju.Collection[ju.Date] = TimesheetLockService.intervalToJavaList(Seq(new Interval(LocalDate.parse("2013-01-01").toDateTimeAtStartOfDay, LocalDate.parse("2013-01-03").toDateTimeAtStartOfDay)))

      val y = WrapAsScala.collectionAsScalaIterable(x).toList

      y should have size 3

      y should contain(LocalDate.parse("2013-01-01").toDate)
      y should contain(LocalDate.parse("2013-01-02").toDate)
      y should contain(LocalDate.parse("2013-01-03").toDate)
    }
  }
}
