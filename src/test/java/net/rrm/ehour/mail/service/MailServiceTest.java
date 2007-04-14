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

import net.rrm.ehour.DummyDataGenerator;
import net.rrm.ehour.dao.BaseDAOTest;
import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.user.domain.User;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;

/**
 * TODO 
 **/

public class MailServiceTest extends BaseDAOTest
{
	private MailService	mailService;
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.test.AbstractSingleSpringContextTests#getConfigLocations()
	 */
	protected String[] getConfigLocations()
	{
		String[] configs = super.getConfigLocations();
		
		configs = (String[])ArrayUtils.add(configs, "classpath:applicationContext-mail.xml");
		configs = (String[])ArrayUtils.add(configs, "classpath:applicationContext-service.xml");
		
		return configs;
	}
	
	/**
	 * 
	 * @param mailService
	 */
	public void setMailService(MailService mailService)
	{
		this.mailService = mailService;
	}
	
	/*
	 * 
	 */
	@Test
	public void testMailPMFixedAllottedReached()
	{
		User	user = new User(1);
		user.setEmail("spam@rrm.net");
		
		ProjectAssignmentAggregate asg = DummyDataGenerator.getProjectAssignmentAggregate(1, 1,1);
		asg.getProjectAssignment().setAssignmentId(1);
		asg.setHours(new Float(121.1f));
		asg.getProjectAssignment().setAllottedHours(100f);
		asg.getProjectAssignment().setAllowedOverrun(100f);
		
		mailService.mailPMFixedAllottedReached(asg, new Date(), user);
		try
		{
			// mailService is async
			Thread.sleep(2000);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * 
	 */
	@Test
	public void testMailPMFixedAllottedReachedFailure()
	{
		User	user = new User(1);
		user.setEmail("unknownemailaddress@rrm.net");
		
		ProjectAssignmentAggregate asg = DummyDataGenerator.getProjectAssignmentAggregate(1, 1,1);
		asg.getProjectAssignment().setAssignmentId(1);
		asg.setHours(new Float(121.1f));
		asg.getProjectAssignment().setAllottedHours(100f);
		asg.getProjectAssignment().setAllowedOverrun(100f);
		
		mailService.mailPMFixedAllottedReached(asg, new Date(), user);
		try
		{
			// mailService is async
			Thread.sleep(2000);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
}
