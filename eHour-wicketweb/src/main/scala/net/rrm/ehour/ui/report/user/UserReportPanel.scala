package net.rrm.ehour.ui.report.user

import net.rrm.ehour.report.criteria.ReportCriteria
import net.rrm.ehour.ui.common.report.AggregatedReportConfig
import net.rrm.ehour.ui.report.aggregate.{AggregateReportPanel, AggregateReportChartGenerator}
import net.rrm.ehour.ui.report.model.TreeReportModel
import org.apache.wicket.model.PropertyModel

class UserReportPanel(id: String, reportModel: TreeReportModel) extends AggregateReportPanel(id, reportModel,
                          AggregatedReportConfig.AGGREGATE_USER,
                          AggregateReportChartGenerator.generateUserReportChart,
                          new UserReportExcel(new PropertyModel[ReportCriteria](reportModel, "reportCriteria"))) {
  implicit val withTurnover: Boolean = false
}
