/**
 * 
 */
package net.rrm.ehour.audit.service;

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
}
