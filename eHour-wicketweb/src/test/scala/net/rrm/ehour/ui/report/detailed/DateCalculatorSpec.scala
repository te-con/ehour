package net.rrm.ehour.ui.report.detailed

import net.rrm.ehour.AbstractSpec
import org.joda.time.{DateTimeConstants, LocalDate}

class DateCalculatorSpec extends AbstractSpec {
  "Date Calculator" should {
    "calculate proper first day of week when first day of week is a Monday" in {
      implicit val weekStart = DateTimeConstants.MONDAY

      val date = DateCalculator.toWeekStart(new LocalDate(2014, DateTimeConstants.MAY, 21).toDate)

      date should equal(new LocalDate(2014, DateTimeConstants.MAY, 19))
    }

    "calculate proper first day of week when first day of week is a Sunday" in {
      implicit val weekStart = DateTimeConstants.SUNDAY

      val date = DateCalculator.toWeekStart(new LocalDate(2014, DateTimeConstants.MAY, 21).toDate)

      date should equal(new LocalDate(2014, DateTimeConstants.MAY, 18))
    }

    "calculate proper first day of week when first day of week is a Sunday and the next day of the original day is a Saturday" in {
      implicit val weekStart = DateTimeConstants.SUNDAY

      val date = DateCalculator.toWeekStart(new LocalDate(2014, DateTimeConstants.MAY, 24).toDate)

      date should equal(new LocalDate(2014, DateTimeConstants.MAY, 18))
    }

    "calculate proper first day of month" in {
      val date = DateCalculator.toMonthStart(new LocalDate(2014, DateTimeConstants.MAY, 24).toDate)

      date should equal(new LocalDate(2014, DateTimeConstants.MAY, 1))
    }

    "calculate proper first day of quarter for May" in {
      val date = DateCalculator.toQuarterStart(new LocalDate(2014, DateTimeConstants.MAY, 24).toDate)

      date should equal(new LocalDate(2014, DateTimeConstants.APRIL, 1))
    }

    "calculate proper first day of quarter for January" in {
      val date = DateCalculator.toQuarterStart(new LocalDate(2014, DateTimeConstants.JANUARY, 24).toDate)

      date should equal(new LocalDate(2014, DateTimeConstants.JANUARY, 1))
    }

    "calculate proper first day of year" in {
      val date = DateCalculator.toYearStart(new LocalDate(2014, DateTimeConstants.MAY, 24).toDate)

      date should equal(new LocalDate(2014, DateTimeConstants.JANUARY, 1))
    }
  }
}
