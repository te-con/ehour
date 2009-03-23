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

package net.rrm.ehour.ui.timesheet.export.excel;

import net.rrm.ehour.ui.common.component.AbstractExcelResource;

/**
 * Created on Mar 23, 2009, 1:30:04 PM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public class ExportReportExcel extends AbstractExcelResource
{
	private static final long serialVersionUID = -4841781257347819473L;

	public static String getId()
	{
		return "exportReportExcel";
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.common.component.AbstractExcelResource#getExcelData(java.lang.String)
	 */
	@Override
	public byte[] getExcelData(String reportId) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.common.component.AbstractExcelResource#getFilename()
	 */
	@Override
	protected String getFilename()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
