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

package net.rrm.ehour.ui.timesheet.export;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ProjectAssignment;

/**
 * Created on Feb 2, 2009, 7:00:14 PM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public class ExportParameters implements Serializable
{
	private static final long serialVersionUID = -1989161908045872279L;

	public enum Action
	{
		PRINT,
		EXCEL;
	}
	
	private Collection<ProjectAssignment> assignments;
	private DateRange exportRange;
	private Action action;
	private Boolean includeSignoff;
	
	public ExportParameters(DateRange exportRange)
	{
		this.exportRange = exportRange;
		
		assignments = new ArrayList<ProjectAssignment>();
	}
	
	/**
	 * @param exportRange the exportRange to set
	 */
	public void setExportRange(DateRange exportRange)
	{
		this.exportRange = exportRange;
	}
	/**
	 * @return the exportRange
	 */
	public DateRange getExportRange()
	{
		return exportRange;
	}


	/**
	 * @param action the action to set
	 */
	public void setAction(Action action)
	{
		this.action = action;
	}

	/**
	 * @return the action
	 */
	public Action getAction()
	{
		return action;
	}
	
	/**
	 * @return the includeSignoff
	 */
	public Boolean getIncludeSignoff()
	{
		return includeSignoff;
	}

	/**
	 * @param includeSignoff the includeSignoff to set
	 */
	public void setIncludeSignoff(Boolean includeSignoff)
	{
		this.includeSignoff = includeSignoff;
	}

	public ExportParameters()
	{
		
	}

	/**
	 * @param assignments the assignments to set
	 */
	public void setAssignments(Collection<ProjectAssignment> assignments)
	{
		this.assignments = assignments;
	}

	/**
	 * @return the assignments
	 */
	public Collection<ProjectAssignment> getAssignments()
	{
		return assignments;
	}
}
