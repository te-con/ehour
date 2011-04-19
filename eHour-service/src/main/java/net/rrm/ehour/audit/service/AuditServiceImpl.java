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

package net.rrm.ehour.audit.service;

import java.util.List;

import net.rrm.ehour.audit.annot.NonAuditable;
import net.rrm.ehour.data.AuditReportRequest;
import net.rrm.ehour.domain.Audit;
import net.rrm.ehour.persistence.audit.dao.AuditDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author thies
 *
 */
@Service("auditService")
public class AuditServiceImpl implements AuditService
{
	private AuditDao	auditDAO;
	
	@Autowired
	public AuditServiceImpl(AuditDao auditDao)
	{
		this.auditDAO = auditDao;
	}
	
	/* (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.audit.service.AuditService#persistAudit(net.rrm.ehour.persistence.persistence.domain.Audit)
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	@NonAuditable
	public void doAudit(final Audit audit)
	{
		auditDAO.persist(audit);
	}	
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.audit.service.AuditService#getAudit(net.rrm.ehour.persistence.persistence.audit.service.dto.AuditReportRequest)
	 */
	@NonAuditable
	public List<Audit> getAudit(AuditReportRequest request)
	{
		return auditDAO.findAudit(request);
	}
	
	/**
	 * 
	 */
	@NonAuditable
	public List<Audit> getAuditAll(AuditReportRequest request)
	{
		return auditDAO.findAllAudits(request);
	}	

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.audit.service.AuditService#getAuditCount(net.rrm.ehour.persistence.persistence.audit.service.dto.AuditReportRequest)
	 */
	@NonAuditable
	public Number getAuditCount(AuditReportRequest request)
	{
		Number number = auditDAO.count(request);
		
		return (number == null) ? 0 : number;
	}
}
