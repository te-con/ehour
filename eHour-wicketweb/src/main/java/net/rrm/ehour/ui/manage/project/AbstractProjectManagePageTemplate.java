package net.rrm.ehour.ui.manage.project;

import net.rrm.ehour.domain.Project;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.sort.ProjectComparator;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.component.AddEditTabbedPanel;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorListView;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorPanel;
import net.rrm.ehour.ui.common.panel.entryselector.HideInactiveFilter;
import net.rrm.ehour.ui.common.panel.entryselector.InactiveFilterChangedEvent;
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

        Fragment projectListHolder = createProjectListHolder(projects);

        entrySelectorPanel = new EntrySelectorPanel(PROJECT_SELECTOR_ID,
                projectListHolder,
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

    @SuppressWarnings("serial")
    private Fragment createProjectListHolder(List<Project> projects) {
        Fragment fragment = new Fragment(EntrySelectorPanel.ITEM_LIST_HOLDER_ID, "itemListHolder", AbstractProjectManagePageTemplate.this);
        fragment.setOutputMarkupId(true);

        projectListView = new EntrySelectorListView<Project>("itemList", projects) {
            @Override
            protected void onPopulate(ListItem<Project> item, IModel<Project> itemModel) {
                Project project = itemModel.getObject();

                if (!project.isActive()) {
                    item.add(AttributeModifier.append("class", "inactive"));
                }

                item.add(new Label("name", project.getName()));
                item.add(new Label("code", project.getProjectCode()));
            }

            @Override
            protected void onClick(ListItem<Project> item, AjaxRequestTarget target) throws ObjectNotFoundException {
                Integer projectId = item.getModelObject().getProjectId();
                getTabbedPanel().setEditBackingBean(createEditBean(projectId));
                getTabbedPanel().switchTabOnAjaxTarget(target, AddEditTabbedPanel.TABPOS_EDIT);
            }
        };

        fragment.add(projectListView);

        return fragment;
    }

    protected abstract T createEditBean(Integer projectId) throws ObjectNotFoundException;

    private List<Project> getProjects() {
        List<Project> projects = currentFilter == null || currentFilter.isHideInactive() ? projectService.getActiveProjects() : projectService.getProjects();

        Collections.sort(projects, new ProjectComparator());

        return projects;
    }
}