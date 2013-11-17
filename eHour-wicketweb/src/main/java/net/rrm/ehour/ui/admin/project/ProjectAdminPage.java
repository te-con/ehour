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

package net.rrm.ehour.ui.admin.project;

import net.rrm.ehour.domain.Project;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.ui.admin.AbstractTabbedAdminPage;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.component.AddEditTabbedPanel;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorFilter;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorPanel;
import net.rrm.ehour.ui.common.sort.ProjectComparator;
import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Collections;
import java.util.List;

import static net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorPanel.FilterChangedEvent;

/**
 * Project admin page
 */

public class ProjectAdminPage extends AbstractTabbedAdminPage<ProjectAdminBackingBean> {
    private static final long serialVersionUID = 9196677804018589806L;

    private static final Logger LOGGER = Logger.getLogger(ProjectAdminPage.class);

    private static final String PROJECT_SELECTOR_ID = "projectSelector";

    private static final int TABPOS_USERS = 2;
    private EntrySelectorPanel entrySelectorPanel;

    @SpringBean
    private ProjectService projectService;

    private EntrySelectorFilter currentFilter = new EntrySelectorFilter();
    private ListView<Project> projectListView;
    private final GreyRoundedBorder greyBorder;

    public ProjectAdminPage() {
        super(new ResourceModel("admin.project.title"),
                new ResourceModel("admin.project.addProject"),
                new ResourceModel("admin.project.editProject"),
                new ResourceModel("admin.project.noEditEntrySelected"));


        greyBorder = new GreyRoundedBorder("entrySelectorFrame", new ResourceModel("admin.project.title"));
        add(greyBorder);

    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();

        List<Project> projects = getProjects();

        Fragment projectListHolder = createProjectListHolder(projects);

        entrySelectorPanel = new EntrySelectorPanel(PROJECT_SELECTOR_ID,
                projectListHolder,
                new ResourceModel("admin.project.hideInactive"));
        greyBorder.add(entrySelectorPanel);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean ajaxEventReceived(AjaxEvent ajaxEvent) {
        AjaxEventType type = ajaxEvent.getEventType();

        if (type == ProjectAjaxEventType.PROJECT_UPDATED
                || type == ProjectAjaxEventType.PROJECT_DELETED) {
            // update project list
            projectListView.setList(getProjects());

            entrySelectorPanel.refreshList(ajaxEvent.getTarget());
            getTabbedPanel().succesfulSave(ajaxEvent.getTarget());
        }

        return false;
    }

    @Override
    protected Component onFilterChanged(FilterChangedEvent filterChangedEvent) {
        currentFilter = filterChangedEvent.getFilter();

        List<Project> projects = getProjects();
        projectListView.setList(projects);

        return projectListView.getParent();
    }

    @Override
    protected Panel getBaseAddPanel(String panelId) {
        return new ProjectFormPanel(panelId,
                new CompoundPropertyModel<ProjectAdminBackingBean>(getTabbedPanel().getAddBackingBean()));
    }

    @Override
    protected Panel getBaseEditPanel(String panelId) {
        return new ProjectFormPanel(panelId, new CompoundPropertyModel<ProjectAdminBackingBean>(getTabbedPanel().getEditBackingBean()));

    }

    @Override
    protected void onTabSwitch(int index) {
        if (index == AddEditTabbedPanel.TABPOS_ADD) {
            getTabbedPanel().removeTab(TABPOS_USERS);
        }
    }

    @Override
    protected ProjectAdminBackingBean getNewAddBaseBackingBean() {
        Project project = new Project();
        project.setActive(true);

        return new ProjectAdminBackingBean(project);
    }

    @Override
    protected ProjectAdminBackingBean getNewEditBaseBackingBean() {
        return new ProjectAdminBackingBean(new Project());
    }


    @SuppressWarnings("serial")
    private Fragment createProjectListHolder(List<Project> projects) {
        Fragment fragment = new Fragment("itemListHolder", "itemListHolder", ProjectAdminPage.this);
        fragment.setOutputMarkupId(true);

        projectListView = new ListView<Project>("itemList", projects) {
            @Override
            protected void populateItem(ListItem<Project> item) {
                Project project = item.getModelObject();

                if (!project.isActive()) {
                    item.add(AttributeModifier.append("class", "inactive"));
                }

                final Integer projectId = project.getProjectId();
                item.add(new Label("name", project.getName()));
                item.add(new Label("code", project.getProjectCode()));


                item.add(new AjaxEventBehavior("onclick") {
                    @Override
                    protected void onEvent(AjaxRequestTarget target) {
                        try {
                            getTabbedPanel().setEditBackingBean(new ProjectAdminBackingBean(projectService.getProjectAndCheckDeletability(projectId)));
                            getTabbedPanel().switchTabOnAjaxTarget(target, AddEditTabbedPanel.TABPOS_EDIT);
                        } catch (ObjectNotFoundException e) {
                            LOGGER.error(e);
                        }
                    }
                });

            }
        };

        fragment.add(projectListView);

        return fragment;
    }

    private List<Project> getProjects() {
        List<Project> projects = projectService.getProjects(currentFilter == null || currentFilter.isFilterToggle());

        Collections.sort(projects, new ProjectComparator());

        return projects;
    }
}
