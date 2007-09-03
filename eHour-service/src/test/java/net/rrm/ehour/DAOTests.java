/**
 * Created on Nov 4, 2006
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

package net.rrm.ehour;

import net.rrm.ehour.customer.dao.CustomerDAOTest;
import net.rrm.ehour.customer.service.CustomerServiceIntegrationTest;
import net.rrm.ehour.mail.dao.MailLogDAOTest;
import net.rrm.ehour.mail.service.MailServiceTest;
import net.rrm.ehour.project.dao.ProjectAssignmentDAOTest;
import net.rrm.ehour.project.dao.ProjectDAOTest;
import net.rrm.ehour.project.service.ProjectAssignmentServiceIntegrationTest;
import net.rrm.ehour.project.service.ProjectServiceIntegrationTest;
import net.rrm.ehour.report.dao.ReportAggregatedDAOTest;
import net.rrm.ehour.timesheet.dao.TimesheetCommentDAOTest;
import net.rrm.ehour.timesheet.dao.TimesheetDAOTest;
import net.rrm.ehour.timesheet.service.TimesheetServiceIntegrationTest;
import net.rrm.ehour.user.dao.CustomerFoldPreferenceDAOImplTest;
import net.rrm.ehour.user.dao.UserDAOTest;
import net.rrm.ehour.user.dao.UserDepartmentDAOTest;
import net.rrm.ehour.user.dao.UserRoleDAOTest;
import net.rrm.ehour.user.service.UserServiceIntegrationTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({TimesheetServiceIntegrationTest.class,
				UserServiceIntegrationTest.class, 
				ProjectAssignmentServiceIntegrationTest.class, 
				UserDAOTest.class,
				UserDepartmentDAOTest.class,
				TimesheetDAOTest.class,
				ReportAggregatedDAOTest.class,
				CustomerDAOTest.class,
				ProjectDAOTest.class,
				UserRoleDAOTest.class,		
				ProjectAssignmentDAOTest.class,
				TimesheetCommentDAOTest.class,
				MailServiceTest.class,
				MailLogDAOTest.class,
				CustomerFoldPreferenceDAOImplTest.class,
				CustomerServiceIntegrationTest.class,
				ProjectServiceIntegrationTest.class
				})
//				ReportPerMonthDAOTest.class}) TODO fix the hibernate tx manager usage here (leave it out)

public class DAOTests
{

}
