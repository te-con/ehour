
package net.rrm.ehour.report.criteria

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import net.rrm.ehour.data.DateRange
import net.rrm.ehour.util.DateUtil
import java.util.{Calendar, GregorianCalendar}

@RunWith(classOf[JUnitRunner])
class ReportCriteriaTest extends FunSuite {
  val availCriteria = new AvailableCriteria()
  availCriteria.setReportRange(new DateRange())

  val nowRange = DateUtil.calendarToMonthRange(new GregorianCalendar())

  test("should use user criteria report range when it's within the bounds of the available criteria") {
    val userCriteria = new UserCriteria
    userCriteria.setReportRange(new DateRange)

    val criteria = new ReportCriteria(availCriteria, userCriteria)
    criteria.getReportRange == userCriteria.getReportRange
  }

  test("should use date start of user criteria when it's within the bounds of the available criteria") {
    val cal = new GregorianCalendar()
    cal.add(Calendar.DAY_OF_YEAR, 1)

    val range = new DateRange()
    range.setDateStart(cal.getTime)

    val userCriteria = new UserCriteria
    userCriteria.setReportRange(range)

    val criteria = new ReportCriteria(availCriteria, userCriteria)

    val criteriaStart = DateUtil.nullifyTime(criteria.getReportRange.getDateStart)
    criteriaStart == criteria.getReportRange.getDateStart
  }

  test("should use the start of the user criteria and the end of the available criteria when the bound of the available criteria is more restrictive") {
    val availCal = new GregorianCalendar()
    availCal.add(Calendar.DAY_OF_YEAR, 2)

    val availRange = new DateRange()
    availRange.setDateStart(availCal.getTime)

    val availableCriteria = new AvailableCriteria()
    availCriteria.setReportRange(availRange)

    val userCal = new GregorianCalendar()
    userCal.add(Calendar.DAY_OF_YEAR, 1)

    val userRange = new DateRange()
    userRange.setDateStart(userCal.getTime)

    val userCriteria = new UserCriteria
    userCriteria.setReportRange(userRange)

    val criteria = new ReportCriteria(availableCriteria, userCriteria )

    val criteriaStart = DateUtil.nullifyTime(userCal)
    criteriaStart == criteria.getReportRange.getDateStart

    val criteriaEnd = DateUtil.maximizeTime(availCal)
    criteriaEnd == criteria.getReportRange.getDateEnd
  }
}
