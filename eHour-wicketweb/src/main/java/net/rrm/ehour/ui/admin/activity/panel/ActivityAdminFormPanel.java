package net.rrm.ehour.ui.admin.activity.panel;

import java.util.Date;
import java.util.List;

import net.rrm.ehour.activity.service.ActivityService;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.ui.admin.activity.dto.ActivityBackingBean;
import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.form.FormConfig;
import net.rrm.ehour.ui.common.form.FormUtil;
import net.rrm.ehour.ui.common.model.AdminBackingBean;
import net.rrm.ehour.ui.common.panel.AbstractFormSubmittingPanel;
import net.rrm.ehour.ui.common.panel.datepicker.LocalizedDatePicker;
import net.rrm.ehour.ui.common.session.EhourWebSession;

import net.rrm.ehour.ui.common.util.WebGeo;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class ActivityAdminFormPanel extends AbstractFormSubmittingPanel<ActivityBackingBean> {

	private static final long serialVersionUID = 4748507989744436489L;

	@SpringBean
	private ActivityService	activityService;

	public ActivityAdminFormPanel(String id, CompoundPropertyModel<ActivityBackingBean> activityModel, List<User> users,
			List<Project> projects) {
		super(id, activityModel);
		GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder("border", WebGeo.AUTO);
		add(greyBorder);

		setOutputMarkupId(true);

		final Form<Void> form = new Form<Void>("activityForm");

		TextField<String> nameField = new TextField<String>("activity.name");
		form.add(nameField);

        form.add(new LocalizedDatePicker("startDateField", new PropertyModel<Date>(activityModel, "activity.dateStart")));
        form.add(new LocalizedDatePicker("endDateField", new PropertyModel<Date>(activityModel, "activity.dateEnd")));

		TextField<Float> allottedHoursField = new TextField<Float>("activity.allottedHours");
		form.add(allottedHoursField);

		DropDownChoice<User> usersDropDownList = new DropDownChoice<User>("activity.assignedUser", users, new ChoiceRenderer<User>("fullName"));
		usersDropDownList.setRequired(true);
		usersDropDownList.setLabel(new ResourceModel("admin.activity.user"));
		form.add(usersDropDownList);

		DropDownChoice<Project> projectsDropDownList = new DropDownChoice<Project>("activity.project", projects, new ChoiceRenderer<Project>("fullName"));
		projectsDropDownList.setRequired(true);
		projectsDropDownList.setLabel(new ResourceModel("admin.activity.project"));
		form.add(projectsDropDownList);
		
		form.add(new CheckBox("activity.active"));

        final FormConfig formConfig = new FormConfig(form);
        formConfig.withDeleteEventType(ActivityEditAjaxEventType.ACTIVITY_DELETED);
        formConfig.withDelete(true);

        formConfig.withSubmitEventType(ActivityEditAjaxEventType.ACTIVITY_UPDATED);
        formConfig.withSubmitTarget(this);

        FormUtil.setSubmitActions(formConfig);
//        FormUtil.setSubmitActions(form, true, this, ActivityEditAjaxEventType.ACTIVITY_UPDATED, ActivityEditAjaxEventType.ACTIVITY_DELETED,
//				((EhourWebSession) getSession()).getEhourConfig());

		greyBorder.add(form);
	}

	@Override
	protected boolean processFormSubmit(AjaxRequestTarget target, AdminBackingBean backingBean, AjaxEventType type) throws Exception {
		ActivityBackingBean activityBackingBean = (ActivityBackingBean) backingBean;
		if (type == ActivityEditAjaxEventType.ACTIVITY_UPDATED) {
			persistActivity(activityBackingBean);

		} else if (type == ActivityEditAjaxEventType.ACTIVITY_DELETED) {
			removeActivity(activityBackingBean);

		}
        return true;
    }

	private void removeActivity(ActivityBackingBean activityBackingBean) {
		activityService.deleteActivity(activityBackingBean.getActivity().getId());
	}

	private void persistActivity(ActivityBackingBean activityBackingBean) {
		activityService.persistActivity(activityBackingBean.getActivity());
	}

}
