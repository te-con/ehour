package net.rrm.ehour.mail.service;

import java.util.Date;
import java.util.List;

import net.rrm.ehour.config.EhourConfig;
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
	 * @param config to use
	 */
	public void mailTestMessage(EhourConfig config);
}
