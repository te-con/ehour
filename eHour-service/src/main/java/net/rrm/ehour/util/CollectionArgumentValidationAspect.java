/**
 * Created on May 31, 2008
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

package net.rrm.ehour.util;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * CollectionArgumentValidationAspect 
 * Checks whether a passed in Collection is not empty
 **/

@Aspect
public class CollectionArgumentValidationAspect
{
	/**
	 * 
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@SuppressWarnings("unchecked") 
	@Around("@annotation(CollectionArgumentValidation)")
	public Object validateCollectionArguments(ProceedingJoinPoint pjp) throws Throwable
	{
		boolean validArguments = true;
		
		for (Object argument : pjp.getArgs())
		{
			if (argument != null && argument instanceof Collection)
			{
				Collection<?> coll = (Collection<?>)argument;
				
				if (CollectionUtils.isEmpty(coll))
				{
					validArguments = false;
					
					break;
				}
			}
		}
		
		if (validArguments)
		{
			return pjp.proceed();
		}
		else
		{
			return new ArrayList();
		}
	}
}
