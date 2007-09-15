/**
 * Created on Jul 10, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
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
import net.rrm.ehour.ui.panel.report.AbstractAggregateReportPanel;
import net.rrm.ehour.ui.panel.report.AggregateReportColumn;
import net.rrm.ehour.ui.report.aggregate.CustomerAggregateReport;
import net.rrm.ehour.ui.session.EhourWebSession;

/**
 * Report table
 **/

public class UserReportPanel extends AbstractAggregateReportPanel
{
	private static final long serialVersionUID = -2740688272163704885L;

	private AggregateReportColumn[]	reportColumns;
	
	/**
	 * 
	 * @param id
	 * @param reportData
	 */
	public UserReportPanel(String id, CustomerAggregateReport reportData)
	{
		super(id, reportData);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.panel.report.user.AbstractAggregateReportPanel#getReportColumns()
	 */
	@Override
	protected AggregateReportColumn[] getReportColumns()
	{
		if (reportColumns == null)
		{
			EhourConfig config = ((EhourWebSession)this.getSession()).getEhourConfig();
			
			reportColumns = UserReportUtil.getReportColumns(config);
		}
		
		return reportColumns;
	}
}
