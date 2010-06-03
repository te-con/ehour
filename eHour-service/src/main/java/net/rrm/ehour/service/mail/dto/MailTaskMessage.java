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

package net.rrm.ehour.service.mail.dto;

import net.rrm.ehour.domain.MailType;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.service.mail.callbacks.MailTaskCallback;

import org.springframework.mail.SimpleMailMessage;

/**
 * DTO for the MailTask
 **/

public class MailTaskMessage
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
