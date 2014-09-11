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

package net.rrm.ehour.ui.timesheet.page;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.notNull;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import net.rrm.ehour.approvalstatus.service.ApprovalStatusService;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.ApprovalStatus;
import net.rrm.ehour.domain.ApprovalStatusType;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.timesheet.dto.TimesheetOverview;
import net.rrm.ehour.timesheet.dto.UserProjectStatus;
import net.rrm.ehour.timesheet.service.IOverviewTimesheet;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import net.rrm.ehour.ui.common.MockExpectations;
import org.junit.Test;

import java.util.Calendar;

import static org.easymock.EasyMock.*;


/**
 * Overview page test
 **/
public class MonthOverviewPageTest extends BaseSpringWebAppTester
{
	@Test
	public void testOverviewPageRender()
	{
		IOverviewTimesheet overviewTimesheet = createMock(IOverviewTimesheet.class);
		getMockContext().putBean(overviewTimesheet);

        ApprovalStatusService approvalStatusService = createMock(ApprovalStatusService.class);
        getMockContext().putBean("approvalStatusService", approvalStatusService);

        MockExpectations.navCalendarEasyMock(overviewTimesheet, getWebApp());

		TimesheetOverview overview = new TimesheetOverview();
        UserProjectStatus userProjectStatus = new UserProjectStatus();
        userProjectStatus.setHours(10);
        Activity activity = new Activity();
        Project project = new Project();
        project.setCustomer(new Customer());
        activity.setProject(project);
        userProjectStatus.setActivity(activity);

        TreeSet<UserProjectStatus> allProjectStatuses = new TreeSet<UserProjectStatus>();
        allProjectStatuses.add(userProjectStatus);

        overview.setProjectStatus(allProjectStatuses);

		expect(overviewTimesheet.getTimesheetOverview((User)notNull(), (Calendar)notNull()))
				.andReturn(overview);

        ApprovalStatus approvalStatus = new ApprovalStatus();
        approvalStatus.setStatus(ApprovalStatusType.IN_PROGRESS);

        ArrayList<ApprovalStatus> allApprovalStatuses = new ArrayList<ApprovalStatus>();
        allApprovalStatuses.add(approvalStatus);

        expect(approvalStatusService.getApprovalStatusForUserWorkingForCustomer(isA(User.class), isA(Customer.class), isA(DateRange.class))).andReturn(allApprovalStatuses);

        replay(overviewTimesheet, approvalStatusService);
		
		getTester().startPage(MonthOverviewPage.class);
		getTester().assertRenderedPage(MonthOverviewPage.class);
		getTester().assertNoErrorMessage();
		
		verify(overviewTimesheet, approvalStatusService);
	}
}
