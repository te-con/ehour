/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.admin.project.page;

import net.rrm.ehour.domain.Project;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.ui.admin.AbstractTabbedAdminPage;
import net.rrm.ehour.ui.admin.project.common.ProjectAjaxEventType;
import net.rrm.ehour.ui.admin.project.dto.ProjectAdminBackingBean;
import net.rrm.ehour.ui.admin.project.dto.ProjectAdminBackingBeanImpl;
import net.rrm.ehour.ui.admin.project.panel.ProjectFormPanel;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.component.AddEditTabbedPanel;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.event.PayloadAjaxEvent;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorAjaxEventType;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorFilter;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorPanel;
import net.rrm.ehour.ui.common.sort.ProjectComparator;
import net.rrm.ehour.ui.common.util.WebGeo;
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
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Collections;
import java.util.List;

/**
 * Project admin page
 **/

public class ProjectAdmin extends AbstractTabbedAdminPage<ProjectAdminBackingBean>
{
	private static final String	PROJECT_SELECTOR_ID = "projectSelector";
	private static final long 	serialVersionUID = 9196677804018589806L;

	private static final int TABPOS_USERS = 2;

	@SpringBean
	private ProjectService		projectService;
	private	final static Logger	logger = Logger.getLogger(ProjectAdmin.class);
	private EntrySelectorFilter	currentFilter;
	private	ListView<Project> 	projectListView;

	public ProjectAdmin()
	{
		super(new ResourceModel("admin.project.title"),
				new ResourceModel("admin.project.addProject"),
				new ResourceModel("admin.project.editProject"),
				new ResourceModel("admin.project.noEditEntrySelected"),
				"admin.project.help.header",
				"admin.project.help.body");

		List<Project> projects = getProjects();

		Fragment projectListHolder = getProjectListHolder(projects);

		GreyRoundedBorder greyBorder = new GreyRoundedBorder("entrySelectorFrame",
											new ResourceModel("admin.project.title"),
											WebGeo.W_ENTRY_SELECTOR);
		add(greyBorder);

		greyBorder.add(new EntrySelectorPanel(PROJECT_SELECTOR_ID,
											projectListHolder,
											new StringResourceModel("admin.project.filter", this, null),
											new ResourceModel("admin.project.hideInactive")));
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.ui.common.page.BasePage#ajaxEventReceived(net.rrm.ehour.persistence.persistence.ui.common.ajax.AjaxEvent)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean ajaxEventReceived(AjaxEvent ajaxEvent)
	{
		AjaxEventType type = ajaxEvent.getEventType();

		if (type == EntrySelectorAjaxEventType.FILTER_CHANGE)
		{
			currentFilter = ((PayloadAjaxEvent<EntrySelectorFilter>)ajaxEvent).getPayload();

			List<Project> projects = getProjects();
			projectListView.setList(projects);
		}
		else if (type == ProjectAjaxEventType.PROJECT_UPDATED
					|| type == ProjectAjaxEventType.PROJECT_DELETED)
		{
			// update project list
			List<Project> projects = getProjects();
			projectListView.setList(projects);

			((EntrySelectorPanel)
					((MarkupContainer)get("entrySelectorFrame"))
						.get(PROJECT_SELECTOR_ID)).refreshList(ajaxEvent.getTarget());

			getTabbedPanel().succesfulSave(ajaxEvent.getTarget());
		}

		return false;
	}


	@Override
	protected Panel getBaseAddPanel(String panelId)
	{
		return new ProjectFormPanel(panelId,
									new CompoundPropertyModel<ProjectAdminBackingBean>(getTabbedPanel().getAddBackingBean()));
	}

	@Override
	protected Panel getBaseEditPanel(String panelId)
	{
		return new ProjectFormPanel(panelId, new CompoundPropertyModel<ProjectAdminBackingBean>(getTabbedPanel().getEditBackingBean()));

	}

	@Override
	protected void onTabSwitch(int index)
	{
		if (index == AddEditTabbedPanel.TABPOS_ADD)
		{
			getTabbedPanel().removeTab(TABPOS_USERS);
		}
	}

	@Override
	protected ProjectAdminBackingBean getNewAddBaseBackingBean()
	{
		Project	project = new Project();
		project.setActive(true);

		return new ProjectAdminBackingBeanImpl(project);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.ui.admin.BaseTabbedAdminPage#getNewEditBackingBean()
	 */
	@Override
	protected ProjectAdminBackingBean getNewEditBaseBackingBean()
	{
		return new ProjectAdminBackingBeanImpl(new Project());
	}

	/**
	 * Get a the projectListHolder fragment containing the listView
	 * @param users
	 * @return
	 */
	@SuppressWarnings("serial")
	private Fragment getProjectListHolder(List<Project> projects)
	{
		Fragment fragment = new Fragment("itemListHolder", "itemListHolder", ProjectAdmin.this);

		projectListView = new ListView<Project>("itemList", projects)
		{
			@Override
			protected void populateItem(ListItem<Project> item)
			{
				Project project = item.getModelObject();
				final Integer	projectId = project.getProjectId();

				AjaxLink<Void> link = new AjaxLink<Void>("itemLink")
				{
					@Override
					public void onClick(AjaxRequestTarget target)
					{
						try
						{
							getTabbedPanel().setEditBackingBean(new ProjectAdminBackingBeanImpl(projectService.getProjectAndCheckDeletability(projectId)));
							getTabbedPanel().switchTabOnAjaxTarget(target, AddEditTabbedPanel.TABPOS_EDIT);
						} catch (ObjectNotFoundException e)
						{
							logger.error(e);
						}
					}
				};

				item.add(link);

				link.add(new Label("linkLabel", project.getProjectCode() + " - " + project.getName() + (project.isActive() ? "" : "*")));
			}
		};

		fragment.add(projectListView);

		return fragment;
	}

	/**
	 * Get the projects from the backend
	 * @return
	 */
	private List<Project> getProjects()
	{
		List<Project> projects;

        if (currentFilter == null)
		{
			projects = projectService.getAllProjects(true);
		}
		else
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("Filtering on " + currentFilter.getCleanFilterInput() + ", hide active: " + currentFilter.isActivateToggle());
			}

			projects = projectService.getProjects(currentFilter.getCleanFilterInput(), currentFilter.isActivateToggle());
		}

		Collections.sort(projects, new ProjectComparator());

		return projects;
	}
}
