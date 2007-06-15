/**
 * Created on Nov 4, 2006
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

package net.rrm.ehour;

import net.rrm.ehour.customer.dao.CustomerDAOTest;
import net.rrm.ehour.mail.dao.MailLogDAOTest;
import net.rrm.ehour.mail.service.MailServiceTest;
import net.rrm.ehour.project.dao.ProjectAssignmentDAOTest;
import net.rrm.ehour.project.dao.ProjectDAOTest;
import net.rrm.ehour.project.service.ProjectServiceIntegrationTest;
import net.rrm.ehour.report.dao.ReportAggregatedDAOTest;
import net.rrm.ehour.timesheet.dao.TimesheetCommentDAOTest;
import net.rrm.ehour.timesheet.dao.TimesheetDAOTest;
import net.rrm.ehour.timesheet.service.TimesheetServiceIntegrationTest;
import net.rrm.ehour.user.dao.UserDAOTest;
import net.rrm.ehour.user.dao.UserDepartmentDAOTest;
import net.rrm.ehour.user.dao.UserRoleDAOTest;
import net.rrm.ehour.user.service.UserServiceIntegrationTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({DAOTests.class,
				ServiceTests.class})

public class AllTests
{

}
