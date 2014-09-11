package net.rrm.ehour.ui.customerreviewer;

import com.google.common.collect.Lists;
import java.util.Calendar;
import java.util.List;

import net.rrm.ehour.approvalstatus.service.ApprovalStatusService;
import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.ApprovalStatus;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.panel.AbstractAjaxPanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.customerreviewer.model.CustomerReviewerDataProvider;
import net.rrm.ehour.ui.timesheet.page.UserOverviewPage;
import net.rrm.ehour.util.DateUtil;

import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
public class CustomerReviewerPanel extends AbstractAjaxPanel<ReportCriteria> {
	
	@SpringBean
	private ApprovalStatusService approvalStatusService;

	public CustomerReviewerPanel(String id, List<Activity> allActivitiesOfCustomerForMonth) {
		super(id);

		addAllComponents(allActivitiesOfCustomerForMonth);

		setOutputMarkupId(true);
	}

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(CssHeaderItem.forReference(new CssResourceReference(CustomerReviewerPanel.class, "style/customerReviewerStyle.css")));
    }

	private void addAllComponents(List<Activity> allActivitiesOfCustomerForMonth) {
		Border greyBorder = new GreyBlueRoundedBorder("border");
		add(greyBorder);

		final Form<Void> form = new Form<Void>("customerReviewerForm");
		
		overviewFor.set(Calendar.DAY_OF_MONTH, 1);
		final DateRange monthRange = DateUtil.calendarToMonthRange(overviewFor);

		@SuppressWarnings("unchecked")
        List<IColumn<Activity, Date>> columns = Lists.newArrayList();
        columns.add(new PropertyColumn<Activity>(new ResourceModel("customerreviewer.timesheet.column.customer"), "project.customer.name");
        columns.add(new PropertyColumn<Activity>(new ResourceModel("customerreviewer.timesheet.column.user"), "assignedUser.firstName");
        columns.add(new DateColumn(new ResourceModel("customerreviewer.timesheet.column.period"), getConfig());
		columns[3] = new AbstractColumn<Activity>(new Model<String>("Status")) {
			@SuppressWarnings({"unchecked"})
			@Override
			public void populateItem(final Item item, String compId, final IModel model) {
				item.add(new Label(compId, getStatus(model)));
			}

			private String getStatus(IModel<Activity> model) {
				String result = null;
				Activity activity = model.getObject();
				List<ApprovalStatus> approvalStatusForCurrentMonth = approvalStatusService.getApprovalStatusForActivity(activity, monthRange);
				if(approvalStatusForCurrentMonth.size() == 1) {
					result = approvalStatusForCurrentMonth.get(0).getStatus().toString();
				} else {
					// Ideally should never come here except when we have no time-sheet for this Activity in the month
				}
				return result;
			}
		};
		columns[4] = new AbstractColumn<Activity>(new Model<String>("view")) {
			@SuppressWarnings({"rawtypes", "unchecked"})
			@Override
			public void populateItem(final Item item, String compId, final IModel model) {
				item.add(new ViewTimesheetPanel(compId, model));
			}
		};
		columns[5] = new AbstractColumn<Activity>(new Model<String>("approve")) {
			@SuppressWarnings({"rawtypes", "unchecked"})
			@Override
			public void populateItem(final Item item, String compId, final IModel model) {
				item.add(new AcceptPanel(compId, model));
			}
		};

		columns[6] = new AbstractColumn<Activity>(new Model<String>("reject")) {
			@SuppressWarnings({"rawtypes", "unchecked" })
        columns.add(new AbstractColumn<Activity, Date>(new Model<String>("reject")) {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public void populateItem(final Item item, String compId, final IModel model) {
				item.add(new RejectPanel(compId, model));
			}
		});


		AjaxFallbackDefaultDataTable<Activity, Date> table = new AjaxFallbackDefaultDataTable<Activity, Date>("data", columns,
				new CustomerReviewerDataProvider(allActivitiesOfCustomerForMonth), 20);

		form.setOutputMarkupId(true);
		form.add(table);

		final WebMarkupContainer dataContainer = new WebMarkupContainer("dataContainer");
		dataContainer.setOutputMarkupId(true);
		dataContainer.add(form);
		greyBorder.add(dataContainer);
	}

	private class DateColumn extends AbstractColumn<Activity, Date> {
		private EhourConfig config;

		public DateColumn(IModel<String> displayModel, EhourConfig config) {
			super(displayModel);
			this.config = config;
		}

		@Override
		public void populateItem(Item<ICellPopulator<Activity>> cellItem, String componentId, IModel<Activity> rowModel) {
			EhourWebSession session = EhourWebSession.getSession();
			Calendar overviewFor = session.getNavCalendar();
			overviewFor.set(Calendar.DAY_OF_MONTH, 1);
			cellItem.add(new Label(componentId, new DateModel(overviewFor, config, DateModel.DATESTYLE_MONTHONLY)));
		}

	}

	class RejectPanel extends Panel {
		
		public RejectPanel(String id, IModel<Activity> model) {
			super(id, model);
			add(new Link<Activity>("reject") {
				@Override
				public void onClick() {
				}
			});
		}
	}
	
	class AcceptPanel extends Panel {
		
		public AcceptPanel(String id, IModel<Activity> model) {
			super(id, model);
			add(new Link<Activity>("accept") {
				@Override
				public void onClick() {
				}
			});
		}
	}	
	
	class ViewTimesheetPanel extends Panel {

		public ViewTimesheetPanel(String id, IModel<Activity> model) {
			super(id, model);
			final Activity activity = model.getObject();
			add(new Link<Activity>("view") {
				@Override
				public void onClick() {
					 UserOverviewPage userOverviewPage = new
					 UserOverviewPage(activity.getAssignedUser());
					 setResponsePage(userOverviewPage);
				}
			});
		}
	}
	
}