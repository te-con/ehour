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

import net.rrm.ehour.domain.MailType;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.mail.callbacks.MailTaskCallback;

import org.springframework.mail.SimpleMailMessage;

/**
 * DTO for the MailTask
 **/

public abstract class MailTaskMessage
{
	private SimpleMailMessage	mailMessage;
	private MailTaskCallback	callback; 
	private	MailType			mailType;
	private	User				toUser;
	
	/**
	 * @return the mailMessage
	 */
	public SimpleMailMessage getMailMessage()
	{
		return mailMessage;
	}

	/**
	 * @param mailMessage the mailMessage to set
	 */
	public void setMailMessage(SimpleMailMessage mailMessage)
	{
		this.mailMessage = mailMessage;
	}

	/**
	 * @return the callBack
	 */
	public MailTaskCallback getCallback()
	{
		return callback;
	}

	/**
	 * @param callBack the callBack to set
	 */
	public void setCallBack(MailTaskCallback callback)
	{
		this.callback = callback;
	}

	/**
	 * @return the mailType
	 */
	public MailType getMailType()
	{
		return mailType;
	}

	/**
	 * @param mailType the mailType to set
	 */
	public void setMailType(MailType mailType)
	{
		this.mailType = mailType;
	}

	/**
	 * @return the toUser
	 */
	public User getToUser()
	{
		return toUser;
	}

	/**
	 * @param toUser the toUser to set
	 */
	public void setToUser(User toUser)
	{
		this.toUser = toUser;
	}
}
