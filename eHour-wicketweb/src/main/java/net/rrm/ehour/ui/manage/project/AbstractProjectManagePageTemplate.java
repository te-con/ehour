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
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorPanel.EntrySelectorBuilder;
import net.rrm.ehour.ui.common.panel.entryselector.InactiveFilterChangedEvent;
import net.rrm.ehour.ui.manage.AbstractTabbedManagePage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Collections;
import java.util.List;

import static net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorData.Header;
import static net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorPanel.ClickHandler;

public abstract class AbstractProjectManagePageTemplate<T extends ProjectAdminBackingBean> extends AbstractTabbedManagePage<T> {
    private static final long serialVersionUID = 9196677804018589806L;

    private static final String PROJECT_SELECTOR_ID = "projectSelector";

    private static final int TABPOS_USERS = 2;
    private EntrySelectorPanel entrySelectorPanel;

    @SpringBean
    private ProjectService projectService;

    public AbstractProjectManagePageTemplate() {
        super(new ResourceModel("admin.project.title"),
                new ResourceModel("admin.project.addProject"),
                new ResourceModel("admin.project.editProject"),
                new ResourceModel("admin.project.noEditEntrySelected"));
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();

        GreyRoundedBorder greyBorder = new GreyRoundedBorder("entrySelectorFrame", new ResourceModel("admin.project.title"));
        addOrReplace(greyBorder);

        EntrySelectorBuilder builder = constructEntrySelectorBuilder();

        entrySelectorPanel = builder.build();
        greyBorder.addOrReplace(entrySelectorPanel);
    }

    protected EntrySelectorBuilder constructEntrySelectorBuilder() {
        ClickHandler clickHandler = new ClickHandler() {
            @Override
            public void onClick(EntrySelectorData.EntrySelectorRow row, AjaxRequestTarget target) throws ObjectNotFoundException {
                Integer projectId = (Integer) row.getId();
                getTabbedPanel().setEditBackingBean(createEditBean(projectId));
                getTabbedPanel().switchTabOnAjaxTarget(target, AddEditTabbedPanel.TABPOS_EDIT);
            }
        };

        return EntrySelectorBuilder.startAs(PROJECT_SELECTOR_ID)
                .onClick(clickHandler)
                .withData(createSelectorData(getProjects(isHideInactive())))
                .withInactiveTooltip(new ResourceModel("admin.project.hideInactive"));
    }

    protected abstract T createEditBean(Integer projectId) throws ObjectNotFoundException;

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
        List<Header> headers = Lists.newArrayList(new Header("admin.project.code.short"), new Header("admin.project.name"));

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