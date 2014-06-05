package net.rrm.ehour.ui.admin.assignment.form;

import com.google.common.collect.Lists;
import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.sort.CustomerComparator;
import net.rrm.ehour.sort.ProjectComparator;
import net.rrm.ehour.ui.admin.assignment.AssignmentAdminBackingBean;
import net.rrm.ehour.ui.common.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.common.component.ValidatingFormComponentAjaxBehavior;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.event.EventPublisher;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.AbstractChoice;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Collections;
import java.util.List;

public abstract class AbstractAssignmentProjectSelectionPanel extends AbstractBasePanel<AssignmentAdminBackingBean> {
    private static final long serialVersionUID = 5513770467507708949L;

    public enum EntrySelectorAjaxEventType implements AjaxEventType {
        PROJECT_CHANGE
    }

    @SpringBean
    private CustomerService customerService;

    @SpringBean
    private ProjectService projectService;

    public AbstractAssignmentProjectSelectionPanel(String id, IModel<AssignmentAdminBackingBean> model) {
        super(id, model);

        addCustomerAndProjectChoices();
    }

    @SuppressWarnings("serial")
    private void addCustomerAndProjectChoices() {
        List<Customer> customers = getCustomers();

        // customer
        final DropDownChoice<Customer> customerChoice = createCustomerDropdown(customers);

        // project model
        IModel<List<Project>> projectChoices = new AbstractReadOnlyModel<List<Project>>() {
            @SuppressWarnings("unchecked")
            @Override
            public List<Project> getObject() {
                // need to re-get it, project set is lazy
                Customer selectedCustomer = getPanelModelObject().getCustomer();
                Customer customer;

                List<Project> projects;

                if (selectedCustomer != null) {
                    customer = customerService.getCustomer(selectedCustomer.getCustomerId());

                    if (customer == null || customer.getProjects() == null || customer.getProjects().size() == 0) {
                        projects = Lists.newArrayList();
                    } else {
                        projects = Lists.newArrayList(customer.getActiveProjects());
                    }
                } else {
                    projects = projectService.getActiveProjects();
                }

                Collections.sort(projects, new ProjectComparator());

                return projects;
            }
        };

        // project
        final AbstractChoice<?, Project> projectChoice = createProjectChoiceDropDown(projectChoices, "projectAssignment.project");
        projectChoice.setRequired(true);
        projectChoice.setOutputMarkupId(true);
        projectChoice.setLabel(new ResourceModel("admin.assignment.project"));
        projectChoice.add(new ValidatingFormComponentAjaxBehavior());
        add(projectChoice);
        add(new AjaxFormComponentFeedbackIndicator("projectValidationError", projectChoice));

        // make project update automatically when customers changed
        customerChoice.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(projectChoice);
            }
        });

        projectChoice.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                AjaxEvent ajaxEvent = new AjaxEvent(EntrySelectorAjaxEventType.PROJECT_CHANGE);
                EventPublisher.publishAjaxEvent(AbstractAssignmentProjectSelectionPanel.this, ajaxEvent);
            }
        });

        projectChoice.add(new ValidatingFormComponentAjaxBehavior());
    }

    private List<Customer> getCustomers() {
        List<Customer> customers = customerService.getActiveCustomers();

        List<Customer> customersWithActiveProjects = Lists.newArrayList();

        for (Customer customer : customers) {
            if (customer.getActiveProjects().size() > 0) {
                customersWithActiveProjects.add(customer);
            }
        }

        Collections.sort(customersWithActiveProjects, new CustomerComparator());

        return customersWithActiveProjects;
    }

    protected abstract AbstractChoice<?, Project> createProjectChoiceDropDown(IModel<List<Project>> projectChoices, String id);

    private DropDownChoice<Customer> createCustomerDropdown(List<Customer> customers) {
        DropDownChoice<Customer> customerChoice = new DropDownChoice<Customer>("customer", customers, new ChoiceRenderer<Customer>("fullName"));
        customerChoice.setLabel(new ResourceModel("admin.assignment.customer"));
        customerChoice.setOutputMarkupId(true);
        customerChoice.setNullValid(true);
        add(customerChoice);
        return customerChoice;
    }
}
