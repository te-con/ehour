/**
 * 
 */
package net.rrm.ehour.audit.service;

import java.util.List;

import net.rrm.ehour.audit.service.dto.AuditReportRequest;
import net.rrm.ehour.domain.Audit;

/**
 * @author thies
 *
 */
public interface AuditService
{
	/**
	 * Persist audit
	 * @param audit
	 */
	public void persistAudit(Audit audit);

	/**
	 * Get audit report
	 * @param request
	 * @return
	 */
	public List<Audit> getAudit(AuditReportRequest request);
	
	/**
	 * get audit count
	 * @param request
	 * @return
	 */
	public Number getAuditCount(AuditReportRequest request);
}
