package net.rrm.ehour.ui.report.panel.aggregate

import net.rrm.ehour.report.criteria.ReportCriteria
import net.rrm.ehour.ui.common.report.AggregatedReportConfig
import net.rrm.ehour.ui.report.TreeReportModel
import net.rrm.ehour.ui.report.excel.UserReportExcel
import org.apache.wicket.model.PropertyModel

class EmployeeReportPanel(id: String, reportModel: TreeReportModel) extends AggregateReportPanel(id, reportModel,
                          AggregatedReportConfig.AGGREGATE_USER,
                          AggregateReportChartGenerator.generateEmployeeReportChart,
                          new UserReportExcel(new PropertyModel[ReportCriteria](reportModel, "reportCriteria"))) {
  implicit val withTurnover: Boolean = false
}
