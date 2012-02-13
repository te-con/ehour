package net.rrm.ehour.ui.report.panel.aggregate

import net.rrm.ehour.ui.common.report.ReportConfig
import java.lang.String
import net.rrm.ehour.ui.chart.AggregateReportChartGenerators
import net.rrm.ehour.ui.report.TreeReportModel

class CustomerReportPanel(id: String, reportModel: TreeReportModel) extends AggregateReportPanel(id, reportModel,
                ReportConfig.AGGREGATE_CUSTOMER,
                AggregateReportChartGenerators.generateCustomerReportChart)
