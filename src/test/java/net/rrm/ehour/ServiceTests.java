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


import net.rrm.ehour.customer.service.CustomerServiceTest;
import net.rrm.ehour.project.service.ProjectAssignmentServiceTest;
import net.rrm.ehour.project.service.ProjectServiceTest;
import net.rrm.ehour.report.criteria.ReportCriteriaTest;
import net.rrm.ehour.report.service.ReportCriteriaServiceTest;
import net.rrm.ehour.report.service.ReportServiceTest;
import net.rrm.ehour.report.util.ReportUtilTest;
import net.rrm.ehour.timesheet.dto.BookedDayComparatorTest;
import net.rrm.ehour.timesheet.service.TimesheetServiceTest;
import net.rrm.ehour.user.service.UserServiceTest;
import net.rrm.ehour.util.DateUtilTest;
import net.rrm.ehour.web.calendar.CalendarUtilTest;
import net.rrm.ehour.web.calendar.tag.NavCalendarTagTest;
import net.rrm.ehour.web.report.reports.CustomerReportTest;
import net.rrm.ehour.web.report.reports.ProjectReportTest;
import net.rrm.ehour.web.report.reports.UserReportTest;
import net.rrm.ehour.web.report.util.UserCriteriaAssemblerTest;
import net.rrm.ehour.web.timesheet.util.TimesheetFormAssemblerTest;
import net.rrm.ehour.web.timesheet.util.TimesheetRowComparatorTest;
import net.rrm.ehour.web.util.AuthUtilTest;
import net.rrm.ehour.web.util.DomainAssemblerTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({UserServiceTest.class,
				TimesheetServiceTest.class, 
				ReportServiceTest.class, 
				ReportCriteriaServiceTest.class,
				ProjectServiceTest.class,
				ProjectAssignmentServiceTest.class,
				CustomerServiceTest.class,
				CalendarUtilTest.class,
				DateUtilTest.class,
				AuthUtilTest.class,
				CalendarUtilTest.class,		
				BookedDayComparatorTest.class,
				DomainAssemblerTest.class,
				NavCalendarTagTest.class,
				CustomerReportTest.class,
				ReportCriteriaTest.class,
				UserCriteriaAssemblerTest.class,
				ReportUtilTest.class,
				UserReportTest.class,
				CustomerReportTest.class,
				TimesheetRowComparatorTest.class,
				TimesheetFormAssemblerTest.class,
				ProjectReportTest.class}) 
public class ServiceTests
{
}
