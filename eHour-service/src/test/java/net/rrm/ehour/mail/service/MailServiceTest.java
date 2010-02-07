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

package net.rrm.ehour.mail.service;

import java.util.Date;

import net.rrm.ehour.AbstractServiceTest;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElementMother;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class MailServiceTest extends AbstractServiceTest
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
		
		AssignmentAggregateReportElement asg = AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(1, 1,1);
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
		
		AssignmentAggregateReportElement asg = AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(1, 1,1);
		asg.getProjectAssignment().setAssignmentId(1);
		asg.setHours(new Float(121.1f));
		asg.getProjectAssignment().setAllottedHours(100f);
		asg.getProjectAssignment().setAllowedOverrun(100f);
		
		mailService.mailPMFixedAllottedReached(asg, new Date(), user);

		Thread.sleep(2000);
	}	
	
}
