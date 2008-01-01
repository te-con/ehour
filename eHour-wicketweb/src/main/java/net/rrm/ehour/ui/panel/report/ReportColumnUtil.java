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
import net.rrm.ehour.ui.model.DateModel;
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
	public static TreeReportColumn[] getReportColumns(EhourConfig config, ReportType reportType)
	{
		if (reportType == ReportType.AGGREGATE_CUSTOMER_SINGLE_USER)
		{
			return new TreeReportColumn[]{
					new TreeReportColumn("userReport.report.customer"),
					new TreeReportColumn("userReport.report.project"),
					new TreeReportColumn("userReport.report.projectCode"),
					new TreeReportColumn("userReport.report.rate", false),
					new TreeReportColumn("userReport.report.rate", false),
					new TreeReportColumn("userReport.report.hours",
												FloatModel.class,
												new Object[]{config},
												true,
												TreeReportColumn.ColumnType.HOUR),
					new TreeReportColumn("userReport.report.turnover", 
												CurrencyModel.class,
												new Object[]{config},
												EhourWebSession.getSession().getEhourConfig().isShowTurnover(), 
												TreeReportColumn.ColumnType.TURNOVER)
			};		
		}
		else if (reportType == ReportType.AGGREGATE_CUSTOMER)
		{
			return new TreeReportColumn[]{
					new TreeReportColumn("userReport.report.customer"),
					new TreeReportColumn("userReport.report.project"),
					new TreeReportColumn("userReport.report.projectCode"),
					new TreeReportColumn("userReport.report.user"),
					new TreeReportColumn("userReport.report.rate",
												CurrencyModel.class,
												new Object[]{config},
												true,
												TreeReportColumn.ColumnType.RATE),
					new TreeReportColumn("userReport.report.hours",
												FloatModel.class,
												new Object[]{config},
												true,
												TreeReportColumn.ColumnType.HOUR),
					new TreeReportColumn("userReport.report.turnover", 
												CurrencyModel.class,
												new Object[]{config},
												true, 
												TreeReportColumn.ColumnType.TURNOVER)
			};
		}
		else if (reportType == ReportType.AGGREGATE_PROJECT)
		{
			return new TreeReportColumn[]{
					new TreeReportColumn("userReport.report.project"),
					new TreeReportColumn("userReport.report.projectCode"),
					new TreeReportColumn("userReport.report.customer"),
					new TreeReportColumn("userReport.report.user"),
					new TreeReportColumn("userReport.report.rate",
												CurrencyModel.class,
												new Object[]{config},
												true,
												TreeReportColumn.ColumnType.RATE),
					new TreeReportColumn("userReport.report.hours",
												FloatModel.class,
												new Object[]{config},
												true,
												TreeReportColumn.ColumnType.HOUR),
					new TreeReportColumn("userReport.report.turnover", 
												CurrencyModel.class,
												new Object[]{config},
												true, 
												TreeReportColumn.ColumnType.TURNOVER)
			};
		}	
		else if (reportType == ReportType.AGGREGATE_EMPLOYEE)
		{
			return new TreeReportColumn[]{
					new TreeReportColumn("userReport.report.user"),
					new TreeReportColumn("userReport.report.customer"),
					new TreeReportColumn("userReport.report.project"),
					new TreeReportColumn("userReport.report.projectCode"),
					new TreeReportColumn("userReport.report.rate",
												CurrencyModel.class,
												new Object[]{config},
												true,
												TreeReportColumn.ColumnType.RATE),							
					new TreeReportColumn("userReport.report.hours",
												FloatModel.class,
												new Object[]{config},
												true,
												TreeReportColumn.ColumnType.HOUR),
					new TreeReportColumn("userReport.report.turnover", 
												CurrencyModel.class,
												new Object[]{config},
												true, 
												TreeReportColumn.ColumnType.TURNOVER)
			};
		}	
		else if (reportType == ReportType.DETAILED_REPORT)
		{
			return new TreeReportColumn[]{
					new TreeReportColumn("userReport.report.customer"),
					new TreeReportColumn("userReport.report.project"),
					new TreeReportColumn("userReport.report.date",
												DateModel.class,
												new Object[]{config, DateModel.DATESTYLE_FULL_SHORT, ""},
												new Class[]{EhourConfig.class, int.class, String.class},
												true,
												TreeReportColumn.ColumnType.OTHER),
					new TreeReportColumn("userReport.report.user"),
					new TreeReportColumn("userReport.report.comment"),
					new TreeReportColumn("userReport.report.hours",
												FloatModel.class,
												new Object[]{config},
												true,
												TreeReportColumn.ColumnType.HOUR),
					new TreeReportColumn("userReport.report.turnover", 
												CurrencyModel.class,
												new Object[]{config},
												true, 
												TreeReportColumn.ColumnType.TURNOVER)
			};
			
		}		
		else
		{
			return null;
		}
	}
}
