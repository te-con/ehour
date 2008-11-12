/**
 * 
 */
package net.rrm.ehour.audit.service;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.rrm.ehour.audit.NonAuditable;
import net.rrm.ehour.audit.dao.AuditDAO;
import net.rrm.ehour.audit.service.dto.AuditReportRequest;
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
	@NonAuditable
	public void persistAudit(Audit audit)
	{
		auditDAO.persist(audit);
	}	
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.audit.service.AuditService#getAudit(net.rrm.ehour.audit.service.dto.AuditReportRequest)
	 */
	public List<Audit> getAudit(AuditReportRequest request)
	{
		return auditDAO.findAudit(request);
	}
	
	/**
	 * @param auditDAO the auditDAO to set
	 */
	public void setAuditDAO(AuditDAO auditDAO)
	{
		this.auditDAO = auditDAO;
	}



}
