package net.rrm.ehour.ui.customerreviewer;

import java.util.Calendar;
import java.util.List;

import net.rrm.ehour.approvalstatus.service.ApprovalStatusService;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.ApprovalStatus;
import net.rrm.ehour.domain.ApprovalStatusType;
import net.rrm.ehour.ui.common.component.KeepAliveTextArea;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.util.DateUtil;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.markup.html.resources.StyleSheetReference;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

@SuppressWarnings("serial")
public class RejectTimesheetContentPanel extends Panel {

	@SpringBean
	private ApprovalStatusService approvalStatusService;

	public RejectTimesheetContentPanel(String id, final CustomerReviewerPanel modalWindowPage, final ModalWindow window, final IModel<Activity> model) {
		super(id);
		final Activity activity = model.getObject();

		ApprovalStatus approvalStatus = findApprovalStatusForActivity(activity);
		
		final PropertyModel<String> commentModel = new PropertyModel<String>(approvalStatus, "comment");
		
        add(new StyleSheetReference("rejectTimesheetStyle", new CompressedResourceReference(RejectTimesheetContentPanel.class, "style/RejectTimesheetContentPage.css")));
        
        add(new Label("reviewerComments", "Reviewer Comment"));
		
		final KeepAliveTextArea textArea = new KeepAliveTextArea("comment", commentModel);

		
		textArea.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				// simple hack to get around IE's prob with nested forms in a modalwindow
			}
		});
		add(textArea);
        
		add(new AjaxLink<Void>("reject") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				if (modalWindowPage != null) {
					String comment = textArea.getModelObject();
					ApprovalStatus approvalStatus = findApprovalStatusForActivity(activity);
					approvalStatus.setStatus(ApprovalStatusType.REJECTED);
					approvalStatus.setComment(comment);
					approvalStatusService.persist(approvalStatus);
					window.close(target);
				}
			}
		});

		add(new AjaxLink<Void>("cancel") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				if (modalWindowPage != null) {
					window.close(target);
				}
			}
		});
	}

	protected ApprovalStatus findApprovalStatusForActivity(Activity activity) {
		ApprovalStatus appStatus = null;
		EhourWebSession session = EhourWebSession.getSession();
		Calendar overviewFor = session.getNavCalendar();
		overviewFor.set(Calendar.DAY_OF_MONTH, 1);
		DateRange monthRange = DateUtil.calendarToMonthRange(overviewFor);
		List<ApprovalStatus> approvalStatusesForActivity = approvalStatusService.getApprovalStatusForActivity(activity, monthRange);
		if (approvalStatusesForActivity != null && approvalStatusesForActivity.size() != 0) {
			appStatus = approvalStatusesForActivity.iterator().next();
		}
		return appStatus;
	}
}
