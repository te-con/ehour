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
import java.util.List;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.domain.ProjectAssignmentType;
import net.rrm.ehour.ui.ajax.AjaxAwareContainer;
import net.rrm.ehour.ui.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.component.DynamicAttributeModifier;
import net.rrm.ehour.ui.component.ServerMessageLabel;
import net.rrm.ehour.ui.model.FloatModel;
import net.rrm.ehour.ui.panel.admin.assignment.dto.AssignmentAdminBackingBean;
import net.rrm.ehour.ui.panel.admin.common.FormUtil;
import net.rrm.ehour.ui.renderers.ProjectAssignmentTypeRenderer;
import net.rrm.ehour.ui.session.EhourWebSession;
import net.rrm.ehour.ui.util.CommonUIStaticData;
import net.rrm.ehour.ui.validator.ConditionalRequiredValidator;
import net.rrm.ehour.ui.validator.DateOverlapValidator;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.NumberValidator;
import org.wicketstuff.dojo.markup.html.form.DojoDatePicker;

/**
 * Assignment form (and yes, it's a little (too) big & complex)
 **/

@SuppressWarnings("serial")
public class AssignmentFormPanel extends Panel implements AjaxAwareContainer
{
	private static final long serialVersionUID = -85486044225123470L;
	private	final static Logger	logger = Logger.getLogger(AssignmentFormPanel.class);
	
