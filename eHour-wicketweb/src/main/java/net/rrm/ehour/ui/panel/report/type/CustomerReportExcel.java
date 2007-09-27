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

package net.rrm.ehour.ui.panel.report.type;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.panel.report.AbstractAggregateExcelReport;
import net.rrm.ehour.ui.panel.report.AggregateReportColumn;
import net.rrm.ehour.ui.panel.report.ReportColumnUtil;
import net.rrm.ehour.ui.panel.report.ReportType;
import net.rrm.ehour.ui.session.EhourWebSession;

import org.apache.wicket.Session;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

/**
 * TODO 
 **/

@AuthorizeInstantiation("ROLE_REPORT")
public class CustomerReportExcel extends AbstractAggregateExcelReport
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7211392869328367507L;
	private AggregateReportColumn[]	reportColumns;
	
	@Override
	protected IModel getExcelReportName()
	{
		// TODO Auto-generated method stub
		return new ResourceModel("report.user.name");
	}

	@Override
	protected IModel getHeaderReportName()
	{
		// TODO Auto-generated method stub
		return new ResourceModel("report.user.name");
	}

	@Override
	protected AggregateReportColumn[] getReportColumns()
	{
		if (reportColumns == null)
		{
			EhourConfig config = ((EhourWebSession)Session.get()).getEhourConfig();
			
			reportColumns = ReportColumnUtil.getReportColumns(config, ReportType.AGGREGATE_CUSTOMER);
		}
		
		return reportColumns;	}

}
