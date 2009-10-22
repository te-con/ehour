package net.rrm.ehour.ui.admin.assignment.panel.form;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.ui.admin.assignment.dto.AssignmentAdminBackingBean;
import net.rrm.ehour.ui.common.ajax.AjaxEvent;
import net.rrm.ehour.ui.common.ajax.AjaxEventType;
import net.rrm.ehour.ui.common.ajax.AjaxUtil;
import net.rrm.ehour.ui.common.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.common.component.ValidatingFormComponentAjaxBehavior;
import net.rrm.ehour.ui.common.sort.ProjectComparator;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class AssignmentProjectSelectionPanel extends Panel
{
	private static final long serialVersionUID = 5513770467507708949L;

	private	final static Logger	LOGGER = Logger.getLogger(AssignmentFormComponentContainerPanel.class);
	
	public enum EntrySelectorAjaxEventType implements AjaxEventType
	{
		PROJECT_CHANGE;
	}
	
	@SpringBean
	private CustomerService	customerService;
	
	public AssignmentProjectSelectionPanel(String id, IModel model)
	{
		super(id);
		
		addCustomerAndProjectChoices(model);
	}

	@SuppressWarnings("serial")
	private void addCustomerAndProjectChoices(final IModel model)
	{
		List<Customer> customers = customerService.getCustomers(true);

		// customer
		DropDownChoice customerChoice = new DropDownChoice("customer", customers, new ChoiceRenderer("fullName"));
		customerChoice.setRequired(true);
		customerChoice.setNullValid(false);
		customerChoice.setLabel(new ResourceModel("admin.assignment.customer"));
		add(customerChoice);
		add(new AjaxFormComponentFeedbackIndicator("customerValidationError", customerChoice));

		// project model
		IModel projectChoices = new AbstractReadOnlyModel()
		{
			@Override
			public Object getObject()
			{
				// need to re-get it, project set is lazy
				Customer selectedCustomer = ((AssignmentAdminBackingBean) model.getObject()).getCustomer();
				Customer customer = null;

				if (selectedCustomer != null)
				{
					try
					{
						customer = customerService.getCustomer(selectedCustomer.getCustomerId());
					} catch (ObjectNotFoundException e)
					{
						// TODO
					}
				}

				if (customer == null || customer.getProjects() == null || customer.getProjects().size() == 0)
				{
					LOGGER.debug("Empty project set for customer: " + customer);
					return Collections.EMPTY_LIST;
				} else
				{
					List<Project> projects = new ArrayList<Project>(customer.getActiveProjects());

					Collections.sort(projects, new ProjectComparator());

					return projects;
				}
			}
		};

		// project
		final DropDownChoice projectChoice = new DropDownChoice("projectAssignment.project", projectChoices, new ChoiceRenderer("fullName"));
		projectChoice.setRequired(true);
		projectChoice.setOutputMarkupId(true);
		projectChoice.setNullValid(false);
		projectChoice.setLabel(new ResourceModel("admin.assignment.project"));
		projectChoice.add(new ValidatingFormComponentAjaxBehavior());
		add(projectChoice);
		add(new AjaxFormComponentFeedbackIndicator("projectValidationError", projectChoice));

		customerChoice.add(new ValidatingFormComponentAjaxBehavior());

		// make project update automatically when customers changed
		customerChoice.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			protected void onUpdate(AjaxRequestTarget target)
			{
				target.addComponent(projectChoice);
			}
		});

		projectChoice.add(new ValidatingFormComponentAjaxBehavior());

		// update any components that showed interest
		projectChoice.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			protected void onUpdate(AjaxRequestTarget target)
			{
				AjaxEvent ajaxEvent = new AjaxEvent(EntrySelectorAjaxEventType.PROJECT_CHANGE);
				
				AjaxUtil.publishAjaxEvent(AssignmentProjectSelectionPanel.this, ajaxEvent);
			}
		});
	}
}
