package net.rrm.ehour.ui.report.detailed

import net.rrm.ehour.AbstractSpec
import org.joda.time.{DateTimeConstants, LocalDate}

class ByQuarterSpec extends AbstractSpec {
  "By Quarter" should {
    "set to beginning of Q4 for December" in {
      val q4 = new LocalDate(new ByQuarter().toDate(new LocalDate(2013, DateTimeConstants.DECEMBER, 1).toDate))

      q4.getDayOfMonth should be (1)
      q4.getMonthOfYear should be (DateTimeConstants.OCTOBER)
    }

    "set to beginning of Q1 for February" in {
      val q1 = new LocalDate(new ByQuarter().toDate(new LocalDate(2013, DateTimeConstants.FEBRUARY, 1).toDate))

      q1.getDayOfMonth should be (1)
      q1.getMonthOfYear should be (DateTimeConstants.JANUARY)
    }
  }
}
