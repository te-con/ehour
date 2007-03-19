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

package net.rrm.ehour.web.sort;

import java.util.Comparator;

import net.rrm.ehour.project.domain.ProjectAssignment;

public class ProjectAssignmentComparator implements Comparator<ProjectAssignment>
{
	public final static int	ASSIGNMENT_COMPARE_NAME = 0;
	public final static int	ASSIGNMENT_COMPARE_START = 1;
	
	private int	compareType;
	
	public ProjectAssignmentComparator()
	{
		compareType = ASSIGNMENT_COMPARE_NAME;
	}
	
	public ProjectAssignmentComparator(int compareType)
	{
		this.compareType = compareType;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
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
			default:
				cmp = 0;
				break;
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
		return o1.getProject().getName().compareToIgnoreCase(o2.getProject().getName());
	}

}
