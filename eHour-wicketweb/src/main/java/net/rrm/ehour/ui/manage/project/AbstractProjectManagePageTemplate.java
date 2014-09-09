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
import net.rrm.ehour.ui.common.panel.entryselector.*;
import net.rrm.ehour.ui.common.report.ColumnType;
import net.rrm.ehour.ui.manage.AbstractTabbedManagePage;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Collections;
import java.util.List;

import static net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorData.Header;

public abstract class AbstractProjectManagePageTemplate<T extends ProjectAdminBackingBean> extends AbstractTabbedManagePage<T> {
    private static final long serialVersionUID = 9196677804018589806L;

    private static final String PROJECT_SELECTOR_ID = "projectSelector";

    private static final int TABPOS_USERS = 2;
    private EntrySelectorPanel entrySelectorPanel;

    @SpringBean
    private ProjectService projectService;

    private HideInactiveFilter currentFilter = new HideInactiveFilter();
    private ListView<Project> projectListView;
    private final GreyRoundedBorder greyBorder;

    public AbstractProjectManagePageTemplate() {
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

        entrySelectorPanel = new EntrySelectorPanel(PROJECT_SELECTOR_ID,
                createSelectorData(projects),
                new ResourceModel("admin.project.hideInactive"));
        greyBorder.addOrReplace(entrySelectorPanel);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Boolean ajaxEventReceived(AjaxEvent ajaxEvent) {
        AjaxEventType type = ajaxEvent.getEventType();

        if (type == ProjectAjaxEventType.PROJECT_UPDATED
                || type == ProjectAjaxEventType.PROJECT_CREATED
                || type == ProjectAjaxEventType.PROJECT_DELETED) {
            // update project list
            projectListView.setList(getProjects());

            entrySelectorPanel.refreshList(ajaxEvent.getTarget());
            getTabbedPanel().succesfulSave(ajaxEvent.getTarget());
        }

        return false;
    }

    @Override
    protected Component onFilterChanged(InactiveFilterChangedEvent inactiveFilterChangedEvent) {
        currentFilter = inactiveFilterChangedEvent.hideInactiveFilter();

        List<Project> projects = getProjects();
        projectListView.setList(projects);

        return projectListView.getParent();
    }

    @Override
    protected void onTabSwitch(int index) {
        if (index == AddEditTabbedPanel.TABPOS_ADD) {
            getTabbedPanel().removeTab(TABPOS_USERS);
        }
    }

    private EntrySelectorData createSelectorData(List<Project> projects) {

        List<Header> headers = Lists.newArrayList(new Header("admin.project.code.short"), new Header("admin.project.name"));

        List<EntrySelectorData.EntrySelectorRow> rows = Lists.newArrayList();

        for (Project project : projects) {
            boolean active = project.isActive();

            rows.add(new EntrySelectorData.EntrySelectorRow(Lists.newArrayList(project.getName(), project.getProjectCode()), active));
        }

        return new EntrySelectorData(headers, rows);
    }

    protected abstract T createEditBean(Integer projectId) throws ObjectNotFoundException;

    private List<Project> getProjects() {
        List<Project> projects = currentFilter == null || currentFilter.isHideInactive() ? projectService.getActiveProjects() : projectService.getProjects();

        Collections.sort(projects, new ProjectComparator());

        return projects;
    }
}