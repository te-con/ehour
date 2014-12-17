package net.rrm.ehour.ui.report.detailed

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.ui.report.cache.ReportCacheService
import org.mockito.Mockito._

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
