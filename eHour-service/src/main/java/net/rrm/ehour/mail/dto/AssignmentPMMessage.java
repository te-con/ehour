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

package net.rrm.ehour.mail.dto;

import java.util.Date;

import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;


/**
 * Mail message for fixed assignment overrun 
 **/

public class AssignmentPMMessage extends MailTaskMessage
{
	private AssignmentAggregateReportElement	aggregate;
	private	Date						bookDate;
	
	/**
	 * @return the aggregate
	 */
	public AssignmentAggregateReportElement getAggregate()
	{
		return aggregate;
	}

	/**
	 * @param aggregate the aggregate to set
	 */
	public void setAggregate(AssignmentAggregateReportElement aggregate)
	{
		this.aggregate = aggregate;
	}

	/**
	 * @return the bookDate
	 */
	public Date getBookDate()
	{
		return bookDate;
	}

	/**
	 * @param bookDate the bookDate to set
	 */
	public void setBookDate(Date bookDate)
	{
		this.bookDate = bookDate;
	}

}
