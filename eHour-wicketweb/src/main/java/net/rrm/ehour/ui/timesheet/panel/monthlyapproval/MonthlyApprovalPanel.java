package net.rrm.ehour.ui.timesheet.panel.monthlyapproval;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.rrm.ehour.activity.service.ActivityService;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.ui.common.border.CustomTitledGreyRoundedBorder;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.WebGeo;
import net.rrm.ehour.util.DateUtil;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Monthly Approval Panel For Users
 **/

public class MonthlyApprovalPanel extends Panel {
	private static final long serialVersionUID = 7854272569953832030L;

	@SpringBean
	private ActivityService activityService;

	public MonthlyApprovalPanel(String id) {
		super(id);
		setOutputMarkupId(true);
		addPanelComponents();
	}

	private void addPanelComponents() {
		
		CustomTitledGreyRoundedBorder greyBorder = new CustomTitledGreyRoundedBorder("greyBorder", new Label("title", "status"), WebGeo.W_CONTENT_MEDIUM);
		
		List<Customer> customers = getAllCustomersForWhichThisUserWorks();
		
		ListView<Customer> customerListView = new ListView<Customer>("monthlyApprovalStatus", customers) {
			private static final long serialVersionUID = 9021357186561428730L;

			@Override
			protected void populateItem(final ListItem<Customer> item) {
				Customer customer = item.getModelObject();
				Label customerLabel = new Label("customerName", customer.getName());
				item.add(customerLabel);
				Form monthlyApprovalStatusForm = new Form("monthlyApprovalStatusForm", item.getDefaultModel());
				
				AjaxLink<Void> requestApprovalLink = new AjaxLink<Void>("monthlyApproveLink") {
					private static final long serialVersionUID = -3337416577258168873L;
					@Override
					public void onClick(AjaxRequestTarget target) {
						target.addComponent(this);
						Customer customer = item.getModelObject();
						System.out.println("############# Reached here for Customer : " + customer.getName());
					}

				};
				monthlyApprovalStatusForm.add(requestApprovalLink);
				item.add(monthlyApprovalStatusForm);
			}
		};
		
		greyBorder.add(customerListView);
		add(greyBorder);
	}

	private List<Customer> getAllCustomersForWhichThisUserWorks() {
		Set<Customer> customers = new HashSet<Customer>();
		List<Customer> customerList = new ArrayList<Customer>();
		
		
		EhourWebSession session = ((EhourWebSession) this.getSession());
		User user = session.getUser().getUser();

		Calendar overviewFor = session.getNavCalendar();
		overviewFor.set(Calendar.DAY_OF_MONTH, 1);
		DateRange monthRange = DateUtil.calendarToMonthRange(overviewFor);
		List<Activity> activitiesForUser = activityService.getActivitiesForUser(user.getUserId(), monthRange);
		for (Activity activity : activitiesForUser) {
			customers.add(activity.getProject().getCustomer());
		}
		
		customerList.addAll(customers);
		
		return customerList;
	}
}
