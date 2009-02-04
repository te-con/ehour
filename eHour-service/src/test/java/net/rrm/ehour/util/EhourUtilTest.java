/**
 * Created on Feb 21, 2007
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

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;

import org.junit.Test;

/**
 * TODO 
 **/

public class EhourUtilTest extends TestCase
{

	protected void setUp() throws Exception
	{
		super.setUp();
	}

	public void testGetPKsFromDomainObjects()
	{
		List<Project>	projectIds = new ArrayList<Project>();
		
		projectIds.add(new Project(1));
		projectIds.add(new Project(2));
		projectIds.add(new Project(3));
		projectIds.add(new Project(4));
		
		List<Integer> ints = EhourUtil.getIdsFromDomainObjects(projectIds);
		
		assertEquals(4, ints.size());
		
		Integer[] ints2 = ints.toArray(new Integer[]{});
	}
	
	@Test
	public void testIsEmptyAggregateList()
	{
		List<AssignmentAggregateReportElement> aggs = new ArrayList<AssignmentAggregateReportElement>();
		
		AssignmentAggregateReportElement agg = new AssignmentAggregateReportElement();
		agg.setHours(0);
		aggs.add(agg);
		
		agg = new AssignmentAggregateReportElement();
		aggs.add(agg);
		
		assertTrue(EhourUtil.isEmptyAggregateList(aggs));
		
	}	

}
