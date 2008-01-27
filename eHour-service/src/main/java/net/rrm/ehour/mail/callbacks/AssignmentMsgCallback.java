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

package net.rrm.ehour.mail.callbacks;

import net.rrm.ehour.domain.MailLogAssignment;
import net.rrm.ehour.mail.dto.AssignmentPMMessage;
import net.rrm.ehour.mail.dto.MailTaskMessage;

import org.springframework.mail.MailException;

/**
 * Callback 
 **/

public class AssignmentMsgCallback extends MailTaskCallback
{

	/* (non-Javadoc)
	 * @see net.rrm.ehour.mail.callbacks.MailTaskCallback#failure(org.springframework.mail.MailException)
	 */
	public void mailTaskFailure(MailTaskMessage mailTaskMessage, MailException me)
	{
		persistMailLogAssignmentMessage(mailTaskMessage, false, me.getMessage());
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.mail.callbacks.MailTaskCallback#success()
	 */
	public void mailTaskSuccess(MailTaskMessage mailTaskMessage)
	{
		persistMailLogAssignmentMessage(mailTaskMessage, true, null);
	}

	/**
	 * Persist mail log assignment message
	 * @param mailTaskMessage
	 * @param success
	 * @param resultMsg
	 */
	private void persistMailLogAssignmentMessage(MailTaskMessage mailTaskMessage, boolean success, String resultMsg)
	{
		MailLogAssignment 	mailLog;
		AssignmentPMMessage asgMessage = (AssignmentPMMessage)mailTaskMessage;
		
		mailLog = new MailLogAssignment();
		mailLog.setProjectAssignment(asgMessage.getAggregate().getProjectAssignment());
		mailLog.setBookDate(asgMessage.getBookDate());
		mailLog.setBookedHours(new Float(asgMessage.getAggregate().getHours().floatValue()));
		
		persistMailMessage(asgMessage, success, resultMsg, mailLog);
	}
}
