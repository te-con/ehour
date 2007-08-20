/**
 * Created on Aug 21, 2007
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

package net.rrm.ehour.ui;

import net.rrm.ehour.ui.page.admin.customer.CustomerAdminTest;
import net.rrm.ehour.ui.page.admin.department.DepartmentAdminTest;
import net.rrm.ehour.ui.page.admin.mainconfig.MainConfigTest;
import net.rrm.ehour.ui.page.admin.project.ProjectAdminTest;
import net.rrm.ehour.ui.page.admin.user.UserAdminTest;
import net.rrm.ehour.ui.page.login.LoginTest;
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
				CustomerReportTest.class
				})
public class AllUITests
{


}
