/**
 * Created on Mar 19, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
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

package net.rrm.ehour.ui.sort;

import java.io.Serializable;
import java.util.Comparator;

import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;

/**
 * Project Assignment aggregate comparator 
 **/

public class ProjectAssignmentAggregateComparator implements Comparator<ProjectAssignmentAggregate>, Serializable
{
	public final static int SORT_ON_CUSTOMER = 1;
	public final static int SORT_ON_PROJECT = 2;
	
	private int	sortType;
	
	public ProjectAssignmentAggregateComparator()
	{
		sortType = SORT_ON_CUSTOMER;
	}
	
	/**
	 * 
	 * @param sortType
	 */
	public ProjectAssignmentAggregateComparator(int sortType)
	{
		this.sortType = sortType;
	}
	
	/**
	 * Compare on project name or customer name
	 */
	public int compare(ProjectAssignmentAggregate o1, ProjectAssignmentAggregate o2)
	{
		int	compare = 0;
		
		switch (sortType)
		{
			case SORT_ON_PROJECT:
				compare = compareOnProject(o1, o2);
				break;
			case SORT_ON_CUSTOMER:
				compare = o1.getProjectAssignment().getProject().getCustomer().getName()
						.compareToIgnoreCase(o2.getProjectAssignment().getProject().getCustomer().getName());
				
				if (compare == 0)
				{
					compare = compareOnProject(o1, o2);
				}
				break;
		}
		
		return compare;
	}
	
	/**
	 * Compare on prj
	 * @param o1
	 * @param o2
	 * @return
	 */
	private int compareOnProject(ProjectAssignmentAggregate o1, ProjectAssignmentAggregate o2)
	{
		return o1.getProjectAssignment().getProject().getName().compareToIgnoreCase(
				o2.getProjectAssignment().getProject().getName());

	}

}
