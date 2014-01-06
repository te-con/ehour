package net.rrm.ehour.ui.admin.assignment;

import com.google.common.collect.Lists;
import net.rrm.ehour.ui.admin.assignment.form.AssignmentFormComponentContainerPanel;
import net.rrm.ehour.ui.admin.assignment.form.AssignmentFormComponentContainerPanel.DisplayOption;
import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.common.component.ServerMessageLabel;
import net.rrm.ehour.ui.common.form.FormConfig;
import net.rrm.ehour.ui.common.form.FormUtil;
import net.rrm.ehour.ui.common.panel.AbstractFormSubmittingPanel;
import net.rrm.ehour.ui.common.util.WebGeo;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

import java.util.Arrays;
import java.util.List;

public class AssignmentFormPanel extends AbstractFormSubmittingPanel<AssignmentAdminBackingBean> {
    private static final long serialVersionUID = 7704141227799017924L;
    public static final String BORDER = "border";

    public AssignmentFormPanel(String id, final IModel<AssignmentAdminBackingBean> model) {
        this(id, model, Lists.<DisplayOption>newArrayList());
    }

    public AssignmentFormPanel(String id, final IModel<AssignmentAdminBackingBean> model, List<DisplayOption> displayOptions) {
        super(id, model);

        setOutputMarkupId(true);

        setUpPage(model, displayOptions);
    }

    private void setUpPage(IModel<AssignmentAdminBackingBean> model, List<DisplayOption> optionList) {

        WebMarkupContainer greyBorder;

        if (optionList.contains(DisplayOption.NO_BORDER)) {
            greyBorder = new WebMarkupContainer(BORDER);
        } else {
            greyBorder = new GreySquaredRoundedBorder(BORDER, WebGeo.AUTO);
        }

        add(greyBorder);

        final Form<AssignmentAdminBackingBean> form = new Form<AssignmentAdminBackingBean>("assignmentForm", model);
        greyBorder.add(form);

        // add submit form
        boolean deletable = ((AssignmentAdminBackingBean) getDefaultModelObject()).getProjectAssignment().isDeletable();
        FormConfig formConfig = FormConfig.forForm(form).withDelete(deletable).withSubmitTarget(this)
                .withDeleteEventType(AssignmentAjaxEventType.ASSIGNMENT_DELETED)
                .withSubmitEventType(AssignmentAjaxEventType.ASSIGNMENT_UPDATED);

        FormUtil.setSubmitActions(formConfig);

        if (optionList.isEmpty()) {
            optionList.addAll(Arrays.asList(DisplayOption.SHOW_PROJECT_SELECTION, DisplayOption.SHOW_SAVE_BUTTON, DisplayOption.SHOW_DELETE_BUTTON));
        }

        form.add(new AssignmentFormComponentContainerPanel("formComponents", form, model, optionList));

        form.add(new ServerMessageLabel("serverMessage", "formValidationError"));
    }
}
