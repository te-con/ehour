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

import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.sort.CustomerComparator;
import net.rrm.ehour.sort.UserComparator;
import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.common.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.common.component.KeepAliveTextArea;
import net.rrm.ehour.ui.common.component.ServerMessageLabel;
import net.rrm.ehour.ui.common.component.ValidatingFormComponentAjaxBehavior;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.form.FormConfig;
import net.rrm.ehour.ui.common.form.FormUtil;
import net.rrm.ehour.ui.common.model.AdminBackingBean;
import net.rrm.ehour.ui.common.panel.AbstractFormSubmittingPanel;
import net.rrm.ehour.ui.common.util.WebGeo;
import net.rrm.ehour.user.service.UserService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Project admin form
 */

public class ProjectFormPanel<T extends ProjectAdminBackingBean> extends AbstractFormSubmittingPanel<T> {
    @SpringBean
    private ProjectService projectService;

    @SpringBean
    private CustomerService customerService;

    @SpringBean
    private UserService userService;

    private static final long serialVersionUID = -8677950352090140144L;

    private boolean editMode;

    public ProjectFormPanel(String id, IModel<T> model) {
        super(id, model);

        setOutputMarkupId(true);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        editMode = getPanelModelObject().getProject().getPK() != null;

        setUpPanel(getPanelModel());
    }

    private void setUpPanel(IModel<T> model) {
        GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder("border", WebGeo.AUTO);
        add(greyBorder);

        Form<T> form = new Form<>("projectForm", model);
        addFormComponents(form);

        boolean deletable = model.getObject().isDeletable();

        ProjectAjaxEventType submitEventType = editMode ? ProjectAjaxEventType.PROJECT_UPDATED : ProjectAjaxEventType.PROJECT_CREATED;

        FormConfig formConfig = FormConfig.forForm(form)
                .withDelete(deletable)
                .withSubmitTarget(this)
                .withDeleteEventType(ProjectAjaxEventType.PROJECT_DELETED)
                .withSubmitEventType(submitEventType);

        FormUtil.setSubmitActions(formConfig);

        greyBorder.addOrReplace(form);

        onFormCreated(form);
    }

    protected void onFormCreated(Form<T> form) {

    }

    private void addFormComponents(Form<T> form) {
        addCustomer(form);
        addDescriptionAndContact(form);
        addGeneralInfo(form);
        addMisc(form);
        form.add(getProjectManager());

        addBillable(form);
        form.add(addDeleteInfo("deletableMsg", form.getModel()));
    }

    protected Fragment addDeleteInfo(String id, IModel<T> model) {
        Fragment fragment = new Fragment(id, "bookedHours", this);

        Project project = model.getObject().getDomainObject();

        Double bookedHours = project.getBookedHours();

        IModel<String> msgModel;

        if (project.isDeletable()) {
            msgModel = new ResourceModel("admin.project.canDelete");
        } else {
            msgModel = new StringResourceModel("admin.project.cannotDelete", null, bookedHours);
        }

        fragment.add(new Label("deleteMessage", msgModel));

        fragment.setVisible(project.getProjectId() != null);

        return fragment;

    }

    private void addBillable(Form<T> form) {
        CheckBox billableCheckbox = new CheckBox("project.billable");
        billableCheckbox.setMarkupId("billable");
        form.add(billableCheckbox);
    }

    private void addGeneralInfo(WebMarkupContainer parent) {
        // name
        RequiredTextField<String> nameField = new RequiredTextField<>("project.name");
        parent.add(nameField);
        nameField.add(StringValidator.maximumLength(64));
        nameField.setLabel(new ResourceModel("admin.project.name"));
        nameField.add(new ValidatingFormComponentAjaxBehavior());
        parent.add(new AjaxFormComponentFeedbackIndicator("nameValidationError", nameField));

        // project code
        RequiredTextField<String> codeField = new RequiredTextField<>("project.projectCode");
        parent.add(codeField);
        codeField.add(StringValidator.maximumLength(16));
        codeField.setLabel(new ResourceModel("admin.project.code"));
        codeField.add(new ValidatingFormComponentAjaxBehavior());
        parent.add(new AjaxFormComponentFeedbackIndicator("codeValidationError", codeField));
    }

    private void addCustomer(WebMarkupContainer parent) {
        // customers
        DropDownChoice<Customer> customerDropdown = new DropDownChoice<>("project.customer", getCustomers(), new ChoiceRenderer<Customer>("fullName"));
        customerDropdown.setRequired(true);
        customerDropdown.setLabel(new ResourceModel("admin.project.customer"));
        customerDropdown.add(new ValidatingFormComponentAjaxBehavior());
        parent.add(customerDropdown);
        parent.add(new AjaxFormComponentFeedbackIndicator("customerValidationError", customerDropdown));
    }

    private DropDownChoice<User> getProjectManager() {
        // project manager
        DropDownChoice<User> projectManager = new DropDownChoice<>("project.projectManager", getEligablePms(), new ChoiceRenderer<User>("fullName"));
        projectManager.setNullValid(true);
        projectManager.setLabel(new ResourceModel("admin.project.projectManager"));

        return projectManager;
    }

    private void addDescriptionAndContact(WebMarkupContainer parent) {
        // description
        TextArea<String> textArea = new KeepAliveTextArea("project.description");
        textArea.setLabel(new ResourceModel("admin.project.description"));
        parent.add(textArea);

        // contact
        TextField<String> contactField = new TextField<>("project.contact");
        parent.add(contactField);
    }

    private void addMisc(WebMarkupContainer parent) {
        CheckBox defaultPrjCheckbox = new CheckBox("project.defaultProject");
        defaultPrjCheckbox.setMarkupId("defaultProject");
        parent.add(defaultPrjCheckbox);

        CheckBox activeCheckbox = new CheckBox("project.active");
        activeCheckbox.setMarkupId("active");
        parent.add(activeCheckbox);

        // data save label
        parent.add(new ServerMessageLabel("serverMessage", "formValidationError"));
    }

    @Override
    protected boolean processFormSubmit(AjaxRequestTarget target, AdminBackingBean backingBean, AjaxEventType type) throws Exception {
        ProjectAdminBackingBean projectBackingBean = (ProjectAdminBackingBean) backingBean;

        if (type == ProjectAjaxEventType.PROJECT_UPDATED) {
            updateProject(projectBackingBean);
        } else if (type == ProjectAjaxEventType.PROJECT_CREATED) {
            createProject(projectBackingBean);
        } else if (type == ProjectAjaxEventType.PROJECT_DELETED) {
            deleteProject(projectBackingBean);
        }
        return true;
    }

    private void createProject(ProjectAdminBackingBean backingBean) {
        projectService.createProject(backingBean.getProject());
    }

    private void updateProject(ProjectAdminBackingBean backingBean) {
        projectService.updateProject(backingBean.getProject());
    }

    private void deleteProject(ProjectAdminBackingBean backingBean) throws ParentChildConstraintException {
        projectService.deleteProject(backingBean.getProject().getProjectId());
    }

    /**
     * Get customers for customer dropdown
     */
    private List<Customer> getCustomers() {
        List<Customer> customers = customerService.getActiveCustomers();

        if (customers != null) {
            Collections.sort(customers, new CustomerComparator());
        } else {
            customers = new ArrayList<>();
        }

        return customers;
    }

    private List<User> getEligablePms() {

        List<User> users = userService.getUsersWithEmailSet();

        if (users != null) {
            Collections.sort(users, new UserComparator(false));
        } else {
            users = new ArrayList<>();
        }

        return users;
    }
}