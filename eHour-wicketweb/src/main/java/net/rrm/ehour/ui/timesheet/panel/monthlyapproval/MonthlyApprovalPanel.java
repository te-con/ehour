package net.rrm.ehour.ui.timesheet.panel.monthlyapproval;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import net.rrm.ehour.approvalstatus.service.ApprovalStatusService;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ApprovalStatus;
import net.rrm.ehour.domain.ApprovalStatusType;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.timesheet.dto.UserProjectStatus;
import net.rrm.ehour.ui.common.border.CustomTitledGreyRoundedBorder;
import net.rrm.ehour.ui.common.util.WebGeo;
import net.rrm.ehour.util.DateUtil;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Monthly Approval Panel For Users
 **/

public class MonthlyApprovalPanel extends Panel {

	private static final long serialVersionUID = 7854272569953832030L;
	
	private CustomTitledGreyRoundedBorder greyBorder;

	@SpringBean
	private ApprovalStatusService approvalStatusService;

	private User user;

	private DateRange monthRange;

	public MonthlyApprovalPanel(String id, Calendar overviewFor, SortedSet<UserProjectStatus> userProjectStatuses, User user) {
		super(id);
		setOutputMarkupId(true);
		this.user = user;
		overviewFor.set(Calendar.DAY_OF_MONTH, 1);
		this.monthRange = DateUtil.calendarToMonthRange(overviewFor);

		addPanelComponents(overviewFor, userProjectStatuses);
	}

	private void addPanelComponents(Calendar overviewFor, SortedSet<UserProjectStatus> userProjectStatuses) {

		greyBorder = new CustomTitledGreyRoundedBorder("greyBorder", new Label("title", new ResourceModel("monthlyapproval.title")), WebGeo.W_CONTENT_MEDIUM);
		greyBorder.setOutputMarkupId(true);

		List<Customer> customers = getAllCustomersForWhichThisUserWorks(userProjectStatuses);

		ListView<Customer> customerListView = new ListView<Customer>("monthlyApprovalStatus", customers) {
			private static final long serialVersionUID = 9021357186561428730L;

			@Override
			protected void populateItem(final ListItem<Customer> item) {
				Customer customer = item.getModelObject();

				Label customerLabel = new Label("customerName", customer.getName());
				item.add(customerLabel);

				final Label approvalStatusLabel = new Label("approvalStatus", approvalStatusService
						.getApprovalStatusForUserWorkingForCustomer(user, customer, monthRange).get(0).getStatus().toString());
				item.add(approvalStatusLabel);
				approvalStatusLabel.setOutputMarkupId(true);

				Form monthlyApprovalStatusForm = new Form("monthlyApprovalStatusForm", item.getDefaultModel());

				AjaxLink<Void> requestApprovalLink = new AjaxLink<Void>("monthlyApproveLink") {
					private static final long serialVersionUID = -3337416577258168873L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						target.addComponent(this);
						target.addComponent(greyBorder);
						Customer customer = item.getModelObject();
						changeApprovalStatus(customer);
						setVisibilityOfApprovalLinkAndUpdateStatus(customer, this, approvalStatusLabel);
					}

					private void changeApprovalStatus(Customer customer) {
						List<ApprovalStatus> currentApprovalStatus = approvalStatusService.getApprovalStatusForUserWorkingForCustomer(user, customer, monthRange);
						// TODO Can be handled in better way.
						// Should get only element in the list
						ApprovalStatus approvalStatus = currentApprovalStatus.get(0);
						approvalStatus.setStatus(ApprovalStatusType.READY_FOR_APPROVAL);
						approvalStatusService.persist(approvalStatus);
					}
				};

				setVisibilityOfApprovalLinkAndUpdateStatus(item.getModelObject(), requestApprovalLink, approvalStatusLabel);

				monthlyApprovalStatusForm.add(requestApprovalLink);
				item.add(monthlyApprovalStatusForm);
			}
		};

		greyBorder.add(customerListView);
		add(greyBorder);
	}

	private void setVisibilityOfApprovalLinkAndUpdateStatus(Customer customer, AjaxLink<Void> requestApprovalLink, Label appprovalStatusLabel) {
		List<ApprovalStatus> approvalStatusForCustomer = approvalStatusService.getApprovalStatusForUserWorkingForCustomer(this.user, customer, this.monthRange);
		ApprovalStatusType approvalStatusType = approvalStatusForCustomer.iterator().next().getStatus();
		
		if(approvalStatusType != null) {
			appprovalStatusLabel = new Label("approvalStatus", approvalStatusType.toString());
		}

		if (approvalStatusType.equals(ApprovalStatusType.IN_PROGRESS) || approvalStatusType.equals(ApprovalStatusType.REJECTED)) {
			requestApprovalLink.setVisible(true);
		} else {
			requestApprovalLink.setVisible(false);
		}
	}

	private List<Customer> getAllCustomersForWhichThisUserWorks(SortedSet<UserProjectStatus> userProjectStatuses) {
		Set<Customer> customers = new HashSet<Customer>();
		List<Customer> customerList = new ArrayList<Customer>();

		for (UserProjectStatus userProjectStatus : userProjectStatuses) {
			customers.add(userProjectStatus.getActivity().getProject().getCustomer());
		}

		customerList.addAll(customers);

		return customerList;
	}
}
