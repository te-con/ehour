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

package net.rrm.ehour.audittest.service;

import java.util.List;

import net.rrm.ehour.audit.annot.NonAuditable;
import net.rrm.ehour.audit.dao.AuditReportRequest;
import net.rrm.ehour.audit.service.AuditService;
import net.rrm.ehour.domain.Audit;

import org.springframework.stereotype.Component;

@Component(value="auditServiceMock")
public class MockAuditImpl implements AuditService, MockAudit
{
	public int called = 0;
	public Audit audit;
	
	public Audit getAudit()
	{
		return audit;
	}
	
	@NonAuditable
	public void doAudit(Audit audit)
	{
		this.audit = audit;
		called++;
	}
	@NonAuditable
	public List<Audit> getAudit(AuditReportRequest request)
	{
		return null;
	}
	@NonAuditable
	public Number getAuditCount(AuditReportRequest request)
	{
		return null;
	}
	public List<Audit> getAuditAll(AuditReportRequest request)
	{
		return null;
	}
	
	public int getCalled()
	{
		return called;
	}
	
	public void resetCalled()
	{
		called = 0;
		audit = null;
	}
}
