package net.rrm.ehour.ui.common.converter

import net.rrm.ehour.AbstractSpec
import net.rrm.ehour.report.reports.element.LockableDate
import java.util.Locale
import org.joda.time.{DateTimeConstants, LocalDate}

class DateToQuarterConverterSpec extends AbstractSpec {
  "Date to Quarter" should {
    val converter = new DateToQuarterConverter
      "Translate January to Q1" in {
        val string = converter.convertToString(new LockableDate(new LocalDate(2014, DateTimeConstants.JANUARY, 1).toDate, false), Locale.US)

        string should be("Q1")
      }

    "Translate September to Q3" in {
      val string = converter.convertToString(new LockableDate(new LocalDate(2014, DateTimeConstants.SEPTEMBER, 1).toDate, false), Locale.US)

      string should be("Q3")
    }
  }
}

