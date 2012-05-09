package net.rrm.ehour.ui.report.panel.aggregate

import net.rrm.ehour.ui.common.report.ReportConfig
import net.rrm.ehour.ui.report.TreeReportModel
import net.rrm.ehour.ui.chart.AggregateReportChartGenerator

class EmployeeReportPanel(id: String, reportModel: TreeReportModel) extends AggregateReportPanel(id, reportModel,
  ReportConfig.AGGREGATE_EMPLOYEE,
  AggregateReportChartGenerator.generateEmployeeReportChart)
