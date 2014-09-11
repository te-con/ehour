package net.rrm.ehour.ui.customerreviewer;

import com.google.common.collect.Lists;
import java.util.Calendar;
import java.util.List;

import net.rrm.ehour.approvalstatus.service.ApprovalStatusService;
import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.ApprovalStatus;
import net.rrm.ehour.domain.ApprovalStatusType;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.panel.AbstractAjaxPanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.customerreviewer.model.CustomerReviewerDataProvider;
import net.rrm.ehour.ui.timesheet.page.UserOverviewPage;
import net.rrm.ehour.util.DateUtil;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
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
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
public class CustomerReviewerPanel extends AbstractAjaxPanel<ReportCriteria> {
	
	@SpringBean
	private ApprovalStatusService approvalStatusService;
	
	private WebMarkupContainer dataContainer;

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
		
		dataContainer = new WebMarkupContainer("dataContainer");
		dataContainer.setOutputMarkupId(true);

		final Form<Void> form = new Form<Void>("customerReviewerForm");
		
		overviewFor.set(Calendar.DAY_OF_MONTH, 1);
		final DateRange monthRange = DateUtil.calendarToMonthRange(overviewFor);

		@SuppressWarnings("unchecked")
        List<IColumn<Activity, Date>> columns = Lists.newArrayList();
        columns.add(new PropertyColumn<Activity, Date>(new ResourceModel("customerreviewer.timesheet.column.customer"), "project.customer.name");
        columns.add(new PropertyColumn<Activity, Date>(new ResourceModel("customerreviewer.timesheet.column.user"), "assignedUser.firstName");
        columns.add(new DateColumn(new ResourceModel("customerreviewer.timesheet.column.period"), getConfig());
        columns.add(new AbstractColumn<Activity, Date>(new Model<String>("Status")) {
			@Override
			public void populateItem(Item<ICellPopulator<Activity>> cellItem, String componentId, IModel<Activity> rowModel) {
				cellItem.add(new Label(componentId, getStatus(rowModel)));
				
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

		});
        columns.add(new AbstractColumn<Activity, Date>(new Model<String>("view")) {
            @Override
            public void populateItem(Item<ICellPopulator<Activity>> cellItem, String componentId, IModel<Activity> rowModel) {
                cellItem.add(new ViewTimesheetPanel(componentId, rowModel));
            }
        });
        columns.add(new AbstractColumn<Activity, Date>(new Model<String>("approve")) {
            @Override
            public void populateItem(Item<ICellPopulator<Activity>> cellItem, String componentId, IModel<Activity> rowModel) {
                AcceptPanel acceptPanel = new AcceptPanel(componentId, rowModel);
                Activity activity = rowModel.getObject();
                ApprovalStatus approvalStatusForActivity = findApprovalStatusForActivity(activity);
                if (approvalStatusForActivity != null && (approvalStatusForActivity.getStatus().equals(ApprovalStatusType.APPROVED) || approvalStatusForActivity.getStatus().equals(ApprovalStatusType.IN_PROGRESS))) {
                    acceptPanel.setEnabled(false);
                }
                if (approvalStatusForActivity == null) {
                    acceptPanel.setEnabled(false);
                }
                cellItem.add(acceptPanel);
            }
        });
        columns.add(new AbstractColumn<Activity, Date>(new Model<String>("reject")) {
			@SuppressWarnings({"rawtypes", "unchecked" })
			@Override
			public void populateItem(Item<ICellPopulator<Activity>> cellItem, String componentId, IModel<Activity> rowModel) {
				cellItem.add(new RejectPanel(componentId, rowModel));
			}
		};
		
		AjaxFallbackDefaultDataTable<Activity, Date> table = new AjaxFallbackDefaultDataTable<Activity, Date>("data", columns, new CustomerReviewerDataProvider(allActivitiesOfCustomerForMonth), 20);
		
		form.setOutputMarkupId(true);
		form.add(table);
		
		dataContainer.add(form);
		greyBorder.add(dataContainer);
	}

	protected ApprovalStatus findApprovalStatusForActivity(Activity activity) {
		ApprovalStatus appStatus = null;
		EhourWebSession session = EhourWebSession.getSession();
		Calendar overviewFor = session.getNavCalendar();
		overviewFor.set(Calendar.DAY_OF_MONTH, 1);
		DateRange monthRange = DateUtil.calendarToMonthRange(overviewFor);
    	List<ApprovalStatus> approvalStatusesForActivity = approvalStatusService.getApprovalStatusForActivity(activity, monthRange);
    	if(approvalStatusesForActivity != null && approvalStatusesForActivity.size()!=0) {
    		appStatus = approvalStatusesForActivity.iterator().next();
    	}
		return appStatus;
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
		
		public RejectPanel(String id, final IModel<Activity> model) {
			super(id, model);
			final ModalWindow rejectTimesheetModalWindow;
			rejectTimesheetModalWindow = new ModalWindow("rejectTimesheetModalWindow");
			
			rejectTimesheetModalWindow.setContent(new RejectTimesheetContentPanel(rejectTimesheetModalWindow.getContentId(), CustomerReviewerPanel.this, rejectTimesheetModalWindow, model));
			
			rejectTimesheetModalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
				public void onClose(AjaxRequestTarget target) {
					target.add(dataContainer);
				}
			});
			rejectTimesheetModalWindow.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
				public boolean onCloseButtonClicked(AjaxRequestTarget target) {
					return true;
				}
			});
			
			add(rejectTimesheetModalWindow);
			
			AjaxLink<Activity> rejectLink = new AjaxLink<Activity>("reject") {
				@Override
				public void onClick(AjaxRequestTarget target) {
					rejectTimesheetModalWindow.show(target);
				}
			};
			
			add(rejectLink);
		}
	}
	
	class AcceptPanel extends Panel {
		public AcceptPanel(String id, final IModel<Activity> model) {
			super(id, model);
			add(new AjaxLink<Activity>("accept") {
				@Override
				public void onClick(AjaxRequestTarget target) {
					Activity activity = model.getObject();
					ApprovalStatus statusForActivity = findApprovalStatusForActivity(activity);
					if (statusForActivity != null) {
						statusForActivity.setStatus(ApprovalStatusType.APPROVED);
						approvalStatusService.persist(statusForActivity);
						target.add(dataContainer);
					}
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
					 UserOverviewPage userOverviewPage = new UserOverviewPage(activity.getAssignedUser());
					 setResponsePage(userOverviewPage);
				}
			});
		}
	}
	
}