	@SpringBean
	private CustomerService	customerService;
	private	EhourConfig		config;
	/**
	 * 
	 * @param id
	 * @param model
	 * @param customers
	 * @param assignmenTypes
	 */
	public AssignmentFormPanel(String id,
								final CompoundPropertyModel model,
								List<Customer> customers,
								List<ProjectAssignmentType> assignmenTypes)
	{
		super(id, model);
		
		config = ((EhourWebSession)getSession()).getEhourConfig();
		
		GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder("border");
		add(greyBorder);
		
		setOutputMarkupId(true);
		
		final Form form = new Form("assignmentForm");

		// assignment type
		Component[] projectDependentComponents = addAssignmentType(form, assignmenTypes, model);

		// setup the customer & project dropdowns
		addCustomerAndProjectChoices(form, model, customers, projectDependentComponents);
		
		// add role
		TextField role = new TextField("projectAssignment.role");
		form.add(role);
		
		// add start & end dates
		addDates(form, model);
		
		// add hourly rate
		TextField	hourlyRate = new TextField("projectAssignment.hourlyRate",
											new FloatModel(new PropertyModel(model, "projectAssignment.hourlyRate"), config, null));
		hourlyRate.setType(Float.class);
		hourlyRate.add(FormUtil.getValidateBehavior(form));
		hourlyRate.add(NumberValidator.POSITIVE);
		form.add(hourlyRate);
		form.add(new AjaxFormComponentFeedbackIndicator("rateValidationError", hourlyRate));

		// and currency
		String currency = config.getCurrency();
		Label currencyLabel = new Label("currency", CommonUIStaticData.getCurrencies().get(currency));
		currencyLabel.setEscapeModelStrings(false);
		form.add(currencyLabel);
		
		// active
		form.add(new CheckBox("projectAssignment.active"));
		
		// data save label
		form.add(new ServerMessageLabel("serverMessage"));
		
		// add submit form
		FormUtil.setSubmitActions(form 
									,((AssignmentAdminBackingBean)model.getObject()).getProjectAssignment().isDeletable() 
									,this 
									,config);

		greyBorder.add(form);
	}

	
	/**
	 * Add assignment types and options
	 * @param form
	 * @param assignmenTypes
	 * @return the notify pm checkbox as it needs to be refreshed by the project dropdown
	 */
	private Component[] addAssignmentType(final Form form, List<ProjectAssignmentType> assignmenTypes, IModel model)
	{
		final PropertyModel	showAllottedHoursModel = new PropertyModel(model, "showAllottedHours");
		final PropertyModel	showOverrunHoursModel = new PropertyModel(model, "showOverrunHours");
		
		// assignment type
		final DropDownChoice assignmentTypeChoice = new DropDownChoice("projectAssignment.assignmentType", assignmenTypes, new ProjectAssignmentTypeRenderer(this));
		assignmentTypeChoice.setRequired(true);
		assignmentTypeChoice.setNullValid(false);
		assignmentTypeChoice.setLabel(new ResourceModel("admin.assignment.type"));
		assignmentTypeChoice.add(FormUtil.getValidateBehavior(form));
		form.add(assignmentTypeChoice);
		form.add(new AjaxFormComponentFeedbackIndicator("typeValidationError", assignmentTypeChoice));
		
		// allotted hours 
		final TextField allottedHours = new RequiredTextField("projectAssignment.allottedHours",
												new FloatModel(new PropertyModel(model, "projectAssignment.allottedHours"), config, null));
		allottedHours.setType(float.class);
		allottedHours.add(FormUtil.getValidateBehavior(form));
		allottedHours.add(NumberValidator.POSITIVE);
		allottedHours.setOutputMarkupId(true);
		allottedHours.setLabel(new ResourceModel("admin.assignment.timeAllotted"));
		allottedHours.setEnabled(((Boolean)showAllottedHoursModel.getObject()).booleanValue());
		
		// allotted hours row
		final WebMarkupContainer allottedRow = new WebMarkupContainer("allottedRow");
		allottedRow.setOutputMarkupId(true);
		allottedRow.add(allottedHours);
		allottedRow.add(new AjaxFormComponentFeedbackIndicator("allottedHoursValidationError", allottedHours));
		allottedRow.add(new DynamicAttributeModifier("style", true, new Model("display: none;"), showAllottedHoursModel));
		form.add(allottedRow);
		
		// overrun hours 
		final TextField overrunHours = new RequiredTextField("projectAssignment.allowedOverrun",
											new FloatModel(new PropertyModel(model, "projectAssignment.allowedOverrun"), config, null));
		overrunHours.setType(float.class);
		overrunHours.add(FormUtil.getValidateBehavior(form));
		overrunHours.add(NumberValidator.POSITIVE);
		overrunHours.setOutputMarkupId(true);
		overrunHours.setEnabled(((Boolean)showOverrunHoursModel.getObject()).booleanValue());
		overrunHours.setLabel(new ResourceModel("admin.assignment.allowedOverrun"));
		
		// overrun hours row
		final WebMarkupContainer overrunRow = new WebMarkupContainer("overrunRow");
		overrunRow.setOutputMarkupId(true);
		overrunRow.add(overrunHours);
		overrunRow.add(new AjaxFormComponentFeedbackIndicator("overrunHoursValidationError", overrunHours));
		overrunRow.add(new DynamicAttributeModifier("style", true, new Model("display: none;"), showOverrunHoursModel));
		form.add(overrunRow);
		
		// notify PM when possible
		CheckBox notifyPm = new CheckBox("projectAssignment.notifyPm");
		notifyPm.add(new DynamicAttributeModifier("style", true, new Model("display: none;"), new PropertyModel(model, "notifyPmEnabled")));
		notifyPm.setOutputMarkupId(true);
		
		Label notifyDisabled = new Label("notifyDisabled", new ResourceModel("admin.assignment.cantNotify"));
		notifyDisabled.add(new DynamicAttributeModifier("style", true, 
														new Model("display: none;"), new PropertyModel(model, "notifyPmEnabled"), true));
		notifyDisabled.setOutputMarkupId(true);
		
		// notify PM row
		final WebMarkupContainer notifyPmRow = new WebMarkupContainer("notifyPmRow");
		notifyPmRow.setOutputMarkupId(true);
		notifyPmRow.add(notifyPm);
		notifyPmRow.add(notifyDisabled);
		notifyPmRow.add(new DynamicAttributeModifier("style", true, new Model("display: none;"), showAllottedHoursModel));
		form.add(notifyPmRow);
		
		assignmentTypeChoice.add(new AjaxFormComponentUpdatingBehavior("onchange")
        {
			protected void onUpdate(AjaxRequestTarget target)
            {
				// to disable the required validation
				allottedHours.setEnabled(((Boolean)showAllottedHoursModel.getObject()).booleanValue());
				overrunHours.setEnabled(((Boolean)showOverrunHoursModel.getObject()).booleanValue());
				target.addComponent(allottedHours);
				target.addComponent(overrunHours);
				
				// show/hide rows dependent on the assignment type selected
				target.addComponent(allottedRow);
				target.addComponent(overrunRow);
				target.addComponent(notifyPmRow);
            }
        });	
		
		return new Component[]{notifyPm, notifyDisabled};
	}

