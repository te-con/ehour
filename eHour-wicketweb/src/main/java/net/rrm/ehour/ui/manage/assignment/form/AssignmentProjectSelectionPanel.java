package net.rrm.ehour.ui.manage.assignment.form;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.report.criteria.Sort;
import net.rrm.ehour.sort.CustomerComparator;
import net.rrm.ehour.sort.ProjectComparator;
import net.rrm.ehour.ui.common.component.*;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.event.EventPublisher;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import net.rrm.ehour.ui.manage.assignment.AssignmentAdminBackingBean;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.AbstractChoice;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AssignmentProjectSelectionPanel extends AbstractBasePanel<AssignmentAdminBackingBean> {
    private static final long serialVersionUID = 5513770467507708949L;
    private AjaxFormComponentFeedbackIndicator projectValidationErrorIndicator;

    public enum EntrySelectorAjaxEventType implements AjaxEventType {
        PROJECT_CHANGE
    }

    @SpringBean
    private CustomerService customerService;

    @SpringBean
    private ProjectService projectService;

    public AssignmentProjectSelectionPanel(String id, IModel<AssignmentAdminBackingBean> model) {
        super(id, model);

        addCustomerAndProjectChoices();
    }

    @SuppressWarnings("serial")
    private void addCustomerAndProjectChoices() {
        List<Customer> customers = getCustomers();

        // customer
        final DropDownChoice<Customer> customerChoice = createCustomerDropdown(customers);

        // project model
        final IModel<List<Project>> projectChoices = new AbstractReadOnlyModel<List<Project>>() {
            @SuppressWarnings("unchecked")
            @Override
            public List<Project> getObject() {
                // need to re-get it, project set is lazy
                Customer selectedCustomer = getPanelModelObject().getCustomer();
                Customer customer;

                List<Project> projects;

                if (selectedCustomer != null) {
                    customer = customerService.getCustomer(selectedCustomer.getCustomerId());

                    if (customer == null || customer.getProjects() == null || customer.getProjects().isEmpty()) {
                        projects = Lists.newArrayList();
                    } else {
                        projects = Lists.newArrayList(customer.getActiveProjects());
                    }
                } else {
                    projects = projectService.getActiveProjects();
                }

                Collections.sort(projects, new ProjectComparator(Sort.PARENT_CODE_FIRST));

                return projects;
            }
        };

        final AbstractChoice<?, Project> projectChoice = createProjectChoice(projectChoices);
        add(projectChoice);
        projectValidationErrorIndicator = new AjaxFormComponentFeedbackIndicator("projectValidationError", projectChoice);
        projectValidationErrorIndicator.setOutputMarkupId(true);
        add(projectValidationErrorIndicator);

        // make project update automatically when customers changed
        customerChoice.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {

                final AbstractChoice<?, Project> updatedProjectChoice = createProjectChoice(projectChoices);

                AssignmentProjectSelectionPanel.this.addOrReplace(updatedProjectChoice);
                target.add(updatedProjectChoice);

                projectValidationErrorIndicator.setIndicatorFor(updatedProjectChoice);
            }
        });
    }

    private AbstractChoice<?, Project> createProjectChoice(IModel<List<Project>> projectChoices) {
        final AbstractChoice<?, Project> projectChoice = createProjectChoiceDropDown("projectAssignment.project", projectChoices, new ProjectRenderer(createProjectToCustomerMap(projectChoices)));
        projectChoice.setRequired(true);
        projectChoice.setOutputMarkupId(true);
        projectChoice.setLabel(new ResourceModel("admin.assignment.project"));
        projectChoice.add(new ValidatingFormComponentAjaxBehavior());

        projectChoice.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                AjaxEvent ajaxEvent = new AjaxEvent(EntrySelectorAjaxEventType.PROJECT_CHANGE);
                EventPublisher.publishAjaxEvent(AssignmentProjectSelectionPanel.this, ajaxEvent);
            }
        });

        projectChoice.add(new ValidatingFormComponentAjaxBehavior());

        return projectChoice;
    }

    private List<Customer> getCustomers() {
        List<Customer> customers = customerService.getActiveCustomers();

        List<Customer> customersWithActiveProjects = Lists.newArrayList();

        for (Customer customer : customers) {
            if (!customer.getActiveProjects().isEmpty()) {
                customersWithActiveProjects.add(customer);
            }
        }

        Collections.sort(customersWithActiveProjects, new CustomerComparator());

        return customersWithActiveProjects;
    }

    private AbstractChoice<?, Project> createProjectChoiceDropDown(String id, IModel<List<Project>> projectChoices, OptGroupRendererMap<Project> renderer) {
        if (getPanelModelObject().isNewAssignment()) {
            PropertyModel<Collection<Project>> selectedProjects = new PropertyModel<>(getDefaultModel(), "selectedProjects");

            return new GroupableListMultipleChoice<>(id, selectedProjects, projectChoices, renderer);
        } else {
            IModel<Project> projectModel;
            projectModel = new PropertyModel<>(getDefaultModel(), "projectAssignment.project");

            GroupableDropDownChoice<Project> choice = new GroupableDropDownChoice<>(id, projectModel, projectChoices, renderer);
            choice.add(DynamicAttributeModifier.remove("style"));
            return choice;
        }
    }

    private DropDownChoice<Customer> createCustomerDropdown(List<Customer> customers) {
        DropDownChoice<Customer> customerChoice = new DropDownChoice<>("customer", customers, new ChoiceRenderer<Customer>("fullName"));
        customerChoice.setLabel(new ResourceModel("admin.assignment.customer"));
        customerChoice.setOutputMarkupId(true);
        customerChoice.setNullValid(true);
        add(customerChoice);
        return customerChoice;
    }

    private Map<Project, String> createProjectToCustomerMap(IModel<List<Project>> projectChoices) {
        List<Project> projects = projectChoices.getObject();

        Map<Project, String> projectToCustomerMap = Maps.newHashMap();

        for (Project project : projects) {
            projectToCustomerMap.put(project, project.getCustomer().getFullName());
        }
        return projectToCustomerMap;
    }

    static class ProjectRenderer extends OptGroupRendererMap<Project> {
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
