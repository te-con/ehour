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

import java.util.Date;

import net.rrm.ehour.DummyDataGenerator;
import net.rrm.ehour.dao.BaseDAOTest;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * TODO 
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext-mail.xml", "classpath:applicationContext-service.xml"})
public class MailServiceTest extends BaseDAOTest
{
	@Autowired
	private MailService	mailService;
	/*
	 * 
	 */
	@Test
	public void testMailPMFixedAllottedReached() throws InterruptedException
	{
		User	user = new User(1);
		user.setEmail("spam@rrm.net");
		
		AssignmentAggregateReportElement asg = DummyDataGenerator.getProjectAssignmentAggregate(1, 1,1);
		asg.getProjectAssignment().setAssignmentId(1);
		asg.setHours(new Float(121.1f));
		asg.getProjectAssignment().setAllottedHours(100f);
		asg.getProjectAssignment().setAllowedOverrun(100f);
		
		mailService.mailPMFixedAllottedReached(asg, new Date(), user);
		// mailService is async
		Thread.sleep(2000);
	}

	/*
	 * 
	 */
	@Test
	public void testMailPMFixedAllottedReachedFailure() throws InterruptedException
	{
		User	user = new User(1);
		user.setEmail("unknownemailaddress@rrm.net");
		
		AssignmentAggregateReportElement asg = DummyDataGenerator.getProjectAssignmentAggregate(1, 1,1);
		asg.getProjectAssignment().setAssignmentId(1);
		asg.setHours(new Float(121.1f));
		asg.getProjectAssignment().setAllottedHours(100f);
		asg.getProjectAssignment().setAllowedOverrun(100f);
		
		mailService.mailPMFixedAllottedReached(asg, new Date(), user);

		Thread.sleep(2000);
	}	
	
}