	/**
	 * Add start & end dates
	 * TODO create seperate component for date range selection out of it
	 * @param form
	 * @param model
	 */
	private void addDates(Form form, final IModel model)
	{
		PropertyModel	infiniteStartDateModel = new PropertyModel(model, "infiniteStartDate");
		PropertyModel	infiniteEndDateModel = new PropertyModel(model, "infiniteEndDate");
		
		// start date
		final DojoDatePicker dateStart = new DojoDatePicker("projectAssignment.dateStart", "dd/MM/yyyy");
		dateStart.add(new ConditionalRequiredValidator(infiniteStartDateModel));
		dateStart.add(FormUtil.getValidateBehavior(form));
		dateStart.setLabel(new ResourceModel("admin.assignment.dateStart"));

		// container for hiding
		final WebMarkupContainer	startDateHider = new WebMarkupContainer("startDateHider");
		startDateHider.setOutputMarkupId(true);
		
		// indicator for validation issues
		startDateHider.add(new AjaxFormComponentFeedbackIndicator("dateStartValidationError", dateStart));
		
		// the inner hider is just there to hide the <br /> as well
		final WebMarkupContainer	innerStartDateHider = new WebMarkupContainer("innerStartDateHider");
		innerStartDateHider.setOutputMarkupId(true);
		innerStartDateHider.add(dateStart);
		innerStartDateHider.add(new DynamicAttributeModifier("style", true, new Model("display: none;"), infiniteStartDateModel, true));

		startDateHider.add(innerStartDateHider);

		form.add(startDateHider);
		
		// infinite start date toggle
		AjaxCheckBox infiniteStart = new AjaxCheckBox("infiniteStartDate")
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				target.addComponent(startDateHider);
			}
		};
		
		startDateHider.add(infiniteStart);

		// end date
		DojoDatePicker dateEnd = new DojoDatePicker("projectAssignment.dateEnd", "dd/MM/yyyy");
		dateEnd.add(FormUtil.getValidateBehavior(form));
		dateStart.add(new ConditionalRequiredValidator(infiniteEndDateModel));
		dateEnd.setLabel(new ResourceModel("admin.assignment.dateEnd"));
		
		final WebMarkupContainer	endDateHider = new WebMarkupContainer("endDateHider");
		endDateHider.setOutputMarkupId(true);
		
		// indicator for validation issues
		endDateHider.add(new AjaxFormComponentFeedbackIndicator("dateEndValidationError", dateEnd));
		
		// the inner hider is just there to hide the <br /> as well
		final WebMarkupContainer	innerEndDateHider = new WebMarkupContainer("innerEndDateHider");
		innerEndDateHider.setOutputMarkupId(true);
		innerEndDateHider.add(dateEnd);
		innerEndDateHider.add(new DynamicAttributeModifier("style", true, new Model("display: none;"), infiniteEndDateModel, true));
		endDateHider.add(innerEndDateHider);		
		form.add(endDateHider);	
		
		// infinite end date toggle
		AjaxCheckBox infiniteEnd = new AjaxCheckBox("infiniteEndDate")
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				target.addComponent(endDateHider);
			}
		};	

		endDateHider.add(infiniteEnd);
		
		form.add(new DateOverlapValidator(dateStart, dateEnd));
	}
	
	/**
	 * Add customer & project dd's
	 * @param form
	 * @param model
	 * @param customers
	 */
	private void addCustomerAndProjectChoices(Form form, 
											final IModel model,
											List<Customer> customers,
											final Component[] projectDependentComponents)
	{
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
		projectChoice.add(FormUtil.getValidateBehavior(form));
		form.add(projectChoice);
		form.add(new AjaxFormComponentFeedbackIndicator("projectValidationError", projectChoice));

		customerChoice.add(FormUtil.getValidateBehavior(form));

		// make project update automatically when customers changed
		customerChoice.add(new AjaxFormComponentUpdatingBehavior("onchange")
        {
			protected void onUpdate(AjaxRequestTarget target)
            {
                target.addComponent(projectChoice);
            }
        });	


		projectChoice.add(FormUtil.getValidateBehavior(form));

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
