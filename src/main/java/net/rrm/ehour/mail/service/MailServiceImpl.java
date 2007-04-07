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

import java.util.Date;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.mail.callbacks.AssignmentMsgCallback;
import net.rrm.ehour.mail.domain.MailType;
import net.rrm.ehour.mail.dto.AssignmentPMMessage;
import net.rrm.ehour.mail.dto.MailTaskMessage;
import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.util.EhourConstants;

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
	private AssignmentMsgCallback	assignmentMsgCallback;
	
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
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.mail.service.MailService#mailPMFixedAllottedReached(net.rrm.ehour.report.reports.ProjectAssignmentAggregate, java.util.Date, net.rrm.ehour.user.domain.User)
	 */
	public void mailPMFixedAllottedReached(ProjectAssignmentAggregate assignmentAggregate, Date bookDate, User user)
	{
		mailPMAggregateMessage(assignmentAggregate,
								"allotted hours reached", 
								EhourConstants.MAILTYPE_FIXED_ALLOTTED_REACHED,
								bookDate,
								user);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.mail.service.MailService#mailPMFlexOverrunReached(net.rrm.ehour.report.reports.ProjectAssignmentAggregate, java.util.Date, net.rrm.ehour.user.domain.User)
	 */
	public void mailPMFlexOverrunReached(ProjectAssignmentAggregate assignmentAggregate, Date bookDate, User user)
	{
		mailPMAggregateMessage(assignmentAggregate, 
								"allotted hours reached", 
								EhourConstants.MAILTYPE_FLEX_OVERRUN_REACHED,
								bookDate,
								user);
	}	

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.mail.service.MailService#mailPMFlexOverrunReached(net.rrm.ehour.report.reports.ProjectAssignmentAggregate, java.util.Date, net.rrm.ehour.user.domain.User)
	 */
	public void mailPMFlexAllottedReached(ProjectAssignmentAggregate assignmentAggregate, Date bookDate, User user)
	{
		mailPMAggregateMessage(assignmentAggregate, 
								"allotted hours reached", 
								EhourConstants.MAILTYPE_FLEX_ALLOTTED_REACHED,
								bookDate,
								user);
	}	
	
	/**
	 * Mail project assignment msg
	 * @param assignmentAggregate
	 * @param mailBody
	 * @param mailTypeId
	 * @param user
	 */
	private void mailPMAggregateMessage(ProjectAssignmentAggregate assignmentAggregate,
										String mailBody, int mailTypeId, Date bookDate, User user)
	{
		SimpleMailMessage	msg = new SimpleMailMessage();
		AssignmentPMMessage	asgMsg = new AssignmentPMMessage();
		MailTask			mailTask;
		
		msg.setText(mailBody);
		
		asgMsg.setMailMessage(msg);
		asgMsg.setToUser(user);
		asgMsg.setBookDate(bookDate);
		asgMsg.setAggregate(assignmentAggregate);
		asgMsg.setMailType(new MailType(mailTypeId));
		asgMsg.setCallBack(assignmentMsgCallback);
		
		mailTask = new MailTask(asgMsg);
		taskExecutor.execute(mailTask);
	}
	
	/**
	 * Actual send off mail and invoke callback afterwards
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
	 * @param assignmentMsgCallback the assignmentMsgCallback to set
	 */
	public void setAssignmentMsgCallback(AssignmentMsgCallback assignmentMsgCallback)
	{
		this.assignmentMsgCallback = assignmentMsgCallback;
	}
}
