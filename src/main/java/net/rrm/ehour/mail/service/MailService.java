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
import java.util.List;

import net.rrm.ehour.mail.domain.MailLogAssignment;
import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.user.domain.User;

/**
 * Mail service
 **/

public interface MailService
{
	/**
	 * Send project assignment overrun e-mail for fixed assignments, allotted reached
	 * @param assignment
	 * @param bookDate
	 * @param mailToUser
	 */
	public void mailPMFixedAllottedReached(ProjectAssignmentAggregate assignment, Date bookDate, User mailToUser);
	
	/**
	 * Send project assignment overrun e-mail for flex assignments, overrun reached
	 * @param assignment
	 * @param bookDate
	 * @param mailToUser
	 */
	public void mailPMFlexOverrunReached(ProjectAssignmentAggregate assignment, Date bookDate, User mailToUser);


	/**
	 * Send project assignment overrun e-mail for flex assignments, allotted reached
	 * @param assignment
	 * @param bookDate
	 * @param mailToUser
	 */
	public void mailPMFlexAllottedReached(ProjectAssignmentAggregate assignment, Date bookDate, User mailToUser);
	
	/**
	 * Get sent mail for assignments
	 * @param assignmentId
	 * @return
	 */
	public List<MailLogAssignment> getSentMailForAssignment(Integer[] assignmentId);
}
