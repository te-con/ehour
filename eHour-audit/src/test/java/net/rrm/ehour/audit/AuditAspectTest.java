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

import net.rrm.ehour.domain.AuditActionType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext-audit.xml"})
public class AuditAspectTest
{
	@Resource
	private MockService 	mockService;
	@Resource
	private MockAuditImpl	auditService;
	
	@Before
	public void setUp()
	{
		auditService.called = 0;
		auditService.audit = null;
	}
	
	@Test
	public void testAnnotated()
	{
		mockService.annotatedMethod();
		
		assertEquals(1, auditService.called);
		assertEquals(AuditActionType.CREATE, auditService.audit.getAuditActionType());
	}
	
	@Test
	public void testNonAnnotated()
	{
		mockService.getNonAnnotatedMethod();
		
		assertEquals(1, auditService.called);
		assertEquals(AuditActionType.READ, auditService.audit.getAuditActionType());
	}	
	
	@Test
	public void testPersistMethod()
	{
		mockService.persistNonAnnotatedMethod();
		
		assertEquals(1, auditService.called);
		assertEquals(AuditActionType.UPDATE, auditService.audit.getAuditActionType());
	}
	
	@Test
	public void testDeleteMethod()
	{
		mockService.deleteNonAnnotatedMethod();
		
		assertEquals(1, auditService.called);
		assertEquals(AuditActionType.DELETE, auditService.audit.getAuditActionType());
	}
	
	
	@Test
	public void testDeleteReadAnnnotatedMethod()
	{
		mockService.deleteButReadAnnotatedMethod();
		
		assertEquals(1, auditService.called);
		assertEquals(AuditActionType.READ, auditService.audit.getAuditActionType());
	}
	
	@Test
	public void testDeleteButNonAuditable()
	{
		mockService.deleteButNonAuditable();
		
		assertEquals(0, auditService.called);
		assertEquals(null, auditService.audit);
	}		

}
