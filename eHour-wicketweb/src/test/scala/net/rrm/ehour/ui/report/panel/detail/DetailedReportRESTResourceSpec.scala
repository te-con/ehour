package net.rrm.ehour.ui.report.panel.detail

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.ui.report.cache.ReportCacheService
import org.mockito.Mockito._
import net.rrm.ehour.ui.report.panel.DetailedReportDataObjectMother
import net.rrm.ehour.report.criteria.AggregateBy
import net.rrm.ehour.data.DateRange
import org.joda.time.{LocalDate, DateTimeConstants, DateTime}

class DetailedReportRESTResourceSpec extends AbstractSpringWebAppSpec {
  "Detailed Report REST resource" should {
    val cacheService = mockService[ReportCacheService]

     before {
      reset(cacheService)

       when(cacheService.retrieveReportData("123")).thenReturn(Some(DetailedReportDataObjectMother.getFlatReportData))
     }

    "properly serialize date value values" in {
      tester.getRequest.setMethod("GET")
      tester.executeUrl("./rest/report/detailed/hour/123")

      val response = tester.getLastResponseAsString

      response should not include "{}"
    }
  }
}
