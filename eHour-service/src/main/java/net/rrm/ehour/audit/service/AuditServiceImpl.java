/**
 * 
 */
package net.rrm.ehour.audit.service;

import net.rrm.ehour.audit.dao.AuditDAO;
import net.rrm.ehour.domain.Audit;

/**
 * @author thies
 *
 */
public class AuditServiceImpl implements AuditService
{
	private AuditDAO	auditDAO;


	/* (non-Javadoc)
	 * @see net.rrm.ehour.audit.service.AuditService#persistAudit(net.rrm.ehour.domain.Audit)
	 */
	public void persistAudit(Audit audit)
	{
		// TODO Auto-generated method stub

	}	
	
	/**
	 * @param auditDAO the auditDAO to set
	 */
	public void setAuditDAO(AuditDAO auditDAO)
	{
		this.auditDAO = auditDAO;
	}
}
