package net.rrm.ehour.ui.customerreviewer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.timesheet.dto.TimesheetOverview;
import net.rrm.ehour.timesheet.dto.UserProjectStatus;
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.panel.AbstractAjaxPanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.timesheet.page.MonthOverviewPage;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;

@SuppressWarnings("serial")
public class CustomerReviewerPanel extends AbstractAjaxPanel<ReportCriteria> {

	public CustomerReviewerPanel(String id, Calendar overviewFor, List<TimesheetOverview> timesheetOverviews) {
		super(id);

		Set<SortedSet<UserProjectStatus>> setOfProjectstatuses = new HashSet<SortedSet<UserProjectStatus>>();

		for (TimesheetOverview timesheetOverview : timesheetOverviews) {
			SortedSet<UserProjectStatus> projectStatus = timesheetOverview.getProjectStatus();
			setOfProjectstatuses.add(projectStatus);
		}

		addAllComponents(overviewFor, setOfProjectstatuses);
		setOutputMarkupId(true);
	}

	private void addAllComponents(Calendar overviewFor, Set<SortedSet<UserProjectStatus>> setOfProjectstatuses) {
		List<UserProjectStatus> statusses = new ArrayList<UserProjectStatus>();

		if (setOfProjectstatuses != null) {
			for (SortedSet<UserProjectStatus> userProjectStatusSet : setOfProjectstatuses) {
				statusses.addAll(userProjectStatusSet);
			}
		}

		Border greyBorder = new GreyBlueRoundedBorder("border");
		add(greyBorder);

		DataView<UserProjectStatus> dataView = new DataView<UserProjectStatus>("customerUserTable",
				new ListDataProvider<UserProjectStatus>(statusses), 5) {
			
			@Override
			protected void populateItem(Item<UserProjectStatus> item) {
				EhourWebSession session = ((EhourWebSession)this.getSession());
				Calendar overviewFor = session.getNavCalendar();
				overviewFor.set(Calendar.DAY_OF_MONTH, 1);
				final UserProjectStatus userProjectStatus = (UserProjectStatus) item.getModelObject();
				Link<UserProjectStatus> link = new Link<UserProjectStatus>("viewTimesheet") {
					@Override
					public void onClick() {
						setResponsePage(MonthOverviewPage.class);
					}
				};
				Label customerLabel = new Label("customer", userProjectStatus.getActivity().getProject().getCustomer().getName());
				Label userLabel = new Label("user", userProjectStatus.getActivity().getAssignedUser().getFirstName());
				Label monthLabel = new Label("period", new DateModel(overviewFor, getConfig(), DateModel.DATESTYLE_MONTHONLY));
				item.add(customerLabel);
				item.add(userLabel);
				item.add(monthLabel);
				item.add(link);
			}
		};

		final WebMarkupContainer dataContainer = new WebMarkupContainer("dataContainer");
		dataContainer.setOutputMarkupId(true);
		dataContainer.add(dataView);
		dataContainer.add(new PagingNavigator("navigator", dataView));

		greyBorder.add(dataContainer);
	}
}
