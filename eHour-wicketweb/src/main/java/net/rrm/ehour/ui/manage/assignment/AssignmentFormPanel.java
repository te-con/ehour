package net.rrm.ehour.ui.manage.assignment;

import com.google.common.collect.Lists;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.common.component.PlaceholderPanel;
import net.rrm.ehour.ui.common.component.ServerMessageLabel;
import net.rrm.ehour.ui.common.decorator.LoadingSpinnerDecorator;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.EventPublisher;
import net.rrm.ehour.ui.common.form.FormConfig;
import net.rrm.ehour.ui.common.form.FormUtil;
import net.rrm.ehour.ui.common.model.MessageResourceModel;
import net.rrm.ehour.ui.common.panel.AbstractFormSubmittingPanel;
import net.rrm.ehour.ui.common.util.WebGeo;
import net.rrm.ehour.ui.manage.assignment.form.AssignmentFormComponentContainerPanel;
import net.rrm.ehour.ui.manage.assignment.form.AssignmentFormComponentContainerPanel.DisplayOption;
import net.rrm.ehour.ui.manage.customer.CustomerManagePage;
import net.rrm.ehour.ui.manage.project.ProjectManagePage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

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

    private WebMarkupContainer createNotBookableWarning(final IModel<AssignmentAdminBackingBean> model, String id) {
        Fragment fragment = new Fragment(id, "inactive", this);

        final Customer customer = model.getObject().getCustomer();
        final boolean activeCustomer = customer == null || customer.isActive();
        String resourceModel = activeCustomer ? "admin.assignment.inactive.project" : "admin.assignment.inactive.customer";
        fragment.add(new Label("itemName", new MessageResourceModel(resourceModel, this)));

        final Project project = model.getObject().getProjectAssignment().getProject();

        Link<Void> link = new Link<Void>("link") {
            @Override
            public void onClick() {
                PageParameters parameters = new PageParameters();

                if (activeCustomer) {
                    parameters.add("id", project.getProjectId());
                    setResponsePage(ProjectManagePage.class, parameters);
                } else {
                    parameters.add("id", customer.getCustomerId());
                    setResponsePage(CustomerManagePage.class, parameters);
                }

            }
        };

        link.add(new Label("linkText", activeCustomer ? project.getFullName() : customer.getFullName()));

        fragment.add(link);

        return fragment;
    }

    private void setUpPage(IModel<AssignmentAdminBackingBean> model, List<DisplayOption> optionList) {
        if (optionList.isEmpty()) {
            optionList.addAll(Arrays.asList(DisplayOption.SHOW_PROJECT_SELECTION));
        }

        WebMarkupContainer greyBorder = optionList.contains(DisplayOption.NO_BORDER) ? new WebMarkupContainer(BORDER) : new GreySquaredRoundedBorder(BORDER, WebGeo.AUTO);

        add(greyBorder);

        final Form<AssignmentAdminBackingBean> form = new Form<>("assignmentForm", model);
        greyBorder.add(form);

        form.add(model.getObject().isBookable() ? PlaceholderPanel.hidden("inactiveParent") : createNotBookableWarning(model, "inactiveParent"));

        // add submit form
        boolean deletable = getPanelModelObject().isDeletable();
        FormConfig formConfig = FormConfig.forForm(form).withDelete(deletable).withSubmitTarget(this)
                .withDeleteEventType(AssignmentAjaxEventType.ASSIGNMENT_DELETED)
                .withSubmitEventType(AssignmentAjaxEventType.ASSIGNMENT_UPDATED);

        FormUtil.setSubmitActions(formConfig);

        AjaxButton cancelButton = createCancelButton("cancelButton", optionList, form);
        form.add(cancelButton);

        form.add(new AssignmentFormComponentContainerPanel("formComponents", form, model, optionList));

        form.add(new ServerMessageLabel("serverMessage", "formValidationError"));
    }

    private AjaxButton createCancelButton(final String id, List<DisplayOption> optionList, final Form<AssignmentAdminBackingBean> form) {
        AjaxButton cancelButton = new AjaxButton(id, form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                EventPublisher.publishAjaxEvent(AssignmentFormPanel.this, new AjaxEvent(AssignmentAjaxEventType.ASSIGNMENT_CANCELLED));
            }

            @Override
            protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                super.updateAjaxAttributes(attributes);

                attributes.getAjaxCallListeners().add(new LoadingSpinnerDecorator());
            }
        };

        cancelButton.setVisible(optionList.contains(DisplayOption.SHOW_CANCEL_BUTTON));
        return cancelButton;
    }
}
