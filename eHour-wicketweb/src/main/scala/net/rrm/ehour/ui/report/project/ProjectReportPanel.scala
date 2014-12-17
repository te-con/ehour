package net.rrm.ehour.ui.report.project

import net.rrm.ehour.report.criteria.ReportCriteria
import net.rrm.ehour.ui.common.report.AggregatedReportConfig
import net.rrm.ehour.ui.report.aggregate.{AggregateReportPanel, AggregateReportChartGenerator}
import net.rrm.ehour.ui.report.model.TreeReportModel
import org.apache.wicket.model.PropertyModel

class ProjectReportPanel(id: String, reportModel: TreeReportModel) extends AggregateReportPanel(id, reportModel,
  AggregatedReportConfig.AGGREGATE_PROJECT,
  AggregateReportChartGenerator.generateProjectReportChart,
  new ProjectReportExcel(new PropertyModel[ReportCriteria](reportModel, "reportCriteria")))
