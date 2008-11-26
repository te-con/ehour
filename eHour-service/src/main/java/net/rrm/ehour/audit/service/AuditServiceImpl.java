/**
 * 
 */
package net.rrm.ehour.audit.service;

import java.util.List;

import net.rrm.ehour.audit.NonAuditable;
import net.rrm.ehour.audit.dao.AuditDAO;
import net.rrm.ehour.audit.service.dto.AuditReportRequest;
import net.rrm.ehour.domain.Audit;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author thies
 *
 */
public class AuditServiceImpl implements AuditService
{
//	private final static Logger LOGGER = Logger.getLogger(AuditServiceImpl.class);
	
	private AuditDAO	auditDAO;

	/* (non-Javadoc)
	 * @see net.rrm.ehour.audit.service.AuditService#persistAudit(net.rrm.ehour.domain.Audit)
	 */
	@Transactional//(propagation=Propagation.REQUIRES_NEW)
	@NonAuditable
	public void persistAudit(Audit audit)
	{
		auditDAO.persist(audit);
	}	
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.audit.service.AuditService#getAudit(net.rrm.ehour.audit.service.dto.AuditReportRequest)
	 */
	@NonAuditable
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

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.audit.service.AuditService#getAuditCount(net.rrm.ehour.audit.service.dto.AuditReportRequest)
	 */
	@NonAuditable
	public Number getAuditCount(AuditReportRequest request)
	{
		return auditDAO.findAuditCount(request);
	}
}
