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

import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.ui.panel.report.AbstractExcelReport;
import net.rrm.ehour.ui.panel.report.ReportConfig;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

/**
 * User report
 **/

@AuthorizeInstantiation("ROLE_CONSULTANT")
public class UserReportExcel extends AbstractExcelReport<AssignmentAggregateReportElement>
{
	private static final long serialVersionUID = 1427524857733863613L;

	public UserReportExcel()
	{
		super(ReportConfig.AGGREGATE_CUSTOMER_SINGLE_USER);
	}	
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.panel.report.AbstractAggregateExcelReport#getExcelReportName()
	 */
	@Override
	protected IModel getExcelReportName()
	{
		return new ResourceModel("report.user.name");
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.panel.report.AbstractAggregateExcelReport#getHeaderReportName()
	 */
	@Override
	protected IModel getHeaderReportName()
	{
		return new ResourceModel("report.user.name");
	}
}
