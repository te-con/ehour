package net.rrm.ehour.ui.admin.activity.page;

import com.google.common.collect.Lists;
import net.rrm.ehour.activity.service.ActivityService;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.ui.admin.activity.dto.ActivityBackingBean;
import net.rrm.ehour.ui.admin.activity.panel.ActivityAdminFormPanel;
import net.rrm.ehour.ui.admin.activity.panel.ActivityEditAjaxEventType;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.component.AddEditTabbedPanel;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorData;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorPanel;
import net.rrm.ehour.ui.manage.AbstractTabbedManagePage;
import net.rrm.ehour.user.service.UserService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ActivityAdmin extends AbstractTabbedManagePage<ActivityBackingBean> {

    @SpringBean
    private UserService userService;

    @SpringBean
    private ProjectService projectService;

    @SpringBean
    private ActivityService activityService;

    private List<Project> projects;

    private List<User> users;

    private EntrySelectorPanel selectorPanel;

    public ActivityAdmin() {
        super(new ResourceModel("admin.activity.title"),
                new ResourceModel("admin.activity.addActivity"),
                new ResourceModel("admin.activity.editActivity"),
                new ResourceModel("admin.activity.noEditActivitySelected"));

        List<Activity> activities;
        activities = getActivities();

        GreyRoundedBorder greyBorder = new GreyRoundedBorder("entrySelectorFrame", new ResourceModel("admin.activity.title"));
        add(greyBorder);

        EntrySelectorPanel.ClickHandler clickHandler = new EntrySelectorPanel.ClickHandler() {

            @Override
            public void onClick(EntrySelectorData.EntrySelectorRow row, AjaxRequestTarget target) throws ObjectNotFoundException {
                final Integer activityid = (Integer) row.getId();

				getTabbedPanel().setEditBackingBean(new ActivityBackingBean(activityService.getActivity(activityid)));
                getTabbedPanel().switchTabOnAjaxTarget(target, AddEditTabbedPanel.TABPOS_EDIT);
            }
        };

		selectorPanel = new EntrySelectorPanel("activitySelector",
				createSelectorData(activities),
				clickHandler,
				new ResourceModel("admin.customer.hideInactive")
		);

        greyBorder.add(selectorPanel);
    }

	private EntrySelectorData createSelectorData(List<Activity> activities) {

		List<EntrySelectorData.Header> headers = Lists.newArrayList(new EntrySelectorData.Header("admin.activity.name"),
				new EntrySelectorData.Header("admin.customer.name"),
				new EntrySelectorData.Header("admin.customer.projects", EntrySelectorData.ColumnType.NUMERIC)
		);

		List<EntrySelectorData.EntrySelectorRow> rows = Lists.newArrayList();

		for (Activity activity : activities) {
			Boolean active = activity.getActive();
			List<String> cells = Lists.newArrayList(activity.getFullName() + (active ? "" : "*"));
			rows.add(new EntrySelectorData.EntrySelectorRow(cells, activity.getId(),  active));
		}

		return new EntrySelectorData(headers, rows);
	}

	private List<Activity> getActivities() {
        return activityService.getActivities();
    }

    @Override
    protected ActivityBackingBean getNewAddBaseBackingBean() {
		return new ActivityBackingBean(new Activity());
    }

    @Override
    protected ActivityBackingBean getNewEditBaseBackingBean() {
		return new ActivityBackingBean(new Activity());
    }

    @Override
    protected Panel getBaseAddPanel(String panelId) {
		return new ActivityAdminFormPanel(panelId, new CompoundPropertyModel<ActivityBackingBean>(
                getTabbedPanel().getAddBackingBean()), getUsers(), getProjects());
    }

    @Override
    protected Panel getBaseEditPanel(String panelId) {
		return new ActivityAdminFormPanel(panelId, new CompoundPropertyModel<ActivityBackingBean>(
                getTabbedPanel().getEditBackingBean()), getUsers(), getProjects());
    }

    @Override
    public Boolean ajaxEventReceived(AjaxEvent ajaxEvent) {
        AjaxEventType eventType = ajaxEvent.getEventType();

        if (ActivityEditAjaxEventType.ACTIVITY_DELETED.equals(eventType) || ActivityEditAjaxEventType.ACTIVITY_UPDATED.equals(eventType)) {
			selectorPanel.updateData(createSelectorData(getActivities()));
			selectorPanel.reRender(ajaxEvent.getTarget());
			getTabbedPanel().succesfulSave(ajaxEvent.getTarget());
        }
        return true;
    }

    public List<Project> getProjects() {
        if (projects == null) {
            projects = projectService.getActiveProjects();
        }

        return projects;
    }

    public List<User> getUsers() {
        if (users == null) {
            users = userService.getUsers();
        }
        return users;
    }

}
