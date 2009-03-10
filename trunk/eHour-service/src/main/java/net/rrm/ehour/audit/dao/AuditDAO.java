package net.rrm.ehour.audit.dao;

import java.util.List;

import net.rrm.ehour.audit.service.dto.AuditReportRequest;
import net.rrm.ehour.dao.GenericDAO;
import net.rrm.ehour.domain.Audit;

public interface AuditDAO extends GenericDAO<Audit, Number>
{
	/**
	 * 
	 * @param request
	 * @return
	 */
	public List<Audit> findAudit(AuditReportRequest request);
	
	public List<Audit> findAuditAll(AuditReportRequest request);
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	public Number findAuditCount(AuditReportRequest request);
}
