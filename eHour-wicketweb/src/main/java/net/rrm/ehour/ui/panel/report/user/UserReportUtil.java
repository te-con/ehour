/**
 * Created on Sep 15, 2007
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

package net.rrm.ehour.ui.panel.report.user;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.model.CurrencyModel;
import net.rrm.ehour.ui.model.FloatModel;
import net.rrm.ehour.ui.panel.report.AggregateReportColumn;
import net.rrm.ehour.ui.session.EhourWebSession;

/**
 * Utility stuff for user report
 **/

public class UserReportUtil
{
	/**
	 * Get the report columns
	 * @param config
	 * @return
	 */
	public static AggregateReportColumn[] getReportColumns(EhourConfig config)
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
}
