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

import net.rrm.ehour.domain.ProjectAssignment;

import java.io.Serializable;
import java.util.Comparator;

public class ProjectAssignmentComparator implements Comparator<ProjectAssignment>, Serializable
{
	private static final long serialVersionUID = -6773438344877864288L;
	public static final int	ASSIGNMENT_COMPARE_NAME = 0;
	public static final int	ASSIGNMENT_COMPARE_START = 1;
	public static final int ASSIGNMENT_COMPARE_CUSTDATEPRJ = 2;
	
	private int	compareType;
	
	public ProjectAssignmentComparator()
	{
		compareType = ASSIGNMENT_COMPARE_NAME;
	}
	
	public ProjectAssignmentComparator(int compareType)
	{
		this.compareType = compareType;
	}
	
	public int compare(ProjectAssignment o1, ProjectAssignment o2)
	{
		int	cmp;
		
		switch (compareType)
		{
			case ASSIGNMENT_COMPARE_NAME:
				cmp = compareNames(o1, o2);
				break;
			case ASSIGNMENT_COMPARE_START:
				cmp = compareDateStart(o1, o2);
				break;
			case ASSIGNMENT_COMPARE_CUSTDATEPRJ:
				cmp = compareCustomerStartDateProject(o1, o2);
				break;
			default:
				cmp = 0;
				break;
		}
		
		return cmp;
	}
	
	/**
	 * Compare first on customer name, if the same, use the project name
	 * @param o1
	 * @param o2
	 * @return
	 */
	private int compareCustomerStartDateProject(ProjectAssignment o1, ProjectAssignment o2)
	{
		int	cmp;
		
		cmp = o1.getProject().getCustomer().getName().compareToIgnoreCase(o2.getProject().getCustomer().getName());
		
		if (cmp == 0)
		{
			cmp = compareDateStart(o1, o2);
			
			if (cmp == 0)
			{
				cmp = compareNames(o1, o2);
			}
		}
		
		return cmp;
	}
	/**
	 * Compare on start date (or name if start date is the same)
	 * @param o1
	 * @param o2
	 * @return
	 */

	private int compareDateStart(ProjectAssignment o1, ProjectAssignment o2)
	{
		int cmp;
		
		// default projects don't have a start date, they go first
		if (o1.getDateStart() == null && o2.getDateStart() != null)
		{
			cmp = -1;
		}
		// default projects don't have a start date, they go first
		else if (o1.getDateStart() != null && o2.getDateStart() == null)
		{
			cmp = 1;
		}
		// default projects don't have a start date, they go first
		else if (o1.getDateStart() == null && o2.getDateStart() == null)
		{
			cmp = 0;
		}
		// same start date, sort on name
		else if (o1.getDateStart().equals(o2.getDateStart()))
		{
			cmp = compareNames(o1, o2);
		}
		else
		{
			cmp = o1.getDateStart().before(o2.getDateStart()) ? -1 : 1;
		}

		return cmp;
	}
	/**
	 * Compare on name
	 * @param o1
	 * @param o2
	 * @return
	 */
	private int compareNames(ProjectAssignment o1, ProjectAssignment o2)
	{
		if (o1 != null && o2 != null
				&& o1.getProject() != null && o2.getProject() != null
				&& o1.getProject().getName() != null)
		{
			return o1.getProject().getFullName().compareToIgnoreCase(o2.getProject().getFullName());
		}
		else
		{
			return o1 == null || o1.getProject() == null ? -1 : 1;
		}
	}

}
