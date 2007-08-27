/**
 * Created on Aug 23, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
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

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.domain.ProjectAssignmentType;
import net.rrm.ehour.ui.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.component.ServerMessageLabel;
import net.rrm.ehour.ui.panel.admin.assignment.dto.AssignmentAdminBackingBean;
import net.rrm.ehour.ui.panel.admin.common.FormUtil;
import net.rrm.ehour.ui.renderers.ProjectAssignmentTypeRenderer;
import net.rrm.ehour.ui.session.EhourWebSession;
import net.rrm.ehour.ui.util.CommonStaticData;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.validator.NumberValidator;
import org.wicketstuff.dojo.markup.html.form.DojoDatePicker;

/**
 * Assignment form (and yes, it's a little big)
 **/

public class AssignmentFormPanel extends Panel
{
	private static final long serialVersionUID = -85486044225123470L;
	private	final static Logger	logger = Logger.getLogger(AssignmentFormPanel.class);
	
	@SpringBean
	private CustomerService	customerService;
	
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
		
		GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder("border");
		add(greyBorder);
		
		setOutputMarkupId(true);
		
		final Form form = new Form("assignmentForm");
		
		// setup the customer & project dropdowns
		addCustomerAndProjectChoices(form, model, customers);
		
		// assignment type
		DropDownChoice assignmentTypeChoice = new DropDownChoice("projectAssignment.assignmentType", assignmenTypes, new ProjectAssignmentTypeRenderer(this));
		assignmentTypeChoice.setRequired(true);
		assignmentTypeChoice.setNullValid(false);
		assignmentTypeChoice.setLabel(new ResourceModel("admin.assignment.type"));
		assignmentTypeChoice.add(getValidateBehavior(form));
		form.add(assignmentTypeChoice);
		form.add(new AjaxFormComponentFeedbackIndicator("typeValidationError", assignmentTypeChoice));
		
		// add start & end dates
		addDates(form, model);
		
		// add hourly rate
		TextField	hourlyRate = new TextField("projectAssignment.hourlyRate");
		hourlyRate.add(getValidateBehavior(form));
		hourlyRate.add(NumberValidator.POSITIVE);
		form.add(hourlyRate);
		form.add(new AjaxFormComponentFeedbackIndicator("rateValidationError", hourlyRate));

		// and currency
		String currency = ((EhourWebSession)getSession()).getEhourConfig().getCurrency();
		form.add(new Label("currency", CommonStaticData.getCurrencies().get(currency)));
		
		
		// data save label
		form.add(new ServerMessageLabel("serverMessage"));
		
		//
		FormUtil.setSubmitActions(form, true);
		
		// can't due the ajax dropdowns, will overwrite the onchange otherwise
//		AjaxFormValidatingBehavior.addToAllFormComponents(form, "onchange", Duration.ONE_SECOND);

		greyBorder.add(form);
	}
	
	/**
	 * Get validate behavior
	 * @param form
	 * @return
	 */
	private AjaxFormSubmitBehavior getValidateBehavior(Form form)
	{
		AjaxFormValidatingBehavior behavior = new AjaxFormValidatingBehavior(form, "onchange");
		behavior.setThrottleDelay(Duration.ONE_SECOND);
		return behavior;
	}
	
	/**
	 * Add start & end dates
	 * @param form
	 * @param model
	 */
	private void addDates(Form form, final IModel model)
	{
		// start date
		final DojoDatePicker dateStart = new DojoDatePicker("projectAssignment.dateStart", "dd/MM/yyyy");
		dateStart.setRequired(true);
		
		final WebMarkupContainer	startDateHider = new WebMarkupContainer("startDateHider");
		startDateHider.setOutputMarkupId(true);
		
		// the inner hider is just there to hide the <br /> as well
		final WebMarkupContainer	innerStartDateHider = new WebMarkupContainer("innerStartDateHider");
		innerStartDateHider.setOutputMarkupId(true);
		innerStartDateHider.add(dateStart);
		innerStartDateHider.setVisible(!((AssignmentAdminBackingBean)model.getObject()).isInfiniteStartDate());
		startDateHider.add(innerStartDateHider);

		form.add(startDateHider);
		
		// infinite start date toggle
		AjaxCheckBox infiniteStart = new AjaxCheckBox("infiniteStartDate")
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				target.addComponent(startDateHider);
				innerStartDateHider.setVisible(!((AssignmentAdminBackingBean)model.getObject()).isInfiniteStartDate());
			}
		};
		
		startDateHider.add(infiniteStart);

		// end date
		DojoDatePicker dateEnd = new DojoDatePicker("projectAssignment.dateEnd", "dd/MM/yyyy");
		dateEnd.setRequired(true);
		
		final WebMarkupContainer	endDateHider = new WebMarkupContainer("endDateHider");
		endDateHider.setOutputMarkupId(true);
		
		// the inner hider is just there to hide the <br /> as well
		final WebMarkupContainer	innerEndDateHider = new WebMarkupContainer("innerEndDateHider");
		innerEndDateHider.setOutputMarkupId(true);
		innerEndDateHider.add(dateEnd);
		innerEndDateHider.setVisible(!((AssignmentAdminBackingBean)model.getObject()).isInfiniteEndDate());
		endDateHider.add(innerEndDateHider);		
		form.add(endDateHider);	
		
		// infinite end date toggle
		AjaxCheckBox infiniteEnd = new AjaxCheckBox("infiniteEndDate")
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				target.addComponent(endDateHider);
				innerEndDateHider.setVisible(!((AssignmentAdminBackingBean)model.getObject()).isInfiniteEndDate());
			}
		};	

		endDateHider.add(infiniteEnd);
	}
	
	/**
	 * Add customer & project dd's
	 * @param form
	 * @param model
	 * @param customers
	 */
	private void addCustomerAndProjectChoices(Form form, 
											final IModel model,
											List<Customer> customers)
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
					logger.debug("Project set size for customer: " + customer.getProjects().size());
					return new ArrayList<Project>(customer.getProjects());
				}
			}
		};
		
		// project
		final DropDownChoice projectChoice = new DropDownChoice("projectAssignment.project", projectChoices, new ChoiceRenderer("fullname"));
		projectChoice.setRequired(true);
		projectChoice.setOutputMarkupId(true);
		projectChoice.setNullValid(false);
		projectChoice.setLabel(new ResourceModel("admin.assignment.project"));
		form.add(projectChoice);
		form.add(new AjaxFormComponentFeedbackIndicator("projectValidationError", projectChoice));
		
		// make project update automatically when customers changed
		customerChoice.add(new AjaxFormComponentUpdatingBehavior("onchange")
        {
			protected void onUpdate(AjaxRequestTarget target)
            {
                target.addComponent(projectChoice);
            }
        });		
	}
}
