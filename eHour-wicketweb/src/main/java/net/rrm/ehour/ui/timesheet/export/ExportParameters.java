/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * eHour is sponsored by TE-CON  - http://www.te-con.nl/
 */

package net.rrm.ehour.ui.timesheet.export;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import net.rrm.ehour.data.DateRange;

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
	
	private List<Serializable> assignmentIds;
	private DateRange exportRange;
	private Action action;
	private Boolean includeSignoff;
	
	public ExportParameters(DateRange exportRange)
	{
		this(null, exportRange,null);
	}
	
	public ExportParameters(List<Serializable> assignmentIds, DateRange exportRange, Action action)
	{
		this.exportRange = exportRange;
		this.assignmentIds = assignmentIds;
		this.setAction(action);
		
		exportRange = new DateRange(new Date(), new Date());
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
	 * @param assignmentIds the assignmentIds to set
	 */
	public void setAssignmentIds(List<Serializable> assignmentIds)
	{
		this.assignmentIds = assignmentIds;
	}
	/**
	 * @return the assignmentIds
	 */
	public List<Serializable> getAssignmentIds()
	{
		return assignmentIds;
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
}
