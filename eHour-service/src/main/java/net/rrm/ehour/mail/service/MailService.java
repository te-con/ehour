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

package net.rrm.ehour.mail.service;

import java.util.Date;
import java.util.List;

import net.rrm.ehour.domain.MailLogAssignment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;

/**
 * Mail service
 **/

public interface MailService
{
	/**
	 * Send project assignment overrun e-mail for fixed assignments, allotted reached
	 * @param assignment
	 * @param bookDate
	 * @param mailToUser
	 */
	public void mailPMFixedAllottedReached(AssignmentAggregateReportElement assignment, Date bookDate, User mailToUser);
	
	/**
	 * Send project assignment overrun e-mail for flex assignments, overrun reached
	 * @param assignment
	 * @param bookDate
	 * @param mailToUser
	 */
	public void mailPMFlexOverrunReached(AssignmentAggregateReportElement assignment, Date bookDate, User mailToUser);


	/**
	 * Send project assignment overrun e-mail for flex assignments, allotted reached
	 * @param assignment
	 * @param bookDate
	 * @param mailToUser
	 */
	public void mailPMFlexAllottedReached(AssignmentAggregateReportElement assignment, Date bookDate, User mailToUser);
	
	/**
	 * Get sent mail for assignments
	 * @param assignmentId
	 * @return
	 */
	public List<MailLogAssignment> getSentMailForAssignment(Integer[] assignmentId);
	
	/**
	 * Send a test message
	 */
	public void mailTestMessage();
}
