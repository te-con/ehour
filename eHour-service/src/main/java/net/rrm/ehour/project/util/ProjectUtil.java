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

package net.rrm.ehour.project.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.rrm.ehour.domain.Project;

/**
 * @author Thies on Aug 24, 2009 10:40:38 PM
 *
 */
public class ProjectUtil
{
	private ProjectUtil()
	{
		
	}
	
	public static List<Project> getBillableProjects(Collection<Project> projects)
	{
		return getProjectsOnBillability(projects, true);
	}

	public static List<Project> getUnbillableProjects(Collection<Project> projects)
	{
		return getProjectsOnBillability(projects, false);
	}

	private static List<Project> getProjectsOnBillability(Collection<Project> projects, boolean billable)
	{
		List<Project> sortedProjects = new ArrayList<Project>();
		
		for (Project project : projects)
		{
			if (project.isBillable() == billable)
			{
				sortedProjects.add(project);
			}
		}
		
		Collections.sort(sortedProjects);
		return sortedProjects;
	}
}
