package net.rrm.ehour.ui.customerreviewer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.timesheet.dto.TimesheetOverview;
import net.rrm.ehour.timesheet.dto.UserProjectStatus;
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.panel.AbstractAjaxPanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.customerreviewer.model.CustomerReviewerDataProvider;
import net.rrm.ehour.ui.timesheet.page.UserOverviewPage;

import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.markup.html.resources.StyleSheetReference;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

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
		add(new StyleSheetReference("customerReviewerStyle", new CompressedResourceReference(CustomerReviewerPanel.class,
				"style/customerReviewerStyle.css")));
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

		final Form<Void> form = new Form<Void>("customerReviewerForm");

		@SuppressWarnings("unchecked")
		IColumn<UserProjectStatus>[] columns = new IColumn[6];
		columns[0] = new PropertyColumn<UserProjectStatus>(new ResourceModel("customerreviewer.timesheet.column.customer"),
				"activity.project.customer.name");
		columns[1] = new PropertyColumn<UserProjectStatus>(new ResourceModel("customerreviewer.timesheet.column.user"),
				"activity.assignedUser.firstName");
		columns[2] = new DateColumn(new ResourceModel("customerreviewer.timesheet.column.period"), getConfig());
		columns[3] = new AbstractColumn<UserProjectStatus>(new Model<String>("view")) {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public void populateItem(final Item item, String compId, final IModel model) {
				item.add(new ViewTimesheetPanel(compId, model));
			}
		};
		columns[4] = new AbstractColumn<UserProjectStatus>(new Model<String>("approve")) {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public void populateItem(final Item item, String compId, final IModel model) {
				item.add(new AcceptPanel(compId, model));
			}
		};

		columns[5] = new AbstractColumn<UserProjectStatus>(new Model<String>("reject")) {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public void populateItem(final Item item, String compId, final IModel model) {
				item.add(new RejectPanel(compId, model));
			}
		};

		AjaxFallbackDefaultDataTable<UserProjectStatus> table = new AjaxFallbackDefaultDataTable<UserProjectStatus>("data", columns,
				new CustomerReviewerDataProvider(statusses), 20);

		form.setOutputMarkupId(true);
		form.add(table);

		final WebMarkupContainer dataContainer = new WebMarkupContainer("dataContainer");
		dataContainer.setOutputMarkupId(true);
		dataContainer.add(form);
		greyBorder.add(dataContainer);
	}

	private class DateColumn extends AbstractColumn<UserProjectStatus> {
		private EhourConfig config;

		public DateColumn(IModel<String> displayModel, EhourConfig config) {
			super(displayModel);
			this.config = config;
		}

		@Override
		public void populateItem(Item<ICellPopulator<UserProjectStatus>> cellItem, String componentId, IModel<UserProjectStatus> rowModel) {
			EhourWebSession session = EhourWebSession.getSession();
			Calendar overviewFor = session.getNavCalendar();
			overviewFor.set(Calendar.DAY_OF_MONTH, 1);
			cellItem.add(new Label(componentId, new DateModel(overviewFor, config, DateModel.DATESTYLE_MONTHONLY)));
		}

	}

	class RejectPanel extends Panel {
		
		public RejectPanel(String id, IModel<UserProjectStatus> model) {
			super(id, model);
			add(new Link("reject") {
				@Override
				public void onClick() {
				}
			});
		}
	}
	
	class AcceptPanel extends Panel {
		
		public AcceptPanel(String id, IModel<UserProjectStatus> model) {
			super(id, model);
			add(new Link("accept") {
				@Override
				public void onClick() {
				}
			});
		}
	}
	
	class ViewTimesheetPanel extends Panel {

		public ViewTimesheetPanel(String id, IModel<UserProjectStatus> model) {
			super(id, model);
			final UserProjectStatus userProjectStatus = (UserProjectStatus) model.getObject();
			add(new Link("view") {
				@Override
				public void onClick() {
					 UserOverviewPage userOverviewPage = new
					 UserOverviewPage(userProjectStatus.getActivity().getAssignedUser());
					 setResponsePage(userOverviewPage);
				}
			});
		}
	}
	
}