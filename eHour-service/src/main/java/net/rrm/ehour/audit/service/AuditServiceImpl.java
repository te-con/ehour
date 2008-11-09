/**
 * 
 */
package net.rrm.ehour.audit.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void persistAudit(Audit audit)
	{
		auditDAO.persist(audit);
	}	
	
	/**
	 * @param auditDAO the auditDAO to set
	 */
	public void setAuditDAO(AuditDAO auditDAO)
	{
		this.auditDAO = auditDAO;
	}

}
