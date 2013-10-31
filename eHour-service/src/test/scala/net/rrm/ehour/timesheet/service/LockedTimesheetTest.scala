package net.rrm.ehour.timesheet.service

import org.scalatest.{Matchers, WordSpec}
import org.joda.time.LocalDate
import java.util.Locale

class LockedTimesheetTest extends WordSpec with Matchers {
  "Locked Timesheet" should {
    implicit val locale = Locale.ENGLISH
    val dateStart = new LocalDate(2013,1,1)
    val dateEnd = new LocalDate(2013,2,1)
    val lockedTimesheet = LockedTimesheet(dateStart, dateEnd)

    "use specified name when supplied" in {
      lockedTimesheet.copy(name = Some("name")).lockName should be("name")
    }

    "combine start and end date when no name is supplied" in {
      lockedTimesheet.lockName should be("1/1/13 - 2/1/13")
    }
  }
}
