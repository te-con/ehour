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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.audit.service.AuditService;
import net.rrm.ehour.audit.service.dto.AuditReportRequest;
import net.rrm.ehour.domain.Audit;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.ui.common.report.AbstractCachableReportModel;
import net.rrm.ehour.ui.common.util.CommonWebUtil;
import net.rrm.ehour.ui.report.TreeReportElement;

import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Audit report
 **/

public class AuditReport extends AbstractCachableReportModel
{
	private static final long serialVersionUID = -5162817322066082796L;

	@SpringBean(name = "auditService")
	private transient AuditService auditService;

	/**
	 * @param criteria
	 */
	public AuditReport(ReportCriteria criteria)
	{
		super(criteria);
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.common.report.AbstractCachableReportModel#getReportData(net.rrm.ehour.report.criteria.ReportCriteria)
	 */
	@Override
	protected ReportData getReportData(ReportCriteria reportCriteria)
	{
		CommonWebUtil.springInjection(this);

		UserCriteria userCriteria = reportCriteria.getUserCriteria();
		
		List<Audit> audit = auditService.getAuditAll((AuditReportRequest) userCriteria);
		
		ReportData reportData = new ReportData(convert(audit), reportCriteria.getReportRange());

		return reportData;
	}
	
	
	private List<TreeReportElement> convert(List<Audit> data)
	{
		List<TreeReportElement> matrix = new ArrayList<TreeReportElement>();
		
		for (Audit audit : data)
		{
			Serializable[] row = new Serializable[4];
			
			row[0] = audit.getDate();
			row[1] = audit.getFullName();
			row[2] = audit.getAction();
			row[3] = audit.getAuditActionType().getValue();
			
			matrix.add(new TreeReportElement(row));
		}
		
		return  matrix;
	}


}
