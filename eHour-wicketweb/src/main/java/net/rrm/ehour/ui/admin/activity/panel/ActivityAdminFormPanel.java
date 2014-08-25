package net.rrm.ehour.ui.admin.activity.panel;

import java.util.List;

import net.rrm.ehour.activity.service.ActivityService;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.ui.admin.activity.dto.ActivityBackingBean;
import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.form.FormUtil;
import net.rrm.ehour.ui.common.model.AdminBackingBean;
import net.rrm.ehour.ui.common.panel.AbstractFormSubmittingPanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class ActivityAdminFormPanel extends AbstractFormSubmittingPanel<ActivityBackingBean> {
	private static final long serialVersionUID = 4748507989744436489L;
	private final static Logger logger = Logger.getLogger(ActivityAdminFormPanel.class);
	
	@SpringBean
	private ActivityService	activityService;

	public ActivityAdminFormPanel(String id, CompoundPropertyModel<ActivityBackingBean> activityModel, List<User> users,
			List<Project> projects) {
		super(id, activityModel);
		GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder("border");
		add(greyBorder);

		setOutputMarkupId(true);

		final Form<Void> form = new Form<Void>("activityForm");

		TextField<String> nameField = new TextField<String>("activity.name");
		form.add(nameField);

		FormUtil.setSubmitActions(form, true, this, ActivityEditAjaxEventType.ACTIVITY_UPDATED, ActivityEditAjaxEventType.ACTIVITY_DELETED,
				((EhourWebSession) getSession()).getEhourConfig());

		greyBorder.add(form);
	}

	@Override
	protected void processFormSubmit(AjaxRequestTarget target, AdminBackingBean backingBean, AjaxEventType type) throws Exception {
		ActivityBackingBean activityBackingBean = (ActivityBackingBean) backingBean;
		if (type == ActivityEditAjaxEventType.ACTIVITY_UPDATED) {
			persistActivity(activityBackingBean);

		} else if (type == ActivityEditAjaxEventType.ACTIVITY_DELETED) {
			removeActivity(activityBackingBean);

		}
	}

	private void removeActivity(ActivityBackingBean activityBackingBean) {
		logger.info("Removing Activity " + activityBackingBean.getActivity().getName());
		activityService.deleteActivity(activityBackingBean.getActivity().getId());
	}

	private void persistActivity(ActivityBackingBean activityBackingBean) {
		logger.info("Persisting Activity " + activityBackingBean.getActivity().getName());
		activityService.persistActivity(activityBackingBean.getActivity());
		
	}

}
