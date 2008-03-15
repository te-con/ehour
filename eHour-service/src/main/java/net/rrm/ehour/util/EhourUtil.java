/**
 * Created on 21-feb-2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.rrm.ehour.domain.DomainObject;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;

/**
 * Ehour util 
 **/

public class EhourUtil
{
	/**
	 * Get a list of primary keys of out a list of domain objects
	 * @param projects
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static<PK extends Serializable> List<PK> getPKsFromDomainObjects(Collection<? extends DomainObject> domainObjects)
	{
		List<PK> pks = new ArrayList<PK>();
		
		for (DomainObject<PK,? extends DomainObject> domainObject : domainObjects)
		{
			pks.add(domainObject.getPK());
		}
		
		return pks;
	}
	
	/**
	 * Check if aggregate list is empty
	 * @param aggregates
	 * @return
	 */
	public static boolean isEmptyAggregateList(Collection<AssignmentAggregateReportElement> aggregates)
	{
		float	hours = 0f;
		
		if (aggregates != null)
		{
			for (AssignmentAggregateReportElement assignmentAggregateReportElement : aggregates)
			{
				if (assignmentAggregateReportElement.getHours() != null)
				{
					hours += assignmentAggregateReportElement.getHours().floatValue();
				}
			}
		}
		
		return hours == 0f;
	}	

}
