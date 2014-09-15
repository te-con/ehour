package net.rrm.ehour.ui.timesheet.panel.monthlyapproval;

import net.rrm.ehour.approvalstatus.service.ApprovalStatusService;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ApprovalStatus;
import net.rrm.ehour.domain.ApprovalStatusType;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.timesheet.dto.UserProjectStatus;
import net.rrm.ehour.ui.common.border.CustomTitledGreyRoundedBorder;
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

import java.util.*;

/**
 * Monthly Approval Panel For Users
 **/

public class MonthlyApprovalPanel extends Panel {

	private static final long serialVersionUID = 7854272569953832030L;

	private CustomTitledGreyRoundedBorder greyBorder;

	private static final String APPROVALSTATUS_RESOURCEKEY_PREFIX = "monthlyTimeSheet.approvalStatus.";

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

		greyBorder = new CustomTitledGreyRoundedBorder("greyBorder", new Label("title", new ResourceModel("monthlyapproval.title")));
        greyBorder.setOutputMarkupId(true);

		List<Customer> customers = getAllCustomersForWhichThisUserWorks(userProjectStatuses);

		ListView<Customer> customerListView = new ListView<Customer>("monthlyApprovalStatus", customers) {
			private static final long serialVersionUID = 9021357186561428730L;

			@Override
			protected void populateItem(final ListItem<Customer> item) {
				Customer customer = item.getModelObject();

				Label customerLabel = new Label("customerName", customer.getName());
				item.add(customerLabel);

				String currentApprovalStatus = approvalStatusService.getApprovalStatusForUserWorkingForCustomer(user, customer, monthRange)
						.get(0).getStatus().toString();
				String currentApprovalStatusResourceBundleKey = APPROVALSTATUS_RESOURCEKEY_PREFIX + currentApprovalStatus.toLowerCase();
				
				final Label approvalStatusLabel = new Label("approvalStatus", new ResourceModel(currentApprovalStatusResourceBundleKey));
				item.add(approvalStatusLabel);
				approvalStatusLabel.setOutputMarkupId(true);

				Form monthlyApprovalStatusForm = new Form<Customer>("monthlyApprovalStatusForm", item.getModel());

				AjaxLink<Void> requestApprovalLink = new AjaxLink<Void>("monthlyApproveLink") {
					private static final long serialVersionUID = -3337416577258168873L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						target.add(this);
						target.add(greyBorder);
						Customer customer = item.getModelObject();
						changeApprovalStatus(customer);
						setVisibilityOfApprovalLinkAndUpdateStatus(customer, this);
					}

					private void changeApprovalStatus(Customer customer) {
						List<ApprovalStatus> currentApprovalStatus = approvalStatusService.getApprovalStatusForUserWorkingForCustomer(user,
								customer, monthRange);
						// TODO Can be handled in better way.
						// Should get only element in the list
						ApprovalStatus approvalStatus = currentApprovalStatus.get(0);
						approvalStatus.setStatus(ApprovalStatusType.READY_FOR_APPROVAL);
						approvalStatusService.persist(approvalStatus);
					}
				};

				setVisibilityOfApprovalLinkAndUpdateStatus(item.getModelObject(), requestApprovalLink);

				monthlyApprovalStatusForm.add(requestApprovalLink);
				item.add(monthlyApprovalStatusForm);
			}
		};

		greyBorder.add(customerListView);
		add(greyBorder);
	}

	private void setVisibilityOfApprovalLinkAndUpdateStatus(Customer customer, AjaxLink<Void> requestApprovalLink) {
		List<ApprovalStatus> approvalStatusForCustomer = approvalStatusService.getApprovalStatusForUserWorkingForCustomer(this.user,
				customer, this.monthRange);
		ApprovalStatusType approvalStatusType = approvalStatusForCustomer.iterator().next().getStatus();

		if (approvalStatusType != null) {
            Label appprovalStatusLabel = new Label("approvalStatus", approvalStatusType.toString());
		}

		if ((approvalStatusType != null ? approvalStatusType.equals(ApprovalStatusType.IN_PROGRESS) : false) || approvalStatusType.equals(ApprovalStatusType.REJECTED)) {
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
