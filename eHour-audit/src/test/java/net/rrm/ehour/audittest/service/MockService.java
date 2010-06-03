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

import net.rrm.ehour.domain.AuditActionType;
import net.rrm.ehour.service.audit.annot.Auditable;
import net.rrm.ehour.service.audit.annot.NonAuditable;

import org.springframework.stereotype.Service;


@Service("mockService")
public class MockService
{
	@Auditable(actionType=AuditActionType.CREATE)
	public void annotatedMethod()
	{
		
	}
	public void getNonAnnotatedMethod()
	{
		
	}
	
	public void persistNonAnnotatedMethod()
	{
		
	}	

	public void deleteNonAnnotatedMethod()
	{
		
	}	

	@Auditable(actionType=AuditActionType.READ)
	public void deleteButReadAnnotatedMethod()
	{
		
	}	

	@NonAuditable
	public void deleteButNonAuditable()
	{
		
	}	

}
