package net.rrm.ehour.ui.report.panel.aggregate

import net.rrm.ehour.ui.common.report.AggregatedReportConfig
import net.rrm.ehour.ui.report.TreeReportModel
import net.rrm.ehour.ui.report.excel.UserReportExcel

class EmployeeReportPanel(id: String, reportModel: TreeReportModel) extends AggregateReportPanel(id, reportModel,
  AggregatedReportConfig.AGGREGATE_USER,
  AggregateReportChartGenerator.generateEmployeeReportChart,
  UserReportExcel.getInstance()){

  implicit val withTurnover: Boolean = false                 }
