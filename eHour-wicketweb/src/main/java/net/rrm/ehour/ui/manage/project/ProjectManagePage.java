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

package net.rrm.ehour.ui.manage.project;

import com.google.common.collect.Lists;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.sort.ProjectComparator;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.component.AddEditTabbedPanel;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorData;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorPanel;
import net.rrm.ehour.ui.common.panel.entryselector.InactiveFilterChangedEvent;
import net.rrm.ehour.ui.manage.AbstractTabbedManagePage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import java.util.Collections;
import java.util.List;

/**
 * Project admin page
 */

public class ProjectManagePage extends AbstractTabbedManagePage<ProjectAdminBackingBean> {
    private static final long serialVersionUID = 9196677804018589806L;
    private static final String PROJECT_SELECTOR_ID = "projectSelector";
    private static final int TABPOS_USERS = 2;

    private EntrySelectorPanel entrySelectorPanel;

    @SpringBean
    private ProjectService projectService;

    public ProjectManagePage() {
        super(new ResourceModel("admin.project.title"),
                new ResourceModel("admin.project.addProject"),
                new ResourceModel("admin.project.editProject"),
                new ResourceModel("admin.project.noEditEntrySelected"));
    }

    public ProjectManagePage(PageParameters params) {
        super(new ResourceModel("admin.project.title"),
                new ResourceModel("admin.project.addProject"),
                new ResourceModel("admin.project.editProject"),
                new ResourceModel("admin.project.noEditEntrySelected"));
        StringValue id = params.get("id");

        if (!id.isEmpty()) {
            try {
                ProjectAdminBackingBean editBean = createEditBean(id.toInt());

                AddEditTabbedPanel<ProjectAdminBackingBean> tabbedPanel = getTabbedPanel();
                tabbedPanel.forceLoadEditTab(editBean);
            } catch (Exception e) {

            }
        }
    }

    @Override
    protected Panel getBaseAddPanel(String panelId) {
        return new ProjectFormContainer<>(panelId, new CompoundPropertyModel<>(getTabbedPanel().getAddBackingBean()));
    }

    @Override
    protected Panel getBaseEditPanel(String panelId) {
        return new ProjectFormContainer<>(panelId, new CompoundPropertyModel<>(getTabbedPanel().getEditBackingBean()));
    }

    @Override
    protected ProjectAdminBackingBean getNewAddBaseBackingBean() {
        return new ProjectAdminBackingBean(new Project());
    }

    @Override
    protected ProjectAdminBackingBean getNewEditBaseBackingBean() {
        return new ProjectAdminBackingBean(new Project());
    }

    protected ProjectAdminBackingBean createEditBean(Integer projectId) throws ObjectNotFoundException {
        return new ProjectAdminBackingBean(projectService.getProjectAndCheckDeletability(projectId));
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();

        GreyRoundedBorder greyBorder = new GreyRoundedBorder("entrySelectorFrame", new ResourceModel("admin.project.title"));
        addOrReplace(greyBorder);

        EntrySelectorPanel.EntrySelectorBuilder builder = constructEntrySelectorBuilder();

        entrySelectorPanel = builder.build();
        greyBorder.addOrReplace(entrySelectorPanel);
    }

    protected EntrySelectorPanel.EntrySelectorBuilder constructEntrySelectorBuilder() {
        EntrySelectorPanel.ClickHandler clickHandler = new EntrySelectorPanel.ClickHandler() {
            @Override
            public void onClick(EntrySelectorData.EntrySelectorRow row, AjaxRequestTarget target) throws ObjectNotFoundException {
                Integer projectId = (Integer) row.getId();
                getTabbedPanel().setEditBackingBean(createEditBean(projectId));
                getTabbedPanel().switchTabOnAjaxTarget(target, AddEditTabbedPanel.TABPOS_EDIT);
            }
        };

        return EntrySelectorPanel.EntrySelectorBuilder.startAs(PROJECT_SELECTOR_ID)
                .onClick(clickHandler)
                .withData(createSelectorData(getProjects(isHideInactive())))
                .withInactiveTooltip(new ResourceModel("admin.project.hideInactive"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Boolean ajaxEventReceived(AjaxEvent ajaxEvent) {
        AjaxEventType type = ajaxEvent.getEventType();

        if (type == ProjectAjaxEventType.PROJECT_UPDATED
                || type == ProjectAjaxEventType.PROJECT_CREATED
                || type == ProjectAjaxEventType.PROJECT_DELETED) {
            // update project list
            entrySelectorPanel.updateData(createSelectorData(getProjects(isHideInactive())));
            entrySelectorPanel.reRender(ajaxEvent.getTarget());
            getTabbedPanel().succesfulSave(ajaxEvent.getTarget());
        }

        return false;
    }

    @Override
    protected void onFilterChanged(InactiveFilterChangedEvent inactiveFilterChangedEvent, AjaxRequestTarget target) {
        entrySelectorPanel.updateData(createSelectorData(getProjects(inactiveFilterChangedEvent.hideInactiveFilter().isHideInactive())));
        entrySelectorPanel.reRender(target);
    }

    @Override
    protected void onTabSwitch(int index) {
        if (index == AddEditTabbedPanel.TABPOS_ADD) {
            getTabbedPanel().removeTab(TABPOS_USERS);
        }
    }

    protected EntrySelectorData createSelectorData(List<Project> projects) {
        List<EntrySelectorData.Header> headers = Lists.newArrayList(new EntrySelectorData.Header("admin.project.code.short"), new EntrySelectorData.Header("admin.project.name"));

        List<EntrySelectorData.EntrySelectorRow> rows = Lists.newArrayList();

        for (Project project : projects) {
            boolean active = project.isActive();

            List<String> cells = Lists.newArrayList(project.getProjectCode(), project.getName());
            rows.add(new EntrySelectorData.EntrySelectorRow(cells, project.getProjectId(), active));
        }

        return new EntrySelectorData(headers, rows);
    }

    private List<Project> getProjects(boolean hideInactive) {
        List<Project> projects;

        if (hideInactive) {
            projects = projectService.getActiveProjects();
        } else {
            projects = projectService.getProjects();
        }

        Collections.sort(projects, new ProjectComparator());

        return projects;
    }
}
