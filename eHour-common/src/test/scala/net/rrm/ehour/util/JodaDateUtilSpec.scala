package net.rrm.ehour.util

import java.util.{Calendar, GregorianCalendar}

import net.rrm.ehour.AbstractSpec
import org.joda.time.chrono.GJChronology
import org.joda.time.{DateTime, Interval, LocalDate}

class JodaDateUtilSpec extends AbstractSpec {
  "Joda Date Util" should {
    "return enumerate the dates between two dates" in {
      val start = new LocalDate(2014, 1, 1)
      val end = new LocalDate(2014, 1, 5)

      val xs = JodaDateUtil.enumerate(start, end)

      xs should have size 5
      xs(0) should equal(start)
      xs(1) should equal(start.plusDays(1))
      xs(2) should equal(start.plusDays(2))
      xs(3) should equal(start.plusDays(3))
      xs(4) should equal(end)
    }

    "should return empty collection when initial start is after end" in {
      val start = new LocalDate(2014, 1, 6)
      val end = new LocalDate(2014, 1, 5)

      val xs = JodaDateUtil.enumerate(start, end)

      xs should be ('empty)
    }

    "find 6 work days in an interval" in {
      val start = new LocalDate(2014, 8, 1)
      val end = new LocalDate(2014, 8, 7)

      val days = JodaDateUtil.findWorkDays(new Interval(start.toDateTimeAtStartOfDay, end.toDateTimeAtStartOfDay))

      days should have size 5
    }

    "find no work days in the weekend" in {
      val start = new LocalDate(2014, 8, 2)
      val end = new LocalDate(2014, 8, 3)

      val days = JodaDateUtil.findWorkDays(new Interval(start.toDateTimeAtStartOfDay, end.toDateTimeAtStartOfDay))

      days should be ('empty)
    }

    "get week for calendar with week starting on Sunday" in {
      val cal = new GregorianCalendar(2014, Calendar.DECEMBER, 1)

      val i = JodaDateUtil.intervalForWeek(cal, Calendar.SUNDAY)

      i.getStart should equal (new DateTime(2014, 11, 30, 0, 0, 0, 0, GJChronology.getInstance()))
      i.getEnd should equal (new DateTime(2014, 12, 6, 23, 59, 59, 999, GJChronology.getInstance()))
    }

    "get week for calendar with week starting on Monday" in {
      val cal = new GregorianCalendar(2014, Calendar.DECEMBER, 1)

      val i = JodaDateUtil.intervalForWeek(cal, Calendar.MONDAY)

      i.getStart should equal (new DateTime(2014, 12, 1, 0, 0, 0, 0, GJChronology.getInstance()))
      i.getEnd should equal (new DateTime(2014, 12, 7, 23, 59, 59, 999, GJChronology.getInstance()))
    }
  }
}
