/**
 * Created on Mar 19, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
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
