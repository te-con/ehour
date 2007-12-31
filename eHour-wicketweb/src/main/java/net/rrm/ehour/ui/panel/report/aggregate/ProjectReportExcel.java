/**
 * Created on Sep 28, 2007
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

package net.rrm.ehour.ui.panel.report.aggregate;

import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.panel.report.AbstractAggregateExcelReport;
import net.rrm.ehour.ui.panel.report.AggregateReportColumn;
import net.rrm.ehour.ui.panel.report.ReportColumnUtil;
import net.rrm.ehour.ui.panel.report.ReportType;
import net.rrm.ehour.ui.session.EhourWebSession;

/**
 * TODO 
 **/

public class ProjectReportExcel extends AbstractAggregateExcelReport
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AggregateReportColumn[]	reportColumns;
	
	@Override
	protected IModel getExcelReportName()
	{
		return new ResourceModel("report.title.project");
	}

	@Override
	protected IModel getHeaderReportName()
	{
		return new ResourceModel("report.title.project");
	}

	@Override
	protected AggregateReportColumn[] getReportColumns()
	{
		if (reportColumns == null)
		{
			EhourConfig config = ((EhourWebSession)Session.get()).getEhourConfig();
			
			reportColumns = ReportColumnUtil.getReportColumns(config, ReportType.AGGREGATE_PROJECT);
		}
		
		return reportColumns;
	}

}
