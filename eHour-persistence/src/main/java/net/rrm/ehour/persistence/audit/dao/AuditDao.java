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

package net.rrm.ehour.persistence.audit.dao;

import java.util.List;

import net.rrm.ehour.data.AuditReportRequest;
import net.rrm.ehour.domain.Audit;
import net.rrm.ehour.persistence.dao.GenericDao;

public interface AuditDao extends GenericDao<Audit, Number>
{
	/**
	 * Find audit for request
	 * @param request
	 * @return
	 */
	public List<Audit> findAudit(AuditReportRequest request);

	/**
	 * Find all audits for a request
	 * @param request
	 * @return
	 */
	public List<Audit> findAllAudits(AuditReportRequest request);
	
	/**
	 * Count audits for request
	 * @param request
	 * @return
	 */
	public Number count(AuditReportRequest request);
}
