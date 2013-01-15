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

import net.rrm.ehour.data.AuditReportRequest;
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
	void doAudit(Audit audit);

	/**
	 * Get audit report
	 * @param request
	 * @return
	 */
	List<Audit> findAudits(AuditReportRequest request, Integer offset, Integer max);
	
	/**
	 * Get audit report ignoring any offset or max
	 * @param request
	 * @return
	 */
	List<Audit> findAudits(AuditReportRequest request);
	
	/**
	 * get audit count
	 * @param request
	 * @return
	 */
	Number getAuditCount(AuditReportRequest request);
}
