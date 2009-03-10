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

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import net.rrm.ehour.audit.NonAuditable;
import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.config.service.ConfigurationService;
import net.rrm.ehour.domain.MailLogAssignment;
import net.rrm.ehour.domain.MailType;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.mail.callbacks.AssignmentMsgCallback;
import net.rrm.ehour.mail.dao.MailLogDAO;
import net.rrm.ehour.mail.dto.AssignmentPMMessage;
import net.rrm.ehour.mail.dto.MailTaskMessage;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.util.EhourConstants;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Mail servce which takes of sending mail async
 **/
@NonAuditable
public class MailServiceImpl implements MailService
{
	private	Logger			logger = Logger.getLogger(this.getClass());
	private MailLogDAO		mailLogDAO;
	private	TaskExecutor	taskExecutor;
	private ConfigurationService 	configurationService;
	private AssignmentMsgCallback	assignmentMsgCallback;
	
	/**
	 * 
	 * @param config
	 */
	public MailServiceImpl(TaskExecutor taskExecutor)
	{
		this.taskExecutor = taskExecutor;
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.mail.service.MailService#mailTestMessage(net.rrm.ehour.config.EhourConfig)
	 */
	public void mailTestMessage(EhourConfig config)
	{
		String subject = "eHour test message";
		String body = "You successfully configured eHour's mail settings.";
		
		SimpleMailMessage	msg; 
		MailTask			mailTask;
		
		msg = new SimpleMailMessage();
		msg.setText(body);
		msg.setSubject(subject);
		msg.setFrom(config.getMailFrom());
		
		MailTaskMessage taskMessage = new MailTaskMessage();
		User user = new User();
		user.setEmail(config.getMailFrom());
		taskMessage.setToUser(user);
		
		taskMessage.setMailMessage(msg);
		
		mailTask = new MailTask(taskMessage, createMailSender(config));
		taskExecutor.execute(mailTask);
	}	
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.mail.service.MailService#mailPMFixedAllottedReached(net.rrm.ehour.report.reports.ProjectAssignmentAggregate, java.util.Date, net.rrm.ehour.user.domain.User)
	 */
	public void mailPMFixedAllottedReached(AssignmentAggregateReportElement assignmentAggregate, Date bookDate, User user)
	{
		String	subject;
		StringBuffer	body = new StringBuffer();
		NumberFormat	numberFormat = NumberFormat.getNumberInstance();
		SimpleDateFormat	dateFormat = new SimpleDateFormat("dd MMM yyyy");
		
		// TODO use templates
		subject = "eHour: All allotted hours used for project " 
					+ assignmentAggregate.getProjectAssignment().getProject().getFullName()
					+ " by "
					+ assignmentAggregate.getProjectAssignment().getUser().getFirstName()
					+ " "
					+ assignmentAggregate.getProjectAssignment().getUser().getLastName();
		
		body.append("Project warning for project " 
						+ assignmentAggregate.getProjectAssignment().getProject().getFullName()
						+ " ("
						+ assignmentAggregate.getProjectAssignment().getProject().getCustomer().getName()
						+ ")"
						+ "\r\n\r\n");
		body.append(numberFormat.format(assignmentAggregate.getProjectAssignment().getAllottedHours().floatValue()) 
						+ " hours were allotted for this project to " 
						+ assignmentAggregate.getProjectAssignment().getUser().getFirstName()
						+ " "
						+ assignmentAggregate.getProjectAssignment().getUser().getLastName() 
						+ "."
						+ "\r\n");
		body.append("On " 
						+ dateFormat.format(bookDate)
						+ ", a total of "
						+ numberFormat.format(assignmentAggregate.getHours()) 
						+ " hours were booked reaching the allotted hours mark."
						+ "\r\n\r\n");
		body.append("Take note, "
						+ assignmentAggregate.getProjectAssignment().getUser().getFirstName()
						+ " "
						+ assignmentAggregate.getProjectAssignment().getUser().getLastName() 
						+ " can't book anymore hours on the project. Add more hours in the project assignment if needed.");
		
		mailPMAggregateMessage(assignmentAggregate,
								subject,
								body.toString(),
								EhourConstants.MAILTYPE_FIXED_ALLOTTED_REACHED,
								bookDate,
								user);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.mail.service.MailService#mailPMFlexOverrunReached(net.rrm.ehour.report.reports.ProjectAssignmentAggregate, java.util.Date, net.rrm.ehour.user.domain.User)
	 */
	public void mailPMFlexOverrunReached(AssignmentAggregateReportElement assignmentAggregate, Date bookDate, User user)
	{
		String	subject;
		StringBuffer	body = new StringBuffer();
		NumberFormat	numberFormat = NumberFormat.getNumberInstance();
		SimpleDateFormat	dateFormat = new SimpleDateFormat("dd MMM yyyy");
		
		// TODO use templates
		
		// and no, stringbuffers aren't faster anymore..
		subject = "eHour: All allotted and overrun hours used for project " 
					+ assignmentAggregate.getProjectAssignment().getProject().getFullName()
					+ " by "
					+ assignmentAggregate.getProjectAssignment().getUser().getFirstName()
					+ " "
					+ assignmentAggregate.getProjectAssignment().getUser().getLastName();
		
		body.append("Project warning for project " 
						+ assignmentAggregate.getProjectAssignment().getProject().getFullName()
						+ " ("
						+ assignmentAggregate.getProjectAssignment().getProject().getCustomer().getName()
						+ ")"
						+ "\r\n\r\n");
		body.append(numberFormat.format(assignmentAggregate.getProjectAssignment().getAllottedHours().floatValue()) 
						+ " hours were allotted for this project to " 
						+ assignmentAggregate.getProjectAssignment().getUser().getFirstName()
						+ " "
						+ assignmentAggregate.getProjectAssignment().getUser().getLastName() 
						+ "."
						+ "\r\n");
		body.append("Additionally an extra overrun of "
				+ numberFormat.format(assignmentAggregate.getProjectAssignment().getAllowedOverrun())
				+ " hours was created for this employee.\r\n\r\n");		
		body.append("On " 
						+ dateFormat.format(bookDate)
						+ ", a total of "
						+ numberFormat.format(assignmentAggregate.getHours()) 
						+ " hours were booked consuming as well the allotted hours as the extra overrun."
						+ "\r\n\r\n");
		body.append("Take note, "
				+ assignmentAggregate.getProjectAssignment().getUser().getFirstName()
				+ " "
				+ assignmentAggregate.getProjectAssignment().getUser().getLastName() 
				+ " can't book anymore hours on the project. Add more hours in the project assignment if needed.");
		
		
		mailPMAggregateMessage(assignmentAggregate,
								subject,
								body.toString(),
								EhourConstants.MAILTYPE_FLEX_OVERRUN_REACHED,
								bookDate,
								user);
	}	

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.mail.service.MailService#mailPMFlexOverrunReached(net.rrm.ehour.report.reports.ProjectAssignmentAggregate, java.util.Date, net.rrm.ehour.user.domain.User)
	 */
	public void mailPMFlexAllottedReached(AssignmentAggregateReportElement assignmentAggregate, Date bookDate, User user)
	{
		String	subject;
		StringBuffer	body = new StringBuffer();
		NumberFormat	numberFormat = NumberFormat.getNumberInstance();
		SimpleDateFormat	dateFormat = new SimpleDateFormat("dd MMM yyyy");
		
		// TODO use templates
		
		// and no, stringbuffers aren't faster anymore..
		subject = "eHour: All allotted hours used for project " 
					+ assignmentAggregate.getProjectAssignment().getProject().getFullName()
					+ " by "
					+ assignmentAggregate.getProjectAssignment().getUser().getFirstName()
					+ " "
					+ assignmentAggregate.getProjectAssignment().getUser().getLastName();
		
		body.append("Project warning for project " 
						+ assignmentAggregate.getProjectAssignment().getProject().getFullName()
						+ " ("
						+ assignmentAggregate.getProjectAssignment().getProject().getCustomer().getName()
						+ ")"
						+ "\r\n\r\n");
		body.append(numberFormat.format(assignmentAggregate.getProjectAssignment().getAllottedHours().floatValue()) 
						+ " hours were allotted for this project to " 
						+ assignmentAggregate.getProjectAssignment().getUser().getFirstName()
						+ " "
						+ assignmentAggregate.getProjectAssignment().getUser().getLastName() 
						+ "."
						+ "\r\n");
		body.append("On " 
						+ dateFormat.format(bookDate)
						+ ", a total of "
						+ numberFormat.format(assignmentAggregate.getHours()) 
						+ " hours were booked reaching the allotted hours mark."
						+ "\r\n\r\n");
		body.append("An extra overrun of "
						+ numberFormat.format(assignmentAggregate.getProjectAssignment().getAllowedOverrun())
						+ " hours was granted when the employee was assigned on this project. Hours can still be booked on this project.");
		
		mailPMAggregateMessage(assignmentAggregate,
								subject,
								body.toString(), 
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
	private void mailPMAggregateMessage(AssignmentAggregateReportElement assignmentAggregate,
										String subject,
										String mailBody, int mailTypeId, Date bookDate, User user)
	{
		SimpleMailMessage	msg; 
		AssignmentPMMessage	asgMsg;
		MailTask			mailTask;
		
		if (!isAssignmentMailAlreadySent(assignmentAggregate, mailTypeId))
		{
			EhourConfig config = configurationService.getConfiguration();
			
			asgMsg = new AssignmentPMMessage();
			msg = new SimpleMailMessage();
			msg.setText(mailBody);
			msg.setSubject(subject);
			msg.setFrom(config.getMailFrom());
			
			asgMsg.setMailMessage(msg);
			asgMsg.setToUser(user);
			asgMsg.setBookDate(bookDate);
			asgMsg.setAggregate(assignmentAggregate);
			asgMsg.setMailType(new MailType(mailTypeId));
			asgMsg.setCallBack(assignmentMsgCallback);
			
			mailTask = new MailTask(asgMsg, createMailSender(config));
			taskExecutor.execute(mailTask);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean isAssignmentMailAlreadySent(AssignmentAggregateReportElement aggregate, int mailTypeId)
	{
		List<MailLogAssignment> mlaList = mailLogDAO.findMailLogOnAssignmentIds(new Integer[]{aggregate.getProjectAssignment().getAssignmentId()});
		boolean	alreadySent = false;
		
		for (MailLogAssignment mailLog : mlaList)
		{
			if ((mailLog.getMailType().getMailTypeId().intValue() == mailTypeId)
				&& mailLog.getSuccess().booleanValue())
			{
				alreadySent = true;
				logger.info("Mail was already sent for assignment " + aggregate.getProjectAssignment().getAssignmentId() + ", not sending again");
				break;
			}
		}
		
		return alreadySent;
	}
	
	/**
	 * Actual send off mail and invoke callback afterwards
	 * @author Thies
	 *
	 */
	private class MailTask implements Runnable
	{
		private	MailTaskMessage mailTaskMessage;
		private MailSender		javaMailSender;
		
		public MailTask(MailTaskMessage mailTaskMessage, MailSender mailSender)
		{
			this.mailTaskMessage = mailTaskMessage;
			javaMailSender = mailSender;
		}
		
		
		/**
		 * 
		 */
		public void run()
		{
			SimpleMailMessage msg = mailTaskMessage.getMailMessage();
			
			msg.setTo(mailTaskMessage.getToUser().getEmail());
			try
			{
				logger.debug("Sending email to " + msg.getTo()[0] + " using " + ((JavaMailSenderImpl)javaMailSender).getHost());	
				javaMailSender.send(msg);
				
				if (mailTaskMessage.getCallback() != null)
				{
					mailTaskMessage.getCallback().mailTaskSuccess(mailTaskMessage);
				}
			}
			catch (MailException me)
			{
				logger.info("Failed to e-mail to " + msg.getTo()[0] + ": " + me.getMessage());
				mailTaskMessage.getCallback().mailTaskFailure(mailTaskMessage, me);
			}			
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.mail.service.MailService#getSentMailForAssignment(java.lang.Integer[])
	 */
	public List<MailLogAssignment> getSentMailForAssignment(Integer[] assignmentIds)
	{
		return mailLogDAO.findMailLogOnAssignmentIds(assignmentIds);
	}
	
	/**
	 * Create mail sender
	 * @param config
	 * @return
	 */
	private MailSender createMailSender(EhourConfig config)
	{
		MailSender mailSender = new JavaMailSenderImpl();
		((JavaMailSenderImpl)mailSender).setHost(config.getMailSmtp());
		
		if (!StringUtils.isBlank(config.getSmtpPort()))
		{
			try
			{
				int port = Float.valueOf(config.getSmtpPort()).intValue();
				((JavaMailSenderImpl)mailSender).setPort(port);
			}
			catch (NumberFormatException nfe)
			{
				logger.error("Using default port 25, couldn't parse configured port " + config.getSmtpPort());
			}
		}
		
		
		if (! StringUtils.isBlank(config.getSmtpUsername())
				&& ! StringUtils.isBlank(config.getSmtpPassword())) 
		{
			logger.debug("Using SMTP authentication");
			
			Properties prop = new Properties();
			prop.put("mail.smtp.auth", "true");
			
			((JavaMailSenderImpl)mailSender).setJavaMailProperties(prop);
			((JavaMailSenderImpl)mailSender).setUsername(config.getSmtpUsername());
			((JavaMailSenderImpl)mailSender).setPassword(config.getSmtpPassword());
		}
		
		return mailSender;
	}	
	
	/**
	 * @param assignmentMsgCallback the assignmentMsgCallback to set
	 */
	public void setAssignmentMsgCallback(AssignmentMsgCallback assignmentMsgCallback)
	{
		this.assignmentMsgCallback = assignmentMsgCallback;
	}

	/**
	 * @param mailLogDAO the mailLogDAO to set
	 */
	public void setMailLogDAO(MailLogDAO mailLogDAO)
	{
		this.mailLogDAO = mailLogDAO;
	}

	/**
	 * @param configurationService the configurationService to set
	 */
	public void setConfigurationService(ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}
}
