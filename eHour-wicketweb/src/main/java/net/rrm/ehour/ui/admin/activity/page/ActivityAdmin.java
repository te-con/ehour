package net.rrm.ehour.ui.admin.activity.page;

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
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorPanel;
import net.rrm.ehour.ui.manage.AbstractTabbedManagePage;
import net.rrm.ehour.user.service.UserService;
import org.apache.log4j.Logger;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ActivityAdmin extends AbstractTabbedManagePage<ActivityBackingBean> {

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

	private EntrySelectorPanel selectorPanel;

	public ActivityAdmin() {
		super(new ResourceModel("admin.activity.title"),
                new ResourceModel("admin.activity.addActivity"),
                new ResourceModel("admin.activity.editActivity"),
                new ResourceModel("admin.activity.noEditActivitySelected"));

		List<Activity> activities;
		activities = getActivities();

		Fragment activityListHolder = getActivityListHolder(activities);

		GreyRoundedBorder greyBorder = new GreyRoundedBorder("entrySelectorFrame", new ResourceModel("admin.activity.title"));
		add(greyBorder);

		selectorPanel = new EntrySelectorPanel("activitySelector", activityListHolder);

		greyBorder.add(selectorPanel);
	}

	private Fragment getActivityListHolder(List<Activity> activities) {
		Fragment fragment = new Fragment("itemListHolder", "itemListHolder", ActivityAdmin.this);

		activityListView = new ListView<Activity>("itemList", activities) {
			private static final long serialVersionUID = 5334338761736798802L;

			@Override
			protected void populateItem(ListItem<Activity> item) {
				final Activity activity = item.getModelObject();
				final Integer activityid = activity.getId();

				AjaxLink<Void> link = new AjaxLink<Void>("itemLink") {
					private static final long serialVersionUID = -3898942767521616039L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						try {
							getTabbedPanel().setEditBackingBean(new ActivityBackingBean(activityService.getActivity(activityid)));
							getTabbedPanel().switchTabOnAjaxTarget(target, 1);
						} catch (ObjectNotFoundException exc) {
							logger.error(exc.getMessage());
						}
					}
				};
				item.add(link);
				link.add(new Label("linkLabel", activity.getFullName() + (activity.getActive() ? "" : "*")));
			}
		};

		fragment.add(activityListView);

		return fragment;
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

	@Override
	public Boolean ajaxEventReceived(AjaxEvent ajaxEvent) {
		AjaxEventType eventType = ajaxEvent.getEventType();

		if (ActivityEditAjaxEventType.ACTIVITY_DELETED.equals(eventType) || ActivityEditAjaxEventType.ACTIVITY_UPDATED.equals(eventType)) {

			activityListView.setList(activityService.getActivities());
			
			((EntrySelectorPanel) get("entrySelectorFrame").get("activitySelector")).refreshList(ajaxEvent.getTarget());

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
