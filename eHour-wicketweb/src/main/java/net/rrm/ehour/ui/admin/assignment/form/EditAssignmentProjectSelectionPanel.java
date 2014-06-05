package net.rrm.ehour.ui.admin.assignment.form;

import com.google.common.collect.Maps;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.ui.admin.assignment.AssignmentAdminBackingBean;
import net.rrm.ehour.ui.common.component.GroupableDropDownChoice;
import net.rrm.ehour.ui.common.component.OptGroupRendererMap;
import org.apache.wicket.markup.html.form.AbstractChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;
import java.util.Map;

public class EditAssignmentProjectSelectionPanel extends AbstractAssignmentProjectSelectionPanel {
    public EditAssignmentProjectSelectionPanel(String id, IModel<AssignmentAdminBackingBean> model) {
        super(id, model);
    }

    @Override
    protected AbstractChoice<?, Project> createProjectChoiceDropDown(String id, IModel<List<Project>> projectChoices) {
        // project
        IModel<Project> projectModel = new PropertyModel<Project>(getDefaultModel(), "projectAssignment.project");

        Map<Project, String> projectToCustomerMap = createProjectToCustomerMap(projectChoices);

        OptGroupRendererMap<Project> renderer = new ProjectRenderer(projectToCustomerMap);

        return new GroupableDropDownChoice<Project>(id, projectModel, projectChoices, renderer);
    }

    private Map<Project, String> createProjectToCustomerMap(IModel<List<Project>> projectChoices) {
        List<Project> projects = projectChoices.getObject();

        Map<Project, String> projectToCustomerMap = Maps.newHashMap();

        for (Project project : projects) {
            projectToCustomerMap.put(project, project.getCustomer().getFullName());
        }
        return projectToCustomerMap;
    }

    private static class ProjectRenderer extends OptGroupRendererMap<Project> {
        public ProjectRenderer(Map<Project, String> projectToCustomerMap) {
            super(projectToCustomerMap);
        }

        @Override
        public Object getDisplayValue(Project project) {
            return project.getFullName();
        }

        @Override
        public String getIdValue(Project project, int index) {
            return Integer.toString(project.getProjectId());
        }
    }
}
