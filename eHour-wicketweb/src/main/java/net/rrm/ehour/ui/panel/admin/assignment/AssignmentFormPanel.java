/**
 * Created on Aug 23, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.panel.admin.assignment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.ui.ajax.AjaxAwareContainer;
import net.rrm.ehour.ui.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.component.ServerMessageLabel;
import net.rrm.ehour.ui.component.ValidatingFormComponentAjaxBehavior;
import net.rrm.ehour.ui.model.FloatModel;
import net.rrm.ehour.ui.panel.admin.AbstractAjaxAwareAdminPanel;
import net.rrm.ehour.ui.panel.admin.assignment.dto.AssignmentAdminBackingBean;
import net.rrm.ehour.ui.panel.admin.common.FormUtil;
import net.rrm.ehour.ui.session.EhourWebSession;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.NumberValidator;

/**
 * Assignment form (and yes, it's a little (too) big & complex)
 **/

@SuppressWarnings("serial")
public class AssignmentFormPanel extends AbstractAjaxAwareAdminPanel
{
	private static final long serialVersionUID = -85486044225123470L;
	private	final static Logger	logger = Logger.getLogger(AssignmentFormPanel.class);
	
	@SpringBean
	private CustomerService	customerService;
	protected	EhourConfig		config;
	/**
	 * 
	 * @param id
	 * @param model
	 * @param customers
	 * @param assignmenTypes
	 */
	public AssignmentFormPanel(String id,
								final CompoundPropertyModel model)
	{
		super(id, model);
		
		config = ((EhourWebSession)getSession()).getEhourConfig();
		
		setOutputMarkupId(true);
		
		setUpPage(this, model);
	}


	/**
	 * Setup form
	 */
	protected void setUpPage(WebMarkupContainer parent, final IModel model)
	{
		Border greyBorder = new GreySquaredRoundedBorder("border", 450);
		add(greyBorder);
		
		final Form form = new Form("assignmentForm");
		greyBorder.add(form);
		
		Component[] projectDependentComponents = addProjectDuration(form, model);
		
		// setup the customer & project dropdowns
		addCustomerAndProjectChoices(form, model, projectDependentComponents);
		
		// Add rate & role
		addRateRole(form, model);
		
		// active
		form.add(new CheckBox("projectAssignment.active"));
		
		// data save label
		form.add(new ServerMessageLabel("serverMessage", "formValidationError"));
		
		// add submit form
		FormUtil.setSubmitActions(form 
									,((AssignmentAdminBackingBean)model.getObject()).getProjectAssignment().isDeletable() 
									,this 
									,config);
		
		parent.add(greyBorder);
	}

	/**
	 * Add rate, role & active
	 * @param form
	 * @param model
	 */
	private void addRateRole(Form form, final IModel model)
	{
		form.add(new AssignmentRateRoleFormPartPanel("rateRole", model));
	}
	
	/**
	 * Add project duration
	 * @param form
	 * @param model
	 * @return
	 */
	protected Component[] addProjectDuration(Form form, final IModel model)
	{
		AssignmentTypeFormPartPanel typePanel = new AssignmentTypeFormPartPanel("assignmentType", model, form);
		form.add(typePanel);
		
		// assignment type
		Component[] projectDependentComponents = typePanel.getNotifiableComponents();
		
		return projectDependentComponents;
	}
	
	/**
	 * Add customer & project dd's
	 * @param form
	 * @param model
	 * @param customers
	 */
	protected void addCustomerAndProjectChoices(Form form, 
											final IModel model,
											final Component[] projectDependentComponents)
	{
		List<Customer> customers = customerService.getCustomers(true);
		
		// customer
		DropDownChoice customerChoice = new DropDownChoice("customer", customers, new ChoiceRenderer("fullName"));
		customerChoice.setRequired(true);
		customerChoice.setNullValid(false);
		customerChoice.setLabel(new ResourceModel("admin.assignment.customer"));
		form.add(customerChoice);
		form.add(new AjaxFormComponentFeedbackIndicator("customerValidationError", customerChoice));

		// project model
		IModel	projectChoices = new AbstractReadOnlyModel()
		{
			@Override
			public Object getObject()
			{
				// need to re-get it, project set is lazy
				Customer selectedCustomer = ((AssignmentAdminBackingBean)model.getObject()).getCustomer();
				Customer customer = null;
				
				if (selectedCustomer != null)
				{
					customer = customerService.getCustomer(selectedCustomer.getCustomerId());
				}
				
				if (customer == null || customer.getProjects() == null || customer.getProjects().size() == 0)
				{
					logger.debug("Empty project set for customer: " + customer);
					return Collections.EMPTY_LIST;
				}
				else
				{
					return new ArrayList<Project>(customer.getActiveProjects());
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
		form.add(projectChoice);
		form.add(new AjaxFormComponentFeedbackIndicator("projectValidationError", projectChoice));

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
				for (Component component : projectDependentComponents)
				{
					target.addComponent(component);	
				}
            }
        });	
	}


	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.ajax.AjaxAwareContainer#ajaxRequestReceived(org.apache.wicket.ajax.AjaxRequestTarget, int)
	 */
	public void ajaxRequestReceived(AjaxRequestTarget target, int type)
	{
		ajaxRequestReceived(target, type, null);
		
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.ajax.AjaxAwareContainer#ajaxRequestReceived(org.apache.wicket.ajax.AjaxRequestTarget, int, java.lang.Object)
	 */
	public void ajaxRequestReceived(AjaxRequestTarget target, int type, Object params)
	{
		// tabs -> panel
		((AjaxAwareContainer)AssignmentFormPanel.this.getParent().getParent()).ajaxRequestReceived(target, type, params);
	}
}
