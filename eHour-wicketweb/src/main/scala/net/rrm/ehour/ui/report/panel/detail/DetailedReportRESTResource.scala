package net.rrm.ehour.ui.report.panel.detail

import org.wicketstuff.rest.resource.gson.GsonRestResource
import org.wicketstuff.rest.annotations.MethodMapping
import org.apache.wicket.spring.injection.annot.SpringBean
import net.rrm.ehour.ui.report.cache.ReportCacheService

class DetailedReportRESTResource extends GsonRestResource{

  @SpringBean
  var reportCacheService: ReportCacheService = _

  @MethodMapping("/hour/{cacheKey}")
  def findReport(cacheKey: String): String = {
    Console.println("Fefe")

    "hai"
  }
}
