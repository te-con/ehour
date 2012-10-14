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

package net.rrm.ehour.ui.timesheet.export.excel.part;

/**
 * Created on Apr 10, 2009, 1:54:16 PM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public enum ExportReportColumn
{
    DATE(0),
    CUSTOMER_CODE(1),
    PROJECT_CODE(2),
    PROJECT(3),
    EMPTY(4, 5),
    HOURS(6);

	private int[] columns;
	
	ExportReportColumn(int... columns)
	{
		this.columns= columns;
	}

	public int[] getColumns()
	{
		return columns;
	}

	public int getColumn()
	{
		return columns[0];
	}
}
