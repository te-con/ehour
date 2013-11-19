/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.sort;

import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Project Assignment aggregate comparator 
 **/

public class ProjectAssignmentAggregateComparator implements Comparator<AssignmentAggregateReportElement>, Serializable
{
	private static final long serialVersionUID = 8223481348525614968L;
	public static final int SORT_ON_CUSTOMER = 1;
	public static final int SORT_ON_PROJECT = 2;
	
	private int	sortType;

	public ProjectAssignmentAggregateComparator(int sortType)
	{
		this.sortType = sortType;
	}
	
	/**
	 * Compare on project name or customer name
	 */
	public int compare(AssignmentAggregateReportElement o1, AssignmentAggregateReportElement o2)
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
	private int compareOnProject(AssignmentAggregateReportElement o1, AssignmentAggregateReportElement o2)
	{
		return o1.getProjectAssignment().getProject().getFullName().compareToIgnoreCase(
				o2.getProjectAssignment().getProject().getFullName());

	}

}
