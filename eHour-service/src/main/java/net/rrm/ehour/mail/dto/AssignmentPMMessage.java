/**
 * Created on Apr 6, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.mail.dto;

import java.util.Date;

import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;


/**
 * Mail message for fixed assignment overrun 
 **/

public class AssignmentPMMessage extends MailTaskMessage
{
	private ProjectAssignmentAggregate	aggregate;
	private	Date						bookDate;
	
	/**
	 * @return the aggregate
	 */
	public ProjectAssignmentAggregate getAggregate()
	{
		return aggregate;
	}

	/**
	 * @param aggregate the aggregate to set
	 */
	public void setAggregate(ProjectAssignmentAggregate aggregate)
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
