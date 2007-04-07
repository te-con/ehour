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
import net.rrm.ehour.mail.callbacks.AssignmentOverrunCallback;
import net.rrm.ehour.mail.dto.FixedAssignmentOverrunMessage;
import net.rrm.ehour.mail.dto.MailTaskMessage;
import net.rrm.ehour.project.domain.ProjectAssignment;
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
	private AssignmentOverrunCallback	assignmentOverrunCallback;
	
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
	public void mailProjectAssignmentOverrun(ProjectAssignment assignment, User user)
	{
		SimpleMailMessage	msg = new SimpleMailMessage();
		FixedAssignmentOverrunMessage	faoMsg = new FixedAssignmentOverrunMessage();
		MailTask			mailTask;
		
		msg.setText("ohoh");
		
		faoMsg.setMailMessage(msg);
		faoMsg.setToUser(user);
		faoMsg.setAssignment(assignment);
		
		faoMsg.setCallBack(assignmentOverrunCallback);
		
		mailTask = new MailTask(faoMsg);
		taskExecutor.execute(mailTask);
	}
	
	/**
	 * 
	 * @author Thies
	 *
	 */
	private class MailTask implements Runnable
	{
		private	MailTaskMessage mailTaskMessage;
		
		/**
		 * 
		 * @param msg
		 */
		public MailTask(MailTaskMessage mailTaskMessage)
		{
			this.mailTaskMessage = mailTaskMessage;
		}

		/**
		 * 
		 */
		public void run()
		{
			SimpleMailMessage msg = mailTaskMessage.getMailMessage();
			
			msg.setFrom(config.getMailFrom());
			msg.setTo(mailTaskMessage.getToUser().getEmail());
			
			try
			{
				logger.debug("Sending email to " + msg.getTo()[0]);	
				mailSender.send(msg);
				
				mailTaskMessage.getCallback().mailTaskSuccess(mailTaskMessage);
			}
			catch (MailException me)
			{
				logger.info("Failed to e-mail to " + msg.getTo()[0] + ": " + me.getMessage());
				mailTaskMessage.getCallback().mailTaskFailure(mailTaskMessage, me);
			}			
		}
	}

	/**
	 * @param assignmentOverrunCallback the assignmentOverrunCallback to set
	 */
	public void setAssignmentOverrunCallback(AssignmentOverrunCallback assignmentOverrunCallback)
	{
		this.assignmentOverrunCallback = assignmentOverrunCallback;
	}
}
