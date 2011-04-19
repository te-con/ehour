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

package net.rrm.ehour.ui.timesheet.dto;

import org.apache.wicket.model.Model;

public class ProjectTotalModel extends Model<Float>
{
	private static final long serialVersionUID = -4141505357937939279L;
	private	TimesheetRow	row;
	
	public ProjectTotalModel(TimesheetRow row)
	{
		this.row = row;
	}
	
	@Override
	public Float getObject()
	{
		float	totalHours = 0;
		
		for (int i = 0; 
				i < row.getTimesheetCells().length;
				i++)
		{
			if (row.getTimesheetCells()[i] != null 
					&& row.getTimesheetCells()[i].getTimesheetEntry() != null 
					&& row.getTimesheetCells()[i].getTimesheetEntry().getHours() != null)
			{
				totalHours += row.getTimesheetCells()[i].getTimesheetEntry().getHours().floatValue();
			}			
		}
		
		return Float.valueOf(totalHours);
	}
}
