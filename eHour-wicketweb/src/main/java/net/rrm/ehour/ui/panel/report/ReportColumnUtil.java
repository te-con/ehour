/**
 * Created on Sep 27, 2007
 * Created by Thies Edeling
 * Created by Thies Edeling
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.panel.report;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.model.CurrencyModel;
import net.rrm.ehour.ui.model.FloatModel;
import net.rrm.ehour.ui.session.EhourWebSession;

/**
 * Report column util 
 **/

public class ReportColumnUtil
{
	/**
	 * Get the report columns for a specfic type
	 * @param config
	 * @return
	 */
	public static AggregateReportColumn[] getReportColumns(EhourConfig config, ReportType reportType)
	{
		if (reportType == ReportType.AGGREGATE_CUSTOMER_SINGLE_USER)
		{
			return new AggregateReportColumn[]{
					new AggregateReportColumn("userReport.report.customer"),
					new AggregateReportColumn("userReport.report.project"),
					new AggregateReportColumn("userReport.report.projectCode"),
					new AggregateReportColumn("userReport.report.rate", false),
					new AggregateReportColumn("userReport.report.rate", false),
					new AggregateReportColumn("userReport.report.hours",
												FloatModel.class,
												new Object[]{config},
												true,
												AggregateReportColumn.ColumnType.HOUR),
					new AggregateReportColumn("userReport.report.turnover", 
												CurrencyModel.class,
												new Object[]{config},
												EhourWebSession.getSession().getEhourConfig().isShowTurnover(), 
												AggregateReportColumn.ColumnType.TURNOVER)
			};		
		}
		else if (reportType == ReportType.AGGREGATE_CUSTOMER)
		{
			return new AggregateReportColumn[]{
					new AggregateReportColumn("userReport.report.customer"),
					new AggregateReportColumn("userReport.report.project"),
					new AggregateReportColumn("userReport.report.projectCode"),
					new AggregateReportColumn("userReport.report.user"),
					new AggregateReportColumn("userReport.report.rate"),
					new AggregateReportColumn("userReport.report.hours",
												FloatModel.class,
												new Object[]{config},
												true,
												AggregateReportColumn.ColumnType.HOUR),
					new AggregateReportColumn("userReport.report.turnover", 
												CurrencyModel.class,
												new Object[]{config},
												true, 
												AggregateReportColumn.ColumnType.TURNOVER)
			};
		}
		else if (reportType == ReportType.AGGREGATE_PROJECT)
		{
			return new AggregateReportColumn[]{
					new AggregateReportColumn("userReport.report.project"),
					new AggregateReportColumn("userReport.report.projectCode"),
					new AggregateReportColumn("userReport.report.customer"),
					new AggregateReportColumn("userReport.report.user"),
					new AggregateReportColumn("userReport.report.rate"),
					new AggregateReportColumn("userReport.report.hours",
												FloatModel.class,
												new Object[]{config},
												true,
												AggregateReportColumn.ColumnType.HOUR),
					new AggregateReportColumn("userReport.report.turnover", 
												CurrencyModel.class,
												new Object[]{config},
												true, 
												AggregateReportColumn.ColumnType.TURNOVER)
			};
		}	
		else if (reportType == ReportType.AGGREGATE_USER)
		{
			return new AggregateReportColumn[]{
					new AggregateReportColumn("userReport.report.user"),
					new AggregateReportColumn("userReport.report.customer"),
					new AggregateReportColumn("userReport.report.project"),
					new AggregateReportColumn("userReport.report.projectCode"),
					new AggregateReportColumn("userReport.report.rate"),
					new AggregateReportColumn("userReport.report.hours",
												FloatModel.class,
												new Object[]{config},
												true,
												AggregateReportColumn.ColumnType.HOUR),
					new AggregateReportColumn("userReport.report.turnover", 
												CurrencyModel.class,
												new Object[]{config},
												true, 
												AggregateReportColumn.ColumnType.TURNOVER)
			};
		}			
		else
		{
			System.out.println("whoops");
			return null;
		}
	}
}
