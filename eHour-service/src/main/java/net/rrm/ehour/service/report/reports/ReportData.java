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

package net.rrm.ehour.service.report.reports;

import java.io.Serializable;
import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.reports.element.ReportElement;

/**
 * Data holder for aggregate reports
 **/

public class ReportData implements Serializable
{
	private static final long serialVersionUID = -6344570520998830487L;
	
	private List<? extends ReportElement>		reportElements;
	private DateRange reportRange;
	
	public ReportData(List<? extends ReportElement> reportElements, DateRange reportRange)
	{
		this.reportElements = reportElements;
		this.reportRange = reportRange;
	}
	
	/**
	 * @return the reportRange
	 */
	public DateRange getReportRange()
	{
		return reportRange;
	}
	
	/**
	 * @return the reportElements
	 */
	public List<? extends ReportElement> getReportElements()
	{
		return reportElements;
	}
}
