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

package net.rrm.ehour.audit;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import net.rrm.ehour.audit.service.AuditService;
import net.rrm.ehour.audittest.service.MockAudit;
import net.rrm.ehour.audittest.service.MockAuditService;
import net.rrm.ehour.audittest.service.MockNonTransactService;
import net.rrm.ehour.audittest.service.MockService;
import net.rrm.ehour.domain.AuditActionType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:test-context-audit.xml"})
public class AuditAspectTest
{
	@Resource
	private MockService 	mockService;
	@Resource(name="auditServiceMock")
	private AuditService	auditService;
	@Resource
	private MockNonTransactService mockNonTransactionalService;
	@Resource
	private MockAuditService mockAuditService;
	
	@Before
	public void setUp()
	{
		((MockAudit)auditService).resetCalled();
	}
	
	@Test
	public void shouldAuditAnnotatedMethod()
	{
		mockService.annotatedMethod();
		
		assertEquals(1, ((MockAudit)auditService).getCalled());
		assertEquals(AuditActionType.CREATE, ((MockAudit)auditService).getAudit().getAuditActionType());
	}
	
	@Test
	public void shouldAuditGetMethod()
	{
		mockService.getNonAnnotatedMethod();
		
		assertEquals(1, ((MockAudit)auditService).getCalled());
		assertEquals(AuditActionType.READ, ((MockAudit)auditService).getAudit().getAuditActionType());
	}	
	
	@Test
	public void testPersistMethod()
	{
		mockService.persistNonAnnotatedMethod();
		
		assertEquals(1, ((MockAudit)auditService).getCalled());
		assertEquals(AuditActionType.UPDATE, ((MockAudit)auditService).getAudit().getAuditActionType());
	}
	
	@Test
	public void testDeleteMethod()
	{
		mockService.deleteNonAnnotatedMethod();
		
		assertEquals(1, ((MockAudit)auditService).getCalled());
		assertEquals(AuditActionType.DELETE, ((MockAudit)auditService).getAudit().getAuditActionType());
	}
	
	
	@Test
	public void testDeleteReadAnnnotatedMethod()
	{
		mockService.deleteButReadAnnotatedMethod();
		
		assertEquals(1, ((MockAudit)auditService).getCalled());
		assertEquals(AuditActionType.READ, ((MockAudit)auditService).getAudit().getAuditActionType());
	}
	
	@Test
	public void testDeleteButNonAuditable()
	{
		mockService.deleteButNonAuditable();
		
		assertEquals(0, ((MockAudit)auditService).getCalled());
		assertEquals(null, ((MockAudit)auditService).getAudit());
	}	
	
	@Test
	public void testNonAuditableType()
	{
		mockNonTransactionalService.getMethod();

		assertEquals(0, ((MockAudit)auditService).getCalled());
		assertEquals(null, ((MockAudit)auditService).getAudit());
	}
	
	@Test
	public void testPackage()
	{
		mockAuditService.getAuditMethod();

		assertEquals(1, ((MockAudit)auditService).getCalled());
		assertEquals(AuditActionType.READ, ((MockAudit)auditService).getAudit().getAuditActionType());
	}

}
