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

import junit.framework.Test;
import junit.framework.TestSuite;
import net.rrm.ehour.customer.dao.CustomerDAOTest;
import net.rrm.ehour.project.dao.ProjectAssignmentDAOTest;
import net.rrm.ehour.project.dao.ProjectDAOTest;
import net.rrm.ehour.project.service.ProjectServiceIntegrationTest;
import net.rrm.ehour.report.dao.ReportDAOTest;
import net.rrm.ehour.timesheet.dao.TimesheetCommentDAOTest;
import net.rrm.ehour.timesheet.dao.TimesheetDAOTest;
import net.rrm.ehour.timesheet.service.TimesheetServiceIntegrationTest;
import net.rrm.ehour.user.dao.UserDAOTest;
import net.rrm.ehour.user.dao.UserDepartmentDAOTest;
import net.rrm.ehour.user.dao.UserRoleDAOTest;
import net.rrm.ehour.user.service.UserServiceIntegrationTest;

public class DAOTests
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite("DAO & integration tests for net.rrm.ehour");
		
		
		//$JUnit-BEGIN$
		suite.addTestSuite(TimesheetServiceIntegrationTest.class);
		suite.addTestSuite(UserServiceIntegrationTest.class);
		suite.addTestSuite(ProjectServiceIntegrationTest.class);

		suite.addTestSuite(UserDAOTest.class);
		suite.addTestSuite(UserDepartmentDAOTest.class);
		suite.addTestSuite(TimesheetDAOTest.class);
		suite.addTestSuite(ReportDAOTest.class);
		suite.addTestSuite(CustomerDAOTest.class);
		suite.addTestSuite(UserRoleDAOTest.class);
		suite.addTestSuite(ProjectDAOTest.class);
		suite.addTestSuite(ProjectAssignmentDAOTest.class);
		suite.addTestSuite(TimesheetCommentDAOTest.class);
		
		//$JUnit-END$
		return suite;
	}
}
