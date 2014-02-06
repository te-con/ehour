package net.rrm.ehour.ui.admin.assignment.form;

import net.rrm.ehour.domain.Project;
import net.rrm.ehour.ui.admin.assignment.AssignmentAdminBackingBean;
import org.apache.wicket.markup.html.form.AbstractChoice;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

import java.util.List;

public class EditAssignmentProjectSelectionPanel extends AssignmentProjectSelectionPanel {
    public EditAssignmentProjectSelectionPanel(String id, IModel<AssignmentAdminBackingBean> model) {
        super(id, model);
    }

    @Override
    protected AbstractChoice<?, Project> createProjectChoiceDropDown(IModel<List<Project>> projectChoices, String id) {
        return new DropDownChoice<Project>(id, projectChoices, new ChoiceRenderer<Project>("fullName"));
    }
}
