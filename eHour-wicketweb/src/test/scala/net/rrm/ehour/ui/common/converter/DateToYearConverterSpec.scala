package net.rrm.ehour.ui.common.converter

import net.rrm.ehour.AbstractSpec
import net.rrm.ehour.report.reports.element.LockableDate
import org.joda.time.{DateTimeConstants, LocalDate}
import java.util.Locale

class DateToYearConverterSpec extends AbstractSpec {
  "Date to Year" should {
    val converter = new DateToYearConverter
    "Translate January 2014 to 2014" in {
      val string = converter.convertToString(new LockableDate(new LocalDate(2014, DateTimeConstants.JANUARY, 1).toDate, false), Locale.US)

      string should be("2014")
    }
  }
}
