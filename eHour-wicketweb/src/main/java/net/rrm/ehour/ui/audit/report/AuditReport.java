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

package net.rrm.ehour.ui.audit.report;

import java.util.List;

import net.rrm.ehour.audit.service.AuditService;
import net.rrm.ehour.audit.service.dto.AuditReportRequest;
import net.rrm.ehour.domain.Audit;
import net.rrm.ehour.ui.report.Report;

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Audit report
 **/

public class AuditReport extends Report
{
	private static final long serialVersionUID = -5162817322066082796L;

	@SpringBean
	private transient AuditService 	auditService;
	private transient List<Audit>	reportData;
	private AuditReportRequest auditReportRequest;

	/**
	 * 
	 * @param auditReportRequest
	 */
	public AuditReport(AuditReportRequest auditReportRequest)
	{
		this.auditReportRequest = auditReportRequest;
	}

	/**
	 * @return the reportData
	 */
	public List<Audit> getReportData()
	{
		if (reportData == null)
		{
			InjectorHolder.getInjector().inject(this);
			
			reportData = auditService.getAudit(auditReportRequest);
		}

		return reportData;
	}

	/**
	 * @return the auditReportRequest
	 */
	public AuditReportRequest getAuditReportRequest()
	{
		return auditReportRequest;
	}
	
	
	
}
