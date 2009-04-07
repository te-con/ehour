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

import net.rrm.ehour.ui.common.model.CurrencyModel;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.model.FloatModel;
import net.rrm.ehour.ui.report.panel.user.TurnoverTreeReportColumn;

import org.apache.wicket.model.Model;

/**
 * All report types 
 **/

public enum ReportConfig
{
	// constructors like these might be a bit over the top..
	// take note, the columnResourceKey is used for serie creation with trend charts (pardon my English, it's late.. or early in the morning)
	DETAILED_REPORT (2, 
						new ReportColumn("userReport.report.customer",
													ReportColumn.COLUMN_VISIBLE,
													ReportColumn.COLUMN_NODUPLICATES,
													true),
						new ReportColumn("userReport.report.project",
													ReportColumn.COLUMN_VISIBLE,
													ReportColumn.COLUMN_NODUPLICATES,
													true),
						new ReportColumn("userReport.report.date",
													DateModel.class,
													new Object[]{DateModel.DATESTYLE_FULL_SHORT, ""},
													new Class[]{int.class, String.class},
													ReportColumn.COLUMN_VISIBLE,
													ReportColumn.ColumnType.DATE),
						new ReportColumn("userReport.report.user",
													ReportColumn.COLUMN_VISIBLE,
													ReportColumn.COLUMN_ALLOWDUPLICATES,
													true),
						new ReportColumn("userReport.report.comment",
													Model.class,
													ReportColumn.COLUMN_VISIBLE,
													ReportColumn.COLUMN_ALLOWDUPLICATES,
													ReportColumn.ColumnType.COMMENT),
						new ReportColumn("userReport.report.hours",
													FloatModel.class,
													ReportColumn.COLUMN_VISIBLE,
													ReportColumn.COLUMN_ALLOWDUPLICATES,
													ReportColumn.ColumnType.HOUR),
						new ReportColumn("userReport.report.turnover", 
													CurrencyModel.class,
													ReportColumn.COLUMN_VISIBLE,
													ReportColumn.COLUMN_ALLOWDUPLICATES,
													ReportColumn.ColumnType.TURNOVER)),
	
	AGGREGATE_CUSTOMER (new ReportColumn("userReport.report.customer"),
						new ReportColumn("userReport.report.project"),
						new ReportColumn("userReport.report.projectCode"),
						new ReportColumn("userReport.report.user"),
						new ReportColumn("userReport.report.rate",
													CurrencyModel.class,
													ReportColumn.COLUMN_VISIBLE,
													ReportColumn.COLUMN_ALLOWDUPLICATES,
													ReportColumn.ColumnType.RATE),
						new ReportColumn("userReport.report.hours",
													FloatModel.class,
													ReportColumn.COLUMN_VISIBLE,
													ReportColumn.COLUMN_ALLOWDUPLICATES,
													ReportColumn.ColumnType.HOUR),
						new ReportColumn("userReport.report.turnover", 
													CurrencyModel.class,
													ReportColumn.COLUMN_VISIBLE,
													ReportColumn.COLUMN_ALLOWDUPLICATES,
													ReportColumn.ColumnType.TURNOVER)),
														
	AGGREGATE_PROJECT (new ReportColumn("userReport.report.project"),
						new ReportColumn("userReport.report.projectCode"),
						new ReportColumn("userReport.report.customer"),
						new ReportColumn("userReport.report.user"),
						new ReportColumn("userReport.report.rate",
													CurrencyModel.class,
													ReportColumn.COLUMN_VISIBLE,
													ReportColumn.COLUMN_ALLOWDUPLICATES,
													ReportColumn.ColumnType.RATE),
						new ReportColumn("userReport.report.hours",
													FloatModel.class,
													ReportColumn.COLUMN_VISIBLE,
													ReportColumn.COLUMN_ALLOWDUPLICATES,
													ReportColumn.ColumnType.HOUR),
						new ReportColumn("userReport.report.turnover", 
													CurrencyModel.class,
													ReportColumn.COLUMN_VISIBLE,
													ReportColumn.COLUMN_ALLOWDUPLICATES,
													ReportColumn.ColumnType.TURNOVER)),
	AGGREGATE_EMPLOYEE (new ReportColumn("userReport.report.user"),
						new ReportColumn("userReport.report.customer"),
						new ReportColumn("userReport.report.project",
													ReportColumn.COLUMN_VISIBLE,
													ReportColumn.COLUMN_ALLOWDUPLICATES,
													true),		
						new ReportColumn("userReport.report.projectCode",
													ReportColumn.COLUMN_VISIBLE,
													ReportColumn.COLUMN_ALLOWDUPLICATES,
													true),		
						new ReportColumn("userReport.report.rate",
													CurrencyModel.class,
													ReportColumn.COLUMN_VISIBLE,
													ReportColumn.COLUMN_ALLOWDUPLICATES,
													ReportColumn.ColumnType.RATE),							
						new ReportColumn("userReport.report.hours",
													FloatModel.class,
													ReportColumn.COLUMN_VISIBLE,
													ReportColumn.COLUMN_ALLOWDUPLICATES,
													ReportColumn.ColumnType.HOUR),
						new ReportColumn("userReport.report.turnover", 
													CurrencyModel.class,
													ReportColumn.COLUMN_VISIBLE,
													ReportColumn.COLUMN_ALLOWDUPLICATES,
													ReportColumn.ColumnType.TURNOVER)),
	AGGREGATE_CUSTOMER_SINGLE_USER (new ReportColumn("userReport.report.customer"),
									new ReportColumn("userReport.report.project"),
									new ReportColumn("userReport.report.projectCode"),
									new ReportColumn("userReport.report.rate", false),
									new ReportColumn("userReport.report.rate", false),
									new ReportColumn("userReport.report.hours",
																FloatModel.class,
																ReportColumn.COLUMN_VISIBLE,
																ReportColumn.COLUMN_ALLOWDUPLICATES,
																ReportColumn.ColumnType.HOUR),
									new TurnoverTreeReportColumn("userReport.report.turnover", 
																CurrencyModel.class)),													
	AUDIT_REPORT (new ReportColumn("audit.report.column.date",
									DateModel.class,
									new Object[]{DateModel.DATESTYLE_FULL_SHORT, ""},
									new Class[]{int.class, String.class},
									ReportColumn.COLUMN_VISIBLE,
									ReportColumn.ColumnType.DATE),
				new ReportColumn("audit.report.column.lastName"),
				new ReportColumn("audit.report.column.action"),
				new ReportColumn("audit.report.column.type"));
												
	private ReportColumn[] 	reportColumns;
	private int					groupByColumn;
	
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
