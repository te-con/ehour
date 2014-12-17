package net.rrm.ehour.ui.report.detailed

import java.util

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.data.DateRange
import net.rrm.ehour.report.criteria.{AggregateBy, ReportCriteria, UserSelectedCriteria}
import net.rrm.ehour.report.reports.ReportData
import net.rrm.ehour.report.reports.element.FlatReportElement
import net.rrm.ehour.report.service.ReportCriteriaService
import org.joda.time.{DateTimeConstants, LocalDate}
import org.scalatest.BeforeAndAfterAll

class DetailedReportModelSpec extends AbstractSpringWebAppSpec with BeforeAndAfterAll {
  val service = mock[ReportCriteriaService]

  override def beforeAll() {
    springTester.getMockContext.putBean(service)
    springTester.setUp()
  }

  def toElement(year: Int, month: Int, day: Int) = {
    val e = new FlatReportElement()
    e.setAssignmentId(1)
    e.setDayDate(new LocalDate(year, month, day).toDate)
    e
  }

  def preprocess(aggregateBy: AggregateBy, elements: util.List[FlatReportElement]) = {
    val userSelectedCriteria = new UserSelectedCriteria()
    userSelectedCriteria.setAggregateBy(aggregateBy)

    val criteria = new ReportCriteria(userSelectedCriteria)

    val data = new ReportData(elements, new DateRange(), criteria.getUserSelectedCriteria)

    val model = new DetailedReportModel(criteria)
    val sortedData = model.preprocess(data, criteria)

    sortedData.getReportElements
  }

  "Detailed Report Model" should {
    "elements should be sorted" in {
      val elementA = toElement(2014, DateTimeConstants.APRIL, 1)

      val elementB = toElement(2014, DateTimeConstants.FEBRUARY, 1)
      val sortedElements = preprocess(AggregateBy.DAY, util.Arrays.asList(elementA, elementB))

      sortedElements.get(0) should equal(elementB)
      sortedElements.get(1) should equal(elementA)
    }

    "elements should be aggregated by week" in {
      val elementA = toElement(2014, DateTimeConstants.APRIL, 1)
      val elementB = toElement(2014, DateTimeConstants.APRIL, 8)
      val elementC = toElement(2014, DateTimeConstants.APRIL, 9)

      val sortedElements = preprocess(AggregateBy.WEEK, util.Arrays.asList(elementA, elementB, elementC))

      sortedElements.size() should equal(2)
    }

    "elements should be aggregated by month" in {
      val elementA = toElement(2014, DateTimeConstants.JANUARY, 1)
      val elementB = toElement(2014, DateTimeConstants.JANUARY, 8)
      val elementC = toElement(2014, DateTimeConstants.FEBRUARY, 9)

      val sortedElements = preprocess(AggregateBy.MONTH, util.Arrays.asList(elementA, elementB, elementC))

      sortedElements.size() should equal(2)
    }

    "elements should be aggregated by quarter" in {
      val elementA = toElement(2014, DateTimeConstants.JANUARY, 1)
      val elementB = toElement(2014, DateTimeConstants.JANUARY, 8)
      val elementC = toElement(2014, DateTimeConstants.APRIL, 1)
      val elementD = toElement(2014, DateTimeConstants.APRIL, 9)

      val sortedElements = preprocess(AggregateBy.QUARTER, util.Arrays.asList(elementA, elementB, elementC, elementD))

      sortedElements.size() should equal(2)
    }

    "elements should be aggregated by year" in {
      val elementA = toElement(2013, DateTimeConstants.JANUARY, 1)
      val elementB = toElement(2013, DateTimeConstants.JANUARY, 8)
      val elementC = toElement(2014, DateTimeConstants.APRIL, 1)
      val elementD = toElement(2015, DateTimeConstants.APRIL, 9)

      val sortedElements = preprocess(AggregateBy.YEAR, util.Arrays.asList(elementA, elementB, elementC, elementD))

      sortedElements.size() should equal(3)
    }
  }
}
