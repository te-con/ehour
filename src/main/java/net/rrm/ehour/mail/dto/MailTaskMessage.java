/**
 * Created on Apr 6, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.mail.dto;

import net.rrm.ehour.mail.callbacks.MailTaskCallback;

import org.springframework.mail.SimpleMailMessage;

/**
 * DTO for the MailTask
 **/

public abstract class MailTaskMessage
{
	private SimpleMailMessage	mailMessage;
	private MailTaskCallback	callback; 

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
}
