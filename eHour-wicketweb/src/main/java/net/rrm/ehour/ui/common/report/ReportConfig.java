/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.common.report;

import static net.rrm.ehour.ui.common.converter.DateConverter.DateStyle.FULL_SHORT;
import static net.rrm.ehour.ui.common.report.ReportColumn.DisplayType.ALLOW_DUPLICATES;
import static net.rrm.ehour.ui.common.report.ReportColumn.DisplayType.VISIBLE;
import net.rrm.ehour.ui.common.converter.CurrencyConverter;
import net.rrm.ehour.ui.common.converter.DateConverter;
import net.rrm.ehour.ui.common.report.ReportColumn.ColumnType;
import net.rrm.ehour.ui.report.panel.user.TurnoverTreeReportColumn;



/**
 * All report definitions
 **/

public enum ReportConfig
{
	// constructors like these might be a bit over the top..
	// take note, the columnResourceKey is used for serie creation with trend charts (pardon my English, it's late.. or early in the morning)
	DETAILED_REPORT (2, 
						new ReportColumn("userReport.report.customer"),
						new ReportColumn("userReport.report.project"),
						new ReportColumn("userReport.report.date", ColumnType.DATE, new DateConverter(FULL_SHORT, ""), VISIBLE),
						new ReportColumn("userReport.report.user"),
						new ReportColumn("userReport.report.comment", ColumnType.COMMENT, VISIBLE, ALLOW_DUPLICATES),
						new ReportColumn("userReport.report.hours", ColumnType.HOUR, VISIBLE, ALLOW_DUPLICATES),  
						new ReportColumn("userReport.report.turnover", ColumnType.TURNOVER, CurrencyConverter.getInstance(), VISIBLE, ALLOW_DUPLICATES)),
	
	AGGREGATE_CUSTOMER (new ReportColumn("userReport.report.customer"),
						new ReportColumn("userReport.report.project"),
						new ReportColumn("userReport.report.projectCode"),
						new ReportColumn("userReport.report.user"),
						new ReportColumn("userReport.report.rate", ColumnType.RATE, CurrencyConverter.getInstance(), VISIBLE, ALLOW_DUPLICATES),
						new ReportColumn("userReport.report.hours", ColumnType.HOUR, VISIBLE, ALLOW_DUPLICATES),
						new ReportColumn("userReport.report.turnover", ColumnType.TURNOVER, CurrencyConverter.getInstance(), VISIBLE, ALLOW_DUPLICATES)),
														
	AGGREGATE_PROJECT (new ReportColumn("userReport.report.project"),
						new ReportColumn("userReport.report.projectCode"),
						new ReportColumn("userReport.report.customer"),
						new ReportColumn("userReport.report.user"),
						new ReportColumn("userReport.report.rate", ColumnType.RATE, CurrencyConverter.getInstance(), VISIBLE, ALLOW_DUPLICATES),
						new ReportColumn("userReport.report.hours", ColumnType.HOUR, VISIBLE, ALLOW_DUPLICATES),
						new ReportColumn("userReport.report.turnover", ColumnType.TURNOVER, CurrencyConverter.getInstance(), VISIBLE, ALLOW_DUPLICATES)),

	AGGREGATE_EMPLOYEE (new ReportColumn("userReport.report.user"),
						new ReportColumn("userReport.report.customer"),
						new ReportColumn("userReport.report.project"),
						new ReportColumn("userReport.report.projectCode"),
						new ReportColumn("userReport.report.rate", ColumnType.RATE, CurrencyConverter.getInstance(), VISIBLE, ALLOW_DUPLICATES),
						new ReportColumn("userReport.report.hours", ColumnType.HOUR, VISIBLE, ALLOW_DUPLICATES),
						new ReportColumn("userReport.report.turnover", ColumnType.TURNOVER, CurrencyConverter.getInstance(), VISIBLE, ALLOW_DUPLICATES)),

	AGGREGATE_CUSTOMER_SINGLE_USER (new ReportColumn("userReport.report.customer"),
									new ReportColumn("userReport.report.project"),
									new ReportColumn("userReport.report.projectCode"),
									new ReportColumn("userReport.report.hours", ColumnType.HOUR, VISIBLE, ALLOW_DUPLICATES),
									new TurnoverTreeReportColumn("userReport.report.turnover", CurrencyConverter.getInstance())),

	AUDIT_REPORT (new ReportColumn("audit.report.column.date", ColumnType.DATE, new DateConverter(FULL_SHORT, ""), VISIBLE),
					new ReportColumn("audit.report.column.lastName"),
					new ReportColumn("audit.report.column.action"),
					new ReportColumn("audit.report.column.type"));
												
	private ReportColumn[] 	reportColumns;
	private int groupByColumn;
	
	/**
	 * 
	 * @param reportColumns
	 */
	
	private ReportConfig(ReportColumn... reportColumns)
	{
		this(-1, reportColumns);
	}
	
	/**
	 * 
	 * @param reportColumns
	 * @param groupByColumn
	 */
	private ReportConfig(int groupByColumn, ReportColumn... reportColumns)
	{
		this.reportColumns = reportColumns;
		this.groupByColumn = groupByColumn;
	}


	/**
	 * @return the reportColumns
	 */
	public ReportColumn[] getReportColumns()
	{
		return reportColumns;
	}


	/**
	 * @return the groupByColumn
	 */
	public int getGroupByColumn()
	{
		return groupByColumn;
	}
}
