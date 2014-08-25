package net.rrm.ehour.ui.admin.activity.page;

import java.util.List;

import net.rrm.ehour.activity.service.ActivityService;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.ui.admin.AbstractTabbedAdminPage;
import net.rrm.ehour.ui.admin.activity.dto.ActivityBackingBean;
import net.rrm.ehour.ui.admin.activity.panel.ActivityAdminFormPanel;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorFilter;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorPanel;
import net.rrm.ehour.ui.common.util.WebGeo;
import net.rrm.ehour.user.service.UserService;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class ActivityAdmin extends AbstractTabbedAdminPage<ActivityBackingBean> {

	private final static Logger logger = Logger.getLogger(ActivityAdmin.class);

	@SpringBean
	private UserService userService;

	@SpringBean
	private ProjectService projectService;

	@SpringBean
	private ActivityService activityService;

	private List<Project> projects;

	private List<User> users;

	private ListView<Activity> activityListView;

	private EntrySelectorFilter currentFilter;

	private EntrySelectorPanel selectorPanel;

	public ActivityAdmin() {
		super(new ResourceModel("admin.activity.title"), new ResourceModel("admin.activity.addActivity"), new ResourceModel(
				"admin.activity.editActivity"), new ResourceModel("admin.activity.noEditActivitySelected"), "admin.activity.help.header",
				"admin.activity.help.body");

		List<Activity> activities;
		activities = getActivities();

		GreyRoundedBorder greyBorder = new GreyRoundedBorder("entrySelectorFrame", new ResourceModel("admin.activity.title"),
				WebGeo.W_ENTRY_SELECTOR);
		add(greyBorder);

	}

	private List<Activity> getActivities() {
		return activityService.getActivities();
	}

	@Override
	protected ActivityBackingBean getNewAddBaseBackingBean() {
		ActivityBackingBean activityBean = new ActivityBackingBean(new Activity());
		return activityBean;
	}

	@Override
	protected ActivityBackingBean getNewEditBaseBackingBean() {
		ActivityBackingBean activityBean = new ActivityBackingBean(new Activity());
		return activityBean;
	}

	@Override
	protected Panel getBaseAddPanel(String panelId) {
		ActivityAdminFormPanel activityAdminFormPanel = new ActivityAdminFormPanel(panelId, new CompoundPropertyModel<ActivityBackingBean>(
				getTabbedPanel().getAddBackingBean()), getUsers(), getProjects());
		return activityAdminFormPanel;
	}

	@Override
	protected Panel getBaseEditPanel(String panelId) {
		ActivityAdminFormPanel activityAdminFormPanel = new ActivityAdminFormPanel(panelId, new CompoundPropertyModel<ActivityBackingBean>(
				getTabbedPanel().getEditBackingBean()), getUsers(), getProjects());
		return activityAdminFormPanel;
	}

	public List<Project> getProjects() {
		if (projects == null) {
			projects = projectService.getAllProjects(true);
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
