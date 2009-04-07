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
