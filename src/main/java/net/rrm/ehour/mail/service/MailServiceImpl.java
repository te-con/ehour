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

package net.rrm.ehour.mail.service;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.user.domain.User;

import org.apache.log4j.Logger;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Mail servce which takes of sending mail async
 **/

public class MailServiceImpl implements MailService
{
	private	EhourConfig		config;
	private	Logger			logger = Logger.getLogger(this.getClass());
	private	MailSender		mailSender;
	private	TaskExecutor	taskExecutor;
	
	/**
	 * 
	 * @param config
	 */
	public MailServiceImpl(EhourConfig config, TaskExecutor taskExecutor)
	{
		this.config = config;
		this.taskExecutor = taskExecutor;
		
		mailSender = new JavaMailSenderImpl();
		((JavaMailSenderImpl)mailSender).setHost(config.getMailSmtp());
	}
	
	/**
	 * Mail project assignment overrun
	 * @param user
	 */
	public void mailProjectAssignmentOverrun(User user)
	{
		SimpleMailMessage	msg = new SimpleMailMessage();
		MailTask			mailTask;
		
		msg.setTo(user.getEmail());
		msg.setFrom(config.getMailFrom());
		msg.setText("ohoh");
		
		mailTask = new MailTask(msg);
		taskExecutor.execute(mailTask);
	}
	
	/**
	 * 
	 * @author Thies
	 *
	 */
	private class MailTask implements Runnable
	{
		private	SimpleMailMessage msg;
		
		/**
		 * 
		 * @param msg
		 */
		public MailTask(SimpleMailMessage msg)
		{
			this.msg = msg;
		}

		/**
		 * 
		 */
		public void run()
		{
			try
			{
				logger.debug("Sending overrun email to " + msg.getTo());	
				mailSender.send(msg);
			}
			catch (MailException me)
			{
				logger.info("Failed to e-mail to " + msg.getTo() + ": " + me.getMessage());
			}			
		}
		
	}
}
