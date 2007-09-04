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

import java.util.Comparator;

import net.rrm.ehour.project.domain.ProjectAssignment;

public class ProjectAssignmentComparator implements Comparator<ProjectAssignment>
{
	public final static int	ASSIGNMENT_COMPARE_NAME = 0;
	public final static int	ASSIGNMENT_COMPARE_START = 1;
	public final static int ASSIGNMENT_COMPARE_CUSTDATEPRJ = 2;
	
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
		return o1.getProject().getName().compareToIgnoreCase(o2.getProject().getName());
	}

}
