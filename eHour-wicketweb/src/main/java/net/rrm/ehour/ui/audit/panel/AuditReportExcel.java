/**
 * Created on Dec 10, 2008
 * Author: Thies
 *
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

package net.rrm.ehour.ui.audit.panel;

import net.rrm.ehour.ui.common.report.AbstractExcelReport;
import net.rrm.ehour.ui.common.report.ReportConfig;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

/**
 * Excel export of audit report
 **/

public class AuditReportExcel extends AbstractExcelReport
{
	private static final long serialVersionUID = 1597838144702980437L;
	
	public AuditReportExcel()
	{
		super(ReportConfig.AUDIT_REPORT);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.common.component.AbstractExcelResource#getFilename()
	 */
	@Override
	protected String getFilename()
	{
		return "audit_report.xls";
	}


	@Override
	protected IModel getExcelReportName()
	{
		return new ResourceModel("audit.report.title");
	}


	@Override
	protected IModel getHeaderReportName()
	{
		return new ResourceModel("audit.report.title");
	}

	public static String getId()
	{
		return "auditReportExcel";
	}
}
