package net.rrm.ehour.ui.admin.assignment.panel.form;

import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.ui.admin.assignment.dto.AssignmentAdminBackingBean;
import net.rrm.ehour.ui.common.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.common.component.ValidatingFormComponentAjaxBehavior;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.event.EventPublisher;
import net.rrm.ehour.ui.common.sort.CustomerComparator;
import net.rrm.ehour.ui.common.sort.ProjectComparator;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AssignmentProjectSelectionPanel extends Panel {
    private static final long serialVersionUID = 5513770467507708949L;

    public enum EntrySelectorAjaxEventType implements AjaxEventType {
        PROJECT_CHANGE
    }

    @SpringBean
    private CustomerService customerService;

    public AssignmentProjectSelectionPanel(String id, IModel<AssignmentAdminBackingBean> model) {
        super(id);

        addCustomerAndProjectChoices(model);
    }

    @SuppressWarnings("serial")
    private void addCustomerAndProjectChoices(final IModel<AssignmentAdminBackingBean> model) {
        List<Customer> customers = customerService.getCustomers(true);
        Collections.sort(customers, new CustomerComparator());

        // customer
        DropDownChoice<Customer> customerChoice = createCustomerDropdown(customers);

        // project model
        IModel<List<Project>> projectChoices = new AbstractReadOnlyModel<List<Project>>() {
            @SuppressWarnings("unchecked")
            @Override
            public List<Project> getObject() {
                // need to re-get it, project set is lazy
                Customer selectedCustomer = model.getObject().getCustomer();
                Customer customer = null;

                if (selectedCustomer != null) {
                    customer = customerService.getCustomer(selectedCustomer.getCustomerId());
                }

                if (customer == null || customer.getProjects() == null || customer.getProjects().size() == 0) {
                    return (List<Project>) Collections.EMPTY_LIST;
                } else {
                    List<Project> projects = new ArrayList<Project>(customer.getActiveProjects());

                    Collections.sort(projects, new ProjectComparator());

                    return projects;
                }
            }
        };

        // project
        final DropDownChoice<Project> projectChoice = new DropDownChoice<Project>("projectAssignment.project", projectChoices, new ChoiceRenderer<Project>("fullName"));
        projectChoice.setRequired(true);
        projectChoice.setOutputMarkupId(true);
        projectChoice.setNullValid(false);
        projectChoice.setLabel(new ResourceModel("admin.assignment.project"));
        projectChoice.add(new ValidatingFormComponentAjaxBehavior());
        add(projectChoice);
        add(new AjaxFormComponentFeedbackIndicator("projectValidationError", projectChoice));

        customerChoice.add(new ValidatingFormComponentAjaxBehavior());

        // make project update automatically when customers changed
        customerChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(projectChoice);
            }
        });

        projectChoice.add(new ValidatingFormComponentAjaxBehavior());

        // update any components that showed interest
        projectChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            protected void onUpdate(AjaxRequestTarget target) {
                AjaxEvent ajaxEvent = new AjaxEvent(EntrySelectorAjaxEventType.PROJECT_CHANGE);

                EventPublisher.publishAjaxEvent(AssignmentProjectSelectionPanel.this, ajaxEvent);
            }
        });
    }

    private DropDownChoice<Customer> createCustomerDropdown(List<Customer> customers) {
        DropDownChoice<Customer> customerChoice = new DropDownChoice<Customer>("customer", customers, new ChoiceRenderer<Customer>("fullName"));
        customerChoice.setRequired(true);
        customerChoice.setNullValid(false);
        customerChoice.setLabel(new ResourceModel("admin.assignment.customer"));
        add(customerChoice);
        add(new AjaxFormComponentFeedbackIndicator("customerValidationError", customerChoice));
        return customerChoice;
    }
}
