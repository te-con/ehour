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

package net.rrm.ehour.report.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.domain.DomainObject;

/**
 * Report util 
 **/

public class ReportUtil
{
	/**
	 * Convert list of projects to id
	 * TODO: this broken (oh?)
	 * @param projects
	 * @return
	 */
	public static<PK extends Serializable> List<PK> getPKsFromDomainObjects(List<? extends DomainObject> domainObjects)
	{
		List<PK> pks = new ArrayList<PK>();
		
		for (DomainObject<PK,? extends DomainObject> domainObject : domainObjects)
		{
			pks.add(domainObject.getPK());
		}
		
		return pks;
	}

}
