package net.rrm.ehour.ui.report.panel.aggregate

import net.rrm.ehour.ui.common.report.AggregatedReportConfig
import java.lang.String
import net.rrm.ehour.ui.report.TreeReportModel
import net.rrm.ehour.ui.report.excel.CustomerReportExcel

class CustomerReportPanel(id: String, reportModel: TreeReportModel) extends AggregateReportPanel(id, reportModel,
                AggregatedReportConfig.AGGREGATE_CUSTOMER,
                AggregateReportChartGenerator.generateCustomerReportChart,
              CustomerReportExcel.getInstance())
