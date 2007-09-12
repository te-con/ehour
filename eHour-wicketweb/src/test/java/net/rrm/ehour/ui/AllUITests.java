/**
 * Created on Aug 21, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui;

import net.rrm.ehour.ui.page.admin.assignment.AssignmentAdminTest;
import net.rrm.ehour.ui.page.admin.customer.CustomerAdminTest;
import net.rrm.ehour.ui.page.admin.department.DepartmentAdminTest;
import net.rrm.ehour.ui.page.admin.mainconfig.MainConfigTest;
import net.rrm.ehour.ui.page.admin.project.ProjectAdminTest;
import net.rrm.ehour.ui.page.admin.user.UserAdminTest;
import net.rrm.ehour.ui.page.login.LoginTest;
import net.rrm.ehour.ui.report.aggregate.CustomerAggregateReportTest;
import net.rrm.ehour.ui.report.reports.aggregate.AggregateReportSectionTest;
import net.rrm.ehour.ui.report.reports.aggregate.CustomerReportTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({CustomerAdminTest.class,
				DepartmentAdminTest.class,
				MainConfigTest.class,
				ProjectAdminTest.class,
				UserAdminTest.class,
				LoginTest.class,
				AggregateReportSectionTest.class,
				CustomerReportTest.class,
				AssignmentAdminTest.class,
                CustomerAggregateReportTest.class
                })
public class AllUITests
{


}
