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

package net.rrm.ehour.ui.customerreviewer;

import net.rrm.ehour.activity.service.ActivityService;
import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.timesheet.dto.TimesheetOverview;
import net.rrm.ehour.timesheet.dto.UserProjectStatus;
import net.rrm.ehour.timesheet.service.IOverviewTimesheet;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.page.AbstractBasePage;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.*;

/**
 * Customer Reviewer Page for viewing all Time-sheets for users
 **/
 @AuthorizeInstantiation("ROLE_CUSTOMERREVIEWER")
public class CustomerReviewerPage extends AbstractBasePage<ReportCriteria> {
	
	@SpringBean
	private IOverviewTimesheet timesheetService;
	
	@SpringBean
	private CustomerService customerService;
	
	@SpringBean
	private ActivityService activityService;
	
	public CustomerReviewerPage() {
		super(new ResourceModel("customerReviewer.title"));
		
		EhourWebSession session = ((EhourWebSession)this.getSession());
		User user = EhourWebSession.getUser();
		
		Set<User> usersForACustomer = new HashSet<User>();
		List<Customer> customersForWhichUserIsAReviewer = customerService.findAllCustomersForWhichUserIsaReviewer(user);
		
		List<Activity> allActivitiesForcustomers = activityService.getAllActivitiesForcustomers(customersForWhichUserIsAReviewer);
		for (Activity activity : allActivitiesForcustomers) {
			usersForACustomer.add(activity.getAssignedUser());
		}
		
		
		Calendar overviewFor = session.getNavCalendar();
		
		overviewFor.set(Calendar.DAY_OF_MONTH, 1);
		
		List<TimesheetOverview> timesheetOverviews = new ArrayList<TimesheetOverview>();
		
		for (User userForCustomer : usersForACustomer) {
			TimesheetOverview timesheetOverview = timesheetService.getTimesheetOverview(userForCustomer, overviewFor);
			timesheetOverviews.add(timesheetOverview);
		}
		
		Set<SortedSet<UserProjectStatus>> userProjectstatuses = new HashSet<SortedSet<UserProjectStatus>>();
		
		for (TimesheetOverview timesheetOverview : timesheetOverviews) {
			SortedSet<UserProjectStatus> projectStatus = timesheetOverview.getProjectStatus();
			userProjectstatuses.add(projectStatus);
		}
		
		GreyRoundedBorder greyBorder = new GreyRoundedBorder("customerReviewerFrame", new ResourceModel("customerReviewer.title"));
		add(greyBorder);
		
		CustomerReviewerPanel customerReviewerPanel = new CustomerReviewerPanel("customerReviewerPanel", overviewFor, timesheetOverviews);
		greyBorder.add(customerReviewerPanel);
	}

}
