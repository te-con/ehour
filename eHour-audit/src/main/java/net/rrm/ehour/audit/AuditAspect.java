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

import java.lang.annotation.Annotation;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import net.rrm.ehour.audit.service.AuditService;
import net.rrm.ehour.domain.Audit;
import net.rrm.ehour.domain.AuditActionType;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.ui.session.EhourWebSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Auditable Aspect
 **/
@Component
@Aspect
public class AuditAspect
{
	@Resource //@Resource not injecting??
	private AuditService	auditService;

	@Pointcut("execution(public * net.rrm.ehour.*.service.*Service*.get*(..)) && " +
			"!@annotation(Auditable) && " +
			"!@annotation(NonAuditable)")
	public void publicGetMethod()
	{
	}
	
	@Pointcut("execution(public * net.rrm.ehour.*.service.*Service.persist*(..)) && " +
			"!@annotation(Auditable) && " +
			"!@annotation(NonAuditable)")
	public void publicPersistMethod()
	{
	}
	
	@Pointcut("execution(public * net.rrm.ehour.*.service.*Service.delete*(..)) && " +
			"!@annotation(Auditable) && " +
			"!@annotation(NonAuditable)")
	public void publicDeleteMethod()
	{
	}	

	/**
	 * Around advise for read-only get methods
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Around("net.rrm.ehour.audit.AuditAspect.publicGetMethod()")
	public Object auditOnGet(ProceedingJoinPoint pjp) throws Throwable
	{
		return doAudit(pjp, AuditActionType.READ);
	}
	
	/**
	 * Around advise for persist methods
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Around("net.rrm.ehour.audit.AuditAspect.publicPersistMethod()")
	public Object auditOnPersist(ProceedingJoinPoint pjp) throws Throwable
	{
		return doAudit(pjp, AuditActionType.UPDATE);
	}	

	/**
	 * Around advise for delete methods
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Around("net.rrm.ehour.audit.AuditAspect.publicDeleteMethod()")
	public Object auditOnDelete(ProceedingJoinPoint pjp) throws Throwable
	{
		return doAudit(pjp, AuditActionType.DELETE);
	}	

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
		return doAudit(pjp, auditable.actionType());
	}
	
	/**
	 * 
	 * @param pjp
	 * @param auditActionType
	 * @return
	 * @throws Throwable
	 */
	private Object doAudit(ProceedingJoinPoint pjp, AuditActionType auditActionType) throws Throwable
	{
		Object returnObject;

		boolean isAuditable = isAuditable(pjp);
		
		User user = getUser();
		
		try
		{
			returnObject = pjp.proceed();
		}
		catch (Throwable t)
		{
			if (isAuditable)
			{
				auditService.persistAudit(createAudit(user, Boolean.FALSE, auditActionType, pjp));
			}
			
			throw t;
		}
		
		if (isAuditable)
		{
			auditService.persistAudit(createAudit(user, Boolean.TRUE, auditActionType, pjp));
		}
		
		return returnObject;		
	}
	
	/**
	 * Check if type is annotated with NonAuditable annotation
	 * @param pjp
	 * @return
	 */
	private boolean isAuditable(ProceedingJoinPoint pjp)
	{
		Annotation[] annotations = pjp.getSignature().getDeclaringType().getAnnotations();
		
		boolean nonAuditable = false;
		
		for (Annotation annotation : annotations)
		{
			nonAuditable = annotation.annotationType() == NonAuditable.class;
			
			if (nonAuditable)
			{
				break;
			}
		}
		
		return !nonAuditable;
	}
	
	/**
	 * 
	 * @return
	 */
	private User getUser()
	{
		User user;
		
		try
		{
			user = EhourWebSession.getSession().getUser().getUser();
		}
		catch (Throwable t)
		{
			user = null;
		}
		
		return user;
	}

	/**
	 * 
	 * @param user
	 * @param success
	 * @param action
	 * @param pjp
	 * @return
	 */
	private Audit createAudit(User user, Boolean success, AuditActionType auditActionType, ProceedingJoinPoint pjp)
	{
		StringBuilder parameters = new StringBuilder();
		
		int i = 0;
		
		for (Object object : pjp.getArgs())
		{
			parameters.append(i++ + ":");
			
			if (object instanceof Calendar)
			{
				parameters.append(((Calendar)object).getTime().toString());
			}
			else
			{
				parameters.append(object.toString());
			}
		}
		
		Audit audit = new Audit()
				.setUser(user)
				.setUserName(user != null ? user.getFullName() : null)
				.setDate(new Date())
				.setSuccess(success)
				.setAction(pjp.getSignature().toShortString())
				.setAuditActionType(auditActionType)
				.setParameters(parameters.toString())
				;

		return audit;
	}

	
	/**
	 * @param auditService the auditService to set
	 */
	@Autowired
	public void setAuditService(AuditService auditService)
	{
		this.auditService = auditService;
	}
}
