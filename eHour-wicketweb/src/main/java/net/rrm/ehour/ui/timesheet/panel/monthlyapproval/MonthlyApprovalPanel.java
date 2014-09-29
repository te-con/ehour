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
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.*;

/**
 * Monthly Approval Panel For Users
 */

public class MonthlyApprovalPanel extends Panel {

    private static final long serialVersionUID = 7854272569953832030L;

    private CustomTitledGreyRoundedBorder greyBorder;

    private static final String APPROVALSTATUS_RESOURCEKEY_PREFIX = "monthlyTimeSheet.approvalStatus.";
    private static final String ACTION_ID = "action";

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

        addPanelComponents(userProjectStatuses);
    }

    private void addPanelComponents(SortedSet<UserProjectStatus> userProjectStatuses) {

        greyBorder = new CustomTitledGreyRoundedBorder("greyBorder", new Label(CustomTitledGreyRoundedBorder.TITLE_ID, new ResourceModel("monthlyapproval.title")));
        greyBorder.setOutputMarkupId(true);

        List<Customer> customers = getAllCustomersForWhichThisUserWorks(userProjectStatuses);

        ListView<Customer> customerListView = new ListView<Customer>("monthlyApprovalStatus", customers) {
            private static final long serialVersionUID = 9021357186561428730L;

            @SuppressWarnings("unchecked")
            @Override
            protected void populateItem(final ListItem<Customer> item) {
                Customer customer = item.getModelObject();

                Label customerLabel = new Label("customerName", customer.getName());
                item.add(customerLabel);

                List<ApprovalStatus> statuses = approvalStatusService.getApprovalStatusForUserWorkingForCustomer(user, customer, monthRange);

                String currentApprovalStatus = statuses.isEmpty() ? "in_progress" : statuses.get(0).getStatus().toString();

                String currentApprovalStatusResourceBundleKey = APPROVALSTATUS_RESOURCEKEY_PREFIX + currentApprovalStatus.toLowerCase();

                final Label approvalStatusLabel = new Label("approvalStatus", new ResourceModel(currentApprovalStatusResourceBundleKey));
                item.add(approvalStatusLabel);
                approvalStatusLabel.setOutputMarkupId(true);

                item.add(isActionVisible(customer) ? createRequestApprovalFragment(item) : new Fragment(ACTION_ID, "noAction", this));
            }

        };
        greyBorder.add(customerListView);
        add(greyBorder);
    }

    private Fragment createRequestApprovalFragment(final ListItem<Customer> item) {
        Fragment requestApprovalFragment = new Fragment(ACTION_ID, "requestApprovalLink", this);

        AjaxLink<Void> requestApprovalLink = new AjaxLink<Void>("monthlyApproveLink") {
            private static final long serialVersionUID = -3337416577258168873L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                target.add(this);
                target.add(greyBorder);
                Customer customer = item.getModelObject();
                approvalStatusService.markReadyForApproval(user, customer, monthRange);

                setVisible(isActionVisible(item.getModelObject()));
                target.add(this);
            }
        };

        requestApprovalLink.setOutputMarkupPlaceholderTag(true);

        requestApprovalFragment.add(requestApprovalLink);
        return requestApprovalFragment;
    }

    private boolean isActionVisible(Customer customer) {
        List<ApprovalStatus> approvalStatusForCustomer = approvalStatusService.getApprovalStatusForUserWorkingForCustomer(this.user,
                customer, this.monthRange);
        Iterator<ApprovalStatus> iterator = approvalStatusForCustomer.iterator();

        ApprovalStatusType approvalStatusType = iterator.hasNext() ? iterator.next().getStatus() : ApprovalStatusType.IN_PROGRESS;

        return (approvalStatusType.equals(ApprovalStatusType.IN_PROGRESS) || approvalStatusType.equals(ApprovalStatusType.REJECTED));
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
