/**
 * Created on Nov 6, 2008
 * Author: Thies
 *
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.audit;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import net.rrm.ehour.audit.service.AuditService;
import net.rrm.ehour.audit.service.MockAudit;
import net.rrm.ehour.audit.service.MockAuditService;
import net.rrm.ehour.audit.service.MockNonTransactService;
import net.rrm.ehour.audit.service.MockService;
import net.rrm.ehour.domain.AuditActionType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext-audit-test.xml"})
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
	public void testAnnotated()
	{
		mockService.annotatedMethod();
		
		assertEquals(1, ((MockAudit)auditService).getCalled());
		assertEquals(AuditActionType.CREATE, ((MockAudit)auditService).getAudit().getAuditActionType());
	}
	
	@Test
	public void testNonAnnotated()
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
	
	// test for 
	@Test
	public void testPackage()
	{
		mockAuditService.getAuditMethod();

		assertEquals(1, ((MockAudit)auditService).getCalled());
		assertEquals(AuditActionType.READ, ((MockAudit)auditService).getAudit().getAuditActionType());
	}

}
