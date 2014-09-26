package net.rrm.ehour.ui.customerreviewer;

import net.rrm.ehour.approvalstatus.service.ApprovalStatusService;
import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.domain.ApprovalStatus;
import net.rrm.ehour.domain.ApprovalStatusType;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.panel.AbstractAjaxPanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.customerreviewer.model.CustomerReviewerDataProvider;
import net.rrm.ehour.ui.timesheet.page.UserModerationPage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
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
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Calendar;
import java.util.List;

@SuppressWarnings("serial")
public class CustomerReviewerPanel extends AbstractAjaxPanel<ReportCriteria> {

	private static final String APPROVALSTATUS_RESOURCEKEY_PREFIX = "monthlyTimeSheet.approvalStatus.";
	
	@SpringBean
	private ApprovalStatusService approvalStatusService;

	private WebMarkupContainer dataContainer;

	public CustomerReviewerPanel(String id, Calendar overviewFor, List<ApprovalStatus> approvalStatuses) {
		super(id);

		addAllComponents(approvalStatuses);
		add(new StyleSheetReference("customerReviewerStyle", new CompressedResourceReference(CustomerReviewerPanel.class,
				"style/customerReviewerStyle.css")));
		setOutputMarkupId(true);
	}

	private void addAllComponents(List<ApprovalStatus> approvalStatuses) {
		Border greyBorder = new GreyBlueRoundedBorder("border");
		add(greyBorder);

		dataContainer = new WebMarkupContainer("dataContainer");
		dataContainer.setOutputMarkupId(true);

		final Form<Void> form = new Form<Void>("customerReviewerForm");

		@SuppressWarnings("unchecked")
		IColumn<ApprovalStatus>[] columns = new IColumn[7];
		columns[0] = new PropertyColumn<ApprovalStatus>(new ResourceModel("customerreviewer.timesheet.column.customer"), "customer.name");
		columns[1] = new PropertyColumn<ApprovalStatus>(new ResourceModel("customerreviewer.timesheet.column.user"), "user.fullName");
		columns[2] = new DateColumn(new ResourceModel("customerreviewer.timesheet.column.period"), getConfig());
		columns[3] = new AbstractColumn<ApprovalStatus>(new Model<String>("Status")) {
			@Override
			public void populateItem(Item<ICellPopulator<ApprovalStatus>> cellItem, String componentId, IModel<ApprovalStatus> rowModel) {
				cellItem.add(new Label(componentId, getCurrentStatusAsResourceModel(rowModel)));

			}

			private ResourceModel getCurrentStatusAsResourceModel(IModel<ApprovalStatus> model) {
				String currentApprovalStatusResourceBundleKey = APPROVALSTATUS_RESOURCEKEY_PREFIX + model.getObject().getStatus().toString().toLowerCase();
				return new ResourceModel(currentApprovalStatusResourceBundleKey);
			}

		};
		columns[4] = new AbstractColumn<ApprovalStatus>(new Model<String>("view")) {
			@Override
			public void populateItem(Item<ICellPopulator<ApprovalStatus>> cellItem, String componentId, IModel<ApprovalStatus> rowModel) {
				cellItem.add(new ViewTimesheetPanel(componentId, rowModel));
			}
		};
		columns[5] = new AbstractColumn<ApprovalStatus>(new Model<String>("approve")) {
			@Override
			public void populateItem(Item<ICellPopulator<ApprovalStatus>> cellItem, String componentId, IModel<ApprovalStatus> rowModel) {
				AcceptPanel acceptPanel = new AcceptPanel(componentId, rowModel);
				
				ApprovalStatus approvalStatus = rowModel.getObject();
				
				if ((approvalStatus.getStatus().equals(ApprovalStatusType.APPROVED) || approvalStatus.getStatus().equals(ApprovalStatusType.IN_PROGRESS))) {
					acceptPanel.setEnabled(false);
				}
				cellItem.add(acceptPanel);
			}
		};
		columns[6] = new AbstractColumn<ApprovalStatus>(new Model<String>("reject")) {
			@Override
			public void populateItem(Item<ICellPopulator<ApprovalStatus>> cellItem, String componentId, IModel<ApprovalStatus> rowModel) {
				cellItem.add(new RejectPanel(componentId, rowModel));
			}
		};

		AjaxFallbackDefaultDataTable<ApprovalStatus> table = new AjaxFallbackDefaultDataTable<ApprovalStatus>("data", columns,
				new CustomerReviewerDataProvider(approvalStatuses), 20);

		form.setOutputMarkupId(true);
		form.add(table);

		dataContainer.add(form);
		greyBorder.add(dataContainer);
	}


