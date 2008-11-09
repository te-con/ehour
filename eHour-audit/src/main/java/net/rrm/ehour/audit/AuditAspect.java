/**
 * Created on Nov 3, 2008
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

import java.util.Date;

import net.rrm.ehour.audit.service.AuditService;
import net.rrm.ehour.domain.Audit;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.ui.session.EhourWebSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Auditable Aspect
 **/
@Component
@Aspect
public class AuditAspect
{
//	@Resource(name="auditService")
	private AuditService	auditService;
	
	/**
	 * Audit 
	 * @param pjp
	 * @param auditable
	 * @return
	 * @throws Throwable
	 */
	@Around("@annotation(auditable)")
	public Object auditable(ProceedingJoinPoint pjp, Auditable auditable) throws Throwable
	{
		User user = EhourWebSession.getSession().getUser().getUser();
		
		Audit audit = new Audit()
					.setUser(user)
					.setDate(new Date());
		
		auditService.persistAudit(audit);	
		
		System.out.println(pjp.getSignature().toLongString());
		System.out.println(pjp.getSignature().toShortString());
		System.out.println(pjp.getTarget().getClass());
		
		System.out.println("user: " + EhourWebSession.getSession().getUser());
		
		for (Object object : pjp.getArgs())
		{
			System.out.println("o:" + object);
		}
		
		return pjp.proceed();
	}
}
