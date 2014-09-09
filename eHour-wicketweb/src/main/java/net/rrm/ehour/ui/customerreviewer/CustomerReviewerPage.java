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

import java.util.Calendar;
import java.util.List;

import net.rrm.ehour.activity.service.ActivityService;
import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.timesheet.dto.TimesheetOverview;
import net.rrm.ehour.timesheet.service.IOverviewTimesheet;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.page.AbstractBasePage;
import net.rrm.ehour.ui.common.panel.calendar.CalendarAjaxEventType;
import net.rrm.ehour.ui.common.panel.calendar.CalendarPanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.*;

/**
 * Customer Reviewer Page for viewing all Time-sheets for users
 **/
 @AuthorizeInstantiation("ROLE_CUSTOMERREVIEWER")
public class CustomerReviewerPage extends AbstractBasePage<ReportCriteria> {
	
	@SpringBean
	private TimesheetService timesheetService;
	
	@SpringBean
	private CustomerService customerService;
	
	@SpringBean
	private ActivityService activityService;
	
	private WebMarkupContainer customerReviewerPanel;
	
	private GreyRoundedBorder greyBorder;
	
	public CustomerReviewerPage() {
		super(new ResourceModel("customerReviewer.title"));
		
		EhourWebSession session = ((EhourWebSession)this.getSession());
		Calendar overviewFor = session.getNavCalendar();
		overviewFor.set(Calendar.DAY_OF_MONTH, 1);
		
		List<Activity> allActivitiesOfCustomerForMonth = getActivitiesForCustomersForWhichUserIsAReviewer();
		
        CalendarPanel calendarPanel = new CalendarPanel("sidePanel", EhourWebSession.getUser());
        add(calendarPanel);
		
		greyBorder = new GreyRoundedBorder("customerReviewerFrame", new ResourceModel("customerReviewer.title"));
		greyBorder.setOutputMarkupId(true);
		add(greyBorder);
		
		customerReviewerPanel = new CustomerReviewerPanel("customerReviewerPanel", overviewFor, allActivitiesOfCustomerForMonth);
		addOrReplaceContentContainer(customerReviewerPanel);
	}
	
	private List<Activity> getActivitiesForCustomersForWhichUserIsAReviewer() {
		EhourWebSession session = ((EhourWebSession) this.getSession());
		Calendar overviewFor = session.getNavCalendar();
		overviewFor.set(Calendar.DAY_OF_MONTH, 1);

		User user = session.getUser().getUser();
		Set<User> usersForACustomer = new HashSet<User>();
		List<Customer> customersForWhichUserIsAReviewer = customerService.findAllCustomersForWhichUserIsaReviewer(user);
		DateRange monthRange = DateUtil.calendarToMonthRange(overviewFor);
		
		List<Activity> allActivitiesForcustomers = activityService.getAllActivitiesForcustomers(customersForWhichUserIsAReviewer, monthRange);

		return allActivitiesForcustomers;
	}

	/**
     * Handle Ajax request
     */
	@Override
	public Boolean ajaxEventReceived(AjaxEvent ajaxEvent) {
		AjaxEventType type = ajaxEvent.getEventType();
		AjaxRequestTarget target = ajaxEvent.getTarget();

		if (type == CalendarAjaxEventType.MONTH_CHANGE) {
			calendarChanged(target);
		}
		return false;
	}

	private void calendarChanged(AjaxRequestTarget target) {
		EhourWebSession session = ((EhourWebSession) this.getSession());
		Calendar overviewFor = session.getNavCalendar();
		overviewFor.set(Calendar.DAY_OF_MONTH, 1);
		customerReviewerPanel = new CustomerReviewerPanel("customerReviewerPanel", overviewFor, getActivitiesForCustomersForWhichUserIsAReviewer());
		addOrReplaceContentContainer(customerReviewerPanel, target);
	}
	
	private void addOrReplaceContentContainer(WebMarkupContainer contentContainer)
    {
        contentContainer.setOutputMarkupId(true);
        greyBorder.addOrReplace(contentContainer);
    }

    private void addOrReplaceContentContainer(WebMarkupContainer contentContainer, AjaxRequestTarget target)
    {
    	addOrReplaceContentContainer(contentContainer);
        target.add(contentContainer);
    }

}