	private class DateColumn extends AbstractColumn<ApprovalStatus> {
		private EhourConfig config;

		public DateColumn(IModel<String> displayModel, EhourConfig config) {
			super(displayModel);
			this.config = config;
		}

		@Override
		public void populateItem(Item<ICellPopulator<ApprovalStatus>> cellItem, String componentId, IModel<ApprovalStatus> rowModel) {
			EhourWebSession session = EhourWebSession.getSession();
			Calendar overviewFor = session.getNavCalendar();
			overviewFor.set(Calendar.DAY_OF_MONTH, 1);
			cellItem.add(new Label(componentId, new DateModel(overviewFor, config, DateModel.DATESTYLE_MONTHONLY)));
		}

	}

	class RejectPanel extends Panel {

		public RejectPanel(String id, final IModel<ApprovalStatus> model) {
			super(id, model);
			final ModalWindow rejectTimesheetModalWindow;

			rejectTimesheetModalWindow = new ModalWindow("rejectTimesheetModalWindow");
			rejectTimesheetModalWindow.setTitle(new StringResourceModel("timesheet.dayCommentsTitle", this, null));
			rejectTimesheetModalWindow.setResizable(false);
			rejectTimesheetModalWindow.setInitialWidth(400);
			rejectTimesheetModalWindow.setInitialHeight(225);

			rejectTimesheetModalWindow.setContent(new RejectTimesheetContentPanel(rejectTimesheetModalWindow.getContentId(),
					CustomerReviewerPanel.this, rejectTimesheetModalWindow, model));

			rejectTimesheetModalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
				public void onClose(AjaxRequestTarget target) {
					target.addComponent(dataContainer);
				}
			});
			rejectTimesheetModalWindow.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
				public boolean onCloseButtonClicked(AjaxRequestTarget target) {
					return true;
				}
			});

			add(rejectTimesheetModalWindow);

			AjaxLink<ApprovalStatus> rejectLink = new AjaxLink<ApprovalStatus>("reject") {
				@Override
				public void onClick(AjaxRequestTarget target) {
					rejectTimesheetModalWindow.show(target);
				}
			};

			add(rejectLink);
		}
	}

	class AcceptPanel extends Panel {
		public AcceptPanel(String id, final IModel<ApprovalStatus> model) {
			super(id, model);
			add(new AjaxLink<ApprovalStatus>("accept") {
				@Override
				public void onClick(AjaxRequestTarget target) {
					ApprovalStatus approvalStatus = model.getObject();
					if (approvalStatus != null) {
						approvalStatus.setStatus(ApprovalStatusType.APPROVED);
						approvalStatusService.persist(approvalStatus);
						target.addComponent(dataContainer);
					}
				}
			});
		}
	}

	class ViewTimesheetPanel extends Panel {
		public ViewTimesheetPanel(String id, IModel<ApprovalStatus> model) {
			super(id, model);
			final ApprovalStatus approvalStatus = model.getObject();
			add(new Link<ApprovalStatus>("view") {
				@Override
				public void onClick() {
					UserModerationPage userModerationPage = new UserModerationPage(approvalStatus.getUser());
					setResponsePage(userModerationPage);
				}
			});
		}
	}

}