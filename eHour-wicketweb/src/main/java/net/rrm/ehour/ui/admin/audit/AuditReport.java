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

package net.rrm.ehour.ui.admin.audit;

import net.rrm.ehour.audit.service.AuditService;
import net.rrm.ehour.data.AuditReportRequest;
import net.rrm.ehour.domain.Audit;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserSelectedCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.ui.common.report.AbstractReportModel;
import net.rrm.ehour.ui.common.util.WebUtils;
import net.rrm.ehour.ui.report.model.TreeReportElement;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Audit report
 **/

public class AuditReport extends AbstractReportModel
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
	 * @see net.rrm.ehour.persistence.persistence.ui.common.report.AbstractReportModel#getReportData(net.rrm.ehour.persistence.persistence.report.criteria.ReportCriteria)
	 */
	@Override
	protected ReportData getReportData(ReportCriteria reportCriteria)
	{
		WebUtils.springInjection(this);

		UserSelectedCriteria userSelectedCriteria = reportCriteria.getUserSelectedCriteria();
		
		List<Audit> audit = auditService.findAudits((AuditReportRequest) userSelectedCriteria);
		
		return new ReportData(convert(audit), reportCriteria.getReportRange(), userSelectedCriteria);
	}
	
	
	private List<TreeReportElement> convert(List<Audit> data)
	{
		List<TreeReportElement> matrix = new ArrayList<>();
		
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
