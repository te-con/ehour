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

import net.rrm.ehour.ui.common.model.CurrencyModel;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.model.FloatModel;
import net.rrm.ehour.ui.panel.report.user.TurnoverTreeReportColumn;

import org.apache.wicket.model.Model;

/**
 * All report types 
 **/

public enum ReportConfig
{
	// constructors like these might be a bit over the top..
	// take note, the columnResourceKey is used for serie creation with trend charts (pardon my English, it's late.. or early in the morning)
	DETAILED_REPORT (new TreeReportColumn[]{
						new TreeReportColumn("userReport.report.customer",
													TreeReportColumn.COLUMN_VISIBLE,
													TreeReportColumn.COLUMN_NODUPLICATES,
													true),
						new TreeReportColumn("userReport.report.project",
													TreeReportColumn.COLUMN_VISIBLE,
													TreeReportColumn.COLUMN_NODUPLICATES,
													true),
						new TreeReportColumn("userReport.report.date",
													DateModel.class,
													new Object[]{DateModel.DATESTYLE_FULL_SHORT, ""},
													new Class[]{int.class, String.class},
													TreeReportColumn.COLUMN_VISIBLE,
													TreeReportColumn.ColumnType.DATE),
						new TreeReportColumn("userReport.report.user",
													TreeReportColumn.COLUMN_VISIBLE,
													TreeReportColumn.COLUMN_ALLOWDUPLICATES,
													true),
						new TreeReportColumn("userReport.report.comment",
													Model.class,
													TreeReportColumn.COLUMN_VISIBLE,
													TreeReportColumn.COLUMN_ALLOWDUPLICATES,
													TreeReportColumn.ColumnType.COMMENT),
						new TreeReportColumn("userReport.report.hours",
													FloatModel.class,
													TreeReportColumn.COLUMN_VISIBLE,
													TreeReportColumn.COLUMN_ALLOWDUPLICATES,
													TreeReportColumn.ColumnType.HOUR),
						new TreeReportColumn("userReport.report.turnover", 
													CurrencyModel.class,
													TreeReportColumn.COLUMN_VISIBLE,
													TreeReportColumn.COLUMN_ALLOWDUPLICATES,
													TreeReportColumn.ColumnType.TURNOVER)},
					  2),
	
	AGGREGATE_CUSTOMER (new TreeReportColumn[]{
							new TreeReportColumn("userReport.report.customer"),
							new TreeReportColumn("userReport.report.project"),
							new TreeReportColumn("userReport.report.projectCode"),
							new TreeReportColumn("userReport.report.user"),
							new TreeReportColumn("userReport.report.rate",
														CurrencyModel.class,
														TreeReportColumn.COLUMN_VISIBLE,
														TreeReportColumn.COLUMN_ALLOWDUPLICATES,
														TreeReportColumn.ColumnType.RATE),
							new TreeReportColumn("userReport.report.hours",
														FloatModel.class,
														TreeReportColumn.COLUMN_VISIBLE,
														TreeReportColumn.COLUMN_ALLOWDUPLICATES,
														TreeReportColumn.ColumnType.HOUR),
							new TreeReportColumn("userReport.report.turnover", 
														CurrencyModel.class,
														TreeReportColumn.COLUMN_VISIBLE,
														TreeReportColumn.COLUMN_ALLOWDUPLICATES,
														TreeReportColumn.ColumnType.TURNOVER)}),
														
	AGGREGATE_PROJECT (new TreeReportColumn[]{
							new TreeReportColumn("userReport.report.project"),
							new TreeReportColumn("userReport.report.projectCode"),
							new TreeReportColumn("userReport.report.customer"),
							new TreeReportColumn("userReport.report.user"),
							new TreeReportColumn("userReport.report.rate",
														CurrencyModel.class,
														TreeReportColumn.COLUMN_VISIBLE,
														TreeReportColumn.COLUMN_ALLOWDUPLICATES,
														TreeReportColumn.ColumnType.RATE),
							new TreeReportColumn("userReport.report.hours",
														FloatModel.class,
														TreeReportColumn.COLUMN_VISIBLE,
														TreeReportColumn.COLUMN_ALLOWDUPLICATES,
														TreeReportColumn.ColumnType.HOUR),
							new TreeReportColumn("userReport.report.turnover", 
														CurrencyModel.class,
														TreeReportColumn.COLUMN_VISIBLE,
														TreeReportColumn.COLUMN_ALLOWDUPLICATES,
														TreeReportColumn.ColumnType.TURNOVER)}),
	AGGREGATE_EMPLOYEE (new TreeReportColumn[]{
							new TreeReportColumn("userReport.report.user"),
							new TreeReportColumn("userReport.report.customer"),
							new TreeReportColumn("userReport.report.project",
														TreeReportColumn.COLUMN_VISIBLE,
														TreeReportColumn.COLUMN_ALLOWDUPLICATES,
														true),		
							new TreeReportColumn("userReport.report.projectCode",
														TreeReportColumn.COLUMN_VISIBLE,
														TreeReportColumn.COLUMN_ALLOWDUPLICATES,
														true),		
							new TreeReportColumn("userReport.report.rate",
														CurrencyModel.class,
														TreeReportColumn.COLUMN_VISIBLE,
														TreeReportColumn.COLUMN_ALLOWDUPLICATES,
														TreeReportColumn.ColumnType.RATE),							
							new TreeReportColumn("userReport.report.hours",
														FloatModel.class,
														TreeReportColumn.COLUMN_VISIBLE,
														TreeReportColumn.COLUMN_ALLOWDUPLICATES,
														TreeReportColumn.ColumnType.HOUR),
							new TreeReportColumn("userReport.report.turnover", 
														CurrencyModel.class,
														TreeReportColumn.COLUMN_VISIBLE,
														TreeReportColumn.COLUMN_ALLOWDUPLICATES,
														TreeReportColumn.ColumnType.TURNOVER)}),
	AGGREGATE_CUSTOMER_SINGLE_USER (new TreeReportColumn[]{
							new TreeReportColumn("userReport.report.customer"),
							new TreeReportColumn("userReport.report.project"),
							new TreeReportColumn("userReport.report.projectCode"),
							new TreeReportColumn("userReport.report.rate", false),
							new TreeReportColumn("userReport.report.rate", false),
							new TreeReportColumn("userReport.report.hours",
														FloatModel.class,
														TreeReportColumn.COLUMN_VISIBLE,
														TreeReportColumn.COLUMN_ALLOWDUPLICATES,
														TreeReportColumn.ColumnType.HOUR),
							new TurnoverTreeReportColumn("userReport.report.turnover", 
														CurrencyModel.class)});
												
	private TreeReportColumn[] 	reportColumns;
	private int					groupByColumn;
	
	/**
	 * 
	 * @param reportColumns
	 */
	
	private ReportConfig(TreeReportColumn[] reportColumns)
	{
		this(reportColumns, -1);
	}
	
	/**
	 * 
	 * @param reportColumns
	 * @param groupByColumn
	 */
	private ReportConfig(TreeReportColumn[] reportColumns, int groupByColumn)
	{
		this.reportColumns = reportColumns;
		this.groupByColumn = groupByColumn;
	}


	/**
	 * @return the reportColumns
	 */
	public TreeReportColumn[] getReportColumns()
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
