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

import java.util.Date;

import net.rrm.ehour.domain.MailLog;
import net.rrm.ehour.mail.dao.MailLogDAO;
import net.rrm.ehour.mail.dto.MailTaskMessage;

import org.springframework.mail.MailException;

/**
 * Mail task callback
 **/

public abstract class MailTaskCallback
{
	protected MailLogDAO	mailLogDAO;
	
	/**
	 * Handle success
	 *
	 */
	public abstract void mailTaskSuccess(MailTaskMessage mailTaskMessage);
	
	/**
	 * Handle failure
	 * @param me
	 */
	public abstract void mailTaskFailure(MailTaskMessage mailTaskMessage, MailException me);

	/**
	 * @param mailLogDAO the mailLogDAO to set
	 */
	public void setMailLogDAO(MailLogDAO mailLogDAO)
	{
		this.mailLogDAO = mailLogDAO;
	}
	
	/**
	 * Store mail message. Use mailLog and enrich it with standard props of msg
	 * @param msg
	 * @param mailLog
	 */
	protected void persistMailMessage(MailTaskMessage msg, boolean success, String resultMsg, MailLog mailLog)
	{
		mailLog.setMailType(msg.getMailType());
		mailLog.setTimestamp(new Date());
		mailLog.setToUser(msg.getToUser());
		mailLog.setSuccess(success);
		mailLog.setResultMsg(resultMsg);
		
		mailLogDAO.persist(mailLog);
	}
}
