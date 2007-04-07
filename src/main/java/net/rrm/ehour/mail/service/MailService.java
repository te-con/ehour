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

import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.user.domain.User;

/**
 * Mail service
 **/

public interface MailService
{
	/**
	 * Send project assignment overrun e-mail
	 * @param user
	 */
	public void mailPMAllottedOverrunReached(ProjectAssignmentAggregate assignment, Date bookDate, User mailToUser);
	
	/**
	 * Send project assignment time allotted hours reached
	 * @param assignment
	 * @param mailToUser
	 */
	public void mailPMAllottedHoursReached(ProjectAssignmentAggregate assignment, Date bookDate, User mailToUser);
}
