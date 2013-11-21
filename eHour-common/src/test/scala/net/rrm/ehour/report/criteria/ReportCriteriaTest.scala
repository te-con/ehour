
package net.rrm.ehour.report.criteria

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import net.rrm.ehour.data.DateRange
import net.rrm.ehour.util.DateUtil
import java.util.{Calendar, GregorianCalendar}
import java.util
import net.rrm.ehour.domain.{ProjectObjectMother, CustomerObjectMother}

@RunWith(classOf[JUnitRunner])
class ReportCriteriaTest extends FunSuite {
  val availCriteria = new AvailableCriteria()
  availCriteria.setReportRange(new DateRange())

  val nowRange = DateUtil.calendarToMonthRange(new GregorianCalendar())

  test("should use user criteria report range when it's within the bounds of the available criteria") {
    val userCriteria = new UserSelectedCriteria
    userCriteria.setReportRange(new DateRange)

    val criteria = new ReportCriteria(availCriteria, userCriteria)
    assert(criteria.getReportRange == userCriteria.getReportRange)
  }

  test("should use date start of user criteria when it's within the bounds of the available criteria") {
    val cal = new GregorianCalendar()
    cal.add(Calendar.DAY_OF_YEAR, 1)

    val range = new DateRange()
    range.setDateStart(cal.getTime)

    val userCriteria = new UserSelectedCriteria
    userCriteria.setReportRange(range)

    val criteria = new ReportCriteria(availCriteria, userCriteria)

    val criteriaStart = DateUtil.nullifyTime(criteria.getReportRange.getDateStart)
    assert(criteriaStart == criteria.getReportRange.getDateStart)
  }

  test("should use the start of the user criteria and the end of the available criteria when the bound of the available criteria is more restrictive") {
    val availCal = new GregorianCalendar()
    availCal.add(Calendar.DAY_OF_YEAR, 2)

    val availRange = new DateRange()
    availRange.setDateEnd(availCal.getTime)

    val availableCriteria = new AvailableCriteria()
    availableCriteria.setReportRange(availRange)

    val userCal = new GregorianCalendar()
    val userRange = new DateRange()
    userRange.setDateStart(userCal.getTime)

    val userCriteria = new UserSelectedCriteria
    userCriteria.setReportRange(userRange)

    val criteria = new ReportCriteria(availableCriteria, userCriteria)

    DateUtil.nullifyTime(userCal)
    assert(userCal.getTime == criteria.getReportRange.getDateStart)

    DateUtil.maximizeTime(availCal)
    assert(availCal.getTime == criteria.getReportRange.getDateEnd)
  }

  test("should update the customer sorting in the available criteria with the one defined in userSelectedCriteria") {
    val userCriteria = new UserSelectedCriteria
    val customerA = CustomerObjectMother.createCustomer(1)
    customerA.setName("A")
    customerA.setCode("B")

    val customerB = CustomerObjectMother.createCustomer(2)
    customerB.setName("B")
    customerB.setCode("A")

    availCriteria.setCustomers(util.Arrays.asList(customerA, customerB))

    val criteria = new ReportCriteria(availCriteria, userCriteria)

    userCriteria.setCustomerSort(Sort.NAME)
    criteria.updateCustomerSort()

    val sortedCustomers = criteria.getAvailableCriteria.getCustomers
    assert(sortedCustomers.get(0).getName == "A")

    userCriteria.setCustomerSort(Sort.CODE)
    criteria.updateCustomerSort()
    assert(sortedCustomers.get(0).getName == "B")
  }

  test("should update the project sorting in the available criteria with the one defined in userSelectedCriteria") {
    val userCriteria = new UserSelectedCriteria
    val projectA = ProjectObjectMother.createProject(1)
    projectA.setName("A")
    projectA.setProjectCode("B")

    val projectB = ProjectObjectMother.createProject(2)
    projectB.setName("B")
    projectB.setProjectCode("A")

    availCriteria.setProjects(util.Arrays.asList(projectA, projectB))

    val criteria = new ReportCriteria(availCriteria, userCriteria)

    userCriteria.setProjectSort(Sort.NAME)
    criteria.updateProjectSort()

    assert(criteria.getAvailableCriteria.getProjects.get(0).getName == "A")

    userCriteria.setProjectSort(Sort.CODE)
    criteria.updateProjectSort()
    assert(criteria.getAvailableCriteria.getProjects.get(0).getName == "B")
  }
}
