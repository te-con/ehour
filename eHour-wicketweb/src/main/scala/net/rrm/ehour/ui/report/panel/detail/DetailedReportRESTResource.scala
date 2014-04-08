package net.rrm.ehour.ui.report.panel.detail

import org.wicketstuff.rest.resource.gson.GsonRestResource
import org.wicketstuff.rest.annotations.MethodMapping

class DetailedReportRESTResource extends GsonRestResource{
  @MethodMapping("/hour")
  def findReport: String = {
    Console.println("Fefe")

    "hai"
  }
}
